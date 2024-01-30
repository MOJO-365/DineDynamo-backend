package com.dinedynamo.controllers;

import com.dinedynamo.collections.Invoice;
import com.dinedynamo.collections.Order;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.repositories.InvoiceRepository;
import com.dinedynamo.repositories.OrderRepository;
import com.dinedynamo.repositories.RestaurantRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

@SuppressWarnings("unchecked")
@RestController
@CrossOrigin("*")
public class InvoiceController {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    InvoiceRepository invoiceRepository;



    @PostMapping("/dinedynamo/invoice/save")
    public ResponseEntity<byte[]> saveInvoice(@RequestBody Order order) {
        Optional<Order> existingOrder = orderRepository.findById(order.getOrderId());

        if (existingOrder.isPresent()) {
            Order retrievedOrder = existingOrder.get();

            try {
                byte[] pdfBytes = generatePDFBytes(retrievedOrder);

                Invoice invoice = new Invoice();
                invoice.setTotalPrice(order.getTotalPrice());
                invoice.setPdfData(pdfBytes);
                invoiceRepository.save(invoice);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("inline", "invoice.pdf");
                return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    //    @PostMapping("/dinedynamo/invoice/getpdf/{id}")
//        public ResponseEntity<byte[]> getInvoicePdf(@PathVariable String id) {
//        Optional<Invoice> existingInvoice = invoiceRepository.findById(id);
//
//        if (existingInvoice.isPresent()) {
//            Invoice invoice = existingInvoice.get();
//            byte[] pdfData = invoice.getPdfData();
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_PDF);
//            headers.setContentDispositionFormData("inline", "invoice.pdf");
//
//            return new ResponseEntity<>(pdfData, headers, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

    @PostMapping("/dinedynamo/invoice/getorder")
    public ResponseEntity<Order> getOrderList(@RequestBody Order order)
    {
        Optional<Order> existingOrder=orderRepository.findById(order.getOrderId());

        if (existingOrder.isPresent())
        {
            Order retrievedOrder=existingOrder.get();

            try
            {
                JSONArray modifiedOrderList = modifyOrderList(retrievedOrder.getOrderList());

                Order modifiedOrder = new Order();
                modifiedOrder.setOrderId(retrievedOrder.getOrderId());
                modifiedOrder.setRestaurantId(retrievedOrder.getRestaurantId());
                modifiedOrder.setTableId(retrievedOrder.getTableId());
                modifiedOrder.setOrderList(modifiedOrderList);

                return new ResponseEntity<>(modifiedOrder, HttpStatus.OK);
            } catch (Exception e)
            {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private JSONArray modifyOrderList(JSONArray orderList)
    {
        Map<String, Integer> itemQuantities = new HashMap<>();

        for (Object orderItem : orderList)
        {
            Map<String, Object> orderObject = (Map<String, Object>) orderItem;
            List<Map<String, Object>> items = (List<Map<String, Object>>) orderObject.get("items");

            for (Map<String, Object> item : items)
            {
                String itemName = (String) item.get("name");
                int quantity = ((Number) item.get("qty")).intValue();

                itemQuantities.put(itemName, itemQuantities.getOrDefault(itemName, 0) + quantity);
            }
        }

        JSONArray modifiedOrderList = new JSONArray();

        for (Object orderItem : orderList) {
            Map<String, Object> orderObject = (Map<String, Object>) orderItem;
            List<Map<String, Object>> items = (List<Map<String, Object>>) orderObject.get("items");

            for (Map<String, Object> item : items) {
                String itemName = (String) item.get("name");
                double price = ((Number) item.get("price")).doubleValue();

                if (itemQuantities.containsKey(itemName)) {
                    int totalQuantity = itemQuantities.get(itemName);

                    Map<String, Object> modifiedItem = new HashMap<>();
                    modifiedItem.put("name", itemName);
                    modifiedItem.put("qty", totalQuantity);
                    modifiedItem.put("price", price);

                    modifiedOrderList.add(modifiedItem);
                    itemQuantities.remove(itemName);
                }
            }
        }

        return modifiedOrderList;
    }

    @PostMapping("/dinedynamo/invoice/getinvoice")
    public ResponseEntity<byte[]> generateInvoice(@RequestBody Order order)
    {
        Optional<Order> existingOrder = orderRepository.findById(order.getOrderId());

        if (existingOrder.isPresent()) {
            Order retrievedOrder = existingOrder.get();

            try {
                byte[] pdfBytes = generatePDFBytes(retrievedOrder);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("inline", "invoice.pdf");
                return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private byte[] generatePDFBytes(Order order) throws IOException, DocumentException
    {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A6, 10, 10, 10, 10);
            PdfWriter.getInstance(document, outputStream);
            document.open();

            addOrderDetailsToPDF(document, order);

            document.close();
            return outputStream.toByteArray();
        }
    }


    private void addHorizontalLine(Document document) throws DocumentException
    {
        int lineLength = 38;
        String line = new String(new char[lineLength]).replace('\0', '-');
        document.add(new Paragraph(line, FontFactory.getFont(FontFactory.COURIER_BOLD, 12)));
    }


    private void addOrderDetailsToPDF(Document document, Order order) throws DocumentException
    {
        // Font titleFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 16);
        Font subtitleFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 14);
        Font normalFont = FontFactory.getFont(FontFactory.COURIER, 13);

        String fontUrl = "src/main/resources/Castellar.ttf";
        FontFactory.register(fontUrl);

        Font castellar = FontFactory.getFont("Castellar", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 16, Font.BOLD, BaseColor.BLACK);

        Paragraph dateParagraph = new Paragraph(new Date().toString(), new Font(Font.FontFamily.COURIER, 8));
        dateParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(dateParagraph);

        Paragraph title = new Paragraph();
        String restaurantId = order.getRestaurantId();
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);

        if (optionalRestaurant.isPresent()) {
            Restaurant restaurant = optionalRestaurant.get();
            title.add(Chunk.NEWLINE);

            Paragraph restaurantName = new Paragraph(restaurant.getRestaurantName(), castellar);
            restaurantName.setAlignment(Element.ALIGN_CENTER);
            title.add(restaurantName);


            title.add(Chunk.NEWLINE);

            Paragraph locationParagraph = new Paragraph(restaurant.getRestaurantLocation(), new Font(Font.FontFamily.COURIER, 10, Font.NORMAL, BaseColor.BLACK));
            locationParagraph.setAlignment(Element.ALIGN_LEFT);
            title.add(locationParagraph);
        }


//        Paragraph abnParagraph = new Paragraph("ABN:7852135457865", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK));
//        abnParagraph.setAlignment(Element.ALIGN_LEFT);
//        title.add(abnParagraph);

        title.add(Chunk.NEWLINE);

        Paragraph taxInvoiceParagraph = new Paragraph("TAX INVOICE", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK));
        taxInvoiceParagraph.setAlignment(Element.ALIGN_CENTER);
        title.add(taxInvoiceParagraph);

        document.add(title);
        addHorizontalLine(document);

        double grandTotal = 0;
        List<Map<String, Object>> orderList = order.getOrderList();
        Map<String, Integer> aggregatedQuantities = new HashMap<>();

        for (Map<String, Object> orderItem : orderList) {
            List<Map<String, Object>> items = (List<Map<String, Object>>) orderItem.get("items");

            for (Map<String, Object> item : items) {
                String itemName = (String) item.get("name");
                int quantity = ((Number) item.get("qty")).intValue();

                aggregatedQuantities.put(itemName, aggregatedQuantities.getOrDefault(itemName, 0) + quantity);
            }
        }

        Set<String> displayedItems = new HashSet<>();

        for (Map<String, Object> orderItem : orderList) {
            List<Map<String, Object>> items = (List<Map<String, Object>>) orderItem.get("items");

            for (Map<String, Object> item : items) {
                String itemName = (String) item.get("name");
                double price = ((Number) item.get("price")).doubleValue();

                if (!displayedItems.contains(itemName)) {
                    int totalQuantity = aggregatedQuantities.get(itemName);

                    document.add(new Paragraph(
                            String.format("%-24s", itemName) +
                                    "\t  " + String.format("%-2s", totalQuantity) +
                                    "\t  " + String.format("%-5s", "$" + price*totalQuantity),
                            normalFont));

                    grandTotal += price;
                    displayedItems.add(itemName);
                }
            }
        }

        Paragraph gstPar = new Paragraph(String.format("%-4s $%.2f","GST:",grandTotal * 0.1),subtitleFont);
        gstPar.setAlignment(Element.ALIGN_LEFT);
        gstPar.setIndentationLeft(178);
        document.add(gstPar);

        grandTotal += grandTotal * 0.1;

        Paragraph grandTotalPar = new Paragraph(String.format("%-4s $%.2f","Total(inc GST):",grandTotal),subtitleFont);
        grandTotalPar.setAlignment(Element.ALIGN_LEFT);
        grandTotalPar.setIndentationLeft(85);
        document.add(grandTotalPar);

        addHorizontalLine(document);




        String websiteURL = "https://hello.com";
        addQRCodeToPDF(document, websiteURL);
        Paragraph web = new Paragraph("**Thank you for visiting**", new Font(Font.FontFamily.COURIER, 10, Font.BOLD, BaseColor.BLACK));
        web.setAlignment(Element.ALIGN_CENTER);
        document.add(web);


    }
    private void addQRCodeToPDF(Document document, String content) throws DocumentException {
        Paragraph qrCodeParagraph = new Paragraph();

        try {

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 80, 80);

            BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrCodeImage, "png", baos);
            Image qrCode = Image.getInstance(baos.toByteArray());

            qrCode.setAlignment(Element.ALIGN_CENTER);
            qrCodeParagraph.add(qrCode);

            document.add(qrCodeParagraph);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

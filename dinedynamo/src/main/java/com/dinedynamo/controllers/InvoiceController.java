package com.dinedynamo.controllers;

import com.dinedynamo.collections.Invoice;
import com.dinedynamo.collections.Order;
import com.dinedynamo.collections.Restaurant;
import com.dinedynamo.collections.Table;
import com.dinedynamo.repositories.InvoiceRepository;
import com.dinedynamo.repositories.OrderRepository;
import com.dinedynamo.repositories.RestaurantRepository;
import com.dinedynamo.repositories.TableRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
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

    @Autowired
    TableRepository tableRepository;





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
    private byte[] generatePDFBytes(Order order) throws IOException, DocumentException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            float contentHeight = calculateContentHeight(order);

            Document document = new Document(new Rectangle(PageSize.A6.getWidth(), contentHeight),10,10,10,10);


            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            document.open();

            addOrderDetailsToPDF(document, order);

            String websiteURL = "https://hello.com";
            addQRCodeToPDF(document, websiteURL);

            Paragraph web = new Paragraph("**Thank you for visiting**", new Font(Font.FontFamily.COURIER, 10, Font.BOLD, BaseColor.BLACK));
            web.setAlignment(Element.ALIGN_CENTER);
            document.add(web);

            document.close();

            return outputStream.toByteArray();
        }
    }

    private float calculateContentHeight(Order order) {
        float baseContentHeight = 200;
        float orderDetailsHeight = calculateOrderDetailsHeight(order);
        float thankYouHeight = calculateThankYouHeight();

        float totalContentHeight;

        if (orderDetailsHeight > 400) {
            totalContentHeight = calculateTotalHeightWithIncreasedPageSize(baseContentHeight, orderDetailsHeight, thankYouHeight);
        } else {
            totalContentHeight = baseContentHeight + orderDetailsHeight + thankYouHeight;
        }

        return totalContentHeight;
    }

    private float calculateTotalHeightWithIncreasedPageSize(float baseContentHeight, float orderDetailsHeight, float thankYouHeight) {

        int pageSize = 400;

        switch (pageSize) {
            case 400:
                return baseContentHeight + orderDetailsHeight + thankYouHeight;

            case 600:

                float additionalHeight = 200;
                return baseContentHeight + orderDetailsHeight + thankYouHeight + additionalHeight;

            default:
                return baseContentHeight + orderDetailsHeight + thankYouHeight;
        }
    }

    private float calculateOrderDetailsHeight(Order order) {

        return 300;
    }

    private float calculateThankYouHeight() {
        float thankYouHeight = 20;

        return thankYouHeight;
    }




    private void addHorizontalLine(Document document) throws DocumentException
    {
        int lineLength = 38;
        String line = new String(new char[lineLength]).replace('\0', '-');
        document.add(new Paragraph(line, FontFactory.getFont(FontFactory.COURIER_BOLD, 12)));
    }


    private void addOrderDetailsToPDF(Document document, Order order) throws DocumentException
    {
        Font subtitleFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 14);
        Font normalFont = FontFactory.getFont(FontFactory.COURIER, 13);

        String fontUrl = "dinedynamo/src/main/resources/Castellar.ttf";
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

            Paragraph abnParagraph = new Paragraph("ABN:7852135457865", new Font(Font.FontFamily.COURIER, 10, Font.BOLD, BaseColor.BLACK));
            abnParagraph.setAlignment(Element.ALIGN_CENTER);
            title.add(abnParagraph);


            Paragraph locationParagraph = new Paragraph(restaurant.getRestaurantLocation(), new Font(Font.FontFamily.COURIER, 10, Font.BOLD, BaseColor.BLACK));
            locationParagraph.setAlignment(Element.ALIGN_CENTER);
            title.add(locationParagraph);
        }

        Optional<Table> optionalTable = tableRepository.findById(order.getTableId());

        if (optionalTable.isPresent()) {
            Table table = optionalTable.get();

            Paragraph tableNameParagraph = new Paragraph("Table: " + table.getTableName(), new Font(Font.FontFamily.COURIER, 10, Font.BOLD, BaseColor.BLACK));
            tableNameParagraph.setAlignment(Element.ALIGN_LEFT);
            title.add(tableNameParagraph);
        }



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










//    @PostMapping("/dinedynamo/invoice/save")
//    public ResponseEntity<byte[]> saveInvoice(@RequestBody Order order) {
//        Optional<Order> existingOrder = orderRepository.findById(order.getOrderId());
//
//        if (existingOrder.isPresent()) {
//            Order retrievedOrder = existingOrder.get();
//
//            try {
//                byte[] pdfBytes = generatePDFBytes(retrievedOrder);
//
//                Invoice invoice = new Invoice();
//                invoice.setTotalPrice(order.getTotalPrice());
//                invoice.setPdfData(pdfBytes);
//                invoiceRepository.save(invoice);
//
//                HttpHeaders headers = new HttpHeaders();
//                headers.setContentType(MediaType.APPLICATION_PDF);
//                headers.setContentDispositionFormData("inline", "invoice.pdf");
//                return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
//            } catch (IOException | DocumentException e) {
//                e.printStackTrace();
//                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }



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
}

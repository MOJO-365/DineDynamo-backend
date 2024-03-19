package com.dinedynamo.controllers.invoice_controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.order_collections.Order;
import com.dinedynamo.collections.order_collections.OrderList;
import com.dinedynamo.collections.restaurant_collections.Restaurant;
import com.dinedynamo.collections.table_collections.Table;
import com.dinedynamo.repositories.invoice_repositories.InvoiceRepository;
import com.dinedynamo.repositories.order_repositories.DineInOrderRepository;
import com.dinedynamo.repositories.restaurant_repositories.RestaurantRepository;
import com.dinedynamo.repositories.table_repositories.TableRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
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
public class DineInInvoiceController {

    @Autowired
    private DineInOrderRepository dineInOrderRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private TableRepository tableRepository;

    @PostMapping("/dinedynamo/invoice/getinvoice")
    public ResponseEntity<byte[]> generateInvoicePDF(@RequestBody Order requestOrder) {
        try {
            List<Order> orderListForTable = dineInOrderRepository.findByTableId(requestOrder.getTableId());

            if (!orderListForTable.isEmpty()) {
                Order consolidatedOrder = consolidateOrders(orderListForTable);
                byte[] pdfBytes = generatePDFBytes(consolidatedOrder);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("inline", "invoice.pdf");

                return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException | DocumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Order consolidateOrders(List<Order> orderList) {
        if (orderList.isEmpty()) {
            return null;
        }

        Order consolidatedOrder = new Order();

        Order firstOrder = orderList.get(0);
        consolidatedOrder.setOrderId(firstOrder.getOrderId());
        consolidatedOrder.setDateTime(firstOrder.getDateTime());
        consolidatedOrder.setTableId(firstOrder.getTableId());
        consolidatedOrder.setRestaurantId(firstOrder.getRestaurantId());
        consolidatedOrder.setOrderList(consolidateOrderLists(orderList));

        return consolidatedOrder;
    }

    private List<OrderList> consolidateOrderLists(List<Order> orderList) {
        return orderList.stream()
                .map(Order::getOrderList)
                .flatMap(List::stream)
                .toList();
    }

    private byte[] generatePDFBytes(Order order) throws IOException, DocumentException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            float contentHeight = calculateContentHeight(order);
            Document document = new Document(new Rectangle(PageSize.A6.getWidth(), contentHeight), 10, 10, 10, 10);
            PdfWriter.getInstance(document, outputStream);

            document.open();

            addOrderDetailsToPDF(document, order);
            String websiteURL = "https://hello.com";
            addQRCodeToPDF(document, websiteURL);

            Paragraph web = new Paragraph("**THANK YOU FOR VISITING**", new Font(Font.FontFamily.COURIER, 10, Font.BOLD, BaseColor.BLACK));
            web.setAlignment(Element.ALIGN_CENTER);
            document.add(web);

            document.close();
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

                float additionalHeight = 100;
                return baseContentHeight + orderDetailsHeight + thankYouHeight + additionalHeight;

            default:
                return baseContentHeight + orderDetailsHeight + thankYouHeight;
        }
    }

    private float calculateOrderDetailsHeight(Order order) {
        return 250;
    }

    private float calculateThankYouHeight() {
        float thankYouHeight = 130;
        return thankYouHeight;
    }

    private void addOrderDetailsToPDF(Document document, Order order) throws DocumentException {
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
            title.setSpacingAfter(1f);

            Paragraph abnParagraph = new Paragraph("ABN:7852135457865", new Font(Font.FontFamily.COURIER, 10, Font.BOLD, BaseColor.BLACK));
            abnParagraph.setAlignment(Element.ALIGN_CENTER);
            title.add(abnParagraph);
            title.setSpacingAfter(1f);

            Paragraph locationParagraph = new Paragraph(restaurant.getRestaurantLocation(), new Font(Font.FontFamily.COURIER, 10, Font.BOLD, BaseColor.BLACK));
            locationParagraph.setAlignment(Element.ALIGN_CENTER);
            title.add(locationParagraph);
            title.setSpacingAfter(1f);

            Paragraph resPhone = new Paragraph("PHONE:(08)64606963", new Font(Font.FontFamily.COURIER, 10, Font.BOLD, BaseColor.BLACK));
            resPhone.setAlignment(Element.ALIGN_CENTER);
            title.add(resPhone);
        }

        Optional<Table> optionalTable = tableRepository.findById(order.getTableId());

        if (optionalTable.isPresent()) {
            Table table = optionalTable.get();
            Paragraph tableNameParagraph = new Paragraph("Table: " + table.getTableName(), new Font(Font.FontFamily.COURIER, 10, Font.BOLD, BaseColor.BLACK));
            tableNameParagraph.setAlignment(Element.ALIGN_LEFT);
            title.add(tableNameParagraph);
        }

        title.add(Chunk.NEWLINE);

        Paragraph taxInvoiceParagraph = new Paragraph("T A X   I N V O I C E", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK));
        taxInvoiceParagraph.setAlignment(Element.ALIGN_CENTER);
        title.add(taxInvoiceParagraph);

        document.add(title);
        addHorizontalLine(document);
        List<OrderList> orderList = order.getOrderList();
        Map<String, Map<Double, Integer>> itemNamePriceToTotalQuantity = new HashMap<>();
        Map<String, Map<Double, Double>> itemNamePriceToTotalPrice = new HashMap<>();

        for (OrderList item : orderList) {
            String itemName = item.getItemName();
            int quantity = item.getQty();
            double price = item.getItemPrice();

            // Update total quantity and total price for each item
            if (!itemNamePriceToTotalQuantity.containsKey(itemName)) {
                itemNamePriceToTotalQuantity.put(itemName, new HashMap<>());
                itemNamePriceToTotalPrice.put(itemName, new HashMap<>());
            }

            Map<Double, Integer> priceToQuantityMap = itemNamePriceToTotalQuantity.get(itemName);
            Map<Double, Double> priceToPriceMap = itemNamePriceToTotalPrice.get(itemName);

            if (!priceToQuantityMap.containsKey(price)) {
                priceToQuantityMap.put(price, 0);
                priceToPriceMap.put(price, 0.0);
            }

            priceToQuantityMap.put(price, priceToQuantityMap.get(price) + quantity);
            priceToPriceMap.put(price, priceToPriceMap.get(price) + (price * quantity));
        }

        for (String itemName : itemNamePriceToTotalQuantity.keySet()) {
            for (Double price : itemNamePriceToTotalQuantity.get(itemName).keySet()) {
                int totalQuantity = itemNamePriceToTotalQuantity.get(itemName).get(price);
                double totalPrice = itemNamePriceToTotalPrice.get(itemName).get(price);

                String itemDetailsString = String.format("%-22s\t  %-2s\t  $%-7.2f", itemName, totalQuantity, totalPrice);
                Paragraph itemDetails = new Paragraph(itemDetailsString, normalFont);
                document.add(itemDetails);
            }
        }

        double grandTotal = 0;
        double gstAmount = 0;

        for (String itemName : itemNamePriceToTotalPrice.keySet()) {
            for (Double price : itemNamePriceToTotalPrice.get(itemName).keySet()) {
                double totalPrice = itemNamePriceToTotalPrice.get(itemName).get(price);
                grandTotal += totalPrice;
            }
        }

        gstAmount = grandTotal * 0.1;
        grandTotal += gstAmount;

        Paragraph gstPar = new Paragraph(String.format("%-4s $%.2f", "GST:", gstAmount), subtitleFont);
        gstPar.setAlignment(Element.ALIGN_LEFT);
        gstPar.setIndentationLeft(172);
        document.add(gstPar);

        Paragraph grandTotalPar = new Paragraph(String.format("%-4s $%.2f", "Total(inc GST):", grandTotal), subtitleFont);
        grandTotalPar.setAlignment(Element.ALIGN_LEFT);
        grandTotalPar.setIndentationLeft(80);
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

            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());        }
    }

    private void addHorizontalLine(Document document) throws DocumentException {
        int lineLength = 38;
        String line = new String(new char[lineLength]).replace('\0', '-');
        document.add(new Paragraph(line, FontFactory.getFont(FontFactory.COURIER_BOLD, 12)));
    }

    private void addDoubleHorizontalLines(Document document) throws DocumentException {
        int lineLength = 38;
        String line = new String(new char[lineLength]).replace('\0', '-');

        Paragraph firstLine = new Paragraph(line, FontFactory.getFont(FontFactory.COURIER_BOLD, 12));
        firstLine.setSpacingBefore(0);
        firstLine.setSpacingAfter(0);
        firstLine.setLeading(0);
        document.add(firstLine);

        Paragraph secondLine = new Paragraph(line, FontFactory.getFont(FontFactory.COURIER_BOLD, 12));
        secondLine.setSpacingBefore(0);
        secondLine.setSpacingAfter(0);
        firstLine.setLeading(0);
        document.add(secondLine);
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
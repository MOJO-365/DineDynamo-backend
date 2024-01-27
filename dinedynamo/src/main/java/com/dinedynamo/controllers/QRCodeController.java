package com.dinedynamo.controllers;


import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.Menu;
import com.dinedynamo.collections.Table;
import com.dinedynamo.repositories.MenuRepository;
import com.dinedynamo.repositories.TableRepository;
import com.dinedynamo.services.QRCodeService;
import com.dinedynamo.services.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

@RestController
public class QRCodeController
{
    @Autowired
    QRCodeService qrCodeService;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    TableRepository tableRepository;

    @Autowired
    TableService tableService;

    @PostMapping(value = "dinedynamo/qr/getqr")  //, produces = MediaType.IMAGE_PNG_VALUE
    public byte[] generateQrCode(@RequestBody Table table) throws IOException {
        int width = 450; // Adjust the desired width of the QR code
        int height = 450; // Adjust the desired height of the QR code

        byte[] qrByteArray = qrCodeService.generateQrCodeImage(table.getTableId(), width, height);
        System.out.println(qrByteArray);
        return qrCodeService.generateQrCodeImage(table.getTableId(), width, height);
    }



    // This method is for the event when QR will be scanned(by the customer) for menu
    @PostMapping("/dinedynamo/qr/scanqr")
    public ResponseEntity<ApiResponse> getRestaurantMenuFromQR(@RequestParam("image") MultipartFile file) throws IOException {
        String tabelId = qrCodeService.decodeQRCode(file);

        System.out.println("TABLE ID AFTER QR SCAN: "+tabelId);
        String restaurantId = tableService.findById(tabelId).getRestaurantId();
        System.out.println("RESTAURANT ID AFTER QR SCAN: "+restaurantId);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK,"success",menuRepository.findByRestaurantId(restaurantId).orElse(null)),HttpStatus.OK);

        //return menuRepository.findByRestaurantId(restaurantId).get();

    }
}

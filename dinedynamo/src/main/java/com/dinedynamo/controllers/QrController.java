package com.dinedynamo.controllers;

import com.dinedynamo.api.ApiResponse;
import com.dinedynamo.collections.QrOrder;
import com.dinedynamo.repositories.QrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class QrController {

    @Autowired
    private QrRepository qrDataRepository;



    @PostMapping("/dinedynamo/save/qrData")
    public ResponseEntity<ApiResponse> saveQrData(@RequestBody QrOrder qrOrder) throws Exception {
        qrDataRepository.save(qrOrder);
        return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "success", null), HttpStatus.OK);
    }

    @GetMapping("/dinedynamo/get/qrData")
    public ResponseEntity<ApiResponse> getQrData(@RequestBody QrOrder qrOrder) {
        QrOrder Data = qrDataRepository.findById(qrOrder.getQrId()).orElse(null);

        if (Data != null) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.OK, "Success", Data), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, "Data not found", null), HttpStatus.NOT_FOUND);
        }
    }
}

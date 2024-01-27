package com.dinedynamo.services;

import com.dinedynamo.collections.Table;
import com.dinedynamo.repositories.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class TableService
{
    @Autowired
    TableRepository tableRepository;

    @Autowired
    QRCodeService genetateQRCodeService;

    @Autowired
    CloudinaryService cloudinaryService;


    public Table findById(String tableId){
        return tableRepository.findById(tableId).orElse(null);
    }



    // This save method will take table details, also generate QR for that table and store it in database
    public Table save(Table table) throws IOException {

        if(table == null){
            return null;
        }

        tableRepository.save(table);


        //This means the QR code for table has not been generated even for once
        if(table.getPublicIdOfQRImage() == null){
            byte[] qrByteArray = genetateQRCodeService.generateQrCodeImage(table.getTableId(), 450, 450);

            Map mapOfImage = cloudinaryService.uploadImageOnCloudinary(qrByteArray);

            table.setTableQRURL((String) mapOfImage.get("url"));
            table.setPublicIdOfQRImage((String) mapOfImage.get("public_id"));
        }

        tableRepository.save(table);
        return table;

    }



    public Table delete(Table table) throws IOException {

        cloudinaryService.deleteImageFromCloudinary(table.getPublicIdOfQRImage());

        tableRepository.delete(table);

        return table;
    }

}

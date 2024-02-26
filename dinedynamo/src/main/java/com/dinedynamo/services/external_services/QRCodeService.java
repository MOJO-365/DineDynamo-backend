package com.dinedynamo.services.external_services;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.stereotype.Service;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.web.multipart.MultipartFile;


@Service
public class QRCodeService
{

    //content = tableId

    /**
     *
     * @param content
     * @param width
     * @param height
     * @return byte[] or generated QR png image
     * @throws IOException
     * Uses zxing library and its classes to generate the qr
     * Here content should be the corresponding tableId
     */
    public byte[] generateQrCodeImage(String content, int width, int height) throws IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hintsMap = new HashMap<>();
        hintsMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix;
        try {
            bitMatrix = qrCodeWriter.encode("http://192.168.77.253:5173/qrdashboard?tableId="+content, BarcodeFormat.QR_CODE, width, height, hintsMap);


        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR code image.", e);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedImage qrImage = toBufferedImage(bitMatrix);
        try {
            ImageIO.write(qrImage, "png", outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write QR code image to output stream.", e);
        }

        return outputStream.toByteArray();
    }

    private BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(Color.BLACK);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (matrix.get(x, y)) {
                    graphics.fillRect(x, y, 1, 1);
                }
            }
        }
        return image;
    }


    //Use: when user scans QR of particular table then the tableId will be decoded from the QR and
    // the restaurantId of that table will be returned.
    public String decodeQRCode(MultipartFile multipartFile) throws IOException {
        File qrCodeimage = new File(multipartFile.getOriginalFilename());
        Files.copy(multipartFile.getInputStream(), qrCodeimage.toPath(), StandardCopyOption.REPLACE_EXISTING);
        BufferedImage bufferedImage = ImageIO.read(qrCodeimage);

        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            System.out.println("There is no QR code in the image");
            return null;
        }
    }
}

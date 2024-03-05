package com.dinedynamo.services.external_services;

import com.dinedynamo.collections.inventory_management.PurchaseOrder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendNewPasswordEmail(String recipientEmail, String newPassword, String restaurantName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Password Reset for " + restaurantName);
        String emailContent = "Hello " + restaurantName + ",\n\n" +
                "We have received a request to reset your password for your " + restaurantName + " account.\n" +
                "Your new password is: " + newPassword + "\n\n" +
                "Please ensure to keep this password confidential and do not share it with anyone.\n" +
                "If you have any questions or concerns, please feel free to contact us.\n\n" +
                "Best regards,\n" +
                "The DineDynamo Team";
        message.setText(emailContent);
        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String recipientEmail, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Password Reset Request");
        String emailContent = "Hello,\n\n" +
                "You have requested to reset your password. Please click on the link below to reset your password:\n" +
                resetLink + "\n\n" +
                "If you did not request a password reset, please ignore this email.\n\n" +
                "Best regards,\n" +
                "The DineDynamo Team";
        message.setText(emailContent);
        mailSender.send(message);
    }

    public boolean sendBugQueryMail(String subject ,String restaurantName, String queryDescription){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("dinedynamo2024@gmail.com");
        message.setSubject(subject);

        String emailContent =
                "You have received a bug query from " + restaurantName + " account.\n" +
                "" + queryDescription + "\n\n" +
                "Best regards,\n" +
                ""+restaurantName;

        message.setText(emailContent);
        mailSender.send(message);

        return true;

    }

    public boolean sendMailForPurchaseOrder(PurchaseOrder purchaseOrder, String restaurantName){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Purchase Order from: "+restaurantName);
        message.setTo(purchaseOrder.getSupplierDetails().getSupplierEmailId());

        String emailContent = "Purchase order details: "+ "\n"
                + "Item Name: " + purchaseOrder.getItemName() + "\n"
                + "Quantity: " + purchaseOrder.getQuantity() + "\n"
                + "Measurement Units: " + purchaseOrder.getMeasurementUnits() + "\n"
                + "Description: " + purchaseOrder.getDescription() + "\n"
                + "Date of Purchase Request: " + purchaseOrder.getDateOfPurchaseRequest();



        message.setText(emailContent);
        mailSender.send(message);
        System.out.println("PURCHASE ORDER MAIL SENT TO SUPPLIER");
        return true;
    }

    public boolean sendMailForCancelPurchaseOrder(PurchaseOrder purchaseOrder, String restaurantName){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Cancel Purchase Order from: "+restaurantName);
        message.setTo(purchaseOrder.getSupplierDetails().getSupplierEmailId());


        String emailContent = "Purchase order details: "+ "\n"
                + "Item Name: " + purchaseOrder.getItemName() + "\n"
                + "Quantity: " + purchaseOrder.getQuantity() + "\n"
                + "Measurement Units: " + purchaseOrder.getMeasurementUnits() + "\n"
                + "Description: " + purchaseOrder.getDescription() + "\n"
                + "Date of Purchase Request: " + purchaseOrder.getDateOfPurchaseRequest() + "\n\n"
                + "Above order has been cancelled from the restaurant";


        message.setText(emailContent);
        mailSender.send(message);
        System.out.println("PURCHASE ORDER MAIL SENT TO SUPPLIER");
        return true;
    }
}


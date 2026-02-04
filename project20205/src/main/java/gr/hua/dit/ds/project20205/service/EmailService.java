package gr.hua.dit.ds.project20205.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendWelcomeEmail(String toEmail, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("property_management@hua.gr");
        message.setTo(toEmail);
        message.setSubject("Επιτυχής Εγγραφή - Property Management System");
        message.setText("Γεια σου " + username + ",\n\nΚαλώς ήρθες στο σύστημά μας! Η εγγραφή σου ολοκληρώθηκε με επιτυχία.");
        
        mailSender.send(message);
    }
}
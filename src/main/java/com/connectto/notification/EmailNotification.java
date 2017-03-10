package com.connectto.notification;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by Serozh on 7/6/2016.
 */
public class EmailNotification {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "mail.connectto.com");
        props.put("mail.smtp.socketFactory.port", "465");//465
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465 ");//"465";//SMTP server- mail.connecttomail.com 587

        final String user="serozh@connectto.com";
        final String password="Hi`1915a24";//"!Bill90@";

// 4 test
        String to="seryozha.hovhannisyan@gmail.com";
        String cc="seryozha.hovhannisyan@gmail.com";

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user,password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("serozh@connectto.com"));
            message.setRecipients(Message.RecipientType.TO,  InternetAddress.parse(to));
            message.setRecipients(Message.RecipientType.CC,  InternetAddress.parse(cc));

            message.setSubject("SSL Email test");
            message.setText("test");

            Transport.send(message);

            System.out.println("Your email has been sent");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
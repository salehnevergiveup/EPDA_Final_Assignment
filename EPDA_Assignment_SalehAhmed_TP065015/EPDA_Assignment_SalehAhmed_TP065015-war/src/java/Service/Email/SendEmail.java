/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service.Email;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author saleh
 */
public class SendEmail {

    private String to;
    private String subject;
    private String content = "";

    private final Integer PORT = 465;
    private final Boolean SSL_ENABLED = true;
    private final Boolean AUTH_ENABLED = true;

    private final String FROM = "salehaldhaheri09@gmail.com";
    private final String HOST = "smtp.gmail.com";
    private final String PASSWORD = "lepq qlva jxkt laub";

    public SendEmail to(String email) {
        this.to = email;
        return this;
    }

    public SendEmail subject(String subject) {
        this.subject = subject;
        return this;
    }

    public SendEmail salute() {
        this.content += "Hello Thier!!";
        return this;
    }

    public SendEmail content(String content) {
        this.content += "\n" + content;
        return this;
    }

    public SendEmail signature() {
        content += "\n Saleh Ahmed";
        content += "\n Thank you";
        return this;
    }

    public void send() {
        Properties properties = System.getProperties();

        // Setup mail server properties
        properties.put("mail.smtp.host", this.HOST);
        properties.put("mail.smtp.port", this.PORT);
        properties.put("mail.smtp.ssl.enable", this.SSL_ENABLED);
        properties.put("mail.smtp.auth", this.AUTH_ENABLED);

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM, PASSWORD);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(this.FROM));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(this.to));

            message.setSubject(this.subject);

            message.setText(this.content);

            Transport.send(message);
        } catch (MessagingException mex) {
            System.out.println(mex.getMessage());
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import servlets.NameAutocompleteServlet;

/**
 *
 * @author jacopo
 */
public class EmailSender
{
    private EmailSender()
    {}
    
    /**
     * Manda una email con testo pari a text ad un recipient pari ad email, utilizzando
     * la casella di posta di eatbit.
     * @param email L'indirizzo email a cui mandare l'email.
     * @param text Il testo della email damandare.
     * @param subject Oggetto della email.
     * @throws ServletException
     * @throws SocketException
     * @throws UnknownHostException 
     */
    final public static void sendEmail(String email, String text, String subject) throws ServletException, SocketException, UnknownHostException {
        try {
                final String username = "eatbitnoreply@gmail.com";
                final String password = "eatbitpassword";
                Properties props = System.getProperties();
                props.setProperty("mail.smtp.host", "smtp.gmail.com");
                props.setProperty("mail.smtp.port", "465");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.debug", "true");
                //crea sessione autenticata
                Session session = Session.getDefaultInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication
                            getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
                //Create a new message
                Message msg = new MimeMessage(session);
                //Set the FROM and TO fields –
                msg.setFrom(new InternetAddress(username + ""));
                msg.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(email, false));
                msg.setSubject(subject);
                msg.setText(text);
                msg.setSentDate(new Date());
                Transport transport = session.getTransport("smtps");
                transport.connect("smtp.gmail.com", 465, username, password);
                transport.sendMessage(msg, msg.getAllRecipients());
                transport.close();
        } catch (MessagingException ex) {
            Logger.getLogger(NameAutocompleteServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            throw new ServletException(ex.toString());
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.DbManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *Servlet che manda ad un indirizzo email un url per la verifica di un account, 
 * l'url contiene il token di verifica e la email in questione, al momento è possibile
 * fare questa verifica solo da localhost, a causa dell'url; inoltre l'url
 * contatta una servlet di verifica mappata su "/verify".
 * @author jacopo
 */
@WebServlet(name = "VerifyByEmailServlet", urlPatterns = {"/VerifyByEmailServlet"})
public class SendVerificationEmailServlet extends HttpServlet {
private DbManager manager;
    
    @Override
    public void init() throws ServletException {
        // inizializza il DBManager dagli attributi di Application
        this.manager = (DbManager)super.getServletContext().getAttribute("dbmanager");
    }
    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        if(request.getParameter("email")!=null)
        {
            try
            {
                String token=manager.getUserVerificationToken(request.getParameter("email"));
                if(token!=null)
                {
                    final String username = "eatbitnoreply@gmail.com";
                    final String password = "eatbitpassword";
                    Properties props = System.getProperties();
                    props.setProperty("mail.smtp.host", "smtp.gmail.com");
                    props.setProperty("mail.smtp.port", "465");
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.starttls.enable", "true");
                    props.put( "mail.debug", "true" );
                    //crea sessione autenticata
                    Session session = Session.getDefaultInstance(props, new
                    Authenticator(){
                    @Override
                    protected PasswordAuthentication
                    getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(username, password);
                    }
                    });
                    //Create a new message
                    Message msg = new MimeMessage(session);
                    //Set the FROM and TO fields –
                    msg.setFrom(new InternetAddress(username + ""));
                    msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(request.getParameter("email"),false));
                    msg.setSubject("eatbit verification");
                    msg.setText("This is a verification email sent from eatbit, to "
                            + "activate your account please visit this url:"+'\n'
                            + "http://localhost:8084/eatbit/verify?token="
                            + token
                            +"&email="+request.getParameter("email"));
                    msg.setSentDate(new Date());
                    Transport transport = session.getTransport("smtps");
                    transport.connect ("smtp.gmail.com", 465, username, password);
                    transport.sendMessage(msg, msg.getAllRecipients());
                    transport.close();
                }
            }
            catch(Exception ex)
            {
                Logger.getLogger(NameAutocompleteServlet.class.getName()).log(Level.SEVERE, null, ex);
                throw new ServletException("email servlet exception");
            }
        }
    }
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.DbManager;
import database.Restaurant;
import database.Review;
import database.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jacopo
 */
@WebServlet(name = "RegisterUserServlet", urlPatterns = {"/RegisterUserServlet"})
public class RegisterUserServlet extends HttpServlet {

    private DbManager manager;

    @Override
    public void init() throws ServletException {
        // inizializza il DBManager dagli attributi di Application
        this.manager = (DbManager) super.getServletContext().getAttribute("dbmanager");
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String name = request.getParameter("name");
            String surname = request.getParameter("surname");
            String nickname = request.getParameter("nickname");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String avatarPath = "/img/user_default.png";
            PrintWriter out = response.getWriter();
            if (name != null && surname != null && nickname != null && email != null && password != null) {
                int res;
                User user = new User();
                user.setName(name);
                user.setSurname(surname);
                user.setNickname(nickname);
                user.setEmail(email);
                user.setPassword(password);
                user.setAvatar_path(avatarPath);
                res = manager.registerUser(user);
                if (res == 0)//se la registrazione è andata a buon fine
                {
                    sendVerificationEmail(email);
                    out.println("0");
                } else if (res == 1) {
                    ;//azione da fare se errore 1
                    out.println("1");
                } else if (res == 2) {
                    ;//tipo 2
                    out.println("2");
                } else if (res == 3) {
                    ;//tipo 3
                    out.println("3");
                }
            } else {
                out.println("-1");//missing parameters
            }
            out.flush();
        } catch (Exception ex) {
            Logger.getLogger(NameAutocompleteServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            throw new ServletException(ex);
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

    private void sendVerificationEmail(String email) throws ServletException {
        try {
            String token = manager.getUserVerificationToken(email);
            if (token != null) {
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
                msg.setSubject("eatbit verification");
                msg.setText("This is a verification email sent from eatbit, to "
                        + "activate your account please visit this url:" + '\n'
                        + "http://localhost:8084/eatbit/verify?token="
                        + token
                        + "&email=" + email);
                msg.setSentDate(new Date());
                Transport transport = session.getTransport("smtps");
                transport.connect("smtp.gmail.com", 465, username, password);
                transport.sendMessage(msg, msg.getAllRecipients());
                transport.close();
            }
        } catch (SQLException | MessagingException ex) {
            Logger.getLogger(NameAutocompleteServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            throw new ServletException(ex.toString());
        }
    }
}

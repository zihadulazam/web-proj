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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
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
                switch (res)
                {
                //se la registrazione è andata a buon fine
                    case 0:
                        sendVerificationEmail(user.getId(),email);
                        out.println("0");
                        break;
                    case 1:
                        ;//azione da fare se errore 1
                        out.println("1");
                        break;
                    case 2:
                        ;//tipo 2
                        out.println("2");
                        break;
                    case 3:
                        ;//tipo 3
                        out.println("3");
                        break;
                    default:
                        break;
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

    private void sendVerificationEmail(int id, String email) throws ServletException, SocketException {
        try {
            String token = manager.getUserVerificationToken(id);
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
                //mando la mail con ip settato a localhost, la verifica funzionerà
                //solo dalla stessa macchina
                /*msg.setText("This is a verification email sent from eatbit, to "
                        + "activate your account please visit this url:" + '\n'
                        + "http://localhost:8084/eatbit/verify?token="
                        + token
                        + "&id=" + Integer.toString(id));*/
                //setta l'ip di questo computer nella mail, la verifica
                //funzionerà da pc diversi da questo, ma potrebbero esserci problemi
                //in caso di nat o network complicati
                msg.setText("This is a verification email sent from eatbit, to "
                        + "activate your account please visit this url:" + '\n'
                        + "http://"
                        + getIp()
                        +":8084/eatbit/verify?token="
                        + token
                        + "&id=" + Integer.toString(id));
                
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
   
    private String getIp()
    {
        // This try will give the Public IP Address of the Host.
        try
        {
            URL url = new URL("https://api.ipify.org");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String ipAddress = new String();
            ipAddress = (in.readLine()).trim();
            /* IF not connected to internet, then
             * the above code will return one empty
             * String, we can check it's length and
             * if length is not greater than zero, 
             * then we can go for LAN IP or Local IP
             * or PRIVATE IP
             */
            if (!(ipAddress.length() > 0))
            {
                try
                {
                    InetAddress ip = InetAddress.getLocalHost();
                    System.out.println((ip.getHostAddress()).trim());
                    return ((ip.getHostAddress()).trim());
                }
                catch(Exception ex)
                {
                    return "ERROR";
                }
            }
            System.out.println("IP Address is : " + ipAddress);

            return (ipAddress);
        }
        catch(Exception e)
        {
            // This try will give the Private IP of the Host.
            try
            {
                InetAddress ip = InetAddress.getLocalHost();
                System.out.println((ip.getHostAddress()).trim());
                return ((ip.getHostAddress()).trim());
            }
            catch(Exception ex)
            {
                return "ERROR";
            }
        }
    }
}


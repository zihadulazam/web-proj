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
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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
import utility.EmailSender;
import utility.IpFinder;
import static utility.IpFinder.getLocalIp;

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
            response.setContentType("text/plain");
            PrintWriter out = response.getWriter();
            String name = request.getParameter("name");
            String surname = request.getParameter("surname");
            String nickname = request.getParameter("nickname");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String avatarPath = "/img/user_default.png";
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
                        out.write("0");
                        break;
                    case 1:
                        ;//azione da fare se errore 1
                        out.write("1");
                        break;
                    case 2:
                        ;//tipo 2
                        out.write("2");
                        break;
                    case 3:
                        ;//tipo 3
                        out.write("3");
                        break;
                    default:
                        break;
                }
            } else {
                out.write("-1");//missing parameters
            }
            out.flush();
        } catch (IOException | SQLException | ServletException ex) {
            Logger.getLogger(RegisterUserServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
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

    private void sendVerificationEmail(int id, String email) throws ServletException, SocketException, UnknownHostException {
        try {
            String token = manager.getUserVerificationToken(id);
            if (token != null) {
                /*3 modi di avere ip: localhost, pubblico, ip locale
                mando la mail con ip settato a localhost, la verifica funzionerà
                solo dalla stessa macchina*/
                String begin= "This is a verification email sent from eatbit, to "
                        + "activate your account please visit this url:" + '\n';
                String t1= "http://localhost:8084/eatbit/verify?token="
                        + token
                        + "&id=" + Integer.toString(id);
                
                /*setta l'ip di questo computer nella mail, la verifica
                funzionerà da pc diversi da questo, ma potrebbero esserci problemi
                in caso di nat o network complicati*/
                String t2= "http://"
                        + IpFinder.getPublicIp()
                        +":8084/eatbit/verify?token="
                        + token
                        + "&id=" + Integer.toString(id);
                //setta come ip da contattare l'ip locale della macchina
                String t3= "http://"
                        + IpFinder.getLocalIp()
                        +":8084/eatbit/verify?token="
                        + token
                        + "&id=" + Integer.toString(id);
                EmailSender.sendEmail(email, begin+t3, "user verification");
            }
        } catch (SQLException ex) {
            Logger.getLogger(RegisterUserServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            throw new ServletException(ex.toString());
        }
    }
}


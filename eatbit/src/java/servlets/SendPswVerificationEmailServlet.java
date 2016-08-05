/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.DbManager;
import database.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utility.EmailSender;
import utility.IpFinder;

/**
 *Servlet per mandare via email un token di cambio psw all'utente, risponderà
 * 1 se id e user corrispondono e se user è loggato in, 0 altrimenti.
 * @author jacopo
 */
@WebServlet(name = "SendPswVerificationEmailServlet", urlPatterns =
{
    "/SendPswVerificationEmailServlet"
})
public class SendPswVerificationEmailServlet extends HttpServlet
{

    private DbManager manager;

    @Override
    public void init() throws ServletException {
        // inizializza il DBManager dagli attributi di Application
        this.manager = (DbManager) super.getServletContext().getAttribute("dbmanager");
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
            throws ServletException, IOException {
            String stringId= request.getParameter("id_user");
            response.setContentType("text/plain");
            PrintWriter out = response.getWriter();
            User user = (User) request.getSession().getAttribute("user");
            System.out.println(stringId);
            if (stringId!=null && user!=null) {
                int id= Integer.parseInt(stringId);
                if (id==user.getId()) {
                    sendPasswordVerificationEmail(id,user.getEmail());
                    out.write("1");
                } else {
                    out.write("0");
                }
            
            }
            else
                out.write("0");
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
    
    private void sendPasswordVerificationEmail(int id, String email) throws ServletException, SocketException, UnknownHostException
    {
        try {
            String token = manager.addToUsersToChangePassword(id);
            String jsp= "changePassword.jsp";
            if (token != null) {
                /*3 modi di avere ip: localhost, pubblico, ip locale
                mando la mail con ip settato a localhost, la verifica funzionerà
                solo dalla stessa macchina*/
                String begin= "This is a password verification email sent from eatbit, to "
                        + "change your account please visit this url:" + '\n';
                String t1= "http://localhost:8084/eatbit/"
                        + jsp
                        +"?token="
                        + token
                        + "&id=" + Integer.toString(id);
                
                /*setta l'ip di questo computer nella mail, la verifica
                funzionerà da pc diversi da questo, ma potrebbero esserci problemi
                in caso di nat o network complicati*/
                String t2= "http://"
                        + IpFinder.getPublicIp()
                        +":8084/eatbit/"
                        + jsp
                        + "?token="
                        + token
                        + "&id=" + Integer.toString(id);
                //setta come ip da contattare l'ip locale della macchina
                String t3= "http://"
                        + IpFinder.getLocalIp()
                        +":8084/eatbit/"
                        + jsp
                        + "?token="
                        + token
                        + "&id=" + Integer.toString(id);
                EmailSender.sendVerificationEmail(email, begin+t3);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RegisterUserServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            throw new ServletException(ex.toString());
        }
    }
}

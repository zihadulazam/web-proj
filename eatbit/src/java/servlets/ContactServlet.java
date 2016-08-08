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
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utility.EmailSender;
import utility.IpFinder;

/**
 *Usato per il form "contact" nel footer, che permette all'utente di mandare una
 *email all'amministrazione del sito.
 * L'account a cui arrivano le email è:
 * eatbitsupp@gmail.com, con password:
 * eatbitpassword.
 * I parametri richiesti da questa servlet sono "email" e "text", cioè
 * la email dell'utente e il testo che l'utente ha dato come input.
 * @author jacopo
 */
@WebServlet(name = "ContactServlet", urlPatterns =
{
    "/ContactServlet"
})
public class ContactServlet extends HttpServlet
{

    private DbManager manager;
    final private static String SENDTO= "eatbitsupp@gmail.com";

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
            String email= request.getParameter("email");
            String text= request.getParameter("text");
            response.setContentType("text/plain");
            PrintWriter out = response.getWriter();
            try
            {
                if (email!=null && text!=null) 
                {
                    {
                        text= "This text has been sent from a user wich wishes to contact the"
                                + " eatbit administration, its contact information is: "
                                + email +".\n"
                                + "The text is:\n"
                                + text;
                        EmailSender.sendEmail(ContactServlet.SENDTO, text, "contact");
                        out.write("1");
                    } 
                }
                else
                    out.write("-1");
            }
            catch( MessagingException ex)
            {
                 Logger.getLogger(ContactServlet.class.getName()).log(Level.SEVERE, null, ex);       
                 out.write("0");
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

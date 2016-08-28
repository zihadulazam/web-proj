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
 *Servlet per mandare via email un token di cambio psw all'utente, risponderà
 * 1 in caso di successo,0 se non è stato possibile svolgere l'operazione a 
 * causa di eccezioni,-1 se manca il parametro necessario (id_user) o non è loggato
 * , -2 se id fornito non corrisponde all'id di User nella sessione.
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
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        String stringId= request.getParameter("id_user");
        if (stringId!=null) 
        {
            User user = (User) request.getSession().getAttribute("user");
            int id= Integer.parseInt(stringId);
            if (id==user.getId()) 
            {
                try
                {
                    sendPasswordVerificationEmail(id,user.getEmail(),request);
                    out.write("1");
                }
                catch(ServletException | SocketException | UnknownHostException | MessagingException | SQLException ex)
                {
                    Logger.getLogger(SendPswVerificationEmailServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
                    out.write("0");
                }
            }
            else 
                out.write("-2");
        }
        else
            out.write("-1");
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
            processRequest(request,response);
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
        processRequest(request, response);
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
    
    /**
     * Manda una email ad un utente con un certo id.
     * @param id Id dell'utente.
     * @param email Email dell'utente.
     * @throws ServletException
     * @throws SocketException
     * @throws UnknownHostException
     * @throws MessagingException
     * @throws SQLException 
     */
    private void sendPasswordVerificationEmail(final int id, final String email,HttpServletRequest req ) throws ServletException, SocketException, UnknownHostException, MessagingException, SQLException
    {
            String token = manager.addToUsersToChangePassword(id);
            String jsp= "passwordReset";
            /*3 modi di avere ip: localhost, pubblico, ip locale
            mando la mail con ip settato a localhost, la verifica funzionerà
            solo dalla stessa macchina*/
            String begin= "This is a password verification email sent from eatbit, to "
                    + "change your account please visit one of these urls (we have sent you many because one or more of them may not work properly depending"
                    + " on the network you are):\n\n";
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
            //url con stessa base dell url tramite cui la servlet è stata contattata
            StringBuffer url = req.getRequestURL();
            String uri = req.getRequestURI();
            String ctx = req.getContextPath();
            String t4 = url.substring(0, url.length() - 
                    uri.length() + ctx.length()) + "/" + 
                    jsp + "?token=" + token + 
                    "&id=" + Integer.toString(id);
            EmailSender.sendEmail(email, begin+t1+"\n\n"+t2+"\n\n"+t3+"\n\n"+t4, "password change verification");
    }
}

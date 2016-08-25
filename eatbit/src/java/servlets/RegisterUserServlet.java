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
 *Servlet per registrazione utente, si occupa di mandare una mail di verifica, i
 * parametri necessari sono:
 * name
 * surname
 * nickname
 * password
 * ritorna: 1 in caso di successo,0 se non è andato a buon fine a causa di eccezioni
 * o se la spedizione della email non è andata a buon fine,-1 se manca un 
 * parametro, -2 se esiste un utente con quella email, -3 con quel nick
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
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        try {
            String name = request.getParameter("name");
            String surname = request.getParameter("surname");
            String nickname = request.getParameter("nickname");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String avatarPath = "img/avatar/avatar.png";
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
                    case -1:
                        ;//esiste già email
                        out.write("-2");
                        break;
                    case -2:
                        ;//esiste già nick
                        out.write("-3");
                        break;
                    case -3://eccezione o altri motivi a livello db
                        out.write("0");
                        break;
                    default://se è >=0 è un id utente, è andato a buon fine
                        try
                        {
                            sendVerificationEmail(user.getId(),email);
                            out.write("1");//successo
                        }
                        catch(Exception ex)
                        {
                            Logger.getLogger(RegisterUserServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
                            manager.unregisterUser(res);
                            out.write("0");//insuccesso causa email non spedita
                        }
                }
            } else {
                out.write("-1");//missing parameters
            }
            out.flush();
        } catch (Exception ex) {
            Logger.getLogger(RegisterUserServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
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

    private void sendVerificationEmail(int id, String email) throws ServletException, SocketException, UnknownHostException, MessagingException, SQLException {
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
                EmailSender.sendEmail(email, begin+t1+"\n\n"+t2+"\n\n"+t3, "user verification");
            }
    }
}


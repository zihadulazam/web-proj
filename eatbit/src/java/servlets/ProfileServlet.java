
package servlets;

import database.DbManager;
import database.Notification;
import database.Review;
import database.User;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author mario
 */
@WebServlet(name = "ProfileServlet", urlPatterns = {"/ProfileServlet"})
public class ProfileServlet extends HttpServlet {
    
    DbManager manager;
    
    @Override
    public void init(){
        // inizializza il DBManager 
        this.manager = (DbManager)super.getServletContext().getAttribute("dbmanager");
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
        //prendo la sessione
        HttpSession session = request.getSession();
        
        ArrayList<Review> listReview = null;
        ArrayList<Notification> listNotification = null;
        
        //prendo l'user della sessione
        User user = (User)session.getAttribute("user");
        //se non esiste una sessione aperta redirigo sulla pagina di login-->lo fa il filtro
        //faccio cmq un controllo
        if (user == null) {
            // metto il messaggio di errore come attributo di Request, così nel JSP si vede il messaggio
            request.setAttribute("message", "Not LOGGED IN !");
            //redirigo alla landingPage
            RequestDispatcher rd = request.getRequestDispatcher("/index.html");           
            rd.forward(request, response);
        }       

        //prendo la tipologia di utente - magari servirà più avanti
        int type = user.getType();

        //provo a interrogare il DB per ottenere le info
        try{
            listNotification = manager.getUserNotifications(user.getId());
            listReview = manager.getUserReviews(user);
        } catch (SQLException ex) {
            Logger.getLogger(ProfileServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        session.setAttribute("listNotification", listNotification);
        session.setAttribute("numberNotification", listNotification.size());
        session.setAttribute("listReview", listReview);
        session.setAttribute("numberReview", listReview.size());
        
        response.sendRedirect(request.getContextPath() + "/userProfile.jsp"); // wherever you wanna redirect this page
        
    }
        

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
        processRequest(request, response);
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

}


package servlets;

import database.DbManager;
import database.Notification;
import database.Restaurant;
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

        session.setAttribute("user", user);
        
        if (type==10){
            //raccolgo dati per l'admin
            
            
            request.getRequestDispatcher("/adminProfile.jsp").forward(request, response);
            
        }else{
            //raccolgo dati per utente
            ArrayList<Review> listReview = null;
            ArrayList<Notification> listNotification = null;
            ArrayList<Restaurant> listRestaurants = null;
            
            //provo a interrogare il DB per ottenere le info
            try{
                listNotification = manager.getUserNotifications(user.getId());
                listReview = manager.getUserReviews(user.getId());
                listRestaurants = manager.getRestaurantsByIdOwner(user.getId());

            } catch (SQLException ex) {
                Logger.getLogger(ProfileServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
                  
            request.setAttribute("listNotification", listNotification);
            request.setAttribute("numberNotification", listNotification.size());
            request.setAttribute("listReview", listReview);
            request.setAttribute("numberReview", listReview.size());
            request.setAttribute("listRestaurants", listRestaurants);
            request.setAttribute("numberRestaurants", listRestaurants.size());       

            request.getRequestDispatcher("/userProfile.jsp").forward(request, response);
        }
        
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

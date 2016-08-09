
package servlets;

import database.DbManager;
import database.PhotoNotification;
import database.Restaurant;
import database.ReviewNotification;
import database.User;
import database.contexts.AttemptContext;
import database.contexts.OwnUserContext;
import database.contexts.PhotoContext;
import database.contexts.ReplyContext;
import database.contexts.ReviewContext;
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

        try {
            HttpSession session = request.getSession();
            User user = (User)session.getAttribute("user");           
            manager.setUserToAdmin(user.getId());
            if (user == null) {
                // metto il messaggio di errore come attributo di Request, così nel JSP si vede il messaggio
                request.setAttribute("message", "Not LOGGED IN !");
                //redirigo alla landingPage
                RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
                rd.forward(request, response);
            }
            int type = user.getType();
            session.setAttribute("user", user);
            if (type==2){
                //raccolgo dati per l'admin
                ArrayList<AttemptContext> ristorantiAttesa = null;
                ArrayList<ReplyContext> risposteConfermare = null;
                ArrayList<PhotoContext> fotoSegnalate = null;
                ArrayList<ReviewContext> reviewSegnalate = null;
                
                //provo a interrogare il DB per ottenere le info                
                ristorantiAttesa = manager.getRestaurantsRequests(5);
                risposteConfermare = manager.getRepliesToBeConfirmed(5);
                fotoSegnalate = manager.getReportedPhotos(5);
                reviewSegnalate = manager.getReportedReviews(5);

                response.setContentType("text/plain");
                request.setAttribute("ristorantiAttesa", ristorantiAttesa);
                request.setAttribute("risposteConfermare", risposteConfermare);
                request.setAttribute("fotoSegnalate", fotoSegnalate);
                request.setAttribute("reviewSegnalate", reviewSegnalate);

                request.getRequestDispatcher("/adminProfile.jsp").forward(request, response);

                
            }else{
                //raccolgo dati per utente
                ArrayList<ReviewContext> listReview = null;
                ArrayList<PhotoNotification> listPhotoNotification = null;
                //ArrayList<ReviewNotification> listReviewNotification = null;
                ArrayList<Restaurant> listRestaurants = null;
                
                OwnUserContext userContext = null;
                
                //provo a interrogare il DB per ottenere le info
                userContext = manager.getUserContext(user.getId());
                listReview = userContext.getReviewContext();
                listRestaurants = manager.getRestaurantsByIdOwner(user.getId());
                listPhotoNotification = manager.getAllUserPhotoNotifications(type);

                response.setContentType("text/plain");
                request.setAttribute("listPhotoNotification", listPhotoNotification);
                request.setAttribute("numberListPhotoNotification", listPhotoNotification.size());
                request.setAttribute("listReview", listReview);
                request.setAttribute("numberReview", listReview.size());
                request.setAttribute("listRestaurants", listRestaurants);
                request.setAttribute("numberRestaurants", listRestaurants.size());                    
                
                request.getRequestDispatcher("/userProfile.jsp").forward(request, response);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProfileServlet.class.getName()).log(Level.SEVERE, null, ex);
            request.getRequestDispatcher("/error.jsp").forward(request, response);
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

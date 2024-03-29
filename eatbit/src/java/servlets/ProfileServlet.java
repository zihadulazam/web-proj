package servlets;

import database.DbManager;
import database.PhotoNotification;
import database.Reply;
import database.Restaurant;
import database.ReviewNotification;
import database.User;
import database.contexts.AttemptContext;
import database.contexts.OwnUserContext;
import database.contexts.PhotoContext;
import database.contexts.ReplyContext;
import database.contexts.RestaurantContext;
import database.contexts.ReviewContext;
import java.io.IOException;
import java.io.PrintWriter;
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
            PrintWriter out = response.getWriter();
            HttpSession session = request.getSession();
            User user = (User)session.getAttribute("user");   
            
            int type = user.getType();
            session.setAttribute("user", user);
            if (type==2){
                //raccolgo dati per l'admin
                ArrayList<AttemptContext> ristorantiAttesa = null;
                ArrayList<ReplyContext> risposteConfermare = null;
                ArrayList<PhotoContext> listPhotoNotification = null;
                ArrayList<ReviewContext> listReviewNotification = null;
                
                //provo a interrogare il DB per ottenere le info                
                ristorantiAttesa = manager.getRestaurantsRequests(5);
                risposteConfermare = manager.getRepliesToBeConfirmed(5);
                listPhotoNotification = manager.getReportedPhotos(5);
                listReviewNotification = manager.getReportedReviews(5);

                response.setContentType("text/plain");
                request.setAttribute("ristorantiAttesa", ristorantiAttesa);
                request.setAttribute("risposteConfermare", risposteConfermare);
                request.setAttribute("listPhotoNotification", listPhotoNotification);
                request.setAttribute("listReviewNotification", listReviewNotification);
                
//                response.getWriter().println(risposteConfermare.get(0).getUser().getName());
//                response.getWriter().println(risposteConfermare.get(0).getReview().getDescription());
//                response.getWriter().println(risposteConfermare.get(0).getReply().getDescription());
                request.getRequestDispatcher("/WEB-INF/adminProfile.jsp").forward(request, response);

                
            }else{
                //raccolgo dati per utente
                ArrayList<ReviewContext> listReview = null;
                ArrayList<PhotoNotification> listPhotoNotification = null;
                ArrayList<ReviewNotification> listReviewNotification = null;
                ArrayList<RestaurantContext> listRestaurants = new ArrayList<>();
                ArrayList<Restaurant> listRist = null;
                
                OwnUserContext userContext = null;
                
                //provo a interrogare il DB per ottenere le info
                listReviewNotification = manager.getUserReviewNotifications(user.getId(),4);
                userContext = manager.getUserContext(user.getId());
                listReview = userContext.getReviewContext();
                listRist = manager.getRestaurantsByIdOwner(user.getId());
                listPhotoNotification = manager.getUserPhotoNotifications(user.getId(),4);
                //creo l'array di contesti ristoranti per avere più info
                for(Restaurant r:listRist){
                    RestaurantContext x = manager.getRestaurantContext(r.getId());
                   // out.println(x.getPhotos().get(0).getPath());
                    listRestaurants.add(x);
                }
                
                response.setContentType("text/plain");
                request.setAttribute("listPhotoNotification", listPhotoNotification);
                request.setAttribute("listReviewNotification", listReviewNotification);
                request.setAttribute("listReview", listReview);
                request.setAttribute("listRestaurants", listRestaurants);                
                
                request.getRequestDispatcher("/WEB-INF/userProfile.jsp").forward(request, response);

            }
        } catch (SQLException ex) {
            Logger.getLogger(ProfileServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);
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

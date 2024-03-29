package servlets;

import database.DbManager;
import database.contexts.AttemptContext;
import database.contexts.PhotoContext;
import database.contexts.ReplyContext;
import database.contexts.ReviewContext;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author andrei
 */
@WebServlet(name = "GetAllAdmin", urlPatterns = {"/GetAllAdmin"})
public class GetAllAdmin extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
               response.setContentType("text/html;charset=UTF-8");
        try{
            HttpSession session = request.getSession();
            
           //raccolgo dati per l'admin
                ArrayList<AttemptContext> ristorantiAttesa = null;
                ArrayList<ReplyContext> risposteConfermare = null;
                ArrayList<PhotoContext> listPhotoNotification = null;
                ArrayList<ReviewContext> listReviewNotification = null;
                
                //provo a interrogare il DB per ottenere le info                
                ristorantiAttesa = manager.getAllRestaurantsRequests();
                risposteConfermare = manager.getAllRepliesToBeConfirmed();
                listPhotoNotification = manager.getAllReportedPhotos();
                listReviewNotification = manager.getAllReportedReviews();

                response.setContentType("text/plain");
                request.setAttribute("ristorantiAttesa", ristorantiAttesa);
                request.setAttribute("risposteConfermare", risposteConfermare);
                request.setAttribute("listPhotoNotification", listPhotoNotification);
                request.setAttribute("listReviewNotification", listReviewNotification);
            
            request.getRequestDispatcher("/WEB-INF/allAdminNotify.jsp").forward(request, response);
            
        }catch(ServletException | IOException | SQLException ex){
            Logger.getLogger(ProfileServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            request.setAttribute("error1", "Errore interno");
            request.getRequestDispatcher("/WEB-INF/errorModifyRest.jsp").forward(request, response);
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

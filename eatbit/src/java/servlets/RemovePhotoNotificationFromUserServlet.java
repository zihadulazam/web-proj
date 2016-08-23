package servlets;

import database.DbManager;
import database.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *Servlet per permettere all'utente di segnalare al sistema che ha preso visione
 * della notifica e che desidera eliminarla.
 * Risponderà con 1 se è andata a buon fine, -1 se l'utente non è loggato,
 * 0 se non è andata a buon fine per altri motivi(p.e. eccezione a causa di 
 * parametro malformato).
 * I parametri necessari sono:
 * id_notification, id della notifica da eliminare
 * @author jacopo
 */
@WebServlet(name = "RemovePhotoNotificationFromUserServlet", urlPatterns =
{
    "/RemovePhotoNotificationFromUserServlet"
})
public class RemovePhotoNotificationFromUserServlet extends HttpServlet
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
        PrintWriter out = response.getWriter();
        try {
            response.setContentType("text/plain");            
            User user = (User) request.getSession().getAttribute("user");
            if (user != null){ 
                manager.removePhotoNotification(user.getId(),Integer.parseInt(request.getParameter("notifyId")));
                out.write("1");
            }else{
                out.write("-1");
                out.flush();
            }
                
        } catch (NumberFormatException | SQLException ex) {
            Logger.getLogger(RemovePhotoNotificationFromUserServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            out.write("0");
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

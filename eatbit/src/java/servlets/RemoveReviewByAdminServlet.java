package servlets;

import database.DbManager;
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
import utility.FileDeleter;

/**
 * Servlet per permettere all'admin di rimuovere una review che era stata segnalata.
 * Rimuove la foto associata alla review dal filesystem.
 * Manderà come risposta 1 se la rimozione è andata a buon fine, 0 se c'è stata 
 * una eccezione, -1 se manca un parametro.
 * @author jacopo
 */
@WebServlet(name = "RemoveReviewByAdminServlet", urlPatterns =
{
    "/RemoveReviewByAdminServlet"
})
public class RemoveReviewByAdminServlet extends HttpServlet
{
    private DbManager manager;
    private String dirName;
    
    @Override
    public void init() throws ServletException {
        // inizializza il DBManager dagli attributi di Application
        this.manager = (DbManager) super.getServletContext().getAttribute("dbmanager");
        dirName= (String) super.getServletContext().getInitParameter("uploadPhotosDir");
        if (dirName == null) 
          throw new ServletException("missing uploadPhotosDir parameter in web.xml for servlet addReviewServlet");
        dirName = getServletContext().getRealPath(dirName).replace("build/", "").replace("build\\", "");
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
        String stringId;
        String photoPath;
        String path;
        try {
            stringId = request.getParameter("id_review");
            
            if (stringId!=null) {
                photoPath = manager.removeReview(Integer.parseInt(stringId));
                //cancello foto da filesystem
                path = dirName + photoPath.replace("img/photos/", "/");
                FileDeleter.deleteFile(path);
                out.write("1");
            }
            else
                out.write("-1");
        } catch ( SQLException ex) {
            Logger.getLogger(RemoveReviewByAdminServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
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

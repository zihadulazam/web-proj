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

/**
 *Servlet per permette all'admin di rimuovore una foto da quelle reportate (quindi
 * non sarà più considerata segnalata ma esisterà ancora).
 * Manderà come risposta 1 se il metodo di rimozione ha terminato, 0 se c'è stata
 * una eccezione, -1 se il parametro manca.
 * @author jacopo
 */
@WebServlet(name = "UnreportPhotoByAdminServlet", urlPatterns =
{
    "/UnreportPhotoByAdminServlet"
})
public class UnreportPhotoByAdminServlet extends HttpServlet
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
        try {
            String stringId = request.getParameter("id_photo");
            if (stringId!=null) {
                manager.unreportPhoto(Integer.parseInt(stringId));
                out.write("1");
            }
            else
                out.write("-1");
            out.flush();
                
        } catch (NumberFormatException | SQLException ex) {
            Logger.getLogger(UnreportPhotoByAdminServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
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

package servlets;

import database.DbManager;
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
import org.json.simple.JSONArray;

/**
 *Servlet per l'autocompletamento di luoghi, ritorna -1 se manca il parametro,
 * 0 se ci sono state eccezioni, altrimenti un array json contenente le stringhe
 * dei luoghi.
 * @author jacopo
 */
@WebServlet(name = "LocationAutoCompleteServlet", urlPatterns = {"/LocationAutoCompleteServlet"})
public class LocationAutoCompleteServlet extends HttpServlet {

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
        String user_keys = request.getParameter("keys");
        if(user_keys!=null)
        {
            try 
            {
                ArrayList<String> luoghi = manager.autoCompleteLocation(user_keys);
                JSONArray jluoghi=new JSONArray();
                for(String elemento:luoghi)
                    jluoghi.add(elemento);
                response.setContentType("application/json");
                response.getWriter().write(jluoghi.toString());
            } 
            catch (SQLException ex) 
            {
             Logger.getLogger(LocationAutoCompleteServlet.class.getName()).log(Level.SEVERE, null, ex);
             response.getWriter().write("0");
            }
        }
        else
            response.getWriter().write("-1");
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

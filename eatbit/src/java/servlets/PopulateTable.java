package servlets;

import database.DbManager;
import database.contexts.RestaurantContext;
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

/**
 *Servlet per la ricerca di un ristorante. Può essere una ricerca per luogo, nome
 * o cucina, o longitudine, latitudine, nome o cucina. Qualsiasi di questi parametri
 * può essere null. Se la ricerca è luogo + nome/cucina o luogo o nome devono essere
 * diversi da null se si vuole ricevere risultati.
 * Parametri:
 * luogo
 * nome
 * longitude
 * latitude
 * @author User
 */
@WebServlet(name = "PopulateTable", urlPatterns = {"/PopulateTable"})
public class PopulateTable extends HttpServlet {
    static final double DISTANCESEARCHINKM=2;
    private DbManager manager;

    @Override
    public void init() throws ServletException {
        // inizializza il DBManager dagli attributi di Application
        this.manager = (DbManager) super.getServletContext().getAttribute("dbmanager");
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
        try{
            ArrayList<RestaurantContext> list=null;
            String location=request.getParameter("luogo");
            String name =request.getParameter("name");
            String sLongitude= request.getParameter("longitude");
            String sLatitude= request.getParameter("latitude");
            if("".equals(location))
                location=null;
            if("".equals(name))
               name=null;
            //se longitude o latitude non sono specificate non è una ricerca nelle vicinanze dell'utente
            if(sLongitude==null||sLatitude==null)
                list=manager.searchRestaurant(location, name);
            else
            {
                double longitude= Double.parseDouble(sLongitude);
                double latitude= Double.parseDouble(sLatitude);
                list=manager.searchRestaurantNear(longitude, latitude,DISTANCESEARCHINKM, name);
            }
            request.setAttribute("list", list);
            request.getRequestDispatcher("/DataTable.jsp").forward(request, response);
            
        } catch (SQLException | NumberFormatException ex) {
            Logger.getLogger(PopulateTable.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            request.getRequestDispatcher("/error.jsp").forward(request, response);
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

}

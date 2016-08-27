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
 *Permette all'admin di accettare la richiesta di creazione o possesso di un ristorante
 * fatta da parte di un utente.
 * Manderà come risposta 1 se l'accettazione è andata a buon fine, 0 se c'è stata una
 * eccezione, -1 se manca uno dei 2 parametri:
 * id_user: id dell'utente che fa la request
 * id_restaurant: restaurant relativo alla request.
 * @author jacopo
 */
@WebServlet(name = "AcceptRestaurantRequestByAdminServlet", urlPatterns =
{
    "/AcceptRestaurantRequestByAdminServlet"
})
public class AcceptRestaurantRequestByAdminServlet extends HttpServlet
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
            String stringIdRestaurant= request.getParameter("id_restaurant");
            String stringIdRequester= request.getParameter("id_user");
            //System.out.println(stringIdRequester);
            //System.out.println(stringIdRestaurant);
            if (stringIdRestaurant!=null && stringIdRequester!=null) {
                manager.acceptRestaurantRequest(Integer.parseInt(stringIdRequester),Integer.parseInt(stringIdRestaurant));
                out.write("1");
            }
            else{
                out.write("-1");
            }

        } catch (NumberFormatException | SQLException ex) {
            Logger.getLogger(AcceptRestaurantRequestByAdminServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
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

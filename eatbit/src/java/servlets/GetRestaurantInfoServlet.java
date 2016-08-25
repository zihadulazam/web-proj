package servlets;

import database.DbManager;
import database.PriceRange;
import database.User;
import database.contexts.RestaurantContext;
import java.io.IOException;
import java.sql.SQLException;
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
 *Raccoglie i dati di un ristorante e reindirizza sulla
 * modifyRestaurant.jsp per attuare modifiche
 * @author andrei
 */
@WebServlet(name = "GetRestaurantInfoServlet", urlPatterns = {"/GetRestaurantInfoServlet"})
public class GetRestaurantInfoServlet extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        
        try{
            //prendo la sessione
            HttpSession session = request.getSession();
            User user = (User)session.getAttribute("user");   
            //verifico che l'utente sia loggato
            if (user == null) {
                // metto il messaggio di errore come attributo di Request, così nel JSP si vede il messaggio
                request.setAttribute("message", "Not LOGGED IN !");
                //redirigo alla landingPage
                RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
                rd.forward(request, response);
            }
            //prendo l'ID del ristorante da modificare
            int id_restaurant = Integer.parseInt(request.getParameter("restaurant_id"));
            //raccolgo le informazioni
            RestaurantContext restaurant = manager.getRestaurantContext(id_restaurant);
            
            //aggiungo il ristorante alla richiesta
            request.setAttribute("restaurant", restaurant);
            //dirigo verso la pagina di modifica del ristorante
            request.getRequestDispatcher("/WEB-INF/modifyRestaurant.jsp").forward(request, response);
            
        }catch(ServletException | IOException | SQLException e){
            Logger.getLogger(ProfileServlet.class.getName()).log(Level.SEVERE, e.toString(), e);
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

package servlets;

import database.DbManager;
import database.User;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Integer.max;
import static java.lang.Integer.min;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet per permettere ad un utente di votare un ristorante (da 1 a 5) senza
 * fare una recensione.
 * Ritorna 1 se il voto dell'utente ha avuto effetto ritorna 0 se c'è stata una eccezione o
 * non esiste un ristorante con quell'id, ritorna -1 se l'utente non ha fatto login 
 * (non c'è User in session) o se manca un parametro, -2 se l'utente non poteva
 * votare xk ha già votato o fatto una recensione meno di 24h fa, -3 se il ristorante
 * con quell'id non esiste o se è il proprietario del ristorante.
 * parametri:
 * vote 
 * id_restaurant
 *
 * @author jacopo
 */
@WebServlet(name = "UserVoteServlet", urlPatterns = {"/UserVoteServlet"})
public class UserVoteServlet extends HttpServlet {

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
            String voteString= request.getParameter("vote");
            String restString= request.getParameter("id_restaurant");
            if (voteString!=null && restString!=null) {
                User user = (User) request.getSession().getAttribute("user");
                int id_rest = Integer.parseInt(restString);
                int voto = Integer.parseInt(voteString);
                voto = min(voto, 5);//pulisco il voto in caso di eventuali errori 
                voto = max(voto, 1);
                int res= manager.addUserVoteOnRestaurant(voto, user.getId(), id_rest);
                switch (res)
                {
                    case 0:
                        out.write("-2");//24h
                        break;
                    case -1:
                        out.write("-3");//proprietario
                        break;
                    default:
                        out.write("1");
                        break;
                }
            }
            else
                out.write("-1");
        } catch (NumberFormatException | SQLException ex) {
            Logger.getLogger(UserVoteServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            out.write("0");
        }
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
        processRequest(request,response);
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

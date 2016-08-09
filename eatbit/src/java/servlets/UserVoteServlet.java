/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.DbManager;
import database.Restaurant;
import database.Review;
import database.User;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Integer.max;
import static java.lang.Integer.min;
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
 * Servlet per permettere ad un utente di votare un ristorante (da 1 a 5) senza
 * fare una recensione.
 * Ritorna 1 se il voto dell'utente ha avuto effetto ritorna 0 se c'è stata una eccezione o
 * non esiste un ristorante con quell'id, ritorna -1 se l'utente non ha fatto login 
 * (non c'è User in session) o se manca un parametro, -2 se l'utente non poteva
 * votare xk ha già votato o fatto una recensione meno di 24h fa o se il ristorante
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
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        try {
            User user = (User) request.getSession().getAttribute("user");
            String voteString= request.getParameter("vote");
            String restString= request.getParameter("id_restaurant");
            if (user != null && voteString!=null && restString!=null) {
                int id_rest = Integer.parseInt(restString);
                int voto = Integer.parseInt(voteString);
                voto = min(voto, 5);//pulisco il voto in caso di eventuali errori 
                voto = max(voto, 1);
                out.write(manager.addUserVoteOnRestaurant(voto, user.getId(), id_rest)>0?"1":"-2");
            }
            else
                out.write("-1");
            out.flush();
        } catch (NumberFormatException | SQLException ex) {
            Logger.getLogger(UserVoteServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            out.write("0");
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

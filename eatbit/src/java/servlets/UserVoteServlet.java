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
 * Ritorna un intero maggiore di 0 se il voto dell'utente ha avuto effetto, questo
 * numero rappresenta il nuovo valore del voto; ritorna 0 se l'utente non poteva
 * votare perchè ha votato lo stesso ristorante meno di 24h ore fa, ritorna -1
 * se l'utente non ha fatto login (non c'è User in session).
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
        try {
            response.setContentType("text/plain");
            PrintWriter out = response.getWriter();
            User user = (User) request.getSession().getAttribute("user");
            if (user != null) {
                int id_rest = Integer.parseInt(request.getParameter("id_restaurant"));
                int voto = Integer.parseInt(request.getParameter("vote"));
                voto = min(voto, 5);//pulisco il voto in caso di eventuali errori 
                voto = max(voto, 1);
                out.write(Integer.toString(manager.addUserVoteOnRestaurant(voto, user.getId(), id_rest)));
            }
            else
                out.write("-1");
            out.flush();
        } catch (NumberFormatException | SQLException ex) {
            Logger.getLogger(UserVoteServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            throw new ServletException(ex);
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.DbManager;
import database.Restaurant;
import database.Review;
import java.io.IOException;
import java.io.PrintWriter;
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
 * Servlet per la verifica della email di un utente attraverso il metodo get, ci
 * si aspetta i parametri token e email nella request, in base al successo o al
 * fallimento il controllo verrà passato a 2 jsp diverse, al momento success.jsp
 * e failure.jsp.
 *
 * @author jacopo
 */
@WebServlet(name = "VerifyUserServlet", urlPatterns = {"/verify"})
public class VerifyUserServlet extends HttpServlet {

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
            String stringId= request.getParameter("id");
            String token = request.getParameter("token");
            if (stringId != null && token != null) {
                int id= Integer.parseInt(stringId);
                //se la verifica è andata a buon fine passo il controllo a una jsp che conferma il successo
                //altrimenti a una jsp che comunica il fallimento della verifica
                if (manager.verifyUser(id, token)) {
                    request.getRequestDispatcher("/success.jsp").forward(request, response);
                } else {
                    request.getRequestDispatcher("/failure.jsp").forward(request, response);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(NameAutocompleteServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            throw new ServletException("sql exception");
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

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
 *Servlet per permettere ad un utente di votare un ristorante (da 0 a 5) senza
 * fare una recensione.
 * @author jacopo
 */
@WebServlet(name = "UserVoteServlet", urlPatterns = {"/UserVoteServlet"})
public class UserVoteServlet extends HttpServlet {
    private DbManager manager;
    
    @Override
    public void init() throws ServletException {
        // inizializza il DBManager dagli attributi di Application
        this.manager = (DbManager)super.getServletContext().getAttribute("dbmanager");
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
        try
        {
            int id_rest= Integer.parseInt(request.getParameter("id_restaurant"));
            int voto= Integer.parseInt(request.getParameter("vote"));
            User user= (User) request.getSession().getAttribute("user");
            if(user!=null)
                manager.addUserVoteOnRestaurant(voto, user.getId(), id_rest);
        }
        catch(NumberFormatException ex)
        {
            Logger.getLogger(NameAutocompleteServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(NameAutocompleteServlet.class.getName()).log(Level.SEVERE, null, ex);
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
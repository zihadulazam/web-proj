/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.DbManager;
import database.Restaurant;
import database.Review;
import database.contexts.RestaurantContext;
import database.contexts.ReviewContext;
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
 * Servlet che mette in request i 5 top ristoranti per voto, 5 per numero
 * recensioni e le ultime 5 review. Passa poi il controllo a /index.jsp.
 *
 * @author jacopo
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {

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
            ArrayList<RestaurantContext> top5RestContextsByValue = manager.getTop5RestaurantContextsByValue();
            ArrayList<RestaurantContext> top5RestContextsByReviews = manager.getTop5RestaurantContextsByReviewsCounter();
            ArrayList<ReviewContext> last5ReviewsContexts = manager.getLast5ReviewContexts();
            
            //**********testing values **********
            /*Review provaR=new Review();
            provaR.setName("Prova 123");
            last5Reviews.add(provaR);
            Restaurant provaRisto=new Restaurant(0, "Sushoku", "dsdf", "url",4, 2, 3, 4, 5, 6, true);
            top5RestByReviews.add(provaRisto);
            top5RestByValue.add(provaRisto);*/
            //::::::::::End Testing::::::::::::
            request.setAttribute("top5RestByValue", top5RestContextsByValue);
            request.setAttribute("top5RestByReviews", top5RestContextsByReviews);
            request.setAttribute("last5Reviews", last5ReviewsContexts);
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(HomeServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
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

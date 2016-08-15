/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.DbManager;
import database.Restaurant;
import database.contexts.RestaurantContext;
import java.io.BufferedReader;
import java.io.FileReader;
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
import org.apache.derby.client.am.SqlException;
import org.json.simple.JSONArray;

/**
 *
 * @author User
 */
@WebServlet(name = "PopulateTable", urlPatterns = {"/PopulateTable"})
public class PopulateTable extends HttpServlet {

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
            String location=request.getParameter("luogo");
            String name =request.getParameter("name");
            
            ArrayList<RestaurantContext> list=manager.searchRestaurant(location, name);
            request.setAttribute("list", list);
            request.getRequestDispatcher("/DataTable.jsp").forward(request, response);
            
        } catch (SQLException ex) {
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

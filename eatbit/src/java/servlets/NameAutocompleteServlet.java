/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.Coordinate;
import database.DbManager;
import database.Restaurant;
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

import org.json.simple.JSONArray;


/**
 *Servlet per l'autocomplete del nome, se manca un parametro manda -1, se ha avuto
 * un eccezione manda 0, altrimenti un array json contenente le stringhe
 * da rappresentare.
 * @author zihadul
 */
@WebServlet(name = "NameAutocompleteServlet", urlPatterns = {"/NameAutocompleteServlet"})
public class NameAutocompleteServlet extends HttpServlet {
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
        String user_keys=request.getParameter("keys");
        if(user_keys!=null)
        {
            try 
            {
                ArrayList<String> nomi = manager.autoCompleteName(user_keys);
                JSONArray jnomi=new JSONArray();
                for(String elemento:nomi)
                    jnomi.add(elemento);
                response.setContentType("application/json");
                response.getWriter().write(jnomi.toString());
            } 
            catch (SQLException ex) 
            {
             Logger.getLogger(NameAutocompleteServlet.class.getName()).log(Level.SEVERE, null, ex);
             response.getWriter().write("0");
            }
        }
        else
        {
            response.getWriter().write("-1");
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

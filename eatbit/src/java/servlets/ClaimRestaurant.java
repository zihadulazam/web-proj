/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.DbManager;
import database.User;
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
 *Servlet per permettere ad un utente di fare il claim di un ristorante, 
 * parametri:
 * id_restaurant un intero
 * text_claim testo che l'utente ha immesso per giustificare il claim
 * Risponde con 1 se è andato a buon fine, 0 se vi è stata una eccezione, -1
 * se manca un parametro.
 * @author jacopo
 */
@WebServlet(name = "ClaimRestaurant", urlPatterns =
{
    "/ClaimRestaurant"
})
public class ClaimRestaurant extends HttpServlet
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
            throws ServletException, IOException
    {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        String sId_rest= request.getParameter("id_rest");
        String text_claim= request.getParameter("text_claim");
        if(sId_rest!=null && text_claim!=null)
        {
            try
            {
                manager.addClaim(((User)request.getSession().getAttribute("user")).getId()
                        ,Integer.parseInt(sId_rest), text_claim, 1);
                out.write("1");
            }
            catch (SQLException ex)
            {
                Logger.getLogger(ClaimRestaurant.class.getName()).log(Level.SEVERE, null, ex);
                out.write("0");
            }
        }
        else
            out.write("-1");
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
            throws ServletException, IOException
    {
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
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>

}

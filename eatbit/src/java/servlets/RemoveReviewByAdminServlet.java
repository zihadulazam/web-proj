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
 * Servlet per permettere all'admin di rimuovere una review che era stata segnalata.
 * n.b. Al momento la foto della review NON viene dal db.
 * TODO aggiungere rimozione foto review nel db.
 * TODO aggiungere passaggio controllo a servlet o metodo che rimuove foto dal filesystem.
 * Manderà come risposta 1 se la rimozione è andata a buon fine, 0 se l'utente
 * non aveva effettuato il login o se non era un admin.
 * @author jacopo
 */
@WebServlet(name = "RemoveReviewByAdminServlet", urlPatterns =
{
    "/RemoveReviewByAdminServlet"
})
public class RemoveReviewByAdminServlet extends HttpServlet
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
            throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        try {
            User user = (User) request.getSession().getAttribute("user");
            String stringId= request.getParameter("id_review");
            //verifico che admin sia loggato e che sia effettivamente un utente di tipo admin
            if (user != null && user.getType()==2 && stringId!=null) {
                String photoToRemove = manager.removeReview(Integer.parseInt(stringId));
                //rimozione foto via utility per rimuovere file da filesystem
                out.write("1");
            }
            else
                out.write("-1");
            out.flush();
                
        } catch (NumberFormatException | SQLException ex) {
            Logger.getLogger(RemoveReviewByAdminServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            out.write("0");
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

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
 *Permette all'admin di accettare la richiesta di creazione o possesso di un ristorante
 * fatta da parte di un utente.
 * Manderà come risposta 1 se l'accettazione è andata a buon fine, 0 se c'è stata una
 * eccezione, -1 se l'utente (l'admin) non aveva effettuato il login o se non era un admin 
 * o se manca uno dei 2 parametri:
 * id_user: id dell'utente che fa la request
 * id_restaurant: restaurant relativo alla request.
 * @author jacopo
 */
@WebServlet(name = "AcceptRestaurantRequestByAdminServlet", urlPatterns =
{
    "/AcceptRestaurantRequestByAdminServlet"
})
public class AcceptRestaurantRequestByAdminServlet extends HttpServlet
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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ReportPhotoServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ReportPhotoServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        try {
            User user = (User) request.getSession().getAttribute("user");

            //verifico che admin sia loggato e che sia effettivamente un utente di tipo admin
            if (user != null && user.getType()==2) {
                manager.acceptRestaurantRequest(user.getId(),Integer.parseInt(request.getParameter("RA_ID")));
                out.write("1");
            }
            else{
                out.write("0");
                out.flush();
            }

        } catch (NumberFormatException ex) {
            Logger.getLogger(AcceptRestaurantRequestByAdminServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            out.write("0");
        }
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

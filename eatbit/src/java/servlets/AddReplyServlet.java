/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.DbManager;
import database.Reply;
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
 * Servlet per aggiungere una reply da parte di un ristoratore ad una review,
 * attraverso il metodo POST, e usa i parametri description e
 * id_review.
 * Ritorna true se la reply è stata aggiunta, falso se non è stata aggiunta perchè
 * l'utente non ha fatto login, o non è il proprietario del ristorante relative a questa
 * review, o la reply per questa review esiste già.
 *
 * @author jacopo
 */
@WebServlet(name = "AddReplyServlet", urlPatterns = {"/AddReplyServlet"})
public class AddReplyServlet extends HttpServlet {

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
        try {
            response.setContentType("text/plain");
            PrintWriter out = response.getWriter();
            User user = (User) request.getSession().getAttribute("user");
            //controllo che utente sia loggato in
            if(user!=null)
            {
                Reply reply = new Reply();
                reply.setDescription(request.getParameter("description"));
                reply.setDate_creation(null);
                reply.setId_review(Integer.parseInt(request.getParameter("id_review")));
                reply.setId_owner(user.getId());
                reply.setDate_validation(null);
                reply.setId_validator(0);
                reply.setValidated(false);
                out.write(Boolean.toString(manager.addReply(reply)));
            }
            else
                out.write("false");
            out.flush();
        } catch (NumberFormatException | SQLException ex) {
            Logger.getLogger(AddReplyServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
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

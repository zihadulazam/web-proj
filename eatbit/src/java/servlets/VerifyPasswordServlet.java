package servlets;

import database.DbManager;
import database.User;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *Servlet per la verifica del cambio password dell'utente, richiede in POST
 * i parametri: 
 * id id dell'utente
 * token token che era stato fornito all'utente via email
 * oldPassword vecchia password
 * newPassword nuova password
 * Verrà verificata l'esistenza dell'utente, la correttezza del token,
 * e in caso di successo sarà passato il controllo a success.jsp, altrimenti failure.jsp.
 * @author jacopo
 */
@WebServlet(name = "VerifyPasswordServlet", urlPatterns =
{
    "/VerifyPasswordServlet"
})
public class VerifyPasswordServlet extends HttpServlet
{

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String stringId= request.getParameter("id");
            String token = request.getParameter("token");
            String newPassword= request.getParameter("password");
            if (stringId != null && stringId.compareTo("null")!=0 && token != null && newPassword!=null)
            {
                int id= Integer.parseInt(stringId);
                
                //recupero lo user per avere l'email
                User user= manager.getUserById(id);
                //se non esiste utente con quell'id mando a failure
                if(user==null)//non esiste utente con quell id
                    request.getRequestDispatcher("/WEB-INF/failure.jsp").forward(request, response);
                
                //l'utente esiste, controllo che il token sia valido
                //cambio la password e mando a success se è valido, altrimenti a failure
                if (manager.verifyPasswordChangeToken(id, token)) 
                {
                    manager.modifyUserPassword(id, newPassword);
                    request.getRequestDispatcher("/home").forward(request, response);
                } 
                else 
                    request.getRequestDispatcher("/WEB-INF/failure.jsp").forward(request, response);
            }
            else//missing param
                request.getRequestDispatcher("/WEB-INF/failure.jsp").forward(request, response);
        } catch (NumberFormatException | SQLException ex) {
            Logger.getLogger(VerifyUserServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);
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
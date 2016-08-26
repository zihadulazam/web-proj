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
 * Ritorna 1 se l'aggiunta della reply è andata a buon fine, 0 se ci sono state
 * eccezioni, -1 se mancavano parametri o l'utente non era loggato, -2 se l'utente
 * non è il propritario del ristorante o se ha già fatto una reply per questa review.
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
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        try {
            
            String description= request.getParameter("reply_text");
            String stringIdRev= request.getParameter("id_review");    
            
            out.println(description + " " + stringIdRev);

            //controllo che utente sia loggato in
            if(description!=null && stringIdRev!=null)
            {
                User user = (User) request.getSession().getAttribute("user");
                Reply reply = new Reply();
                reply.setDescription(description);
                reply.setDate_creation(null);
                reply.setId_review(Integer.parseInt(stringIdRev));
                reply.setId_owner(8);
                reply.setId_owner(user.getId());
                reply.setDate_validation(null);
                reply.setId_validator(-1);
                reply.setValidated(false);
                out.write(manager.addReply(reply)?"1":"-2");
                manager.unreportReview(Integer.parseInt(stringIdRev));
                request.setAttribute("titolo", "Pubblicazione risposta");
                request.setAttribute("status", "ok");
                request.setAttribute("description", "Pubblicazione andata a buon fine");
                request.getRequestDispatcher("/WEB-INF/info_1.jsp").forward(request, response);
            }
            else
                out.write("-1");
            out.flush();
        } catch (NumberFormatException | SQLException ex) {
            Logger.getLogger(AddReplyServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            out.write("0");
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        doPost(request, response);
    }
}

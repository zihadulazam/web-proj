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
 * Servlet per inserire o modificare il like di un utente ad una recensione,
 * attraverso il metodo get vanno forniti l'id della review e il tipo di like.
 * Viene eseguito un controllo in sessione per vedere se esiste l'attributo
 * "user", perchè solo gli utenti registrati possono votare.
 * Ritorna in plaintext 2 se esisteva già un like e il valore è stato cambiato,
 * 1 se il like non esisteva ed è stato aggiunto, 0 se ci sono
 * state eccezioni, -1 se manca un parametro o l'utente non ha fatto login, -2
 * se l'utente ha già fatto like a questa review, se la review non esiste o se
 * l'utente sta tentando di fare like ad una sua review.
 *
 * @author jacopo
 */
@WebServlet(name = "UserLikeDislikeReviewServlet", urlPatterns = {"/UserLikeDislikeReviewServlet"})
public class UserLikeDislikeReviewServlet extends HttpServlet {

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
            String revString= request.getParameter("id_review");
            String likeString= request.getParameter("like_type");
            
            if (user != null && revString!=null && likeString!=null)
            {
                int review_id = Integer.parseInt(revString);
                int like_type = Integer.parseInt(likeString);
                //se il like non è 0 o 1 lo setto come like
                if(!(like_type==0 || like_type==1))
                    like_type=1;
                out.write(""+manager.addLike(review_id, like_type, user.getId()));
                //like_type: 0 per dislike, 1 per like
            }
            else
                out.write("-1");
                
        } catch (NumberFormatException | SQLException ex) {
            Logger.getLogger(UserLikeDislikeReviewServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
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

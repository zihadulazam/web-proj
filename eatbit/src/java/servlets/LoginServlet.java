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
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author User
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    private DbManager manager;
    
    @Override
    public void init() throws ServletException {
        // inizializza il DBManager dagli attributi di Application
        this.manager = (DbManager)super.getServletContext().getAttribute("dbmanager");
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
        String email = request.getParameter("emailorNickname");
        String password = request.getParameter("password");
        
        // controllo nel DB se esiste un utente con lo stesso username + password
        User user = null;
      
        String msg = " ";
        response.setContentType("text/plain");  // content type of the response so that jQuery knows what it can expect.
        response.setCharacterEncoding("UTF-8"); 
        
        
        try {
            user =manager.loginUserByEmailOrNickname(email, password);
        } catch (SQLException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        // se non esiste, ridirigo verso pagina di login con messaggio di errore
        if (user == null) {
            // metto il messaggio di errore come attributo di Request, così nel JSP si vede il messaggio
            msg="errore";

        } else {
            // imposto l'utente connesso come attributo di sessione
            // per adesso e' solo un oggetto String con il nome dell'utente, ma posso metterci anche un oggetto User
            // con, ad esempio, il timestamp di login
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            /*
            session.setAttribute("user_name", user.getName());
            session.setAttribute("user_surname",user.getSurname());
            session.setAttribute("user_nickname",user.getNickname());
            session.setAttribute("user_avatar",user.getAvatar_path());*/
            msg="loggato";
        }
        response.getWriter().write(msg); // Write response body.
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description: servlet for login";
    }// </editor-fold>

}

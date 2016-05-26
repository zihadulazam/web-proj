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
@WebServlet(name = "RegisterServlet", urlPatterns = {"/RegisterServlet"})
public class RegisterServlet extends HttpServlet {

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
        
        User user = null;
        user.setName(request.getParameter("name"));
        user.setSurname(request.getParameter("surname"));
        user.setNickname(request.getParameter("nickname"));
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("password"));
        
        
       
<<<<<<< HEAD
       try {
=======
       try
       {
>>>>>>> 8fac148314c2ea849ba2bcc12b01e2db23aa640f
           if (manager.registerUser(user)==0) {
               
               HttpSession session = request.getSession(true);
               session.setAttribute("user", user);
               
               // metto il messaggio di errore come attributo di Request, così nel JSP si vede il messaggio
               request.setAttribute("message", "Username/password non esistente !");
               RequestDispatcher rd = request.getRequestDispatcher("/index.html");
               rd.forward(request, response);
               
           } else {
               
               // imposto l'utente connesso come attributo di sessione
               // per adesso e' solo un oggetto String con il nome dell'utente, ma posso metterci anche un oggetto User
               // con, ad esempio, il timestamp di login
               
               
               // mando un redirect alla servlet che carica i prodotti
               response.sendRedirect(request.getContextPath() + "/userProfile.jsp");
           }
<<<<<<< HEAD
       } catch (SQLException ex) {
=======
       } catch (SQLException ex)
       {
>>>>>>> 8fac148314c2ea849ba2bcc12b01e2db23aa640f
           Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
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
    }
}

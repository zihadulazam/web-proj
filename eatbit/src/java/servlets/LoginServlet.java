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
 *Servlet per il login, ritorna 1 se il login ha avuto successo, -1 se manca un 
 * parametro, 0 se il login nn è riuscito. (per utente non esistente, passowrd 
 * sbagliata, eccezione).
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
        String email = request.getParameter("emailOrNickname");
        String password = request.getParameter("password");
        response.setContentType("text/plain"); 
        PrintWriter out= response.getWriter();
        
        if(email!=null && password !=null)
        {
            //verifico utente
            User user;
            try 
            {
                user =manager.loginUserByEmailOrNickname(email, password);
                if(user==null)//il login nn ha funzinato (xk nn esiste o la psw è sbagliata)
                    out.write("0");
                else//login ha funzionato, metto user in session
                {
                    request.getSession(true).setAttribute("user", user);
                    out.write("1");
                }
            } 
            catch (SQLException ex) 
            {
                Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
                out.write("0");
            }
        }
        else
            out.write("-1");
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

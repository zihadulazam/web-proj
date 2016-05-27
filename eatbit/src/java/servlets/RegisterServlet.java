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
        
        User user = new User();
        user.setName(request.getParameter("name"));
        user.setSurname(request.getParameter("surname"));
        user.setNickname(request.getParameter("nickname"));
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("regPassword"));
        //
        //set avatar default path
        //
        user.setAvatar_path("img/avater/avater.png");
        
        int res;
        String msg;
        response.setContentType("text/plain");  // content type of the response so that jQuery knows what it can expect.
        response.setCharacterEncoding("UTF-8");
       
        try
        {
            res=manager.registerUser(user);
            
            if (res==0) {
                //andato buon fine
                //avvio e imposto session attribiut
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                msg="registrato";

            } else {
                if(res==1){
                    //esiste già email
                    msg="errore-email";
                }
                else{
                    if(res==2){
                        //esiste già nickname
                        msg="errore-nickname";
                    }
                    else
                    {
                        msg="errore";
                    }
                }
            }
            //return msg
            response.getWriter().write(msg); 
        }
        catch (SQLException ex)
        {
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
        return "Short description: servlet for registration";
    }
}

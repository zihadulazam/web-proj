/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import database.DbManager;
import database.User;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.UUID;
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
 * @author andrei
 */
@WebServlet(name = "ModifyProfileServlet", urlPatterns = {"/ModifyProfileServlet"})
public class ModifyProfileServlet extends HttpServlet {
    
    public String dirName;
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
        response.setContentType("text/plain");
        //prendo la sessione
        HttpSession session = request.getSession();
        //variabili per le modifiche al profilo privato
        String _name = null;
        String _surname = null;
        //prendo l'user della sessione
        User user = (User)session.getAttribute("user");
        //se non esiste una sessione aperta redirigo sulla pagina di login-->lo fa il filtro
        //faccio cmq un controllo
        if (user == null) {
            // metto il messaggio di errore come attributo di Request, così nel JSP si vede il messaggio
            request.setAttribute("message", "Not LOGGED IN !");
            //redirigo alla landingPage
            RequestDispatcher rd = request.getRequestDispatcher("/index.html");           
            rd.forward(request, response);
        } 
        
        session.setAttribute("user", user);
        PrintWriter out = response.getWriter();
        //creo il realpath della directory di salvataggio avatar
        String strRealPath = request.getSession().getServletContext().getRealPath("") + "img\\avater";
        out.println(strRealPath);        
        
        dirName = strRealPath;
         // Use an advanced form of the constructor that specifies a character
            // encoding of the request (not of the file contents) and a file
            // rename policy.
            MultipartRequest multi = new MultipartRequest(request, dirName, 10*1024*1024, "ISO-8859-1", new DefaultFileRenamePolicy());
            out.println("PARAMS:");
            //prendo i parametri passati dagli input.text
            Enumeration params = multi.getParameterNames();            
            //stampo parametri in caso di mancato forward
            while (params.hasMoreElements()) {
                String name = (String)params.nextElement();
                String value = multi.getParameter(name);
                switch(name){
                    case "name": _name = value; out.println(name + "=" + _name); break;
                    case "surname": _surname = value; out.println(name + "=" + _surname); break;
                }
            }
            
            try {
                //CAMBIO NOME COGNOME UTENTE e NICKNAME
                manager.modifyUserNameSurname(user.getId(), _name, _surname);
                out.println("superata modifica ");
            } catch (SQLException ex) {
                Logger.getLogger(ModifyProfileServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            }
            out.println();
            out.println("avatar_path: " + user.getAvatar_path());
            //stampo parametri del file caricato in caso di mancato forward
            out.println("FILE:");
            Enumeration files = multi.getFileNames();
            while (files.hasMoreElements()) {
                String name = (String)files.nextElement();
                String filename = multi.getFilesystemName(name);
                String originalFilename = multi.getOriginalFileName(name);
                String type = multi.getContentType(name);
                
                File f = multi.getFile(name); //file caricato
                //cambio il nome del file per non avere collisioni
                String photoPath = f.getParent()+"\\"+UUID.randomUUID().toString()+"."+getExtension(filename);
                File f2 = new File(photoPath);
                f.renameTo(f2);     
                try {
                    //CAMBIO FOTO UTENTE
                    manager.modifyUserPhoto(user.getId(), photoPath);
                    out.println("name: " + name);
                } catch (SQLException ex) {
                    Logger.getLogger(ModifyProfileServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                //stampo qualche parametro 
                out.println("name: " + name);
                out.println("filename: " + filename);
                out.println("originalFilename: " + originalFilename);
                out.println("type: " + type);

                if (f != null) {
                    out.println("f.toString(): " + f.toString());
                    out.println("f.getName(): " + f.getName());
                    out.println("f.exists(): " + f.exists());
                    out.println("f.length(): " + f.length());
                }
                out.println();
            }
            
            
            //forward della richiesta
            response.sendRedirect(request.getHeader("referer"));    
            
        }
    
    private String getExtension(String name){
        
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
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

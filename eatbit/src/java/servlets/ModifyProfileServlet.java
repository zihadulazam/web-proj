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
import javax.servlet.ServletConfig;
import utility.FileDeleter;
//import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author andrei
 */
@WebServlet(name = "ModifyProfileServlet", urlPatterns = {"/ModifyProfileServlet"})
public class ModifyProfileServlet extends HttpServlet {
    private String dirName;
    private DbManager manager;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // inizializza il DBManager dagli attributi di Application
        this.manager = (DbManager) super.getServletContext().getAttribute("dbmanager");
        //prendo la directory di upload e prendo un path assoluto che mi manda in build, tolgo il build dal path per arrivare al path dove salviamo le immagini
        dirName = getServletContext().getRealPath(config.getInitParameter("uploadDir")).replace("build/", "").replace("build\\", "");
        if (dirName == null) 
          throw new ServletException("missing uploadDir parameter in web.xml for servlet ModifyProfileServlet");
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
        String strRealPath = request.getSession().getServletContext().getRealPath("") + "img\\avatar";
        out.println(request.getSession().getServletContext());        
        //String _path = getRelativePath("", strRealPath, strRealPath);
        out.println("str " + strRealPath);
         // Use an advanced form of the constructor that specifies a character
            // encoding of the request (not of the file contents) and a file
            // rename policy.
            MultipartRequest multi = new MultipartRequest(request,
                    dirName, 
                    10*1024*1024, "ISO-8859-1", 
                    new DefaultFileRenamePolicy());
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
                if ((!"".equals(_name)) && (!"".equals(_surname))){
                    manager.modifyUserNameSurname(user.getId(), _name, _surname);
                    user.setName(_name);
                    user.setSurname(_surname);
                    out.println("superata modifica nome cognome ");
                }else{
                    out.println("Name/SurName are empty");
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(ModifyProfileServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            }
            out.println();
            
            //stampo parametri del file caricato in caso di mancato forward
            out.println("FILE:");
            Enumeration files = multi.getFileNames();
            try {
                if (files.hasMoreElements()) {
                    String name = (String)files.nextElement();
                    String filename = multi.getFilesystemName(name);
                    String originalFilename = multi.getOriginalFileName(name);
                    String type = multi.getContentType(name);

                    File f = multi.getFile(name); //file caricato
                    //cambio il nome del file per non avere collisioni
                    String r = UUID.randomUUID().toString()+"."+getExtension(f.toString());
                    String photoPath = dirName+"/"+r;
                    File f2 = new File(photoPath);
                    f.renameTo(f2);    
                    
                    //CANCELLO FOTO VECCHIA
                    String oldAvatarPath = dirName + user.getAvatar_path().replace("img/avatar/", "/");
                    out.println("old_avatar_path :" + oldAvatarPath);   
                    boolean success = FileDeleter.deleteFile(oldAvatarPath);
                    if(success){
                        out.println("file vecchio cancellato ");  
                    }else{
                        out.println("non sono riuscito a cancellare il file ");  
                    }
                    out.println("delete = " + success );    
                    //CAMBIO FOTO UTENTE
                    manager.modifyUserPhoto(user.getId(), "img/avatar/"+r);
                    user.setAvatar_path("img/avatar/"+r);
                    out.println("superata modifica avatar ");         
                    
                    
                         
                    
                    //stampo qualche parametro 
                    out.println("dirName: " + dirName);
                    out.println("originalFilename: " + originalFilename);                
                    out.println();
                }  
                
            } catch (Exception ex) {
                Logger.getLogger(ModifyProfileServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            //forward della richiesta
            request.getRequestDispatcher("/ProfileServlet").forward(request, response);
            
        }
    
    private String getExtension(String name){
        
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return e.toString();
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

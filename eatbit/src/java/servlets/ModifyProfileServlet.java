package servlets;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import database.DbManager;
import database.User;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletConfig;
import org.apache.derby.iapi.services.io.FileUtil;
import utility.FileDeleter;
//import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author andrei
 */
@WebServlet(name = "ModifyProfileServlet", urlPatterns = {"/ModifyProfileServlet"})
public class ModifyProfileServlet extends HttpServlet {
    private String global_dirName;
    private DbManager manager;

    @Override
    public void init() throws ServletException {
        // inizializza il DBManager dagli attributi di Application
        this.manager = (DbManager) super.getServletContext().getAttribute("dbmanager");
        //prendo la directory di upload e prendo un path assoluto che mi manda in build, tolgo il build dal path per arrivare al path dove salviamo le immagini
        global_dirName= (String) super.getServletContext().getInitParameter("uploadDir");
        if (global_dirName == null) 
          throw new ServletException("missing uploadDir parameter in web.xml for servlet ModifyProfileServlet");
        global_dirName = getServletContext().getRealPath(global_dirName);
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
        String dirName= global_dirName;
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
        PrintWriter out = response.getWriter();
        //creo il realpath della directory di salvataggio avatar       
        String strRealPath = request.getSession().getServletContext().getRealPath("") + "img\\avatar";
        //String _path = getRelativePath("", strRealPath, strRealPath);
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
                case "name": _name = value;break;
                case "surname": _surname = value;break;
            }
        }
        try {
            //CAMBIO NOME COGNOME UTENTE e NICKNAME
            manager.modifyUserNameSurname(user.getId(), _name, _surname);
            user.setName(_name);
            user.setSurname(_surname);
        } catch (SQLException ex) {
            Logger.getLogger(ModifyProfileServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
        }
        Enumeration files = multi.getFileNames();
        try {
            if (files.hasMoreElements()) {
                String name = (String)files.nextElement();
                String filename = multi.getFilesystemName(name);
                String originalFilename = multi.getOriginalFileName(name);
                String type = multi.getContentType(name);
                File f = multi.getFile(name); //file caricato
                if(f!=null)
                {
                    //cambio il nome del file per non avere collisioni
                    String r = UUID.randomUUID().toString()+"."+getExtension(f.toString());
                    String photoPath = dirName+"/"+r;
                    File f2 = new File(photoPath);
                    f.renameTo(f2); 
                    File fWeb= new File(dirName.replace("build/", "").replace("build\\", "")+"/"+r);
                    Files.copy(f2.toPath(),fWeb.toPath(),COPY_ATTRIBUTES);
                    dirName= dirName.replace("build/", "").replace("build\\", "");
                    //CANCELLO FOTO VECCHIA se è diversa dall'avatar di default
                    if(user.getAvatar_path().compareTo("img/avatar/avatar.png")!=0)
                    {
                        String oldAvatarPath = dirName + user.getAvatar_path().replace("img/avatar/", "/");
                        out.println("old_avatar_path :" + oldAvatarPath);   
                        FileDeleter.deleteFile(oldAvatarPath);
                    }
                    //CAMBIO FOTO UTENTE
                    manager.modifyUserPhoto(user.getId(), "img/avatar/"+r);
                    user.setAvatar_path("img/avatar/"+r);
                    session.setAttribute("user", user);
                }
            }  
        } catch (Exception ex) {
            Logger.getLogger(ModifyProfileServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        //forward della richiesta
        request.getRequestDispatcher("/ProfileServlet").forward(request, response);
        }
    
    private String getExtension(String name){
        
        try {
            int val= name.lastIndexOf(".");
            if(val==-1)
                return "jpg";
            else
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

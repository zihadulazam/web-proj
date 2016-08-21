/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import database.DbManager;
import database.Photo;
import database.User;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jacopo
 */
@WebServlet(name = "AddPhotoToRestaurantServlet", urlPatterns =
{
    "/AddPhotoToRestaurantServlet"
})
public class AddPhotoToRestaurantServlet extends HttpServlet
{

    private String dirName;
    private DbManager manager;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // inizializza il DBManager dagli attributi di Application
        this.manager = (DbManager) super.getServletContext().getAttribute("dbmanager");
        //prendo la directory di upload e prendo un path assoluto che mi manda in build, tolgo il build dal path per arrivare al path dove salviamo le immagini
        dirName= (String) super.getServletContext().getInitParameter("uploadPhotosDir");
        if (dirName == null) 
          throw new ServletException("missing uploadPhotosDir parameter in web.xml for servlet addPhotoToRestaurantServlet");
        dirName = getServletContext().getRealPath(dirName).replace("build/", "").replace("build\\", "");
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        
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
            throws ServletException, IOException
    {
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
            throws ServletException, IOException
    {
        //prendo richiesta multipart
        MultipartRequest multi = new MultipartRequest(request,
                    dirName, 
                    10*1024*1024, "ISO-8859-1", 
                    new DefaultFileRenamePolicy());
        //prendo il file
        Enumeration files = multi.getFileNames();
        String sId_rest= multi.getParameter("id_rest");
        String sPhoto_description= multi.getParameter("photo_description");
        if(sId_rest==null || !files.hasMoreElements() || sPhoto_description==null){
            request.setAttribute("title", "Risultato Operazione:");
            request.setAttribute("status", "danger");
            request.setAttribute("description", "Errore: Mancano i parametri");
            request.getRequestDispatcher("/info.jsp").forward(request, response);
        }
        else
        {
            //prendo foto e rinonimo per evitare collisioni di nomi
            String name = (String)files.nextElement();
            String filename = multi.getFilesystemName(name);
            String originalFilename = multi.getOriginalFileName(name);
            String type = multi.getContentType(name);
            File f = multi.getFile(name); 
            String newName = UUID.randomUUID().toString()+"."+getExtension(f.toString());
            String newPath = dirName+"/"+newName;
            File f2 = new File(newPath);
            f.renameTo(f2);
            try
            {
                Photo photo= new Photo();
                photo.setId_owner(((User)request.getSession().getAttribute("user")).getId());
                photo.setId_restaurant(Integer.parseInt(sId_rest));
                photo.setPath("img/photos/"+newName);
                photo.setName(name);
                photo.setDescription(sPhoto_description);
                int id_photo= manager.addPhoto(photo);
                if(id_photo==-1)
                {
                    //ristorante non esiste
                    //va cancellato file
                    request.setAttribute("title", "Risultato Operazione:");
                    request.setAttribute("status", "danger");
                    request.setAttribute("description", "Errore: Ristorante non esiste.");
                    request.getRequestDispatcher("/info.jsp").forward(request, response);
                }
                else
                {
                    request.setAttribute("title", "Risultato Operazione:");
                    request.setAttribute("status", "ok");
                    request.setAttribute("description", "Ok: La tua <strong>Photo</strong> è stato inserito con successo.");
                    request.getRequestDispatcher("/info.jsp").forward(request, response);
                    manager.notifyPhoto(Integer.parseInt(sId_rest), id_photo);
                }
            }
            catch (SQLException | NumberFormatException ex)
            {
                Logger.getLogger(AddPhotoToRestaurantServlet.class.getName()).log(Level.SEVERE, null, ex);
                request.setAttribute("title", "Risultato Operazione:");
                request.setAttribute("status", "danger");
                request.setAttribute("description", "Errore!!!");
                request.getRequestDispatcher("/info.jsp").forward(request, response);
            }
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>

    private String getExtension(String name){
        
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return e.toString();
        }
    }
}

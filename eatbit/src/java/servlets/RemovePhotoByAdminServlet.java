package servlets;

import database.DbManager;
import database.Photo;
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
import utility.FileDeleter;

/**
 * Servlet per permettere all'admin di rimuovere una foto che era stata segnalata.
 * La rimozione rimuove la foto sia a livello di db che di fs, se la foto era collegata
 * a una review viene rimossa in automatico anche la review grazie a delete on cascade.
 * Manderà come risposta 1 se la rimozione è andata a buon fine(n.b. se il metodo runna
 * e non trova niente da rimuovere manda cmq 1, richiedere di rimuovere foto non esistenti
 * manderà quindi 1, se il resto è corretto), 0 se vi è stata * una eccezione,
 * -1 se manca un parametro.
 * @author jacopo
 */
@WebServlet(name = "RemovePhotoByAdminServlet", urlPatterns =
{
    "/RemovePhotoByAdminServlet"
})
public class RemovePhotoByAdminServlet extends HttpServlet
{
    private DbManager manager;
    private String dirName;
    
    @Override
    public void init() throws ServletException {
        // inizializza il DBManager dagli attributi di Application
        this.manager = (DbManager) super.getServletContext().getAttribute("dbmanager");
        //recupero path cartella foto
        dirName= (String) super.getServletContext().getInitParameter("uploadPhotosDir");
        if (dirName == null) 
          throw new ServletException("missing uploadPhotosDir parameter in web.xml for servlet addReviewServlet");
        dirName = getServletContext().getRealPath(dirName).replace("build/", "").replace("build\\", "");
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
            String stringId =request.getParameter("id_photo");
            if (stringId!=null) 
            {
                Photo photo= manager.getPhotoById(Integer.parseInt(stringId));
                //rimuovo associazione foto - risto da db
                manager.removePhoto(Integer.parseInt(stringId));
                //rimuovo foto da filesystem
                String Path = dirName + photo.getPath().replace("img/photos/", "\\");
                FileDeleter.deleteFile(Path);
                out.write("1");
            }
            else
                out.write("-1");
        } catch (NumberFormatException | SQLException ex) {
            Logger.getLogger(RemovePhotoByAdminServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
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

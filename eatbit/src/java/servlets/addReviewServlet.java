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
import database.Review;
import database.User;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
 *Servlet per aggiungere una recensione, restituisce:
 * 1 se è andato a buon fine
 * 0 se c'è stata una eccezione nel db o se uno dei parametri che doveva essere
 * un intero non lo è
 * -1 se manca un parametro o la foto
 * -2 se il ristorante non esiste
 * -3 se l'utente ha già dato un voto o una recensione a questo ristorante
 * nelle ultime 24h
 * -4 se il recensore è il proprietario del ristorante.
 * 
 * parametri necessari:
 * atmosphere INTERO
 * description STRINGA
 * food INTERO
 * global_value INTERO
 * id_rest INTERO
 * name STRINGA
 * service INTERO
 * value_for_money INTERO
 * photo_description STRINGA
 * Oltrea a questi parametri deve essere mandato un file (foto).
 * @author jacopo
 */

@WebServlet(name = "addReviewServlet", urlPatterns =
{
    "/addReviewServlet"
})
public class addReviewServlet extends HttpServlet
{
    private String dirName;
    private DbManager manager;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // inizializza il DBManager dagli attributi di Application
        this.manager = (DbManager) super.getServletContext().getAttribute("dbmanager");
        //prendo la directory di upload e prendo un path assoluto che mi manda in build, tolgo il build dal path per arrivare al path dove salviamo le immagini
        dirName = getServletContext().getRealPath(config.getInitParameter("uploadPhotosDir")).replace("build/", "").replace("build\\", "");
        if (dirName == null) 
          throw new ServletException("missing uploadPhotosDir parameter in web.xml for servlet addReviewServlet");
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
            throws ServletException, IOException
    {
        PrintWriter out= response.getWriter();
        //prendo richiesta multipart
        MultipartRequest multi = new MultipartRequest(request,
                    dirName, 
                    10*1024*1024, "ISO-8859-1", 
                    new DefaultFileRenamePolicy());
        //prendo il file
        Enumeration files = multi.getFileNames();
        //preparo le stringhe per raccogliere i parametri
        String sAtmosphere=null;//deve essere un numero intero
        String sDescription=null;
        String sFood=null;//deve essere un numero intero
        String sGlobal_value=null;//deve essere un numero intero
        String sId_rest=null;//deve essere un numero intero
        String sName=null;
        String sService=null;//deve essere un numero intero
        String sValue_for_money=null;//deve essere un numero intero
        String sPhoto_description=null;
        
        //prendo i parametri
        Enumeration params = multi.getParameterNames();            
        while (params.hasMoreElements()) 
        {
            String name = (String)params.nextElement();
            String value = multi.getParameter(name);
            switch(name)
            {
                case "atmosphere": sAtmosphere=value;break;
                case "description": sDescription=value;break;
                case "food": sFood=value;break;
                case "global_value": sGlobal_value=value;break;
                case "id_rest": sId_rest=value;break;
                case "name": sName=value;break;
                case "service": sService=value;break;
                case "value_for_money": sValue_for_money=value;break;
                case "photo_description": sPhoto_description=value;break;
                default: break;
            }
        }
        //controllo che siano presenti tutti i parametri necessari
        if(sAtmosphere==null||sDescription==null||sFood==null||sGlobal_value==null
                ||sId_rest==null||sName==null||sService==null||sValue_for_money==null
                ||sPhoto_description==null||!files.hasMoreElements())
            out.write("-1");
        else
        {
            //prendo foto e rinonimo per evitare collisioni di nomi
            String name = (String)files.nextElement();
            String filename = multi.getFilesystemName(name);
            String originalFilename = multi.getOriginalFileName(name);
            String type = multi.getContentType(name);
            File f = multi.getFile(name); 
            String newName = UUID.randomUUID().toString()+"."+getExtension(f.toString());
            String newPath = dirName+"\\"+newName;
            File f2 = new File(newPath);
            f.renameTo(f2);
            //converto i parametri che lo necessitano in numeri e faccio
            //le chiamate al db, preparando prima gli oggetto necessari
            try
            {
                User user= (User) request.getSession().getAttribute("user");//user deve essere trovato causa filtro
                //aggiungo foto e ottengo il suo id
                Photo photo= new Photo();
                photo.setId_owner(user.getId());
                photo.setId_restaurant(Integer.parseInt(sId_rest));
                photo.setPath("img/photos/"+newName);
                photo.setName(name);
                photo.setDescription(sPhoto_description);
                int id_photo= manager.addPhoto(photo);
                if(id_photo==-1)
                {
                    //ristorante non esiste
                    out.write("-2");
                }
                else
                {
                    //una volta caricata la foto setto la review e la inserisco
                    Review review= new Review();
                    review.setAtmosphere(Integer.parseInt(sAtmosphere));
                    review.setDescription(sDescription);
                    review.setFood(Integer.parseInt(sFood));
                    review.setGlobal_value(Integer.parseInt(sGlobal_value));
                    review.setId_creator(user.getId());
                    review.setId_photo(id_photo);
                    review.setId_restaurant(Integer.parseInt(sId_rest));
                    review.setName(sName);
                    review.setService(Integer.parseInt(sService));
                    review.setValue_for_money(Integer.parseInt(sValue_for_money));
                    switch (manager.addReview(review))
                    {//n.b. va cancellata dal fs la foto per i casi -2 -3
                    //l'utente ha già fatto un voto o una review a questo ristorante nelle ultime 24h
                        case -2: out.write("-3");break;
                    //l'utente ristoratore sta cercando di recensire il proprio ristorante
                        case -3: out.write("-4");break;
                    //successo
                        default: out.write("1");break;
                    }
                }
            }
            catch(NumberFormatException | SQLException e)
            {
                //va deletata foto da filesystem
                Logger.getLogger(addReviewServlet.class.getName()).log(Level.SEVERE, e.toString(), e);
                out.write("0");
            }
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
            throws ServletException, IOException
    {
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
            throws ServletException, IOException
    {
        processRequest(request, response);
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

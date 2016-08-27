package servlets;

import database.DbManager;
import database.HoursRange;
import database.contexts.RestaurantContext;
import database.contexts.ReviewContext;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.apache.tomcat.util.codec.binary.Base64;

/**
 *Servlet che recupera un contesto di ristorante in base all'id passato come parametro,
 * inserisce il contesto nella request e passa il controllo alla jsp del ristorante.
 * Viene inoltre aggiunta una stringa nella request (qr_url) che rappresenta il qr code del 
 * ristorante, generato a partire dal nome, indirizzo e orari del ristorante.
 * Se il contesto è null significa che non esiste ristorante con quell'id.
 * Può essere che lo User owner sia null, se nessuno ha ancora reclamato la prorietà
 * del ristorante.
 * @author jacopo
 */
@WebServlet(name = "GetRestaurantContextForwardToJspServlet", urlPatterns =
{
    "/GetRestaurantContextForwardToJspServlet"
})
public class GetRestaurantContextForwardToJspServlet extends HttpServlet
{
    
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
        try {
            int id_restaurant = Integer.parseInt(request.getParameter("id_restaurant"));
            RestaurantContext context= manager.getRestaurantContext(id_restaurant);
            int[] averages= context.getAverages();
                request.setAttribute("voto_per_cibo", averages[0]);
                request.setAttribute("voto_per_servizio", averages[1]);
                request.setAttribute("voto_per_atmosfera", averages[2]);
                request.setAttribute("voto_per_prezzo", averages[3]);
            
            //GENERAZIONE QR
            String qrContent="";
            String qrUrl;//il risultato finale
            //genero il contenuto del qr
            if(context.getRestaurant()!=null)
                qrContent+= context.getRestaurant().getName()+"\n";
            if(context.getCoordinate()!=null)
                qrContent+= context.getCoordinate().getAddress()+", "+
                        context.getCoordinate().getCity()+", "+
                        context.getCoordinate().getState()+"\n";
            ArrayList<HoursRange> hours= context.getHoursRanges();
            for (HoursRange hour : hours)
                qrContent+=hour.getFormattedWeeklyHour()+"\n";
            //genero il byte[] per il qr code a partire dal contenuto, e lo trasformo in un url leggibile dalla jsp
            byte[] bytes= QRCode.from(qrContent).to(ImageType.PNG).withSize(250, 250).stream().toByteArray();
            //codifico l'immagine in un url
            qrUrl ="data:image/png;base64," + Base64.encodeBase64String(bytes);
            //fine generazione QR
            
            request.setAttribute("vicini",manager.getRestaurantNeighbourhood(id_restaurant, 10));
            request.setAttribute("restaurant_context", manager.getRestaurantContext(id_restaurant));
            request.setAttribute("qr_url", qrUrl);
            request.getRequestDispatcher("/WEB-INF/restaurant.jsp").forward(request, response);
        } catch (SQLException | NumberFormatException ex) {
            Logger.getLogger(GetRestaurantContextForwardToJspServlet.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);
        }
    }

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

    /* Handles the HTTP <code>POST</code> method.
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



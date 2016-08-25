package servlets;

import database.Coordinate;
import database.DbManager;
import database.HoursRange;
import database.Restaurant;
import database.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *Servlet per modificare un ristorante, restituisce 1 se è andato a buon fine, 0 se 
 * c'è stata una eccezione sql o di formato dei numeri (parametri che dovevano essere
 * numeri non lo erano), -1 se mancavano parametri.
 * parametri necessari:
 * name
 * description
 * url
 * cuisine
 * address
 * city
 * province
 * state
 * latitude//deve essere numero, anche reale
 * longitude//deve essere numero, anche reale
 * hour//formato 110:0018:40    [giorno da 1 a 7][ora:minuto][ora:minuto]
 * min//deve essere numero, anche reale
 * max//deve essere numero, anche reale
 * @author andrei
 */
@WebServlet(name = "ModifyRestaurantServlet", urlPatterns =
{
    "/ModifyRestaurantServlet"
})

public class ModifyRestaurantServlet extends HttpServlet
{
    private DbManager manager;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
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
            throws ServletException, IOException
    {
        PrintWriter out= response.getWriter();
        //preparo le stringhe e gli oggetti per raccogliere i parametri
        //stringhe per risto
        String sName=request.getParameter("name");
        String sDescription=request.getParameter("description");
        String sUrl=request.getParameter("web_site");
        String sId_restaurant=request.getParameter("id_restaurant");
        //cucine
        String[] sCuisines=request.getParameterValues("cuisine");
        //stringhe per coordinata
        String sAddress=request.getParameter("address");
        String sCity=request.getParameter("city");
        String sProvince=request.getParameter("province");
        String sState=request.getParameter("state");
        String sLatitude=request.getParameter("latitude");//deve essere numero, anche reale
        String sLongitude=request.getParameter("longitude");//deve essere numero, anche reale
        //ORARI
        String orarioLun = request.getParameter("orarioL");
        String orarioMar = request.getParameter("orarioM");
        String orarioMer = request.getParameter("orarioMe");
        String orarioGio = request.getParameter("orarioG");
        String orarioVen = request.getParameter("orarioV");
        String orarioSab = request.getParameter("orarioS");
        String orarioDom = request.getParameter("orarioD");
        
        //altre Stringhe
        String sMin=request.getParameter("prezzo_min");//deve essere numero, anche reale
        String sMax=request.getParameter("prezzo_max");//deve essere numero, anche reale

        //controllo che siano presenti tutti i parametri necessari
        if(sDescription==null||sName==null||sUrl==null||sCuisines==null
                ||sAddress==null||sCity==null||sProvince==null||sState==null||sLatitude==null
                ||sLongitude==null||sMin==null||sMax==null ||orarioLun==null ||orarioMar==null
                || orarioMer==null ||orarioGio==null||orarioVen==null||orarioSab==null||orarioDom==null)
            out.write("-1");
        else
        {
            //converto i parametri che lo necessitano in numeri e faccio
            //le chiamate al db, preparando prima gli oggetto necessari
            try
            {
                ArrayList<HoursRange> hours = new ArrayList<>();
                HoursRange lun = new HoursRange(orarioLun);
                HoursRange mar = new HoursRange(orarioMar);
                HoursRange mer = new HoursRange(orarioMer);
                HoursRange gio = new HoursRange(orarioGio);
                HoursRange ven = new HoursRange(orarioVen);
                HoursRange sab = new HoursRange(orarioSab);
                HoursRange dom = new HoursRange(orarioDom);
                hours.add(lun);
                hours.add(mar);
                hours.add(mer);
                hours.add(gio);
                hours.add(ven);
                hours.add(sab);
                hours.add(dom);
                int id_restaurant= Integer.parseInt(sId_restaurant);
                double min=Double.parseDouble(sMin);
                double max=Double.parseDouble(sMax);
                
                User user= (User) request.getSession().getAttribute("user");//user deve essere trovato causa filtro
                Restaurant restaurant = new  Restaurant();
                Coordinate coordinate= new Coordinate();
                restaurant.setId(id_restaurant);
                restaurant.setName(sName);
                restaurant.setDescription(sDescription);
                restaurant.setWeb_site_url(sUrl);
                restaurant.setId_owner(user.getId());
                coordinate.setAddress(sAddress);
                coordinate.setCity(sCity);
                coordinate.setProvince(sProvince);
                coordinate.setState(sState);
                coordinate.setLatitude(Double.parseDouble(sLatitude));
                coordinate.setLongitude(Double.parseDouble(sLongitude));
                manager.modifyRestaurant(restaurant, sCuisines, coordinate, hours, min,max);
                out.write("1");
            }
            catch(NumberFormatException | SQLException e)
            {
                Logger.getLogger(AddReviewServlet.class.getName()).log(Level.SEVERE, e.toString(), e);
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

    private ArrayList<HoursRange> parseHours(final String[] hours)
    {
        ArrayList<HoursRange> res=new ArrayList<>();
        
        for (String hour : hours)
        {
            //formato è 110:0018:30  [giorno][apertura][chiusura]
            try
            {
                HoursRange tmp=new HoursRange(hour);
                
                res.add(tmp);
            }
            catch(NumberFormatException | IndexOutOfBoundsException ex)
            {
                Logger.getLogger(ModifyRestaurantServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return res;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import database.Coordinate;
import database.DbManager;
import database.HoursRange;
import database.Photo;
import database.Restaurant;
import database.User;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
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
 * @author jacopo
 */
@WebServlet(name = "ModifyRestaurantServletBackup", urlPatterns =
{
    "/ModifyRestaurantServletBackup"
})

public class ModifyRestaurantServletBackup extends HttpServlet
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
        String sName=null;
        String sDescription=null;
        String sUrl=null;
        //cucine
        String[] sCuisines=null;
        //stringhe per coordinata
        String sAddress=null;
        String sCity=null;
        String sProvince=null;
        String sState=null;
        String sLatitude=null;//deve essere numero, anche reale
        String sLongitude=null;//deve essere numero, anche reale
        //orari
        String[] sHours=null;
        //altre Stringhe
        String sMin=null;//deve essere numero, anche reale
        String sMax=null;//deve essere numero, anche reale
        //prendo i parametri
        Enumeration params = request.getParameterNames();            
        while (params.hasMoreElements()) 
        {
            String name = (String)params.nextElement();
            String[] value = request.getParameterValues(name);
            switch(name)
            {
                case "description": sDescription=value[0];break;
                case "name": sName=value[0];break;
                case "url": sUrl=value[0];break;
                case "cuisine": sCuisines=value;break;
                case "address": sAddress=value[0];break;
                case "city": sCity=value[0];break;
                case "province": sProvince=value[0];break;
                case "state": sState=value[0];break;
                case "latitude": sLatitude=value[0];break;
                case "longitude": sLongitude=value[0];break;
                case "hour": sHours=value;break;
                case "min": sMin=value[0];break;
                case "max": sMax=value[0];break;
                default: break;
            }
        }
        
        //controllo che siano presenti tutti i parametri necessari
        if(sDescription==null||sName==null||sUrl==null||sCuisines==null
                ||sAddress==null||sCity==null||sProvince==null||sState==null||sLatitude==null
                ||sLongitude==null||sHours==null||sMin==null||sMax==null)
            out.write("-1");
        else
        {
            //converto i parametri che lo necessitano in numeri e faccio
            //le chiamate al db, preparando prima gli oggetto necessari
            try
            {
                User user= (User) request.getSession().getAttribute("user");//user deve essere trovato causa filtro
                Restaurant restaurant = new  Restaurant();
                Coordinate coordinate= new Coordinate();
                restaurant.setName(sName);
                restaurant.setDescription(sDescription);
                restaurant.setWeb_site_url(sUrl);
                restaurant.setId_creator(user.getId());
                coordinate.setAddress(sAddress);
                coordinate.setCity(sCity);
                coordinate.setProvince(sProvince);
                coordinate.setState(sState);
                coordinate.setLatitude(Double.parseDouble(sLatitude));
                coordinate.setLongitude(Double.parseDouble(sLongitude));
                ArrayList<HoursRange> hours=parseHours(sHours);
                double minPrice=Double.parseDouble(sMin);
                double maxPrice=Double.parseDouble(sMax);
                manager.modifyRestaurant(restaurant, sCuisines, coordinate, hours, minPrice, maxPrice);
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
            catch(NumberFormatException | IndexOutOfBoundsException e)
            {
                //se parte una eccezione su una ora non faccio niente e vado
                //avanti a parsare il resto
            }
        }
        return res;
    }
}

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
import database.PriceRange;
import database.Restaurant;
import database.User;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import javax.servlet.http.HttpSession;

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
        String sName=request.getParameter("name");;
        String sDescription=request.getParameter("description");;
        String sUrl=request.getParameter("web_site");
        //cucine
        String[] sCuisines=null;
        //stringhe per coordinata
        String sAddress=request.getParameter("address");
        String sCity=request.getParameter("city");;
        String sProvince=request.getParameter("province");;
        String sState=request.getParameter("state");;
        String sLatitude=request.getParameter("latitude");;//deve essere numero, anche reale
        String sLongitude=request.getParameter("longitude");;//deve essere numero, anche reale
        //ORARI
        String orarioLun = request.getParameter("orarioL");
        String orarioMar = request.getParameter("orarioM");
        String orarioMer = request.getParameter("orarioMe");
        String orarioGio = request.getParameter("orarioG");
        String orarioVen = request.getParameter("orarioV");
        String orarioSab = request.getParameter("orarioS");
        String orarioDom = request.getParameter("orarioD");
        
        HoursRange lun = new HoursRange(orarioLun);
        HoursRange mar = new HoursRange(orarioMar);
        HoursRange mer = new HoursRange(orarioMer);
        HoursRange gio = new HoursRange(orarioGio);
        HoursRange ven = new HoursRange(orarioVen);
        HoursRange sab = new HoursRange(orarioSab);
        HoursRange dom = new HoursRange(orarioDom);
        
        ArrayList<HoursRange> hours = new ArrayList<>();
        hours.add(lun);
        hours.add(mar);
        hours.add(mer);
        hours.add(gio);
        hours.add(ven);
        hours.add(sab);
        hours.add(dom);
        
        //altre Stringhe
        Double sMin=null;//deve essere numero, anche reale
        Double sMax=null;//deve essere numero, anche reale
        //prendo i parametri

        //variabili per la mofifica dati ristorante
        int id_restaurant = -1;
        try{
            id_restaurant = Integer.parseUnsignedInt(request.getParameter("id_restaurant"));
        }catch (NumberFormatException e){
            Logger.getLogger(ModifyRestaurantServlet.class.getName()).log(Level.SEVERE, e.toString(), e);
            request.setAttribute("errore1", "Errore interno con l'ID del ristorante");
            
        }
        out.println("ID: "+id_restaurant);
;
        try{
            sMin = Double.parseDouble(request.getParameter("prezzo_min"));
            sMax = Double.parseDouble(request.getParameter("prezzo_max"));
            if (sMin>sMax){
                throw new NumberFormatException();
            }
        }catch(NumberFormatException e){
            Logger.getLogger(ModifyRestaurantServlet.class.getName()).log(Level.SEVERE, e.toString(), e);
            request.setAttribute("errore2", "Il prezzo minimo e massimo non é inserito correttamente oppure il minimo é MINORE del massimo");
        }


        String[] checkboxes = request.getParameterValues("cuisine");
        ArrayList<String> cucine = new ArrayList<String>();
        Collections.addAll(cucine,checkboxes);

        out.println("Cucine");
         out.println(checkboxes.length);
        for(String c:cucine){
            out.println(c);
        }

        out.println(sName);
      out.println(sDescription);
      out.println(sLatitude);
      out.println(sLongitude);
      out.println(sAddress);
      out.println(sCity);
      out.println(sProvince);
      out.println(sState);
      out.println(sUrl);
      out.println(sMin);
      out.println(sMax);

        out.println("ORARI");
        out.println("Lun:" + orarioLun);
        out.println("Mar:"+orarioMar);
        out.println("Mer:"+orarioMer);
        out.println("Gio:"+orarioGio);
        out.println("Ven:"+orarioVen);
        out.println("Sab:"+orarioSab);
        out.println("Dom:"+orarioDom);
        
        
        //controllo che siano presenti tutti i parametri necessari
        if(sDescription==null||sName==null||sUrl==null||cucine==null
                ||sAddress==null||sCity==null||sProvince==null||sState==null||sLatitude==null
                ||sLongitude==null||sMin==null||sMax==null)
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
                
                manager.modifyRestaurant(restaurant, sCuisines, coordinate,hours, sMin, sMin);
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

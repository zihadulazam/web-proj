/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import database.Coordinate;
import database.DbManager;
import database.HoursRange;
import database.PriceRange;
import database.Restaurant;
import database.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author mario
 */
@WebServlet(name = "ModifyRestaurantServlet", urlPatterns = {"/ModifyRestaurantServlet"})
public class ModifyRestaurantServlet extends HttpServlet {
    private DbManager manager;
    boolean err;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // inizializza il DBManager dagli attributi di Application
        this.manager = (DbManager) super.getServletContext().getAttribute("dbmanager");
        err = false;
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
        
        PrintWriter out = response.getWriter();
        //prendo la sessione
        HttpSession session = request.getSession();
        //variabili per la mofifica dati ristorante
        int id_restaurant = -1;
        try{
            id_restaurant = Integer.parseInt(request.getParameter("id_restaurant"));
        }catch (NumberFormatException e){
            request.setAttribute("errore1", "Errore interno con l'ID del ristorante");
            err = true;
        }
        out.println("ID: "+id_restaurant);
        String nome = request.getParameter("name");
        String descrizione =  request.getParameter("descrizione");
        String sito = request.getParameter("sito");
        
        Double max = null;
        Double min = null;
        try{
            min = Double.parseDouble(request.getParameter("prezzo_min"));
            max = Double.parseDouble(request.getParameter("prezzo_max"));
            if (min>max){
                throw new NumberFormatException();
            }
        }catch(NumberFormatException e){
            request.setAttribute("errore2", "Il prezzo minimo e massimo non é inserito correttamente oppure il minimo é MINORE del massimo");
            err = true;
        }
        PriceRange priceRange = new PriceRange();
        priceRange.setMin(min);
        priceRange.setMin(max);
        out.println("Range Prezzi: Max-" + max.toString() + " Min-"+min.toString());
        
        String[] checkboxes = request.getParameterValues("CUCINE");
        ArrayList<String> cucine = new ArrayList<>();
        Collections.addAll(cucine,checkboxes);
        
        out.println("checkBoxes");
         out.println(cucine.size());
        for(String c:cucine){
            out.println(c);
        }
        
        ArrayList<HoursRange> orari;
        orari = new ArrayList<>();
        
        out.println("ORARI");
        // RECUPERO GLI ORARI PER OGNI GG DELLA SETTIMANA
        //LUNEDI
        String oraInizioLunedi = request.getParameter("oraInizioLunedi");
        String oraFineLunedi = request.getParameter("oraFineLunedi");
        Date dateStart = null;
        Date dateEnd = null;
        try {
            dateStart = new SimpleDateFormat("HH:mm").parse(oraInizioLunedi);
            dateEnd = new SimpleDateFormat("HH:mm").parse(oraFineLunedi);
            
        }
        catch (ParseException e) {
            request.setAttribute("errore", "Inserieci lora seguendo questo formato:   HH:mm      -Es: 20:30 \n "
                                    + "e assicurati che l'ora inizio sia minore di ora fine"); 
            err = true;
        }
        HoursRange hoursRangeLunedi = new HoursRange();
        hoursRangeLunedi.setStart_hour(new Time(dateStart.getTime()));
        hoursRangeLunedi.setEnd_hour(new Time(dateEnd.getTime()));
        hoursRangeLunedi.setDay(1);
        orari.add(hoursRangeLunedi);
        out.println(hoursRangeLunedi.getFormattedWeeklyHour());
        
        //MARTEDI
        String oraInizioMartedi = request.getParameter("oraInizioMartedi");
        String oraFineMartedi = request.getParameter("oraFineMartedi");
        Date dateStartMartedi = null;
        Date dateEndMartedi = null;
        try {
            dateStartMartedi = new SimpleDateFormat("HH:mm").parse(oraInizioMartedi);
            dateEndMartedi = new SimpleDateFormat("HH:mm").parse(oraFineMartedi);
            
        }
        catch (ParseException e) {
            request.setAttribute("errore", "Inserieci lora seguendo questo formato:   HH:mm      -Es: 20:30 \n "      
                    + "e assicurati che l'ora inizio sia minore di ora fine");
            err = true;
        }
        HoursRange hoursRangeMartedi = new HoursRange();
        hoursRangeMartedi.setStart_hour(new Time(dateStartMartedi.getTime()));
        hoursRangeMartedi.setEnd_hour(new Time(dateEndMartedi.getTime()));
        hoursRangeMartedi.setDay(2);
        orari.add(hoursRangeMartedi);
        out.println(hoursRangeMartedi.getFormattedWeeklyHour());
        
        //MERCOLEDì
        String oraInizioMercoledi = request.getParameter("oraInizioMercoledi");
        String oraFineMercoledi= request.getParameter("oraFineMercoledi");
        Date dateStartMercoledi = null;
        Date dateEndMercoledi = null;
        try {
            dateStartMercoledi = new SimpleDateFormat("HH:mm").parse(oraInizioMercoledi);
            dateEndMercoledi = new SimpleDateFormat("HH:mm").parse(oraFineMercoledi);
           
        }
        catch (ParseException e) {
            request.setAttribute("errore", "Inserieci lora seguendo questo formato:   HH:mm      -Es: 20:30 \n "         
                    + "e assicurati che l'ora inizio sia minore di ora fine");
            err = true;
        }
        HoursRange hoursRangeMercoledi = new HoursRange();
        hoursRangeMercoledi.setStart_hour(new Time(dateStartMercoledi.getTime()));
        hoursRangeMercoledi.setEnd_hour(new Time(dateEndMercoledi.getTime()));
        hoursRangeMercoledi.setDay(3);
        orari.add(hoursRangeMercoledi);
        out.println(hoursRangeMercoledi.getFormattedWeeklyHour());
        
        //GIOVEDì
        String oraInizioGiovedi = request.getParameter("oraInizioGiovedi");
        String oraFineGiovedi = request.getParameter("oraFineGiovedi");
        Date dateStartGiovedi = null;
        Date dateEndGiovedi = null;
        try {
            dateStartGiovedi = new SimpleDateFormat("HH:mm").parse(oraInizioGiovedi);
            dateEndGiovedi = new SimpleDateFormat("HH:mm").parse(oraFineGiovedi);
            
        }
        catch (ParseException e) {
            request.setAttribute("errore", "Inserieci lora seguendo questo formato:   HH:mm      -Es: 20:30 \n "   
                    + "e assicurati che l'ora inizio sia minore di ora fine");
            err = true;
        }
        HoursRange hoursRangeGiovedi = new HoursRange();
        hoursRangeGiovedi.setStart_hour(new Time(dateStartGiovedi.getTime()));
        hoursRangeGiovedi.setEnd_hour(new Time(dateEndGiovedi.getTime()));
        hoursRangeGiovedi.setDay(4);
        orari.add(hoursRangeGiovedi);
        out.println(hoursRangeGiovedi.getFormattedWeeklyHour());
        
        //VENERDI
        String oraInizioVenerdi = request.getParameter("oraInizioVenerdi");
        String oraFineVenerdi = request.getParameter("oraFineVenerdi");
        Date dateStartVenerdi = null;
        Date dateEndVenerdi = null;
        try {
            dateStartVenerdi = new SimpleDateFormat("HH:mm").parse(oraInizioVenerdi);
            dateEndVenerdi = new SimpleDateFormat("HH:mm").parse(oraFineVenerdi);
            
        }
        catch (ParseException e) {
            request.setAttribute("errore", "Inserieci lora seguendo questo formato:   HH:mm      -Es: 20:30 \n  "                   
                                    + "e assicurati che l'ora inizio sia minore di ora fine");
            err = true;
        }
        HoursRange hoursRangeVenerdi = new HoursRange();
        hoursRangeVenerdi.setStart_hour(new Time(dateStartVenerdi.getTime()));
        hoursRangeVenerdi.setEnd_hour(new Time(dateEndVenerdi.getTime()));
        hoursRangeVenerdi.setDay(5);
        orari.add(hoursRangeVenerdi);
        out.println(hoursRangeVenerdi.getFormattedWeeklyHour());
        
        //SABATO
        String oraInizioSabato = request.getParameter("oraInizioSabato");
        String oraFineSabato = request.getParameter("oraFineSabato");
        Date dateStartSabato = null;
        Date dateEndSabato = null;
        try {
            dateStartSabato = new SimpleDateFormat("HH:mm").parse(oraInizioSabato);
            dateEndSabato = new SimpleDateFormat("HH:mm").parse(oraFineSabato);
            
        }
        catch (ParseException e) {
            request.setAttribute("errore", "Inserieci lora seguendo questo formato:   HH:mm      -Es: 20:30 \n "         
                    + "e assicurati che l'ora inizio sia minore di ora fine");
            err = true;
        }
        HoursRange hoursRangeSabato = new HoursRange();
        hoursRangeSabato.setStart_hour(new Time(dateStartSabato.getTime()));
        hoursRangeSabato.setEnd_hour(new Time(dateEndSabato.getTime()));
        hoursRangeSabato.setDay(6);
        orari.add(hoursRangeSabato);
        out.println(hoursRangeSabato.getFormattedWeeklyHour());
        
        //DOMENICA
        String oraInizioDomenica = request.getParameter("oraInizioDomenica");
        String oraFineDomenica = request.getParameter("oraFineDomenica");
        Date dateStartDomenica = null;
        Date dateEndDomenica = null;
        try {
            dateStartDomenica = new SimpleDateFormat("HH:mm").parse(oraInizioDomenica);
            dateEndDomenica = new SimpleDateFormat("HH:mm").parse(oraFineDomenica);
            
        }
        catch (ParseException e) {
            request.setAttribute("errore", "Inserieci lora seguendo questo formato:   HH:mm      -Es: 20:30 \n "      
                    + "e assicurati che l'ora inizio sia minore di ora fine");
            err = true;
        }
        HoursRange hoursRangeDomenica= new HoursRange();
        hoursRangeDomenica.setStart_hour(new Time(dateStartDomenica.getTime()));
        hoursRangeDomenica.setEnd_hour(new Time(dateEndDomenica.getTime()));
        hoursRangeDomenica.setDay(7);
        orari.add(hoursRangeDomenica);
        out.println(hoursRangeDomenica.getFormattedWeeklyHour());
        
        //Coordinate
        Double latitudine = null;
        Double longitudine = null;
        try{
            latitudine = Double.parseDouble(request.getParameter("latitudine"));
            longitudine = Double.parseDouble(request.getParameter("longitudine"));
        }catch (NumberFormatException e){
            request.setAttribute("errore3", "Inserisci le coordinate LONGITUDINE e LATITUDINE nel formato dei numeri decimali divisi da un punto.         -Es:  12.1");
            err = true;
        }
        String address = request.getParameter("address");
        String citta = request.getParameter("citta");
        String provincia = request.getParameter("procincia");
        String stato = request.getParameter("stato");
        Coordinate coordinate = new Coordinate();
        coordinate.setAddress(address);
        coordinate.setLatitude(latitudine);
        coordinate.setLongitude(longitudine);
        coordinate.setCity(citta);
        coordinate.setProvince(provincia);
        coordinate.setState(stato);
        out.println("coordinate lat="+latitudine+" lon="+longitudine);
        
        try {
            Restaurant r = manager.getRestaurantById(id_restaurant);
            manager.modifyRestaurant(r, cucine, coordinate, orari, min, max);
        }catch(SQLException | IOException | NullPointerException e){
            request.setAttribute("errore4", "Errore interno durante la modifica dei dati - Riprova più tardi");
            err = true;
        }
        
        //prendo l'user della sessione
        User user = (User)session.getAttribute("user");
        //se non esiste una sessione aperta redirigo sulla pagina di login-->lo fa il filtro
        //faccio cmq un controllo
        if (user == null) {
            // metto il messaggio di errore come attributo di Request, così nel JSP si vede il messaggio
            request.setAttribute("error5", "Not LOGGED IN !");
            //redirigo alla landingPage
            RequestDispatcher rd = request.getRequestDispatcher("/index.html");           
            err = true;
        } 
        
        session.setAttribute("user", user);
        
        
        
        if (err){
            request.getRequestDispatcher("/errorModifyRest.jsp").forward(request, response);
        }else{
            //forward della richiesta
            //request.getRequestDispatcher("/ProfileServlet").forward(request, response);
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

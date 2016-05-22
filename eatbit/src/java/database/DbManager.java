/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.derby.client.am.SqlException;

/**
 *
 * @author jacopo
 */
public class DbManager {
    private Connection con;
    
    public DbManager(String url) throws ClassNotFoundException, SQLException
    {
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        con= DriverManager.getConnection(url);
        con.setAutoCommit(false);
    }
    
      /**
     * Prova ad inserire(registrare sul sito) un utente nel database.
     * @param user Oggetto User con i dati all'iterno
     * @return 0 se è andata a buon fine, 1 se nn ha registrato xk esiste un
     * utente con quella email, 2 se esiste un utente con quel nick, 3 se non
     * è andata a buon fine per altri motivi
     *  
     */
    public int registerUser(User user) 
    {
        int res=3;
        try 
        {
            PreparedStatement st=con.prepareStatement("insert into users(NAME,SURNAME,"
                    + "NICKNAME,EMAIL,PASSWORD,AVATAR_PATH,REVIEWS_COUNTER,REVIEWS_POSITIVE) values(?,?,?,?,?,?,?,?)");
            st.setString(1,user.getName());
            st.setString(2,user.getSurname());
            st.setString(3,user.getNickname());
            st.setString(4,user.getEmail());
            st.setString(5,BCrypt.hashpw(user.getPassword(),BCrypt.gensalt()));
            st.setString(6,user.getAvatar_path());
            st.setInt(7,user.getReviews_counter());
            st.setInt(8,user.getReviews_positive());
            try
            {
                st.executeUpdate();
                con.commit();
                res=0;
            }
            catch(SQLException e)
            {
                //codice se trova roba con stesse keys o unique
                if(e.getSQLState().equals("23505"))
                {
                    if(findUserByEmail(user.getEmail()))
                        res=1;
                    else if(findUserByNickname(user.getNickname()))
                        res=2;
                    //fare che ritorna intero e 2 statement per fare query e cercare se è a causa email o nome
                }
                else 
                    throw e;
            }
            finally
            {
                st.close();
            }
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    /**
     * Per autenticare uno User, di cui si fornisce nick/mail e password.
     * @param nickOrEmail nick o email dell utente che fa login
     * @param password password
     * @return null se la password non matcha o se l'utente non esiste, uno User con password "pulita via" altrimenti
     * @throws java.sql.SQLException
     */
    public User loginUserByEmailOrNickname(String nickOrEmail, String password) throws SQLException
    {
        User user=null;
        try
        (PreparedStatement st = con.prepareStatement("select * from USERS where EMAIL=? OR NICKNAME=?")) {
            st.setString(1,nickOrEmail);
            st.setString(2,nickOrEmail);
            try
            (ResultSet rs = st.executeQuery()) {
                con.commit();
                if(rs.next())
                {
                    user=new User();
                    user.setId(rs.getInt("ID"));
                    user.setName(rs.getString("NAME"));
                    user.setSurname(rs.getString("SURNAME"));
                    user.setNickname(rs.getString("NICKNAME"));
                    user.setEmail(rs.getString("EMAIL"));
                    user.setPassword(rs.getString("PASSWORD"));                
                    user.setAvatar_path(rs.getString("AVATAR_PATH"));
                    user.setReviews_counter(rs.getInt("REIVEWS_COUNTER"));
                    user.setReviews_positive(rs.getInt("REVIEWS_POSITIVE"));
                    if(!BCrypt.checkpw(password, user.getPassword()))
                        user=null;
                    else
                        user.setPassword("placeholder");
                }
            }
        }
        catch(SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }
    private boolean findUserByEmail(String email) throws SQLException
    {
        boolean res=true;
        try
        (PreparedStatement st = con.prepareStatement("select * from USERS where EMAIL=?")) {
            st.setString(1, email);
            try
            (ResultSet rs = st.executeQuery()) {
                con.commit();
                res=rs.next();
            }
        }
        catch(SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
        private boolean findUserByNickname(String nick) throws SQLException
    {
        boolean res=true;
        try
        (PreparedStatement st = con.prepareStatement("select * from USERS where NICKNAME=?")) {
            st.setString(1,nick);
            try
            (ResultSet rs = st.executeQuery()) {
                con.commit();
                res=rs.next();
            }
        }
        catch(SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    /**
     * Restituisce le review di un utente.
     * @param user L'utente di cui cercare le reviews.
     * @return Un ArrayList di oggetti Review dell'utente.
     * @throws java.sql.SQLException
     */
    public ArrayList<Review> getUserReviews(User user) throws SQLException
    {
        ArrayList<Review> reviews=new ArrayList();
        try
        (PreparedStatement st = con.prepareStatement("select * from REVIEWS where ID_CREATOR=?")) 
        {
            st.setInt(1,user.getId());
            try
            (ResultSet rs = st.executeQuery()) {
                con.commit();
                while(rs.next())
                {
                    Review review=new Review();
                    review.setId(rs.getInt("ID"));
                    review.setGlobal_value(rs.getInt("GLOBAL_VALUE"));
                    review.setFood(rs.getInt("FOOD"));
                    review.setService(rs.getInt("SERVICE"));
                    review.setValue_for_money(rs.getInt("VALUE_FOR_MONEY"));
                    review.setAtmosphere(rs.getInt("ATMOSPHERE"));
                    review.setName(rs.getString("NAME"));
                    review.setDescription(rs.getString("DESCRIPTION"));
                    review.setDate_creation(rs.getTimestamp("DATE_CREATION"));
                    review.setId_restaurant(rs.getInt("ID_RESTAURANT"));
                    review.setId_creator(rs.getInt("ID_CREATOR"));
                    review.setId_photo(rs.getInt("ID_PHOTO"));
                    review.setLikes(rs.getInt("LIKES"));
                    reviews.add(review);
                }
            }
        }
        return reviews;
    }
    
    /**
     * Le notificazioni di un utente (n.b. NON per admin).
     * @param user L'utente di cui si vuole cercare le notifiche.
     * @return Un ArrayList contenente oggetti di tipo Notification.
     * @throws SQLException 
     */
    public ArrayList<Notification> getUserNotifications(User user) throws SQLException
    {
        ArrayList<Notification> notifications=new ArrayList();
        try
        (PreparedStatement st = con.prepareStatement("select * from NOTIFICATIONS where ID=?")) 
        {
            st.setInt(1,user.getId());
            try
            (ResultSet rs = st.executeQuery()) {
                con.commit();
                while(rs.next())
                {
                    Notification notification=new Notification();
                    notification.setUser_id(rs.getInt("ID"));
                    notification.setDescription(rs.getString("DESCRIPTION"));
                    notifications.add(notification);
                }
            }
            
        }
        //Timestamp currentTimestamp =new Timestamp(Calendar.getInstance().getTime().getTime());
        // SELECT * FROM SAMEPLE_TABLE FETCH FIRST 10 ROWS ONLY
        //roba per dopo
        return notifications;
    }
    
    /**
     * Restituisce un numero di contesti, una classe formata da una review, uno user e una reply;
     * lo user è chi ha fatto la reply, la review è ciò a cui la reply vuole rispondere.
     * Da usare p.e. per mostrare ad un admin per confermare o no una reply.
     * Il timestamp di un contesto "prelevato" viene refrehsato al timestamp attuale
     * in modo che non sarà prelevabile da altri admin per 1 ora.
     * @param many Quanti contesti (al max) da ricevere.
     * @return Un ArrayList di contesti.
     * @throws SQLException 
     */
    public ArrayList<ReplyContext> getRepliesToBeConfirmed(int many) throws SQLException
    {
        ArrayList<ReplyContext> contesti= new ArrayList();
        try
        (PreparedStatement st= con.prepareStatement("select * from REPLIES_TO_BE_CONFIRMED"
                + " where {fn TIMESTAMPDIFF( SQL_TSI_HOUR, DATE_ADMIN_TOOK,?)} >= 1 "
                + "FETCH FIRST ? ROWS ONLY"))
        {
            Timestamp timestamp= new Timestamp(Calendar.getInstance().getTime().getTime());
            st.setTimestamp(1,timestamp);
            st.setInt(2, many);
            try
            (ResultSet rs= st.executeQuery())
            {
                con.commit();
                while(rs.next())
                {
                    contesti.add(getReplyContext(rs.getInt("ID")));
                    rs.updateTimestamp("DATE_ADMIN_TOOK", timestamp);
                    con.commit();
                }
            }
        }
        return contesti;
    }
    
    private ReplyContext getReplyContext(int id_reply) throws SQLException
    {
        ReplyContext contesto= new ReplyContext();
        contesto.setReply(getReplyFromId(id_reply));
        contesto.setReview(getReviewById(contesto.getReply().getId_review()));
        contesto.setUser(getUserById(contesto.getReply().getId_owner()));
        return contesto;
    }
    
    private Reply getReplyFromId(int id_reply) throws SQLException
    {
        Reply reply= null;
        try(PreparedStatement st= con.prepareStatement("select * from REPLIES where id=?"))
        {
            st.setInt(1, id_reply);
            try
            (ResultSet rs= st.executeQuery())
            {
                con.commit();
                if(rs.next())
                {
                    reply= new Reply();
                    reply.setId(id_reply);
                    reply.setDescription(rs.getString("DESCRIPTION"));
                    reply.setDate_creation(rs.getTimestamp("DATE_CREATION"));
                    reply.setId_review(rs.getInt("ID_REVIEW"));
                    reply.setId_owner(rs.getInt("ID_OWNER"));
                    reply.setDate_validation(rs.getTimestamp("DATE_VALIDATION"));
                    reply.setId_validator(rs.getInt("ID_VALIDATOR"));
                    reply.setValidated(rs.getBoolean("VALIDATED"));
                }
            }
        }
        return reply;
    }
    
    private Review getReviewById(int id) throws SQLException
    {
        Review review=null;
        try(PreparedStatement st= con.prepareStatement("select * from REVIEWS where id=?"))
        {
            st.setInt(1,id);
            try
            (ResultSet rs= st.executeQuery())
            {
                con.commit();
                if(rs.next())
                {
                    review= new Review();
                    review.setId(rs.getInt("ID"));
                    review.setGlobal_value(rs.getInt("GLOBAL_VALUE"));
                    review.setFood(rs.getInt("FOOD"));
                    review.setService(rs.getInt("SERVICE"));
                    review.setValue_for_money(rs.getInt("VALUE_FOR_MONEY"));
                    review.setAtmosphere(rs.getInt("ATMOSPHERE"));
                    review.setName(rs.getString("NAME"));
                    review.setDescription(rs.getString("DESCRIPTION"));
                    review.setDate_creation(rs.getTimestamp("DATE_CREATION"));
                    review.setId_restaurant(rs.getInt("ID_RESTAURANT"));
                    review.setId_creator(rs.getInt("ID_CREATOR"));
                    review.setId_photo(rs.getInt("ID_PHOTO"));
                    review.setLikes(rs.getInt("LIKES"));
                }
            }
        }
        return review;
    }
    /**
     * 
     * @param id
     * @return Uno user con la password rimossa, null se non esiste o qualcosa va storto.
     * @throws SQLException 
     */
    private User getUserById(int id) throws SQLException
    {
        User user=null;
        try(PreparedStatement st= con.prepareStatement("select * from USERS where id=?"))
        {
            st.setInt(1, id);
            try
            (ResultSet rs= st.executeQuery())
            {
                con.commit();
                if(rs.next())
                {
                    user=new User();
                    user.setId(rs.getInt("ID"));
                    user.setName(rs.getString("NAME"));
                    user.setSurname(rs.getString("SURNAME"));
                    user.setNickname(rs.getString("NICKNAME"));
                    user.setEmail(rs.getString("EMAIL"));
                    user.setPassword("placeholder");                
                    user.setAvatar_path(rs.getString("AVATAR_PATH"));
                    user.setReviews_counter(rs.getInt("REIVEWS_COUNTER"));
                    user.setReviews_positive(rs.getInt("REVIEWS_POSITIVE"));
                }
            }
        }
        return user;
    }
    /**
     * Funzione per prendere una serie di photo segnalate e i loro contesti, 
     * solitamente usato per l'admin. Un contesto è la foto più lo user che lha 
     * postata. Una volta che un contesto è prelevato (restituito) non sarà
     * possibile restituirlo ancora tramite questo metodo per un ora.
     * @param many Quanti contesti (al max) si vuole ricevere.
     * @return Un ArrayList di contesti.
     * @throws SQLException 
     */
    public ArrayList<PhotoContext> getReportedPhotos(int many) throws SQLException
    {
        ArrayList<PhotoContext> contesti= new ArrayList();
        try
        (PreparedStatement st= con.prepareStatement("select * from REPORTED_PHOTOS"
                + " where {fn TIMESTAMPDIFF( SQL_TSI_HOUR, DATE_ADMIN_TOOK,?)} >= 1 "
                + "FETCH FIRST ? ROWS ONLY"))
        {
            Timestamp timestamp= new Timestamp(Calendar.getInstance().getTime().getTime());
            st.setTimestamp(1,timestamp);
            st.setInt(2, many);
            try
            (ResultSet rs= st.executeQuery())
            {
                con.commit();
                while(rs.next())
                {
                    contesti.add(getPhotoContext(rs.getInt("ID")));
                    rs.updateTimestamp("DATE_ADMIN_TOOK", timestamp);
                    con.commit();
                }
            }
        }
        return contesti;
    }
    
    private PhotoContext getPhotoContext(int id_photo) throws SQLException
    {
        PhotoContext contesto= new PhotoContext();
        contesto.setPhoto(getPhotoById(id_photo));
        contesto.setUser(getUserById(contesto.getPhoto().getId_owner()));
        return contesto;
    }
    
    private Photo getPhotoById(int id) throws SQLException
    {
        Photo photo= null;
        try(PreparedStatement st= con.prepareStatement("select * from PHOTOS where id=?"))
        {
            st.setInt(1, id);
            try
            (ResultSet rs= st.executeQuery())
            {
                con.commit();
                if(rs.next())
                {
                    photo= new Photo();
                    photo.setId(id);
                    photo.setName(rs.getString("NAME"));
                    photo.setDescription(rs.getString("DESCRIPTION"));
                    photo.setPath(rs.getString("PATH"));
                    photo.setId_restaurant(rs.getInt("ID_RESTAURANT"));
                    photo.setId_owner(rs.getInt("ID_OWNER"));
                }
            }
        }
        return photo;
    }
    
    private Restaurant getRestaurantById(int id) throws SQLException
    {
        Restaurant restaurant= null;
        try(PreparedStatement st= con.prepareStatement("select * from RESTAURANTS where id=?"))
        {
            st.setInt(1, id);
            try
            (ResultSet rs= st.executeQuery())
            {
                con.commit();
                if(rs.next())
                {
                    restaurant= new Restaurant();
                    restaurant.setId(rs.getInt("ID"));
                    restaurant.setName(rs.getString("NAME"));
                    restaurant.setDescription(rs.getString("DESCRIPTION"));
                    restaurant.setWeb_site_url(rs.getString("WEB_SITE_URL"));
                    restaurant.setGlobal_value(rs.getInt("GLOBAL_VALUE"));
                    restaurant.setId_owner(rs.getInt("ID_OWNER"));
                    restaurant.setId_creator(rs.getInt("ID_CREATOR"));
                    restaurant.setId_price_range(rs.getInt("ID_PRICE_RANGE"));
                    restaurant.setReviews_counter(rs.getInt("REVIEWS_COUNTER"));
                    restaurant.setValidated(rs.getBoolean("VALIDATED"));
                }
            }
        }
        return restaurant;
    }
    
    private AttemptContext getAttemptContext(int id_user, int id_restaurant, int creation_claim_both) throws SQLException
    {
        AttemptContext contesto= new AttemptContext();
        contesto.setUser(getUserById(id_user));
        contesto.setRestaurant(getRestaurantById(id_restaurant));
        contesto.setIsClaim(creation_claim_both);
        return contesto;
    }
    
    /**
     * Funzione per prendere una serie di richieste di ristoranti e i loro contesti, 
     * solitamente usato per l'admin. Un contesto è l'utente che ha fatto la richiesta,
     * il ristorante riguarda il ristorante in questione e una flag che indica il
     * tipo di richiesta.
     * Una volta che un contesto è restituito da questa funzione non sarà
     * possibile restituirlo ancora tramite questo metodo per un ora.
     * @param many Quanti contesti (al max) si vuole ricevere.
     * @return Un ArrayList di contesti.
     * @throws SQLException 
     */
    public ArrayList<AttemptContext> getRestaurantsRequests(int many) throws SQLException
    {
        ArrayList<AttemptContext> contesti= new ArrayList();
        try
        (PreparedStatement st= con.prepareStatement("select * from RESTAURANTS_REQUESTS"
                + " where {fn TIMESTAMPDIFF( SQL_TSI_HOUR, DATE_ADMIN_TOOK,?)} >= 1 "
                + "FETCH FIRST ? ROWS ONLY"))
        {
            Timestamp timestamp= new Timestamp(Calendar.getInstance().getTime().getTime());
            st.setTimestamp(1,timestamp);
            st.setInt(2, many);
            try
            (ResultSet rs= st.executeQuery())
            {
                con.commit();
                while(rs.next())
                {
                    contesti.add(getAttemptContext(rs.getInt("ID"),rs.getInt("ID_RESTAURANT"),
                            rs.getInt("CREATION_CLAIM_BOTH_FLAG")));
                    rs.updateTimestamp("DATE_ADMIN_TOOK", timestamp);
                    con.commit();
                }
            }
        }
        return contesti;
    }
    
    /**
     *Per ricevere un numero pari a many di contesti (user+review) riguardanti
     * le review che hanno subito un report. 
     * Una volta che un contesto è stato restituito da questo metodo non sarà
     * restituit per un ora.
     * @param many Quanti contesti (al max) si vuole ricevere.
     * @return Un ArrayList con i contesti.
     * @throws SQLException
     */
    public ArrayList<ReviewContext> getReportedReviews(int many) throws SQLException
    {
        ArrayList<ReviewContext> contesti= new ArrayList();
        try
        (PreparedStatement st= con.prepareStatement("select * from REPORTED_REVIEWS"
                + " where {fn TIMESTAMPDIFF( SQL_TSI_HOUR, DATE_ADMIN_TOOK,?)} >= 1 "
                + "FETCH FIRST ? ROWS ONLY"))
        {
            Timestamp timestamp= new Timestamp(Calendar.getInstance().getTime().getTime());
            st.setTimestamp(1,timestamp);
            st.setInt(2, many);
            try
            (ResultSet rs= st.executeQuery())
            {
                con.commit();
                while(rs.next())
                {
                    contesti.add(getReviewContext(rs.getInt("ID")));
                    rs.updateTimestamp("DATE_ADMIN_TOOK", timestamp);
                    con.commit();
                }
            }
        }
        return contesti;
    }
    
    private ReviewContext getReviewContext(int review_id) throws SQLException
    {
        ReviewContext contesto= new ReviewContext();
        contesto.setReview(getReviewById(review_id));
        contesto.setUser(getUserById(contesto.getReview().getId_creator()));
        return contesto;
    }
    
    /**
     * Chiude la connessione al database!
     */
    public static void shutdown() 
    {
        try {
            DriverManager.getConnection("jdbc:derby://localhost:1527/eatbitDB;shutdown=true;user=eatbitDB;password=password");
        } catch (SQLException ex) {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
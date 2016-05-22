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
     * @param user Il bean user con i dati all'iterno
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
     * 
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
     * 
     * @param user L'utente di cui cercare le reviews.
     * @return Un vettore di oggetti Review dell'utente.
     * @throws java.sql.SQLException
     */
    public Vector<Review> getUserReviews(User user) throws SQLException
    {
        Vector<Review> reviews=new Vector();
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
     * 
     * @param user L'utente di cui si vuole cercare le notifiche.
     * @return Un vettore contenente oggetti di tipo Notification.
     * @throws SQLException 
     */
    public Vector<Notification> getUserNotifications(User user) throws SQLException
    {
        Vector<Notification> notifications=new Vector();
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
    
    public static void shutdown() 
    {
        try {
            DriverManager.getConnection("jdbc:derby://localhost:1527/eatbitDB;shutdown=true;user=eatbitDB;password=password");
        } catch (SQLException ex) {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
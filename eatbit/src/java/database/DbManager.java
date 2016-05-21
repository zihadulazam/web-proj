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
    
    private boolean findUserByEmail(String email) throws SQLException
    {
        boolean res=true;
        try
        {
            PreparedStatement st=con.prepareStatement("select * from USERS where EMAIL=?");
            st.setString(1, email);
            ResultSet rs=st.executeQuery(email);
            con.commit();
            res=rs.next();
            rs.close();
            st.close();
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
        {
            PreparedStatement st=con.prepareStatement("select * from USERS where NICKNAME=?");
            st.setString(1,nick);
            ResultSet rs=st.executeQuery(nick);
            con.commit();
            res=rs.next();
            rs.close();
            st.close();
        }
        catch(SQLException ex)
        {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
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
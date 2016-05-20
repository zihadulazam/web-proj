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
    }
    
    /*private boolean alreadyRegistered(User user) 
    {
        try {
            ResultSet rs=null;
            PreparedStatement st= con.prepareStatement("select * from users where NICKNAME=? OR EMAIL=?");
            boolean res=true;
            st.setString(1,user.getNickname());
            st.setString(2,user.getEmail());
            rs=st.executeQuery();
            res=rs.next();//nn posso fare direttamente in return xk se chiudo la query result si chiude
            st.close();
            rs.close();
            return res;
        } catch (SQLException ex) {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }*/
    
    public boolean register(User user) 
    {
        try 
        {
            PreparedStatement st= con.prepareStatement("insert into users values(?,?,?,?,?,?,?,?)");
            st.setInt(1,user.getId());
            st.setString(2,user.getName());
            st.setString(3,user.getSurname());
            st.setString(4,user.getNickname());
            st.setString(5,user.getEmail());
            st.setString(6,BCrypt.hashpw(user.getPassword(),BCrypt.gentsalt()));
            st.setInt(7,user.getReviews_counter());
            st.setInt(8,user.getReviews_positive());
            try
            {
                con.setAutoCommit(false);
                st.executeUpdate();
                con.commit();
            }
            catch(SQLException e)
            {
                if(e.getSQLState().equals("23505"))
                {
                    return false;
                }
                else 
                    throw e;
            }
            finally
            {
                st.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
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
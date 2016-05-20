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
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    private boolean alreadyRegistered(User user) 
    {
        try {
            ResultSet rs;
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
    }
    
    /*public User register(User user) 
    {
        if(!alreadyRegistered(user))
        {
            try {
                PreparedStatement st= con.prepareStatement("insert into users values(?,?)");
                st.setString(1, nameInput);
                st.setString(2, passwordInput);
                st.executeUpdate();
                user=new User(nameInput);
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return user;
    }*/
    public static void shutdown() 
    {
        try {
            DriverManager.getConnection("jdbc:derby://localhost:1527/eatbitDB;shutdown=true;user=eatbitDB;password=password");
        } catch (SQLException ex) {
            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
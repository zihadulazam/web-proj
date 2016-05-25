/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import database.DbManager;
import database.User;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 *
 * @author jacopo
 */
public class ContextListener implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        try 
        {
            String url= (String) sce.getServletContext().getInitParameter("dburl");
            sce.getServletContext().setAttribute("dbmanager", new DbManager(url));
            User user= new User();
            user.setAvatar_path("à");
            user.setEmail(".ì");
            user.setId(1);
            user.setName("yy");
            user.setNickname("kek");
            user.setPassword("ciao");
            user.setReviews_counter(1);
            user.setReviews_negative(2);
            user.setReviews_positive(2);
            user.setSurname("ge");
            user.setType(2);
            ((DbManager)sce.getServletContext().getAttribute("dbmanager")).registerUser(user);
        } 
        catch (ClassNotFoundException ex) {
            Logger.getLogger(ContextListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SQLException ex) {
            Logger.getLogger(ContextListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) 
    {
        DbManager.shutdown();
    }
}

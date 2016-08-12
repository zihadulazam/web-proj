/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import database.DbManager;
import database.User;
import info.debatty.java.stringsimilarity.KShingling;
import java.io.IOException;
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
            DbManager manager= new DbManager(url);
            sce.getServletContext().setAttribute("dbmanager", manager);
        } 
        catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(ContextListener.class.getName()).log(Level.SEVERE, ex.toString(), ex);
            System.exit(1);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) 
    {
        DbManager manager= (DbManager) sce.getServletContext().getAttribute("dbmanager");
        manager.shutdown();
    }
}

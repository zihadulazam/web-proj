/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.io.File;

/**
 * 
 * @author mario
 */
public class FileDeleter {
    /**
     * procedura statica che prende il path assoluto di un file e lo cancella
     * @param _path percorso assoluto del file da cancellare 
     * es_path:  C:\Users\andrei\Desktop\PROGETTO\web-proj\eatbit\web\img\avater\be320b71-bd27-471a-80ba-b1e64dacf25e.jpg
     * @return ritorna true se il file é stato cancellato, false altrimenti
     */
    public static boolean deleteFile(String _path){     

        boolean success = (new File (_path)).delete();
        if (success) {
            System.out.println("The file has been successfully deleted!!");           
        }else{
            System.out.println("Maybe some error!!");
        }
        return success;
    }
    
}

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
    
    private FileDeleter(){}
    
    /**
     * procedura statica che prende il path assoluto di un file e lo cancella
     * @param _path percorso assoluto del file da cancellare 
     * es_path:  C:\Users\andrei\Desktop\PROGETTO\web-proj\eatbit\web\img\avatar\be320b71-bd27-471a-80ba-b1e64dacf25e.jpg
     */
    public static void deleteFile(String _path){     

        (new File (_path)).delete();
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * prende il path di un file, crea il path reale e lo cancella
 * @author andrei
 */
public class FileDeleter {

    /*
    public static void main(String[] args)
    {
        deleteFile("img/avater/dccc8c95-30a8-4570-a592-e3191051792c.jpg");
    }
    */
    
    public FileDeleter(){};
    
    public static boolean deleteFile(String _path){
        String replace = _path.replace("/", "\\");
        Path path;
        path = FileSystems.getDefault().getPath(".");
        System.out.println("Absolute Path: " + path.toAbsolutePath());
        
        String p = path.toAbsolutePath().toString().replace(".", "") + "web\\";
        
        String realPath = p + replace;
        System.out.println("real: " + realPath);
        
        boolean success = (new File (realPath)).delete();
        if (success) {
            System.out.println("The file has been successfully deleted!!");
            return true;
        }else{
            System.out.println("Maybe some error!!");
            return false;
        }
    }
}

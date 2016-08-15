/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * prende il path di un file, crea il path reale e lo cancella
 * @author andrei
 */
public class FileDeleter {

    public static void main(String[] args) {
        deleteFile("img/avater.png");
    }
    
    public static void deleteFile(String _path){
        
        Path path;

        path = Paths.get(".");
        System.out.println("toString Path: " + path.toAbsolutePath());

        path = FileSystems.getDefault().getPath(".");
        System.out.println("Absolute Path: " + path.toAbsolutePath());
        
        Path p1 = Paths.get(_path);
        String realPath = p1.toAbsolutePath().toString();
        System.out.println("real: " + realPath);
        
        boolean success = (new File (realPath)).delete();
        if (success) {
            System.out.println("The file has been successfully deleted");
        }else{
            System.out.println("Maybe some error!!");
        }
    }
}

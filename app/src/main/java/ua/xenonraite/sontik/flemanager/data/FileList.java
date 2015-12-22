package ua.xenonraite.sontik.flemanager.data;

import java.util.ArrayList;

/**
 * Created by Sontik on 22.12.2015.
 */
public class FileList {

    private static FileList instance = null;

    private static ArrayList<FileCell> lileList = null;

    private FileList(){
        lileList = new ArrayList<FileCell>();
    }

     public static ArrayList<FileCell> getFileList(){
         ifNotExistClass();
         return lileList;
    }

    public static void addFileList(FileCell cell){
        ifNotExistClass();
        lileList.add(cell);
    }

    public static void cleanFileList(){
        ifNotExistClass();
        lileList.clear();
    }

    private static void ifNotExistClass(){
        if(instance == null) {
            instance = new FileList();
        }
    }
}

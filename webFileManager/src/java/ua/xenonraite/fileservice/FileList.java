/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.xenonraite.fileservice;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Sontik
 */
class FileList {

    String[] fileList;
    int[] fileSizeList;
    public static final String DIRECTORY_PATH = "D://files";

    public FileList() {
        File folder = new File(DIRECTORY_PATH);
        File[] listOfFiles = folder.listFiles();

        
        ArrayList<String> fileListArray = new ArrayList<String>();
        ArrayList<Integer> listSize = new ArrayList<Integer>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
                fileListArray.add(listOfFiles[i].getName());
                
                listSize.add((int)listOfFiles[i].length());
                System.out.println("File size" + (int)listOfFiles[i].length());
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
        
        fileSizeList = new int[fileListArray.size()];
        
        for(int t = 0; t< fileListArray.size() ; t++){
            fileSizeList[t] = listSize.get(t);
        }
        
        fileList = new String[fileListArray.size()];
        fileListArray.toArray(fileList);
    }

    public int[] getFileSizeList() {
        return fileSizeList;
    }

    public void setFileSizeList(int[] fileSizeList) {
        this.fileSizeList = fileSizeList;
    }

    public void setFileList(String[] fileList) {
        this.fileList = fileList;
    }

    public String[] getFileList() {
        return fileList;
    }
    
    public static void removeFile(String file){
        File f = new File(DIRECTORY_PATH+"//"+file);
        f.delete();
    }
    
    public static boolean renameFile(String oldnamefile,String newFIleName){
        File f = new File(DIRECTORY_PATH+"//"+oldnamefile);
        boolean success = f.renameTo(new File(DIRECTORY_PATH+"//"+ newFIleName));
        return success;
    }

}

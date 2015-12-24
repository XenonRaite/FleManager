/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.xenonraite.fileservice;

/**
 *
 * @author Sontik
 */
public class ServerAPI {
    public static final String GET_FILE_LIST = "/printfile";
    public static final String RENAME_FILE = "/remanefile";
    public static final String RENAME_FILE_NEW_METHOD = "/remanefilejson";
    public static final String DELITE_FILES = "/deletefile";
    public static final String DELITE_FILES_NEW_METHOD = "/deletefilejson";

    public static final String FILE_DOWNLOAD = "/downloadfilejson";
    


    private static final String BASE_URL = "http://109.86.155.68:8050/FileManager/webresources/filemanager";
    
}

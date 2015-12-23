package ua.xenonraite.sontik.flemanager.http;

/**
 * Created by Sontik on 22.12.2015.
 */
public class ServerAPI {
    public static final String GET_FILE_LIST = "/printfile";
    public static final String RENAME_FILE = "/remanefile";
    public static final String DELITE_FILES = "/deletefile";
    public static final String DELITE_FILES_NEW_METHOD = "/deletefilejson";


    private static final String BASE_URL = "http://109.86.155.68:8050/FileManager/webresources/filemanager";
}

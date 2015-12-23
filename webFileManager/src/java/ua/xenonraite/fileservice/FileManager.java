/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.xenonraite.fileservice;

import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Sontik
 */
@Path("filemanager")
public class FileManager {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of FileManager
     */
    public FileManager() {
    }

    /**
     * Retrieves representation of an instance of
     * ua.xenonraite.fileservice.FileManager
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {

        return "hello";
    }

    /**
     * PUT method for updating or creating an instance of FileManager
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }

    @GET
    @Path(ServerAPI.GET_FILE_LIST)
    @Produces(MediaType.APPLICATION_JSON)
    public FileList produceJSONfile() {

        return new FileList();

    }

    @DELETE
    @Path(ServerAPI.DELITE_FILES+"/{filename}")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response produceDeleteFile(@PathParam("filename") PathSegment fileName) {

        
        Set<String> ids = fileName.getMatrixParameters().keySet();

        String names = "";
        for (String filename : ids) {
            FileList.removeFile(filename);
            names = names + filename;
        }
        System.out.println("file to deleted" + names);

        return Response.status(Response.Status.OK).entity(names).build();
    }

    @DELETE
    @Path(ServerAPI.DELITE_FILES_NEW_METHOD)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response produceJSONDeleteFile(ListIds ids) {

        if (ids != null && ids.getIds() != null) {

            FileList fileList = new FileList();
            for(int id : ids.getIds()){
                FileList.removeFile(fileList.getFileList()[id]);
            }
            
            return Response.ok().build();
        }

        return Response.serverError().build();
    }

    @GET
    @Path("/listids")
    @Produces(MediaType.APPLICATION_JSON)
    public ListIds getListIds() {

        return new ListIds(new int[]{1, 2, 3, 4, 5, 6});
    }

    @PUT
    @Path(ServerAPI.RENAME_FILE+"/{fileoldname}/{filenewname}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response produceRenameFile(@PathParam("fileoldname") String fileoldname, @PathParam("filenewname") String filenewname) {

        boolean status = FileList.renameFile(fileoldname, filenewname);
        if (status) {
            return Response.status(Response.Status.FOUND).entity("file renamed").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("file renamed failure").build();
        }
    }
    
    @PUT
    @Path(ServerAPI.RENAME_FILE_NEW_METHOD)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response produceRenameFileJSON(RenameFileCell renameFileCell) {

        try {
            System.out.println("new Name"+renameFileCell.getNewname()+new String(renameFileCell.getNewname().getBytes("ANSI-1251"), "UTF-8") );
        
        
        
        boolean status = FileList.renameFile(new String(renameFileCell.getOldname().getBytes("ANSI-1251"), "UTF-8"), new String(renameFileCell.getNewname().getBytes("ANSI-1251"), "UTF-8"));
        if (status) {
            return Response.status(Response.Status.OK).entity("file renamed").build();
        } else {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity("file renamed failure").build();
        }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.EXPECTATION_FAILED).entity("file renamed failure").build();
    }

}

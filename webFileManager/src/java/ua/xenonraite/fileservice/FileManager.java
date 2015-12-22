/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.xenonraite.fileservice;

import java.util.Set;
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
    @Path("/printfile")
    @Produces(MediaType.APPLICATION_JSON)
    public FileList produceJSONfile() {

        return new FileList();

    }
    
    @DELETE
    @Path("/deletefile/{filename}")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response produceDeleteFile(@PathParam("filename")PathSegment fileName) {
        
        //System.out.println(fileName);
        
       // String[] files = fileName.split(";");
       
       Set<String> ids = fileName.getMatrixParameters().keySet();
       
       String names = "";
       for(String filename : ids){
            FileList.removeFile(filename);
            names = names+filename;
       }
       System.out.println("file to deleted"+names);
       
       //String filedeleted = "File delete" + fileName;
        
        
        //for(String fileNameForDelete : files)
        //    FileList.removeFile(fileNameForDelete);
        
        return Response.status(Response.Status.OK).entity(names).build();
    }
    
    @PUT
    @Path("/remanefile/{fileoldname}/{filenewname}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response produceJSONdeletefile( @PathParam("fileoldname") String fileoldname,@PathParam("filenewname") String filenewname) {
        
        FileList.renameFile(fileoldname, filenewname);
        
        return Response.status(Response.Status.OK).entity("file rnamed").build();
    }
    
}

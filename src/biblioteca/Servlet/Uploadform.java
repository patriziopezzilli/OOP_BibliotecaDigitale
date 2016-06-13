package biblioteca.Servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import biblioteca.Model.Utente;
import biblioteca.Util.DataUtil;
import biblioteca.Util.Database;
import biblioteca.Util.FreeMarker;
import biblioteca.Util.SecurityLayer;
/**
 * @author Patrizio
 *
 */
public class Uploadform extends HttpServlet {

	/*
	 * uso il parametro "data" in modo globale, per utilizzarlo sia nel GET che nel POST 
	 */

	Map<String,Object> data= new HashMap<String,Object>();
    
	 private static final long serialVersionUID = 1L;
	 
	    private static final String UPLOAD_DIRECTORY = "upload";
	    private static final int THRESHOLD_SIZE     = 1024 * 1024 * 3;  // 3MB
	    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
	    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
	 

	 /**
	     * Caricamento pagina di Home
	     *
	     * @param request servlet request
	     * @param response servlet response
	     * @throws ServletException if a servlet-specific error occurs
	     * @throws IOException if an I/O error occurs
	     */
	    @Override
	    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
	        
           HttpSession s = SecurityLayer.checkSession(request);

	    	  if (s != null) {
	    		  	
	    		  	String nome= request.getParameter("nome");
	    		  	int idd= Integer.parseInt(request.getParameter("id"));
	    			int indice= Integer.parseInt(request.getParameter("index"));
	    		  	data.put("nomeopera", nome);
	    		  	data.put("index", indice);
	    		  	data.put("id", idd);
	                FreeMarker.process("uploadform.html", data, response, getServletContext());
	                
	            } else FreeMarker.process("index.html", data, response, getServletContext());
	    }

	     @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws IOException {
	    	 
	    	 // checks if the request actually contains upload file
	    	 Map<String,Object> map= new HashMap<String,Object>();
	         if (!ServletFileUpload.isMultipartContent(request)) {
	             PrintWriter writer = response.getWriter();
	             writer.println("Request does not contain upload data");
	             writer.flush();
	             return;
	         }
	          
	         // configures upload settings
	         DiskFileItemFactory factory = new DiskFileItemFactory();
	         factory.setSizeThreshold(THRESHOLD_SIZE);
	         factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
	          
	         ServletFileUpload upload = new ServletFileUpload(factory);
	         upload.setFileSizeMax(MAX_FILE_SIZE);
	         upload.setSizeMax(MAX_REQUEST_SIZE);
	          
	         // constructs the directory path to store upload file
	         String uploadPath = getServletContext().getRealPath("")
	             + File.separator + UPLOAD_DIRECTORY;
	         // creates the directory if it does not exist
	         File uploadDir = new File(uploadPath);
	         if (!uploadDir.exists()) {
	             uploadDir.mkdir();
	         }
	          
	         try {
	             // parses the request's content to extract file data
	             List formItems = upload.parseRequest(request);
	             Iterator iter = formItems.iterator();
	              
	             // iterates over form's fields
	             while (iter.hasNext()) {
	                 FileItem item = (FileItem) iter.next();
	                 // processes only fields that are not form fields
	                 if (!item.isFormField()) {
	                     String fileName2 = new File(item.getName()).getName();
	                     String fileName= item.getName();
	                     String filePath = uploadPath + File.separator + fileName;
	                     File storeFile = new File(filePath);
	                      map.put("path", "upload/"+fileName);
	                     // saves the file on disk
	                     item.write(storeFile);
	                 }
	             }
	             request.setAttribute("message", "Upload has been done successfully!");
	         } catch (Exception ex) {
	             request.setAttribute("message", "There was an error: " + ex.getMessage());
	         }
	         
	         map.put("id_pub", data.get("id"));
	         map.put("indice", data.get("index"));

	         map.put("revisionato", 0);
	         try {
				Database.connect();
				Database.insertRecord("immagini", map);
		         Database.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         
	         data.put("messaggio", "Upload effettuato correttamente!");
			 FreeMarker.process("uploadform.html", data, response, getServletContext());


		}
}

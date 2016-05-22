package biblioteca.Servlet;

import static java.util.Objects.isNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import biblioteca.Model.Image;
import biblioteca.Model.Opera;
import biblioteca.Util.DataUtil;
import biblioteca.Util.Database;
import biblioteca.Util.FreeMarker;
import biblioteca.Util.SecurityLayer;

@MultipartConfig(maxFileSize = 16177215)    // upload file's size up to 16MB

public class Detail extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int BUFFER_SIZE = 16177215;
	
	
	 protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
	 
		 	
			Map<String,Object> data= new HashMap<String,Object>();
			HttpSession s = SecurityLayer.checkSession(request);
			String test = null;
			
		    String nome = null;
			 
			
		    if(!isNull(request.getParameter("next"))){
		    	 int idd= Integer.parseInt(request.getParameter("idd"));
				 int indice= Integer.parseInt(request.getParameter("indice"));
				 
				 data.put("id", idd);
				  data.put("nomeopera", nome);
				    data.put("index", indice+1);
				    response.sendRedirect("detail?id="+ data.get("id") +"&index="+data.get("index"));
		    	
		    }
		    
		    if(!isNull(request.getParameter("prev"))){
		    	
		    	 int idd= Integer.parseInt(request.getParameter("idd"));
				 int indice= Integer.parseInt(request.getParameter("indice"));
				 
				 data.put("id", idd);
				  data.put("nomeopera", nome);
				    data.put("index", indice-1);
				    response.sendRedirect("detail?id="+ data.get("id") +"&index="+data.get("index"));
		    	
		    }
		    	
		    	
		    /* Method POST for Img */
			 if(!isNull(request.getParameter("upload"))){
				 
				 int idd= Integer.parseInt(request.getParameter("idd"));
				 int indice= Integer.parseInt(request.getParameter("indice"));
				 String path= request.getParameter("path");
				 
				 Map<String,Object> Map= new HashMap<String,Object>();
			      
			        try {
						Database.connect();
						
						Map.put("indice", indice);
						Map.put("path", path);
						Map.put("id_pub", idd);
						
						Database.insertRecord("immagini", Map);
						Database.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        
			        data.put("id", idd);
				    data.put("nomeopera", nome);
				    data.put("index", indice);
				    data.put("path", path);
				    response.sendRedirect("detail?id="+ data.get("id") +"&index="+data.get("index"));
			   //     FreeMarker.process("success.html" , data, response, getServletContext());
				}
				
			try {
				test = DataUtil.getUsername((String) s.getAttribute("username"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		        data.put("test", test);
		        String path= null;
		        int index= Integer.parseInt(request.getParameter("index"));
			    int id=Integer.parseInt(request.getParameter("id")) ;
		      try {
				Database.connect();
				ResultSet rs= Database.selectRecord("pub", "id_op="+id);
				while( rs.next() ){
					 nome= rs.getString("nome");
				}
				Database.close();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      /* Prendo immagine da mostrare a video (se esiste) */
		      Database.connect();
		      ResultSet cs= Database.selectRecord("immagini", "indice="+index+"&&id_pub="+id+"");
		      while(cs.next()){
		    	  
		    	path=cs.getString("path");
		    	
		      }
		      
		      Database.close();
		      /* Prendo immagine da mostrare a video */
		      data.put("path",path);
		      data.put("id", id);
		      data.put("nomeopera", nome);
		      data.put("index", index);
		      
		  FreeMarker.process("detail.html", data, response, getServletContext());
		 
		 
		 
	 }
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Detail() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		        try {
					processRequest(request, response);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			processRequest(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	     
	}
}

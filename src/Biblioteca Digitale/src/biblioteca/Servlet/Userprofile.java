package biblioteca.Servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import biblioteca.Model.Opera;
import biblioteca.Util.DataUtil;
import biblioteca.Util.Database;
import biblioteca.Util.FreeMarker;
import biblioteca.Util.SecurityLayer;

public class Userprofile extends HttpServlet{

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
	    private void action_error(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    	Map<String,Object> data= new HashMap<String,Object>();

	    	//assumiamo che l'eccezione sia passata tramite gli attributi della request
	        //we assume that the exception has been passed using the request attributes
	        Exception exception = (Exception) request.getAttribute("exception");
	        String message;
	        if (exception != null && exception.getMessage() != null) {
	            message = exception.getMessage();
	        } else {
	            message = "Unknown error";
	        }
	        data.put("errore", message);
	        FreeMarker.process("404page.html", data, response, getServletContext());
	        
	      
	    }
	          
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
	    	Map<String,Object> data= new HashMap<String,Object>();

	    	 HttpSession s = SecurityLayer.checkSession(request);

	    	  if (s != null) {
	                data.put("session",s.getAttribute("username"));
	                String test = null;
					try {
						test = DataUtil.getUsername((String) s.getAttribute("username"));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		 	        data.put("test", test);
	                FreeMarker.process("userprofile.html", data, response, getServletContext());
	                
	            } else FreeMarker.process("index.html", data, response, getServletContext());
	    
	    	
	       
	    }
}
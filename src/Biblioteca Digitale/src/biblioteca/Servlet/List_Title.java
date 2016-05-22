/**
 * 
 */
package biblioteca.Servlet;

import static java.util.Objects.isNull;
import biblioteca.Model.Opera;
import biblioteca.Util.DataUtil;
import biblioteca.Util.Database;
import biblioteca.Util.FreeMarker;
import biblioteca.Util.SecurityLayer;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 * @author Patrizio
 *
 */
public class List_Title extends HttpServlet {
     
	    private void action_error(HttpServletRequest request, HttpServletResponse response) throws IOException {
	   	 Map data= new HashMap();

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
	     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	     * methods.
	     *
	     * @param request servlet request
	     * @param response servlet response
	     * @throws Exception 
	     * @throws ServletException if a servlet-specific error occurs
	     */
	    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception{
	   	 Map<String,Object> data= new HashMap<String,Object>();

	    	 HttpSession s = SecurityLayer.checkSession(request);
	    	 if(s!=null){
	    		
	    		 /* inserisco lista in lista_opere */
	    		 
	    		 
	    		List<Opera> temp= null;
	 	    	try{

	 				 Database.connect();
	 			        
	 			        ResultSet rs =Database.selectRecord("pub", "pubblicato = 1");
	 			        temp= new ArrayList<Opera>(); 
	 			        
	 			         while(rs.next()){ 
	 			        	 int id= rs.getInt("id_op");
	 			        	String nome= rs.getString("nome");
	 			        	Date date= rs.getDate("data");
	 			        	String autore=rs.getString("autore");
	 			        	String lingua=rs.getString("lingua");
	 			        	String utente=rs.getString("user");
	 			        	 
	 			     Opera tempopera= new Opera (id,nome,date,autore,lingua,utente);
	 			     
	 			        	temp.add(tempopera);
	 			       }
	 			      
	 			    Database.close();
	 			      }catch(NamingException e)
	 			      {     
	 			      } catch (SQLException e) {
	 			      }
	    		 
	    		 /* lo passo a data */
	    		 
	    		 String test= DataUtil.getUsername((String) s.getAttribute("username"));
	 	        data.put("test", test);
	 	        data.put("lista_opere", temp);
	 	        data.put("index", 0);
	 	        FreeMarker.process("list_title.html", data, response, getServletContext());
	    	 }else 	
	    		 FreeMarker.process("index.html", data, response, getServletContext());

	    	 
	       
	}

	    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	    /**
	     * Handles the HTTP <code>GET</code> method.
	     *
	     * @param request servlet request
	     * @param response servlet response
	     * @throws ServletException if a servlet-specific error occurs
	     * @throws IOException if an I/O error occurs
	     */
	    @Override
	    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        try {
				processRequest(request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }

	    /**
	     * Handles the HTTP <code>POST</code> method.
	     *
	     * @param request servlet request
	     * @param response servlet response
	     * @throws ServletException if a servlet-specific error occurs
	     * @throws IOException if an I/O error occurs
	     */
	    @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        try {
				processRequest(request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }

	    /**
	     * Returns a short description of the servlet.
	     *
	     * @return a String containing servlet description
	     */
	    @Override
	    public String getServletInfo() {
	        return "Short description";
	    }// </editor-fold>

	 
	}

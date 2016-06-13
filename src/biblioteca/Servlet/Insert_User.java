package biblioteca.Servlet;

import static biblioteca.Util.DataUtil.crypt;
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
/**
 * @author Patrizio
 *
 */
public class Insert_User extends HttpServlet{

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
	                FreeMarker.process("aggiungipers.html", data, response, getServletContext());
	                
	            } else FreeMarker.process("index.html", data, response, getServletContext());
	    
	    	
	       
	    }

	     @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
	    	 HttpSession s = SecurityLayer.checkSession(request);
	    	 
	    		Map<String,Object> data= new HashMap<String,Object>();

	    	  String email = request.getParameter("email");
	    	  String password = request.getParameter("password");
	    	  String nome = request.getParameter("nome");
	    	  String cognome = request.getParameter("cognome");
	    	  String annonascita= request.getParameter("annonascita");
	    	  String citta= request.getParameter("citta");
	    	  int gruppo= 1;

	    	  
             
             data.put("email",email);
             data.put("password", crypt(password));
             data.put("nome",nome);
             data.put("cognome",cognome);
             data.put("annonascita",annonascita);
             data.put("citta", citta);
             data.put("gruppo", gruppo);

             try {
				Database.connect();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             System.out.print(data.get("nome"));
            try {
				Database.insertRecord("users", data);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            try {
				Database.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            data.put("session",s.getAttribute("username"));
            String test = null;
				try {
					test = DataUtil.getUsername((String) s.getAttribute("username"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				List<Opera> temp= null;
	 	    	try{

	 				 Database.connect();
	 			        
	 			        ResultSet rs =Database.selectRecord("pub", "1");
	 			        temp= new ArrayList<Opera>(); 
	 			        
	 			         while(rs.next()){ 
	 			        	 int id= rs.getInt("id_op");
	 			        	String nome_op= rs.getString("nome");
	 			        	Date date= rs.getDate("data");
	 			        	String autore_op=rs.getString("autore");
	 			        	String lingua_op=rs.getString("lingua");
	 			        	String utente=rs.getString("user");
	 			        	 
	 			     Opera tempopera= new Opera (id,nome_op,date,autore_op,lingua_op,utente);
	 			     
	 			        	temp.add(tempopera);
	 			       }
	 			      
	 			    Database.close();
	 			      }catch(NamingException e)
	 			      {     
	 			      } catch (SQLException e) {
	 			      } catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				
				
				data.put("lista_opere", temp);
	 	        data.put("test", test);
            FreeMarker.process("list_title.html", data, response, getServletContext());
            
	    }

	    

	    /**
	     * Returns a short description of the servlet.
	     *
	     * @return a String containing servlet description
	     */
	    @Override
	    public String getServletInfo() {
	        return "Servlet per la gestione della home";
	    }
	    
	    
	    

	}
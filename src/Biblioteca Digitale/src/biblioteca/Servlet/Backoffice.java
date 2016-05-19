/**
 * 
 */
package biblioteca.Servlet;

import static java.util.Objects.isNull;
import biblioteca.Model.Opera;
import biblioteca.Model.Utente;
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
public class Backoffice extends HttpServlet {
	
	 Map data= new HashMap();
     
	    private void action_error(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
	    	
	    	 HttpSession s = SecurityLayer.checkSession(request);
	    	 if(s!=null){
	    		 
	    		 
	    		 /**
	    		  * 
	    		  * 
	    		  * 
	    		  * Gestione Opere
	    		  * 
	    		  * 
	    		  * 
	    		  */
	    		 
	    		 if(!isNull(request.getParameter("elimina"))){
	    			 
	    			 String opera= request.getParameter("opera");
	    			 Database.connect();
	    			 Database.deleteRecord("pub", "nome='"+opera+"'");
	    			 Database.close();
	    		 }
	    		 
	    		 
	    		 /**
	    		  * 
	    		  * 
	    		  * 
	    		  * Gestione Utenza
	    		  * 
	    		  * 
	    		  * 
	    		  */
	    		 
	    		 if(!isNull(request.getParameter("avanzato"))){
	    			 
	    			 String Utente= request.getParameter("utente");
	    			 Database.connect();
	    			 Map<String, Object> temp= new HashMap<String,Object>();
	    			 temp.put("gruppo",'7');   //Come da accordo con DB
	    			 Database.updateRecord("users", temp, "email='"+Utente+"'");
	    			 Database.close();
	    		 }
	    		 
	    		 if(!isNull(request.getParameter("aquisitore"))){
	    			 
	    			 String Utente= request.getParameter("utente");
	    			 Database.connect();
	    			 Map<String, Object> temp= new HashMap<String,Object>();
	    			 temp.put("gruppo",'5');
	    			 Database.updateRecord("users", temp, "email='"+Utente+"'");
	    			 Database.close();
	    		 }
	    		 
	    		 if(!isNull(request.getParameter("trascrittore"))){
	    			 
	    			 String Utente= request.getParameter("utente");
	    			 Database.connect();
	    			 Map<String, Object> temp= new HashMap<String,Object>();
	    			 temp.put("gruppo",'3');
	    			 Database.updateRecord("users", temp, "email='"+Utente+"'");
	    			 Database.close();
	    		 }
	    		 
	    		 if(!isNull(request.getParameter("revisore_a"))){
	    			 
	    			 String Utente= request.getParameter("utente");
	    			 Database.connect();
	    			 Map<String, Object> temp= new HashMap<String,Object>();
	    			 temp.put("gruppo",'6');
	    			 Database.updateRecord("users", temp, "email='"+Utente+"'");
	    			 Database.close();
	    		 }
	    		 
	    		 if(!isNull(request.getParameter("revisore_t"))){
	    			 
	    			 String Utente= request.getParameter("utente");
	    			 Database.connect();
	    			 Map<String, Object> temp= new HashMap<String,Object>();
	    			 temp.put("gruppo",'4');
	    			 Database.updateRecord("users", temp, "email='"+Utente+"'");
	    			 Database.close();
	    		 }
	    		 
	    		 if(!isNull(request.getParameter("pubblica"))){
	    			  String opera = request.getParameter("opera");
	    			  Database.connect();
	    			  Map<String, Object> temp= new HashMap<String,Object>();
	    			  temp.put("pubblicato",'1');
	    			  Database.updateRecord("pub", temp, "nome ='"+opera+"'");
	    			     		 }
	    		 
	    		 List<Opera> lista_opere= new ArrayList<Opera>();
	    		 List<Utente> lista_utenti= new ArrayList<Utente>();

	    		 /* inserisco lista in lista_opere */
	    		 
	    		 lista_opere= returnList();
	    		 lista_utenti= returnListutenti();
	    		 /* lo passo a data */
	    		 
	    		 String test= DataUtil.getUsername((String) s.getAttribute("username"));
	 	        data.put("test", test);
	 	        data.put("lista_opere", lista_opere);
	 	       data.put("lista_utenti", lista_utenti);
	 	       
	 	       
	 	       
	 	        FreeMarker.process("backoffice.html", data, response, getServletContext());
	    	 }else 	 FreeMarker.process("index.html", data, response, getServletContext());

	    	 
	       
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

	    
	    private List<Opera> returnList() throws Exception{
	    	
	    	List<Opera> temp= new ArrayList<Opera>();


	    	try{

				 Database.connect();
			        
			         ResultSet rs =Database.selectRecord("pub","1");
			         
			       
			         while(rs.next()){ 
			        	 int id= rs.getInt("id_op");
			        	String nome= rs.getString("nome");
			        	Date data= rs.getDate("data");
			        	String autore=rs.getString("autore");
			        	String lingua=rs.getString("lingua");
			        	String utente=rs.getString("user");
			        	 
			        	Opera tempopera= new Opera (id,nome,data,autore,lingua,utente);
			        	temp.add(tempopera);
			       }
			                   
			      }catch(NamingException e)
			      {     
			      } catch (SQLException e) {
			      }
	    	
			        return temp; 
	    	
	    	
	    }
	    
	    	private List<Utente> returnListutenti() throws Exception{
	    	
	    	List<Utente> temp2= new ArrayList<Utente>();


	    	try{

				 Database.connect();
			        
			         ResultSet ss =Database.selectRecord("users","1");
			         
			         while(ss.next()){ 
			        	 
				        String email= ss.getString("email");

			        	String nome= ss.getString("nome");
			        	String cognome= ss.getString("cognome");
			        	Date annonascita=ss.getDate("annonascita");
			        	String citta=ss.getString("citta");
			        	int gruppo=ss.getInt("gruppo");
			        	 
			        	Utente temputente= new Utente (email,nome,cognome,citta,annonascita,gruppo);
			        	temp2.add(temputente);
			       }
			        
			                   
			      }catch(NamingException e)
			      {     
			      } catch (SQLException e) {
			      }
	    	
			        return temp2; 
	    	
	    	
	    }
	
	}
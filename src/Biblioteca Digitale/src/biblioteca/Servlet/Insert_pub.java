/**
 * 
 */
package biblioteca.Servlet;

import biblioteca.Model.Opera;
import biblioteca.Util.DataUtil;
import static biblioteca.Util.DataUtil.crypt;
import biblioteca.Util.Database;
import biblioteca.Util.FreeMarker;
import biblioteca.Util.SecurityLayer;

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
import static java.util.Objects.isNull;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class Insert_pub extends HttpServlet{

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
	                FreeMarker.process("insert_pub.html", data, response, getServletContext());
	                
	            } else FreeMarker.process("index.html", data, response, getServletContext());
	    
	    	
	       
	    }

	     @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
	    	 HttpSession s = SecurityLayer.checkSession(request);
	    	Map<String,Object> data= new HashMap<String,Object>();

	    	  String nome = request.getParameter("titolo");
	    	  
	    	  //getting current date and time using Date class
	          DateFormat df = new SimpleDateFormat("dd/MM/yy");
	          Date dd = new Date();
              String autore = request.getParameter("autore");
              String numpagine = request.getParameter("numpagine");
              String lingua = request.getParameter("lingua");
              String user= (String) s.getAttribute("username");
              
              data.put("nome",nome);
              data.put("data",df.format(dd));
              data.put("autore",autore);
              data.put("num_pagine",numpagine);
              data.put("lingua",lingua);
              data.put("user", user);

              try {
				Database.connect();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
              
             try {
				Database.insertRecord("pub", data);
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
	 			        
	 			        ResultSet rs =Database.selectRecord("pub", "pubblicato = 1");
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
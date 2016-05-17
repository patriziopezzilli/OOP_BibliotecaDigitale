/**
 * 
 */
package biblioteca.Servlet;

import biblioteca.Util.DataUtil;
import static biblioteca.Util.DataUtil.crypt;
import biblioteca.Util.Database;
import biblioteca.Util.FreeMarker;
import biblioteca.Util.SecurityLayer;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
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
public class Index extends HttpServlet{

	
	Map<String,Object> data= new HashMap<String,Object>();
     
	  
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
	                data.put("session",s.getAttribute("username"));
	                String test = null;
					try {
						test = DataUtil.getUsername((String) s.getAttribute("username"));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		 	        data.put("test", test);
	                FreeMarker.process("list_title.html", data, response, getServletContext());
	                
	            } else FreeMarker.process("index.html", data, response, getServletContext());
	    }

	     @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
	        
	            HttpSession s = SecurityLayer.checkSession(request);


   		     String email = request.getParameter("email_login");
             String pass = request.getParameter("password_login");
             String email_reg = request.getParameter("email_reg");
             String pass_reg = request.getParameter("pass_reg");
             String nome_reg = request.getParameter("nome_reg");
             String cognome_reg = request.getParameter("cognome_reg");
             String citta_reg = request.getParameter("citta_reg");
             String data_reg = request.getParameter("data_reg");
             
             if(!isNull(email)){ 
                 
                 /*LOGIN -- METHOD POST */

           int userid = 0;
		try {
			userid = DataUtil.checkUser(email, pass);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
           System.out.println(userid);
            //... VALIDAZIONE IDENTITA'...
            //... IDENTITY CHECKS ...
            
          
                //se la validazione ha successo
                //if the identity validation succeeds
                //carichiamo lo userid dal database utenti
                //load userid from user database
                SecurityLayer.createSession(request, email , userid);
                data.put("session",email);
                try {
					data.put("test", DataUtil.getUsername(email));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                FreeMarker.process("list_title.html", data, response, getServletContext());
           
               
              } 
             
             if(!isNull(email_reg)){
	              
                 /*REGISTRAZIONE -- METHOD POST */
                 
                   try {
           Database.connect();
           Map<String,Object> map= new HashMap<String,Object>();
           map.put("email",email_reg);
           map.put("password", crypt(pass_reg));
           map.put("nome",nome_reg);
           map.put("cognome",cognome_reg);
           map.put("citta",citta_reg);
           map.put("annonascita",data_reg);
           map.put("gruppo",1);
           
           Database.insertRecord("users",map);
               
              
               
           FreeMarker.process("index.html", data, response, getServletContext());
                   
            try {
               Database.close();
           } catch (SQLException ex) {
               Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
           }
       } catch (NamingException ex) {
           Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
       } catch (SQLException ex) {
           Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
       } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
             
       
             }
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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.bibliotecadigitale.Servlet;

import it.bibliotecadigitale.Utility.DataUtil;
import static it.bibliotecadigitale.Utility.DataUtil.crypt;
import it.bibliotecadigitale.Utility.Database;
import it.bibliotecadigitale.Utility.FreeMarker;
import it.bibliotecadigitale.Utility.SecurityLayer;
import java.io.IOException;
import java.io.PrintWriter;
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
 *
 * @author Patrizio
 */
public class Index extends HttpServlet {

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
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
           String email = request.getParameter("email_login");
              String pass = request.getParameter("password_login");
              String email_reg = request.getParameter("email_reg");
              String pass_reg = request.getParameter("pass_reg");
              String nome_reg = request.getParameter("nome_reg");
              String cognome_reg = request.getParameter("cognome_reg");
              String citta_reg = request.getParameter("citta_reg");
              String data_reg = request.getParameter("data_reg");
              
            HttpSession s = SecurityLayer.checkSession(request);
            if (s != null) {
                 data.put("session",s.getAttribute("username"));
                FreeMarker.process("list_title.html", data, response, getServletContext());
                
            } else {
                
                /*NON SONO IN SESSIONE, DEVO CONTROLLARE SE RICEVO LOGIN O REGISTRAZIONE */
                
           if(!isNull(email)||!isNull(pass)){ 
                  
                 /*LOGIN -- METHOD POST */

           int userid=DataUtil.checkUser(email, pass);
           System.out.println(userid);
            //... VALIDAZIONE IDENTITA'...
            //... IDENTITY CHECKS ...
            
          
                //se la validazione ha successo
                //if the identity validation succeeds
                //carichiamo lo userid dal database utenti
                //load userid from user database
                SecurityLayer.createSession(request, email , userid);
                data.put("session",email);
                FreeMarker.process("list_title.html", data, response, getServletContext());
           
               
              } 
              
              
            if(!isNull(email_reg) || !isNull(pass_reg) ){
              
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
            if(Database.insertRecord("users",map))
                
                data.put("risultato","Registrato Correttamente");
                ResultSet rs= Database.selectRecord("users", "email='"+email+"'");
                int k = 0;
                while(rs.next()){ k= rs.getInt("id");}
                SecurityLayer.createSession(request, email, k);
            
           
                data.put("session",email);
           
                FreeMarker.process("list_title.html", data, response, getServletContext());
                    
             try {
                Database.close();
            } catch (SQLException ex) {
                Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NamingException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
              
        
              }
            
            }
            
          FreeMarker.process("index.html", data, response, getServletContext());

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
        
        processRequest(request, response);
    }

     @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        
        processRequest(request, response);
    }

    

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet per la gestione del login";
    }

}
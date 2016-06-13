/**
 * 
 */
package biblioteca.Util;


import biblioteca.Model.Utente;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.util.Objects.isNull;
import javax.naming.NamingException;
/**
 * @author Patrizio
 * @author Stefano
 * @author Luca
 *
 */
public class DataUtil {
	
	
	
	 
	public static String getUsername(String email) throws Exception {
		
		String nome = null;
		
		try{

			 Database.connect();
		        
		        
		           
		         String condition="email='"+email+"'";
		         
		         ResultSet rs =Database.selectRecord("users",condition);
		       
		         while(rs.next()){ 
		          nome= rs.getString("nome");
		        	 
		       }
		                   
		      }catch(NamingException e)
		      {     
		      } catch (SQLException e) {
		      }
		        return nome;  
	}

    public static int checkUser(String email,String pass) throws Exception 
     {
      int st = 0;
      try{

	 Database.connect();
        if(!isNull(pass)){
            pass=crypt(pass);
        }
        
         System.out.println(email);
         System.out.println(st);
         System.out.println(pass);
           
         String condition="email='"+email+"' AND password='"+pass+"'";
         
          System.out.println(condition);
         ResultSet rs =Database.selectRecord("users",condition);
       while(rs.next()){ 
           st=rs.getInt("id");
       }
        
       

                  
      }catch(NamingException e)
      {     
      } catch (SQLException e) {
      }
        return st;    
    }   
  
  public static Utente getUser(String email) throws Exception{
      Utente utente= new Utente();
      
      try{

            
         Database.connect();
         ResultSet rs= Database.selectRecord("utenti", "email="+email);
   
         
         while (rs.next()) {
              
                
                String usermail = rs.getString("email");
                String nome =rs.getString("nome");
                String cognome =rs.getString("cognome");
                String citta =rs.getString("citta");
                Date annonascita =rs.getDate("annonascita");
                int gruppi =rs.getInt("gruppo");
                
                utente= new Utente(usermail,nome,cognome,citta,annonascita,gruppi);
          }
         
            
          /*     Iterator<Pub> it = lista.iterator();
                while( it.hasNext()){
                    Pub temp= it.next();
                    pubblicazioni.put(temp.getNome(), temp.getDescrizione());
                } */
         
         try {
                Database.close();
            } catch (SQLException ex) {
            }
         
         
      } catch (NamingException ex) {
            System.out.println("ciao1");
        }catch (SQLException ex) {
            System.out.println("ciao1.2");
        }
  
      return utente;
}
  
  
  /**
     * Controllo su String. Contiene solo caratteri alfanumerici?
     * @param toCheck   stringa sul quale effettuare il controllo
     * @param space     se true accetta anche gli spazi.
     * @return          true se la stringa è alfanumerica, false altrimenti.
     */
    public static boolean isAlphanumeric(String toCheck, boolean space){
        if(toCheck.equals("")) return true;
        
        if(space){
            return toCheck.matches("[a-zA-Z' ]+");
        }else{
            return toCheck.matches("[a-zA-Z']+");
        }
        
    }
    
    /**
     * Eliminazione degli spazi esterni e dei doppi spazi interni
     * @param toTrim    stringa da elaborare
     * @return          stringa "pulita"
     */
    public static String spaceTrim(String toTrim){
        return toTrim.trim().replaceAll("\\s+", " ");
    }

    
    /**
     * Cripta una stringa
     * @param string    stringa da criptare
     * @return          stringa criptata
     */
    public static String crypt(String string){
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            byte[] passBytes = string.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<digested.length;i++){
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }

    }
    
    /**
     * Verifica se una stringa criptata è stata generata da un'altra stringa
     * @param string_crypted    stringa criptata
     * @param to_check          stringa da verificare
     * @return                  true se la password è stata verificata, false altrimenti
     */
    public static boolean decrypt(String string_crypted, String to_check){
        if(to_check == null || string_crypted == null) return false;
        return string_crypted.equals(crypt(to_check));
    }
    
}

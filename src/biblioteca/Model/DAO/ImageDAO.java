package biblioteca.Model.DAO;

import java.util.HashMap;
import java.util.Map;

import biblioteca.Util.Database;

public class ImageDAO implements ImageDAO_Interface {
	
	public static void delete_image(int indice, int idd) throws Exception{
		 Database.connect();
	     Database.deleteRecord("immagini", "indice='" + indice + "' AND id_pub = '" + idd + "'");
	     Database.close();
	}
	
	public static void revisiona(int indice, int idd) throws Exception{
		
		 Database.connect();
	     Map<String, Object> imm = new HashMap<String, Object>();
	     imm.put("revisionato", '1');
	     Database.updateRecord("immagini", imm, "indice='" + indice + "' AND id_pub = '" + idd + "'");
	     Database.close();

	}
	
}

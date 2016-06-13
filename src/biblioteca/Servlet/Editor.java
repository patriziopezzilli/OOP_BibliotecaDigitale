package biblioteca.Servlet;

import static biblioteca.Util.DataUtil.crypt;
import static java.util.Objects.isNull;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import biblioteca.Model.Opera;
import biblioteca.Model.DAO.TrascrizioneDAO;
import biblioteca.Util.DataUtil;
import biblioteca.Util.Database;
import biblioteca.Util.FreeMarker;
import biblioteca.Util.SecurityLayer;
/**
 * @author Patrizio
 *
 */
public class Editor extends HttpServlet {

	Map<String, Object> data = new HashMap<String, Object>();

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession s = SecurityLayer.checkSession(request);

		/* Mette la trascrizione tra i validati tramite un booleano */
		
		if (!isNull(request.getParameter("valida"))) {
			int idd = Integer.parseInt(request.getParameter("idd"));
			int indice = Integer.parseInt(request.getParameter("indice"));

			data.put("id", idd);
			data.put("index", indice);

			TrascrizioneDAO.valida(indice, idd);

			response.sendRedirect("detail?id=" + data.get("id") + "&index=" + data.get("index"));
		}

		/* Elimina trascrizione relativa all'indice corrente  */
		if (!isNull(request.getParameter("elimina"))) {
			int idd = Integer.parseInt(request.getParameter("idd"));
			int indice = Integer.parseInt(request.getParameter("indice"));

			data.put("id", idd);
			data.put("index", indice);

			TrascrizioneDAO.delete(indice, idd);

			response.sendRedirect("detail?id=" + data.get("id") + "&index=" + data.get("index"));
		}
		
		FreeMarker.process("editorTEI.html", data, response, getServletContext());
		
	}

	/**
	 * Caricamento pagina di Home
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession s = SecurityLayer.checkSession(request);
		int id = Integer.parseInt(request.getParameter("id"));
		String nomeopera = null;
		int index = Integer.parseInt(request.getParameter("index"));
		String contenuto = null;
		
		/* Tramite ID dell'opera reperisco dal DB il nome
		 *  di quest'ultima damostrare a video
		 */
		
		try {
			Database.connect();
			ResultSet cc= Database.selectRecord("pub", "id_op="+ id );
			while(cc.next()){ nomeopera= cc.getString("nome");}
			data.put("nomeopera", nomeopera);
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		if (s != null) {
			
			data.put("session", s.getAttribute("username"));
			String test = null;
			
			try {
				test = DataUtil.getUsername((String) s.getAttribute("username"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			System.out.println(id);
			System.out.println(index);
			
			try {
				
			     /* Recupero trascrizioni */
				ResultSet rs = Database.selectRecord("trascrizioni", "id_pub=" + id + "&& indice=" + index);
				while (rs.next()) {

					contenuto = rs.getString("contenuto");
					System.out.println(contenuto);
				}
				Database.close();

			} catch (Exception e) {
				
				e.printStackTrace();
			}

			/* Riempio la mappa da passare alla classe View */
			data.put("contenuto", contenuto);
			data.put("test", test);
			data.put("index", index);
			data.put("idopera", id);
			FreeMarker.process("editorTEI.html", data, response, getServletContext());

		} else
			FreeMarker.process("index.html", data, response, getServletContext());
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			processRequest(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
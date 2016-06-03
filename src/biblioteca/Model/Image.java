package biblioteca.Model;

import java.sql.Blob;

public class Image {
		
	int id;
	int indice;
	Blob file;
	int id_pub;
	
	
	
	public Image(int id, int indice, Blob file, int id_pub) {
		super();
		this.id = id;
		this.indice = indice;
		this.file = file;
		this.id_pub = id_pub;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIndice() {
		return indice;
	}
	public void setIndice(int indice) {
		this.indice = indice;
	}
	public Blob getFile() {
		return file;
	}
	public void setFile(Blob file) {
		this.file = file;
	}
	public int getId_pub() {
		return id_pub;
	}
	public void setId_pub(int id_pub) {
		this.id_pub = id_pub;
	}
	
	
	
}

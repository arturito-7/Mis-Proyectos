package negocio.modeloAerolinea;

public class TModeloAerolinea {

	private int idModelo;

	private int idAerolinea;

	public TModeloAerolinea() {

	}

	public TModeloAerolinea(int idModelo, int idAerolinea) {
		this.idModelo = idModelo;
		this.idAerolinea = idAerolinea;
	}

	public int getIdModelo() {
		return this.idModelo;
	}

	public int getIdAerolinea() {
		return this.idAerolinea;
	}

	public void setIdModelo(int idModelo) {
		this.idModelo = idModelo;
	}

	public void setIdAerolinea(int idAerolinea) {
		this.idAerolinea = idAerolinea;
	}

	public String toString() {
		return "idModelo: " + this.idModelo + "\nidAerolinea: " + this.idAerolinea;
	}
	
}
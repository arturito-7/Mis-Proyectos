
package negocio.contrato;

public class TContrato {

	private int id;

	private int id_aerolinea;

	private double precio;

	public TContrato() {

	}

	public TContrato(int id, int id_aerolinea, double precio) {
		this.id = id;
		this.id_aerolinea = id_aerolinea;
		this.precio = precio;
	}

	public TContrato(int id_aerolinea) {
		this.id_aerolinea = id_aerolinea;
	}

	public int getId() {
		return this.id;
	}

	public int getIdAerolinea() {
		return this.id_aerolinea;
	}

	public double getPrecio() {
		return this.precio;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setIdAerolinea(int idAerolinea) {
		this.id_aerolinea = idAerolinea;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String toString() {
		return "id: " + this.id + "\nid_aerolinea: " + this.id_aerolinea + "\nprecio: " + this.precio + "\n";
	}
}
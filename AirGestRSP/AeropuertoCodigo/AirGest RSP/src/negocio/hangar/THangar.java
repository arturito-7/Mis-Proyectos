package negocio.hangar;

public class THangar {

	private int id;
	private String direccion;
	private int stock;
	private double costeDia;
	private int espacioAlmacenaje;
	private boolean activo;

	public THangar() {
	}

	public THangar(int id, String direccion, int stock, double costeDia, int espacioAlmacenaje, boolean activo) {
		this.id = id;
		this.direccion = direccion;
		this.stock = stock;
		this.costeDia = costeDia;
		this.espacioAlmacenaje = espacioAlmacenaje;
		this.activo = activo;
	}

	public int getId() {
		return this.id;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public int getStock() {
		return this.stock;
	}

	public double getCosteDia() {
		return this.costeDia;
	}

	public int getEspacioAlmacenaje() {
		return this.espacioAlmacenaje;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public void setCosteDia(double costeDia) {
		this.costeDia = costeDia;
	}

	public void setEspacioAlmacenaje(int espacioAlmacenaje) {
		this.espacioAlmacenaje = espacioAlmacenaje;
	}

	public boolean getActivo() {
		return this.activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	@Override
	public String toString() {
		return "id: " + this.id + "\n" + "direccion: " + direccion + "\nstock: " + stock + "\ncosteDia: " + costeDia
				+ "\nespacioAlmacenaje:" + espacioAlmacenaje + "\nactivo: " + activo + "\n";
	}
	
}
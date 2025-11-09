
package negocio.aerolinea;


public class TAerolinea {

	private int id;

	private String nombre;

	private boolean activo;

	public TAerolinea() {

	}

	public TAerolinea(int id, String nombre, boolean activo) {
		this.id = id;
		this.nombre = nombre;
		this.activo = activo;
	}

	public int getId() {
		return this.id;
	}

	public String getNombre() {
		return this.nombre;
	}

	public boolean getActivo() {
		return this.activo;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String toString() {
		return "id: " + this.id + "\nnombre: " + this.nombre + "\nactivo: " + this.activo + "\n";
	}
}
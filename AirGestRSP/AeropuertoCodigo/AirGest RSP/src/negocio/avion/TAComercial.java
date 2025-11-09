
package negocio.avion;

import java.time.LocalDate;

public class TAComercial extends TAvion {

	private int trabajadores;

	public TAComercial(int id, int numAsientos, LocalDate fechaFabricacion, String nombre, String matricula,
			boolean activo, int idAerolinea, int idModelo, int idHangar, int trabajadores) {
		super(id, numAsientos, fechaFabricacion, nombre, matricula, activo, idAerolinea, idModelo, idHangar);
		this.trabajadores = trabajadores;
	}

	public int getTrabajadores() {
		return this.trabajadores;
	}

	public void setTrabajadores(int trabajadores) {
		this.trabajadores = trabajadores;
	}

	@Override
	public String toString() {
		return super.toString() + "\nNúmero de trabajadores: " + this.getTrabajadores();
	}
}
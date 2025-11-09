
package negocio.avion;

import java.time.LocalDate;

public class TAPrivado extends TAvion {

	private String nombreDuenyo;
	private int idCarnet;

	public TAPrivado(int id, int numAsientos, LocalDate fechaFabricacion, String nombre, String matricula,
			boolean activo, int idAerolinea, int idModelo, int idHangar, String nombre_duenyo, int idCarnet) {
		super(id, numAsientos, fechaFabricacion, nombre, matricula, activo, idAerolinea, idModelo, idHangar);
		this.nombreDuenyo = nombre_duenyo;
		this.idCarnet = idCarnet;
	}

	public int getIdCarnet() {
		return this.idCarnet;
	}

	public String getNombreDuenyo() {
		return this.nombreDuenyo;
	}

	public void setIdCarnet(int idCarnet) {
		this.idCarnet = idCarnet;
	}

	public void setNombreDuenyo(String nombre_duenyo) {
		this.nombreDuenyo = nombre_duenyo;
	}

	@Override
	public String toString() {
		return super.toString() + "\nNombre dueño: " + this.getNombreDuenyo() + "\nId carnet: " + this.getIdCarnet();
	}
}

package negocio.lineaContrato;

import java.time.LocalDate;

public class TLineaContrato {

	private int id_contrato;

	private int id_hangar;

	private LocalDate fecha_ini;

	private LocalDate fecha_fin;

	private double precio;

	public TLineaContrato() {

	}

	public TLineaContrato(int id_contrato, int id_hangar, LocalDate fecha_ini, LocalDate fecha_fin, double precio) {
		this.id_contrato = id_contrato;
		this.id_hangar = id_hangar;
		this.fecha_ini = fecha_ini;
		this.fecha_fin = fecha_fin;
		this.precio = precio;
	}

	public int getIdContrato() {
		return this.id_contrato;
	}

	public int getIdHangar() {
		return this.id_hangar;
	}

	public LocalDate getFechaIni() {
		return this.fecha_ini;
	}

	public LocalDate getFechaFin() {
		return this.fecha_fin;
	}

	public double getPrecio() {
		return this.precio;
	}

	public void setIdContrato(int id_contrato) {
		this.id_contrato = id_contrato;
	}

	public void setIdHangar(int id_hangar) {
		this.id_hangar = id_hangar;
	}

	public void setFechaIni(LocalDate fecha_ini) {
		this.fecha_ini = fecha_ini;
	}

	public void setFechaFin(LocalDate fecha_fin) {
		this.fecha_fin = fecha_fin;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String toString() {
		return "id_contrato: " + this.id_contrato + "\nid_hangar: " + this.id_hangar + "\nfecha_ini: " + this.fecha_ini
				+ "\nfecha_fin: " + this.fecha_fin + "\nprecio: " + this.precio;
	}
}
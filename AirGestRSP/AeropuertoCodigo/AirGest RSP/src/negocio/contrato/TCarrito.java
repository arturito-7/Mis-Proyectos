
package negocio.contrato;

import java.util.HashSet;
import java.util.Set;
import negocio.lineaContrato.TLineaContrato;

public class TCarrito {

	private int id_aerolinea;

	private TContrato tContrato;

	private Set<TLineaContrato> tLineaContrato;

	public TCarrito() {

	}

	public TCarrito(int id_aerolinea) {
		this.id_aerolinea = id_aerolinea;
		this.tContrato = new TContrato(id_aerolinea);
		this.tLineaContrato = new HashSet<>();
	}

	public int getIdAerolinea() {
		return this.id_aerolinea;
	}

	public Set<TLineaContrato> getLineasContrato() {
		return this.tLineaContrato;
	}

	public void setIdAerolinea(int idAerolinea) {
		this.id_aerolinea = idAerolinea;
	}

	public TContrato getContrato() {
		return this.tContrato;
	}

	public void setContrato(TContrato tContrato) {
		this.tContrato = tContrato;
	}

	public void anyadirLinea(TLineaContrato linea) {
		this.tLineaContrato.add(linea);
	}

	public void eliminarLinea(int id_hangar) {
		for (TLineaContrato linea : this.tLineaContrato) {
			if (linea.getIdHangar() == id_hangar) {
				this.tLineaContrato.remove(linea);
				return;
			}
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("id_aerolinea: " + this.id_aerolinea + "\n");
		sb.append("contrato: " + this.tContrato + "\n");
		sb.append("lineas de contrato: " + this.tLineaContrato + "\n");

		return sb.toString();
	}
}
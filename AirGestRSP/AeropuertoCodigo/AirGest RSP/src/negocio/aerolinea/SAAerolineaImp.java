package negocio.aerolinea;

import java.util.List;

import integracion.aerolinea.DAOAerolinea;
import integracion.avion.DAOAvion;
import integracion.contrato.DAOContrato;
import integracion.factoria.FactoriaIntegracion;
import negocio.UtilidadesN;

public class SAAerolineaImp implements SAAerolinea {

	public int altaAerolinea(TAerolinea tAerolinea) {
		if (ValidadorAerolinea.comprobarAerolinea(tAerolinea)) {
			DAOAerolinea da = FactoriaIntegracion.getInstance().crearDAOAerolinea();
			TAerolinea leido = da.leerAerolineaPorNombre(tAerolinea.getNombre());

			if (leido == null)
				return da.altaAerolinea(tAerolinea);
			else if (!leido.getActivo()) {
				tAerolinea.setId(leido.getId());
				da.modificarAerolinea(tAerolinea);
				return tAerolinea.getId();
			}
		}

		return -1;
	}

	public boolean bajaAerolinea(int id) {
		if (UtilidadesN.comprobarId(id)) {
			DAOAerolinea da = FactoriaIntegracion.getInstance().crearDAOAerolinea();

			TAerolinea leido = da.leerAerolineaPorId(id);

			if (leido != null && leido.getActivo()) {
				DAOAvion dav = FactoriaIntegracion.getInstance().crearDAOAvion();
				DAOContrato dc = FactoriaIntegracion.getInstance().crearDAOContrato();

				if (dav.consultarAvionesActivosPorAerolinea(id).isEmpty()
						&& dc.leerContratosPorAerolinea(id).isEmpty()) {
					return da.bajaAerolinea(id);
				}
			}
		}
		return false;
	}

	public TAerolinea consultarAerolineaPorId(int id) {
		if (UtilidadesN.comprobarId(id)) {
			DAOAerolinea da = FactoriaIntegracion.getInstance().crearDAOAerolinea();

			return da.leerAerolineaPorId(id);
		}

		return null;
	}

	public List<TAerolinea> consultarTodasAerolineas() {
		DAOAerolinea da = FactoriaIntegracion.getInstance().crearDAOAerolinea();
		return da.consultarTodasAerolineas();
	}

	public boolean modificarAerolinea(TAerolinea tAerolinea) {
		if (ValidadorAerolinea.comprobarAerolinea(tAerolinea)) {
			DAOAerolinea da = FactoriaIntegracion.getInstance().crearDAOAerolinea();

			int id = tAerolinea.getId();
			String nombre = tAerolinea.getNombre();

			TAerolinea leido = da.leerAerolineaPorId(id);

			if (leido != null) {
				if (leido.getActivo()
						&& (leido.getNombre().equals(nombre) || da.leerAerolineaPorNombre(nombre) == null)) {
					return da.modificarAerolinea(tAerolinea);
				}
			}
		}
		return false;
	}

	public TAerolinea consultarAerolineaPorNombre(String nombre) {
		if (ValidadorAerolinea.comprobarNombre(nombre)) {
			DAOAerolinea da = FactoriaIntegracion.getInstance().crearDAOAerolinea();

			return da.leerAerolineaPorNombre(nombre);
		}

		return null;
	}

}
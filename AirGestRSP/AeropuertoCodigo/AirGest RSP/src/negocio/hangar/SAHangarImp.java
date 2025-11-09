package negocio.hangar;

import java.util.List;

import integracion.avion.DAOAvion;
import integracion.factoria.FactoriaIntegracion;
import integracion.hangar.DAOHangar;
import negocio.UtilidadesN;

public class SAHangarImp implements SAHangar {

	public int altaHangar(THangar tHangar) {
		if (ValidadorHangar.comprobarDatos(tHangar)) {
			DAOHangar dh = FactoriaIntegracion.getInstance().crearDAOHangar();
			THangar leido = dh.leerHangarPorDireccion(tHangar.getDireccion());

			if (leido == null)
				return dh.altaHangar(tHangar);
			else if (!leido.getActivo()) {
				tHangar.setId(leido.getId());
				dh.modificarHangar(tHangar);
				return tHangar.getId();
			}
		}

		return -1;
	}

	public boolean bajaHangar(int id) {
		if (UtilidadesN.comprobarId(id)) {
			DAOHangar dh = FactoriaIntegracion.getInstance().crearDAOHangar();

			THangar leido = dh.leerHangarPorId(id);

			if (leido != null && leido.getActivo()) {
				DAOAvion da = FactoriaIntegracion.getInstance().crearDAOAvion();

				if (da.consultarAvionesActivosPorHangar(id).isEmpty()) {
					return dh.bajaHangar(id);
				}
			}
		}

		return false;
	}

	public THangar consultarHangarPorId(int id) {
		if (UtilidadesN.comprobarId(id)) {
			DAOHangar dh = FactoriaIntegracion.getInstance().crearDAOHangar();

			return dh.leerHangarPorId(id);
		}

		return null;
	}

	public List<THangar> consultarTodosHangares() {
		DAOHangar dh = FactoriaIntegracion.getInstance().crearDAOHangar();
		return dh.consultarTodosHangares();
	}

	public boolean modificarHangar(THangar tHangar) {
		if (UtilidadesN.comprobarId(tHangar.getId()) && ValidadorHangar.comprobarDatos(tHangar)) {
			DAOHangar dh = FactoriaIntegracion.getInstance().crearDAOHangar();

			int id = tHangar.getId();
			String direccion = tHangar.getDireccion();

			THangar leido = dh.leerHangarPorId(id);

			if (leido != null) {
				if (leido.getActivo()
						&& (leido.getDireccion().equals(direccion) || dh.leerHangarPorDireccion(direccion) == null)) {
					return dh.modificarHangar(tHangar);
				}
			}
		}
		return false;
	}

}
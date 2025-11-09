package negocio.modelo;

import java.util.List;

import integracion.aerolinea.DAOAerolinea;
import integracion.avion.DAOAvion;
import integracion.factoria.FactoriaIntegracion;
import integracion.modelo.DAOModelo;
import integracion.modeloAerolinea.DAOModeloAerolinea;
import negocio.UtilidadesN;
import negocio.aerolinea.TAerolinea;
import negocio.modeloAerolinea.TModeloAerolinea;
import negocio.modeloAerolinea.ValidadorModeloAerolinea;

public class SAModeloImp implements SAModelo {

	public int altaModelo(TModelo tModelo) {
		if (ValidadorModelo.comprobarDatos(tModelo)) {
			DAOModelo dm = FactoriaIntegracion.getInstance().crearDAOModelo();
			TModelo leido = dm.leerModeloPorNombre(tModelo.getNombre());

			if (leido == null)
				return dm.altaModelo(tModelo);
			else if (!leido.getActivo()) {
				tModelo.setId(leido.getId());
				dm.modificarModelo(tModelo);
				return tModelo.getId();
			}
		}

		return -1;
	}

	public boolean bajaModelo(int id) {
		if (UtilidadesN.comprobarId(id)) {
			DAOModelo dm = FactoriaIntegracion.getInstance().crearDAOModelo();

			TModelo leido = dm.leerModeloPorId(id);

			if (leido != null && leido.getActivo()) {
				DAOAvion da = FactoriaIntegracion.getInstance().crearDAOAvion();

				if (da.consultarAvionesActivosPorModelo(id).isEmpty()) {
					return dm.bajaModelo(id);
				}
			}
		}

		return false;
	}

	public TModelo consultarModelo(int id) {
		if (UtilidadesN.comprobarId(id)) {
			DAOModelo dm = FactoriaIntegracion.getInstance().crearDAOModelo();

			return dm.leerModeloPorId(id);
		}

		return null;
	}

	public List<TModelo> consultarTodosModelos() {
		DAOModelo dm = FactoriaIntegracion.getInstance().crearDAOModelo();
		return dm.consultarTodosModelos();
	}

	public boolean modificarModelo(TModelo tModelo) {
		if (UtilidadesN.comprobarId(tModelo.getId()) && ValidadorModelo.comprobarDatos(tModelo)) {
			DAOModelo dm = FactoriaIntegracion.getInstance().crearDAOModelo();

			int id = tModelo.getId();
			String nombre = tModelo.getNombre();

			TModelo leido = dm.leerModeloPorId(id);

			if (leido != null) {
				if (leido.getActivo() && (leido.getNombre().equals(nombre) || dm.leerModeloPorNombre(nombre) == null)) {
					return dm.modificarModelo(tModelo);
				}
			}
		}
		return false;
	}

	public boolean vincularModelo(TModeloAerolinea tModeloAerolinea) {
		int idModelo = tModeloAerolinea.getIdModelo();
		int idAerolinea = tModeloAerolinea.getIdAerolinea();

		if (ValidadorModeloAerolinea.comprobarDatos(idModelo, idAerolinea)) {
			DAOModelo dm = FactoriaIntegracion.getInstance().crearDAOModelo();
			DAOAerolinea da = FactoriaIntegracion.getInstance().crearDAOAerolinea();

			TModelo mLeido = dm.leerModeloPorId(idModelo);
			TAerolinea aLeida = da.leerAerolineaPorId(idAerolinea);

			if (mLeido != null && mLeido.getActivo() && aLeida != null && aLeida.getActivo()) {
				DAOModeloAerolinea dma = FactoriaIntegracion.getInstance().crearDAOModeloAerolinea();

				if (!dma.comprobarVinculacion(idModelo, idAerolinea)) {
					return dma.vincular(idModelo, idAerolinea);
				}
			}
		}

		return false;
	}

	public boolean desvincularModelo(TModeloAerolinea tModeloAerolinea) {
		int idModelo = tModeloAerolinea.getIdModelo();
		int idAerolinea = tModeloAerolinea.getIdAerolinea();

		if (ValidadorModeloAerolinea.comprobarDatos(idModelo, idAerolinea)) {
			DAOModelo dm = FactoriaIntegracion.getInstance().crearDAOModelo();
			DAOAerolinea da = FactoriaIntegracion.getInstance().crearDAOAerolinea();

			TModelo mLeido = dm.leerModeloPorId(idModelo);
			TAerolinea aLeida = da.leerAerolineaPorId(idAerolinea);

			if (mLeido != null && mLeido.getActivo() && aLeida != null && aLeida.getActivo()) {
				DAOModeloAerolinea dma = FactoriaIntegracion.getInstance().crearDAOModeloAerolinea();

				if (dma.comprobarVinculacion(idModelo, idAerolinea)) {
					return dma.desvincular(idModelo, idAerolinea);
				}
			}
		}

		return false;
	}

}
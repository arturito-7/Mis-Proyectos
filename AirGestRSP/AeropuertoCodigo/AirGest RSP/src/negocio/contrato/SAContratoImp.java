package negocio.contrato;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import integracion.aerolinea.DAOAerolinea;
import integracion.contrato.DAOContrato;
import integracion.factoria.FactoriaIntegracion;
import integracion.hangar.DAOHangar;
import integracion.lineaContrato.DAOLineaContrato;
import negocio.UtilidadesN;
import negocio.aerolinea.TAerolinea;
import negocio.hangar.THangar;
import negocio.lineaContrato.TLineaContrato;

public class SAContratoImp implements SAContrato {

	public TCarrito abrirContrato(int id_aerolinea) {
		return new TCarrito(id_aerolinea);
	}

	public int cerrarContrato(TCarrito tCarrito) {
		DAOAerolinea da = FactoriaIntegracion.getInstance().crearDAOAerolinea();
		TAerolinea aerolinea = da.leerAerolineaPorId(tCarrito.getIdAerolinea());

		if (aerolinea != null && aerolinea.getActivo()) {
			if (tCarrito.getLineasContrato().isEmpty()){
				return -1;
			}
			
			for (TLineaContrato linea : tCarrito.getLineasContrato()) {
				for (TLineaContrato l : tCarrito.getLineasContrato()) {
					if (l != linea && l.getIdHangar() == linea.getIdHangar()) {
						return -1;
					}
				}
			}

			DAOHangar dh = FactoriaIntegracion.getInstance().crearDAOHangar();
			// Bucle para comprobar que los hangares existan y que la fecha de
			// inicio sea anterior a la de fin
			for (TLineaContrato linea : tCarrito.getLineasContrato()) {
				THangar hangar = dh.leerHangarPorId(linea.getIdHangar());
				if (hangar == null || !hangar.getActivo() || linea.getFechaIni().isAfter(linea.getFechaFin())) {
					return -1;
				}
			}

			DAOLineaContrato dl = FactoriaIntegracion.getInstance().crearDAOLineaContrato();
			// Bucle para comprobar disponibilidad
			for (TLineaContrato linea : tCarrito.getLineasContrato()) {
				List<TLineaContrato> lineasPorHangar = dl.leerLineasPorHangar(linea.getIdHangar());

				for (TLineaContrato l : lineasPorHangar) {
					if (isBetween(linea.getFechaIni(), l.getFechaIni(), l.getFechaFin())
							|| isBetween(linea.getFechaFin(), l.getFechaIni(), l.getFechaFin())
							|| isBetween(l.getFechaIni(), linea.getFechaIni(), linea.getFechaFin())
							|| isBetween(l.getFechaFin(), linea.getFechaIni(), linea.getFechaFin())) {
						return -1;
					}
				}
			}

			double precioContrato = 0;

			for (TLineaContrato linea : tCarrito.getLineasContrato()) {
				long diasDiferencia = ChronoUnit.DAYS.between(linea.getFechaIni(), linea.getFechaFin()) + 1;
				THangar hangar = dh.leerHangarPorId(linea.getIdHangar());
				precioContrato += diasDiferencia * hangar.getCosteDia();
			}

			DAOContrato dc = FactoriaIntegracion.getInstance().crearDAOContrato();
			TContrato contrato = tCarrito.getContrato();
			contrato.setPrecio(precioContrato);
			int id = dc.altaContrato(contrato);

			for (TLineaContrato linea : tCarrito.getLineasContrato()) {
				long diasDiferencia = ChronoUnit.DAYS.between(linea.getFechaIni(), linea.getFechaFin()) + 1;
				THangar hangar = dh.leerHangarPorId(linea.getIdHangar());
				linea.setPrecio(diasDiferencia * hangar.getCosteDia());
				linea.setIdContrato(id);
				dl.altaLineaContrato(linea);
			}

			return id;
		}

		return -1;
	}

	private boolean isBetween(LocalDate fecha, LocalDate inicio, LocalDate fin) {
		return (fecha.isAfter(inicio) && fecha.isBefore(fin)) || (fecha.isEqual(inicio) || fecha.isEqual(fin));
	}

	public TInfoContrato consultarContratoPorId(int id) {
		if (UtilidadesN.comprobarId(id)) {
			DAOContrato dc = FactoriaIntegracion.getInstance().crearDAOContrato();

			TContrato contrato = dc.leerContratoPorId(id);

			if (contrato != null) {
				DAOAerolinea da = FactoriaIntegracion.getInstance().crearDAOAerolinea();
				TAerolinea aerolinea = da.leerAerolineaPorId(contrato.getIdAerolinea());

				DAOLineaContrato dl = FactoriaIntegracion.getInstance().crearDAOLineaContrato();
				List<TLineaContrato> lineas = dl.leerLineasPorContrato(id);

				DAOHangar dh = FactoriaIntegracion.getInstance().crearDAOHangar();
				HashMap<Integer, THangar> hangares = new HashMap<>();

				for (TLineaContrato linea : lineas) {
					THangar hangar = dh.leerHangarPorId(linea.getIdHangar());
					hangares.put(hangar.getId(), hangar);
				}

				TInfoContrato info = new TInfoContrato(contrato, aerolinea, lineas, hangares);
				return info;
			}
		}

		return null;
	}

	public List<TContrato> consultarTodosContratos() {
		DAOContrato dc = FactoriaIntegracion.getInstance().crearDAOContrato();
		return dc.leerTodosContratos();
	}

	public boolean modificarContrato(TContrato tContrato) {
		if (ValidadorContrato.comprobarDatos(tContrato)) {
			DAOContrato dc = FactoriaIntegracion.getInstance().crearDAOContrato();

			int id = tContrato.getId();

			TContrato leido = dc.leerContratoPorId(id);

			if (leido != null) {
				DAOAerolinea da = FactoriaIntegracion.getInstance().crearDAOAerolinea();
				TAerolinea aerolinea = da.leerAerolineaPorId(tContrato.getIdAerolinea());
				if (aerolinea != null && aerolinea.getActivo()) {
					return dc.modificarContrato(tContrato);
				}
			}
		}

		return false;
	}

	public boolean modificarLineaContrato(TLineaContrato linea) {
		if (ValidadorLineaContrato.comprobarDatos(linea)) {
			DAOLineaContrato dl = FactoriaIntegracion.getInstance().crearDAOLineaContrato();

			if (dl.leerLineaContrato(linea.getIdContrato(), linea.getIdHangar())) {

				if (!linea.getFechaIni().isAfter(linea.getFechaFin())) {
					List<TLineaContrato> lineasPorHangar = dl.leerLineasPorHangar(linea.getIdHangar());

					for (TLineaContrato l : lineasPorHangar) {
						if (l.getIdHangar() != linea.getIdHangar() || l.getIdContrato() != linea.getIdContrato()) {
							if (isBetween(linea.getFechaIni(), l.getFechaIni(), l.getFechaFin())
									|| isBetween(linea.getFechaFin(), l.getFechaIni(), l.getFechaFin())
									|| isBetween(l.getFechaIni(), linea.getFechaIni(), linea.getFechaFin())
									|| isBetween(l.getFechaFin(), linea.getFechaIni(), linea.getFechaFin())) {
								return false;
							}
						}
					}

					DAOHangar dh = FactoriaIntegracion.getInstance().crearDAOHangar();
					THangar hangar = dh.leerHangarPorId(linea.getIdHangar());

					if (hangar != null && hangar.getActivo()) {
						long diasDiferencia = ChronoUnit.DAYS.between(linea.getFechaIni(), linea.getFechaFin()) + 1;
						linea.setPrecio(diasDiferencia * hangar.getCosteDia());
						dl.modificarLineaContrato(linea);

						List<TLineaContrato> lineas = dl.leerLineasPorContrato(linea.getIdContrato());

						double precioContrato = 0;

						for (TLineaContrato l : lineas) {
							precioContrato += l.getPrecio();
						}

						DAOContrato dc = FactoriaIntegracion.getInstance().crearDAOContrato();
						TContrato contrato = dc.leerContratoPorId(linea.getIdContrato());
						contrato.setPrecio(precioContrato);
						return dc.modificarContrato(contrato);
					}
				}
			}
		}
		return false;
	}

	public List<TContrato> consultarContratosPorAerolinea(int id_aerolinea) {
		List<TContrato> contratos = new ArrayList<>();
		if (UtilidadesN.comprobarId(id_aerolinea)) {
			DAOAerolinea da = FactoriaIntegracion.getInstance().crearDAOAerolinea();

			TAerolinea aerolinea = da.leerAerolineaPorId(id_aerolinea);

			if (aerolinea != null) {
				DAOContrato dc = FactoriaIntegracion.getInstance().crearDAOContrato();
				contratos = dc.leerContratosPorAerolinea(id_aerolinea);
			}
		}

		return contratos;
	}

}
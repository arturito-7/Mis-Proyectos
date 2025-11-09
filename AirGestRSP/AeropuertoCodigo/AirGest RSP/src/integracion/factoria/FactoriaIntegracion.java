package integracion.factoria;

import integracion.modelo.DAOModelo;
import integracion.modeloAerolinea.DAOModeloAerolinea;
import integracion.personal.DAOPersonal;
import integracion.personalHangar.DAOPersonalHangar;
import integracion.avion.DAOAvion;
import integracion.contrato.DAOContrato;
import integracion.hangar.DAOHangar;
import integracion.lineaContrato.DAOLineaContrato;
import integracion.aerolinea.DAOAerolinea;

public abstract class FactoriaIntegracion {

	private static FactoriaIntegracion instancia;

	public static FactoriaIntegracion getInstance() {
		if (instancia == null)
			instancia = new FactoriaIntegracionImp();
		return instancia;
	}

	public abstract DAOModelo crearDAOModelo();

	public abstract DAOAvion crearDAOAvion();

	public abstract DAOAerolinea crearDAOAerolinea();

	public abstract DAOModeloAerolinea crearDAOModeloAerolinea();

	public abstract DAOPersonalHangar crearDAOPersonalHangar();

	public abstract DAOHangar crearDAOHangar();

	public abstract DAOContrato crearDAOContrato();

	public abstract DAOLineaContrato crearDAOLineaContrato();

	public abstract DAOPersonal crearDAOPersonal();

}
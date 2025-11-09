
package negocio.factoria;

import negocio.hangar.SAHangar;
import negocio.aerolinea.SAAerolinea;
import negocio.avion.SAAvion;
import negocio.contrato.SAContrato;
import negocio.modelo.SAModelo;
import negocio.personal.SAPersonal;

public abstract class FactoriaNegocio {

	private static FactoriaNegocio instancia;

	public static FactoriaNegocio getInstance() {
		if (instancia == null)
			instancia = new FactoriaNegocioImp();
		return instancia;
	}

	public abstract SAModelo crearSAModelo();

	public abstract SAHangar crearSAHangar();

	public abstract SAAvion crearSAAvion();

	public abstract SAAerolinea crearSAAerolinea();

	public abstract SAPersonal crearSAPersonal();

	public abstract SAContrato crearSAContrato();
}
package negocio.contrato;

import negocio.UtilidadesN;

public class ValidadorContrato {

	public static boolean comprobarDatos(TContrato tContrato) {
		return UtilidadesN.comprobarId(tContrato.getIdAerolinea()) && tContrato.getPrecio() > 0;
	}
}
package negocio.personalHangar;

import negocio.UtilidadesN;

public class ValidadorPersonalHangar {

	public static boolean comprobarDatos(TPersonalHangar tPersonalHangar) {
		return UtilidadesN.comprobarId(tPersonalHangar.getIdHangar())
				&& UtilidadesN.comprobarId(tPersonalHangar.getIdPersonal());
	}
}
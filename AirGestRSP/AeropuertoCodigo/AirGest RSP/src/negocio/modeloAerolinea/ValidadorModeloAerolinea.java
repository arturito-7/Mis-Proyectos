package negocio.modeloAerolinea;

import negocio.UtilidadesN;

public class ValidadorModeloAerolinea {
	
	public static boolean comprobarDatos(int idModelo, int idAerolinea) {
		return UtilidadesN.comprobarId(idModelo) && UtilidadesN.comprobarId(idAerolinea);
	}

}
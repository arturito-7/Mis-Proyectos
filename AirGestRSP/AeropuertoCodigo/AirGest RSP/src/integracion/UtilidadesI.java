package integracion;

public class UtilidadesI {

	public static boolean test = false;

	public static void esTest() {
		test = true;
	}

	public static final String RUTA_BASE_DE_DATOS = "recursos/base de datos/";
	public static final String RUTA_TESTS = "recursos/tests/";

	public static final String ruta(String modulo) {
		if (test)
			return RUTA_TESTS + modulo + "/";
		return RUTA_BASE_DE_DATOS + modulo + "/";
	}

}

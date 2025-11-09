package negocio.aerolinea;

public class ValidadorAerolinea {

	public static boolean comprobarAerolinea(TAerolinea tAerolinea) {
		return comprobarNombre(tAerolinea.getNombre());
	}

	public static boolean comprobarNombre(String nombre) {
		return nombre.matches("[a-zA-Z]+");
	}

}
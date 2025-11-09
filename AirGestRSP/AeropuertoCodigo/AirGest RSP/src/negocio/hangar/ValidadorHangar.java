
package negocio.hangar;

public class ValidadorHangar {

	public static boolean comprobarDatos(THangar tHangar) {
		return comprobarDireccion(tHangar.getDireccion()) && comprobarStock(tHangar.getStock())
				&& comprobarCosteDia(tHangar.getCosteDia())
				&& comprobarEspacioAlmacenaje(tHangar.getEspacioAlmacenaje());
	}

	public static boolean comprobarDireccion(String direccion) {
		return direccion.matches("[a-zA-Z0-9 ]+");
	}

	public static boolean comprobarStock(int stock) {
		return stock >= 0;
	}

	public static boolean comprobarCosteDia(double costeDia) {
		return costeDia > 0;
	}

	public static boolean comprobarEspacioAlmacenaje(int espacioAlmacenaje) {
		return espacioAlmacenaje > 0;
	}
}
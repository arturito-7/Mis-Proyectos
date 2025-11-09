package presentacion.controlador;

public abstract class Controlador {

	private static Controlador instancia;

	public static Controlador getInstance() {
		if (instancia == null)
			instancia = new ControladorImp();
		return instancia;
	}

	public abstract void accion(EventosControlador evento, Object datos);
}
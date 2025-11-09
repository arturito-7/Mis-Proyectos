package main;

import presentacion.controlador.Controlador;
import presentacion.controlador.EventosControlador;

public class Main {

	public static void main(String[] args) {

		Controlador ctrl = Controlador.getInstance();
		ctrl.accion(EventosControlador.VISTA_PRINCIPAL, null);
	}
}

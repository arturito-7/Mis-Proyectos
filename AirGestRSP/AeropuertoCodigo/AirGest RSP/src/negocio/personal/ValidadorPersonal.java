package negocio.personal;

public class ValidadorPersonal {

	public static boolean comprobarDatos(TPersonal tPersonal) {
		boolean valido = tPersonal.getIdEmpleado() > 0 && tPersonal.getAreaAsignada() != null
				&& !tPersonal.getAreaAsignada().isEmpty();

		if (tPersonal instanceof TPLimpieza)
			valido = valido && ((TPLimpieza) tPersonal).getRol() != null
					&& !((TPLimpieza) tPersonal).getRol().isEmpty();
		else
			valido = valido && ((TPSeguridad) tPersonal).getNumPlaca() > 0;

		return valido;
	}

}
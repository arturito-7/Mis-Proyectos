package negocio.personal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import integracion.UtilidadesI;

public class ValidadorPersonalTest {

	@Test
	public void comprobarDatosTest() {
		UtilidadesI.esTest();

		TPersonal personal;

		personal = new TPSeguridad(0, 5423, "cámaras", true, 56);
		assertTrue("debería ser valido", ValidadorPersonal.comprobarDatos(personal));

		personal = new TPLimpieza(0, 123, "baños", true, "desatascador");
		assertTrue("debería ser valido", ValidadorPersonal.comprobarDatos(personal));

		// Prueba con idEmpleado no válido
		personal = new TPSeguridad(0, 0, "cámaras", true, 56);
		assertFalse("debería ser inválido", ValidadorPersonal.comprobarDatos(personal));

		// Prueba con área asignada vacía
		personal = new TPLimpieza(0, 123, "", true, "desatascador");
		assertFalse("debería ser inválido", ValidadorPersonal.comprobarDatos(personal));

		// Prueba con numPlaca no válido
		personal = new TPSeguridad(0, 5423, "cámaras", true, 0);
		assertFalse("debería ser inválido", ValidadorPersonal.comprobarDatos(personal));

		// Prueba con rol vacío
		personal = new TPLimpieza(0, 123, "baños", true, "");
		assertFalse("debería ser inválido", ValidadorPersonal.comprobarDatos(personal));
	}

}

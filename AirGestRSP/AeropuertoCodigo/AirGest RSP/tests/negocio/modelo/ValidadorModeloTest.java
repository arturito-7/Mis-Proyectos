package negocio.modelo;

import static org.junit.Assert.*;

import org.junit.Test;

import integracion.UtilidadesI;

public class ValidadorModeloTest {
	
	private boolean noImporta = true;
	
	@Test
	public void comprobar_datos_test() {
		UtilidadesI.esTest();

		TModelo modelo = new TModelo(1, "boeing-888", "PPM-98", noImporta);

		assertTrue("fasfa", ValidadorModelo.comprobarDatos(modelo));
	}

	@Test
	public void comprobar_nombre_test() {
		UtilidadesI.esTest();

		assertTrue("boeing-747 deberia ser un nombre valido", ValidadorModelo.comprobarNombre("boeing-747"));
		assertFalse("mal1", ValidadorModelo.comprobarNombre("boeing7-747"));
		assertFalse("mal2", ValidadorModelo.comprobarNombre("boeing7-747"));
		assertFalse("mal3", ValidadorModelo.comprobarNombre("boeing7-747"));
		assertFalse("mal4", ValidadorModelo.comprobarNombre("boeing-747h"));
		assertFalse("mal5", ValidadorModelo.comprobarNombre("boeing747"));
		assertFalse("mal6", ValidadorModelo.comprobarNombre("boeing"));
		assertFalse("mal7", ValidadorModelo.comprobarNombre("747"));
		assertFalse("mal8", ValidadorModelo.comprobarNombre("boeing_747"));
		assertFalse("mal9", ValidadorModelo.comprobarNombre("&boeing-747"));
		assertFalse("mal0", ValidadorModelo.comprobarNombre("boeing-"));
		assertFalse("mal11", ValidadorModelo.comprobarNombre("-747"));
		
	}
	
	@Test
	public void comprobar_motor_test() {
		UtilidadesI.esTest();

		assertTrue("1", ValidadorModelo.comprobarMotor("DFB-77"));
		assertFalse("2", ValidadorModelo.comprobarMotor("GME-dd"));
		assertFalse("3", ValidadorModelo.comprobarMotor("ded-88"));
	}
}

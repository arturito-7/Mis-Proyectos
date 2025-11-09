package negocio.personal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import integracion.UtilidadesI;
import negocio.personalHangar.TPersonalHangar;

public class SAPersonalTest {

	@Test
	public void altaPersonalTest() {
		UtilidadesI.esTest();

		SAPersonal sp = new SAPersonalImp();
		TPersonal personal = new TPLimpieza(0, 5, "fsdfds", true, "algo");
		assertEquals("no se puede dar de alta personal con el mismo id empleado", -1, sp.altaPersonal(personal));
		personal = new TPSeguridad(0, 900, "yeah", true, 4535);
		assertEquals("el empleado con id 4 ya existia pero estaba dado de baja", 4, sp.altaPersonal(personal));

	}

	@Test
	public void bajaPersonalTest() {
		UtilidadesI.esTest();

		SAPersonal sp = new SAPersonalImp();
		
		assertFalse("no se puede dar de baja un empleaod que no existe", sp.bajaPersonal(500));
		assertTrue("deberia poder darse de baja el personal 4", sp.bajaPersonal(4));
		
	}

	@Test
	public void vincularPersonalTest() {
		UtilidadesI.esTest();

		SAPersonal sp = new SAPersonalImp();
		TPersonalHangar tph = new TPersonalHangar(2, 2);

		sp.desvincularPersonal(tph); // desvinculamos
		
		// prueba exitosa
		assertTrue("No se han podido vincular", sp.vincularPersonal(tph));

		// prueba ya vinculados
		assertFalse("ya estaban vinulados", sp.vincularPersonal(tph));

	}

	@Test
	public void desvincularPersonalTest() {
		UtilidadesI.esTest();

		SAPersonal sp = new SAPersonalImp();
		TPersonalHangar tph = new TPersonalHangar(1, 1);

		sp.vincularPersonal(tph); // vinculamos
		
		// prueba exitosa
		assertTrue("No se han podido desvincular", sp.desvincularPersonal(tph));

		// prueba ya desvinculados
		assertFalse("ya estaban desvinulados", sp.desvincularPersonal(tph));
	}

	@Test
	public void modificarPersonalTest() {
		UtilidadesI.esTest();

		SAPersonal sp = new SAPersonalImp();
		TPersonal personal = new TPLimpieza(5, 111, "modificacion", true, "modificacion2");
		
		assertTrue("deberia modificarse", sp.modificarPersonal(personal));
		personal.setId(1);
		assertFalse("no se puede modificar un personal con el id de otro activo", sp.modificarPersonal(personal));
		
		personal = new TPSeguridad(6, 67890, "ggs", true, 7686);
		assertFalse("no se puede modificar personal no activo", sp.modificarPersonal(personal));
	}

	@Test
	public void consultarPersonalPorIdTest() {
		UtilidadesI.esTest();

		SAPersonal sp = new SAPersonalImp();
		
		assertEquals("no existe personal con id 500", null, sp.consultarPersonalPorId(500));
		assertEquals("el personal 1 tiene el area asignada seguridad", "Seguridad", sp.consultarPersonalPorId(1).getAreaAsignada());
		
	}
}

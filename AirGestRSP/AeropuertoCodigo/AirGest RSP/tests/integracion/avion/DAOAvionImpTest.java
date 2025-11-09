package integracion.avion;

import static org.junit.Assert.*;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import org.junit.Test;

import integracion.UtilidadesI;
import negocio.avion.TAComercial;
import negocio.avion.TAPrivado;
import negocio.avion.TAvion;

public class DAOAvionImpTest {

	@Test
	public void consultarAvionesPorModelo_test() {
		UtilidadesI.esTest();

		DAOAvion da = new DAOAvionImp();

		List<TAvion> aviones = da.consultarAvionesPorModelo(1);

		assertEquals("tiene que haber 3 aviones con modelo 1", 3, aviones.size());
		
		aviones = da.consultarAvionesPorModelo(2);
		
		assertEquals("tiene que haber 1 aviones con modelo 2", 1, aviones.size());

	}

	@Test
	public void consultarAvionesActivosPorModelo_test() {
		UtilidadesI.esTest();

		DAOAvion da = new DAOAvionImp();

		List<TAvion> aviones = da.consultarAvionesActivosPorModelo(1);

		assertEquals("tiene que haber 3 aviones activos con modelo 1", 3, aviones.size());
		
		aviones = da.consultarAvionesActivosPorModelo(2);

		assertEquals("tiene que haber 0 aviones activos con modelo 2", 0, aviones.size());
	}

	@Test
	public void consultarAvionPorId_test() {
		UtilidadesI.esTest();

		DAOAvion da = new DAOAvionImp();

		assertEquals("el avion con id 1 tiene matricula EC-12C", "EC-12C", da.consultarAvionPorId(1).getMatricula());
	}

	@Test
	public void consultarAvionesPorMatricula_test() {
		UtilidadesI.esTest();

		DAOAvion da = new DAOAvionImp();

		assertEquals("el avion con matricula EC-12C tiene id 1", 1, da.consultarAvionesPorMatricula("EC-12C").getId());
	}

	@Test
	public void consultarTodosAviones_test() {
		UtilidadesI.esTest();

		DAOAvion da = new DAOAvionImp();

		List<TAvion> aviones = da.consultarTodosAviones();

		File carpeta = new File(UtilidadesI.ruta("avion"));
		File[] lista = carpeta.listFiles();

		assertEquals("tiene que haber tantos aviones como ficheros", lista.length, aviones.size());
	}

	@Test
	public void consultarAvionesPorAerolinea_test() {
		UtilidadesI.esTest();

		DAOAvion da = new DAOAvionImp();

		List<TAvion> aviones = da.consultarAvionesPorAerolinea(1);

		assertEquals("tiene que haber 4 aviones con aerolinea 1", 4, aviones.size());
		
		aviones = da.consultarAvionesPorAerolinea(5);

		assertEquals("tiene que haber 0 aviones con aerolinea 5", 0, aviones.size());
	}

	@Test
	public void consultarAvionesPorHangar_test() {
		UtilidadesI.esTest();

		DAOAvion da = new DAOAvionImp();

		List<TAvion> aviones = da.consultarAvionesPorHangar(3);

		assertEquals("tiene que haber 2 aviones con hangar 3", 2, aviones.size());
	}

	@Test
	public void consultarAvionesActivosPorAerolinea_test() {
		UtilidadesI.esTest();

		DAOAvion da = new DAOAvionImp();

		List<TAvion> aviones = da.consultarAvionesActivosPorAerolinea(1);

		assertEquals("tiene que haber 3 aviones activos con aerolinea 1", 3, aviones.size());
		
		aviones = da.consultarAvionesActivosPorAerolinea(5);

		assertEquals("tiene que haber 0 aviones activos con aerolinea 5", 0, aviones.size());
	}

	@Test
	public void consultarAvionesActivosPorHangar_test() {
		UtilidadesI.esTest();

		DAOAvion da = new DAOAvionImp();

		List<TAvion> aviones = da.consultarAvionesActivosPorHangar(3);

		assertEquals("tiene que haber 0 aviones activos con hangar 3", 0, aviones.size());
	}
	
	@Test
	public void altaAvion_test() {
		UtilidadesI.esTest();

		DAOAvion da = new DAOAvionImp();
		File carpeta = new File(UtilidadesI.ruta("avion"));
		File[] lista = carpeta.listFiles();

		TAvion avion = new TAComercial(0, 5, LocalDate.of(2004, 12, 6), "nombrePrueba", "EC-1234", 
				true, 4, 10, 11, 12);
		assertEquals("Deberia darse de alta el avion comercial", lista.length + 1, da.altaAvion(avion));
		
		lista = carpeta.listFiles();
		
		avion = new TAPrivado(0, 5, LocalDate.of(2004, 12, 6), "nombrePrueba2", "EC-2", 
				true, 7, 8, 9, "Patricio", 7);
		assertEquals("Deberia darse de alta el avion comercial", lista.length + 1, da.altaAvion(avion));
	}
	
	@Test
	public void modificarAvion_test() {
		UtilidadesI.esTest();
		DAOAvion da = new DAOAvionImp();

		TAvion avion = new TAComercial(5, 5, LocalDate.of(2004, 12, 6), "nombrePruebaModif", "EC-123ASD", 
				true, 4, 10, 11, 12);

		assertTrue("Deberia modificarse el avion comercial", da.modificarAvion(avion));
		
		avion = new TAPrivado(6, 5, LocalDate.of(2004, 12, 6), "nombrePruebaModif2", "EC-2Cambio", 
				true, 7, 8, 9, "Pablo", 7);

		assertTrue("Deberia modificarse el avion privado", da.modificarAvion(avion));
	}
	
	@Test
	public void bajaAvion_test() {
		UtilidadesI.esTest();

		DAOAvion da = new DAOAvionImp();

		assertTrue("Deberia darse de baja el avion", da.bajaAvion(5));
		assertTrue("Deberia darse de baja el avion", da.bajaAvion(6));

	}
}

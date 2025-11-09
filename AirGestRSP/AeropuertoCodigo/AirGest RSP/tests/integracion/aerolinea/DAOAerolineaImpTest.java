package integracion.aerolinea;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;

import integracion.UtilidadesI;
import negocio.aerolinea.TAerolinea;

public class DAOAerolineaImpTest {
	
	@Test
	public void alta_aerolinea_test() {
		UtilidadesI.esTest();

		DAOAerolinea da = new DAOAerolineaImp();

		TAerolinea aerolinea = new TAerolinea(3, "tres", true);

		File carpeta = new File(UtilidadesI.ruta("aerolinea"));
		File[] lista = carpeta.listFiles();

		assertEquals("No ha devuelto el id correcto", lista.length + 1, da.altaAerolinea(aerolinea));
	}
	
	@Test
	public void leerAerolineaPorIdTest(){
		UtilidadesI.esTest();
		
		DAOAerolinea da = new DAOAerolineaImp();
		
		TAerolinea ta = da.leerAerolineaPorId(1);
		
		System.out.println(ta);
		
		assertNotNull("No existe aerolinea", ta);
	}
	

	@Test
	public void leer_aerolinea_por_nombre_test() {
		UtilidadesI.esTest();

		DAOAerolinea da = new DAOAerolineaImp();

		assertEquals("la aerolinea con nombre uno es la id 1", 1, da.leerAerolineaPorNombre("uno").getId());
	}
	
	
	@Test
	public void modificar_aerolinea_test() {
		UtilidadesI.esTest();
		DAOAerolinea da = new DAOAerolineaImp();

		TAerolinea aerolinea = new TAerolinea(1, "unoMODIFICADO",true);

		assertTrue("Ha leido mal el fichero", da.modificarAerolinea(aerolinea));
	}

	@Test
	public void baja_aerolinea_test() {
		UtilidadesI.esTest();

		DAOAerolinea da = new DAOAerolineaImp();

		assertTrue("No se ha dado de baja", da.bajaAerolinea(2));
	}

	@Test
	public void consultar_todas_aerolineas_test() {
		UtilidadesI.esTest();

		DAOAerolinea da = new DAOAerolineaImp();

		List<TAerolinea> aerolineas = da.consultarTodasAerolineas();

		File carpeta = new File(UtilidadesI.ruta("aerolinea"));
		File[] lista = carpeta.listFiles();

		assertEquals("tiene que haber tantas aerolineas como ficheros", lista.length, aerolineas.size());
	}
}

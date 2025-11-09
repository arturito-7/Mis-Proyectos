package integracion.hangar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Test;

import integracion.UtilidadesI;
import negocio.hangar.THangar;

public class DAOHangarImpTest {
	@Test
	public void alta_hangar_test() {
		UtilidadesI.esTest();

		DAOHangar dh = new DAOHangarImp();

		THangar hangar = new THangar(0, "paseo hola", 4, 50.8, 4, true);

		File carpeta = new File(UtilidadesI.ruta("hangar"));
		File[] lista = carpeta.listFiles();

		assertEquals("No ha devuelto el id correcto", lista.length + 1, dh.altaHangar(hangar));
	}
	
	@Test
	public void baja_hangar_test() {
		UtilidadesI.esTest();

		DAOHangar dh = new DAOHangarImp();

		assertTrue("No se ha dado de baja", dh.bajaHangar(2));
	}
	
	@Test
	public void leer_hangar_por_id_test() {
		UtilidadesI.esTest();

		DAOHangar dh = new DAOHangarImp();

		assertEquals("el hangar con id 3 tiene direccion paseo hola", "paseo hola", dh.leerHangarPorId(3).getDireccion());
	}
	
	@Test
	public void consultar_todos_hangares_test() {
		UtilidadesI.esTest();

		DAOHangar dh = new DAOHangarImp();

		List<THangar> hangares = dh.consultarTodosHangares();

		File carpeta = new File(UtilidadesI.ruta("hangar"));
		File[] lista = carpeta.listFiles();

		assertEquals("tiene que haber tantos modelos como ficheros", lista.length, hangares.size());
	}
	
	@Test
	public void modificar_hangar_test() {
		UtilidadesI.esTest();
		DAOHangar dh = new DAOHangarImp();

		THangar hangar = new THangar(1, "paseo adios", 4, 50.8, 4, true);

		assertTrue("Ha leido mal el fichero", dh.modificarHangar(hangar));
	}
	
	@Test
	public void actualizar_stock_test(){
		UtilidadesI.esTest();
		DAOHangar dh = new DAOHangarImp();
		
		assertTrue("No encuentra hangar", dh.actualizarStock(1, -1));
	}
}

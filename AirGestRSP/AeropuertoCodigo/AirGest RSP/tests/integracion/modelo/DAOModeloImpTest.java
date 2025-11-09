package integracion.modelo;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;

import integracion.UtilidadesI;
import negocio.modelo.TModelo;

public class DAOModeloImpTest {

	@Test
	public void leer_modelo_por_nombre_test() {
		UtilidadesI.esTest();

		DAOModelo dm = new DAOModeloImp();

		assertEquals("el modelo con nombre pepe es el id 3", 3, dm.leerModeloPorNombre("pepe").getId());
	}

	@Test
	public void alta_modelo_test() {
		UtilidadesI.esTest();

		DAOModelo dm = new DAOModeloImp();

		TModelo modelo = new TModelo(0, "nombre", "motor", true);

		File carpeta = new File(UtilidadesI.ruta("modelo"));
		File[] lista = carpeta.listFiles();

		assertEquals("No ha devuelto el id correcto", lista.length + 1, dm.altaModelo(modelo));
	}

	@Test
	public void modificar_modelo_test() {
		UtilidadesI.esTest();
		DAOModelo dm = new DAOModeloImp();

		TModelo modelo = new TModelo(1, "florencia", "hola", true);

		assertTrue("Ha leido mal el fichero", dm.modificarModelo(modelo));
	}

	@Test
	public void baja_modelo_test() {
		UtilidadesI.esTest();

		DAOModelo dm = new DAOModeloImp();

		assertTrue("No se ha dado de baja", dm.bajaModelo(4));
	}

	@Test
	public void consultar_todos_modelos_test() {
		UtilidadesI.esTest();

		DAOModelo dm = new DAOModeloImp();

		List<TModelo> modelos = dm.consultarTodosModelos();

		File carpeta = new File(UtilidadesI.ruta("modelo"));
		File[] lista = carpeta.listFiles();

		assertEquals("tiene que haber tantos modelos como ficheros", lista.length, modelos.size());
	}
	
	@Test
	public void leer_modelo_por_id_test() {
		UtilidadesI.esTest();

		DAOModelo dm = new DAOModeloImp();

		assertEquals("el modelo con id 3 tiene nombre pepe", "pepe", dm.leerModeloPorId(3).getNombre());
	}

}

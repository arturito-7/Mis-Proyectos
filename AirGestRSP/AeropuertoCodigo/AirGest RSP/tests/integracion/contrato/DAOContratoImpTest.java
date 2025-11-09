package integracion.contrato;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Test;

import integracion.UtilidadesI;
import negocio.contrato.TContrato;

public class DAOContratoImpTest {
	@Test
	public void alta_contrato_test() {
		UtilidadesI.esTest();

		DAOContrato dc = new DAOContratoImp();

		TContrato contrato = new TContrato(0, 1, 234.4);

		File carpeta = new File(UtilidadesI.ruta("contrato"));
		File[] lista = carpeta.listFiles();

		assertEquals("No ha devuelto el id correcto", lista.length + 1, dc.altaContrato(contrato));
	}
	
	@Test
	public void leer_contrato_por_id_test() {
		UtilidadesI.esTest();

		DAOContrato dc = new DAOContratoImp();

		assertEquals("el contrato con id 1 tiene la aerolinea 1", 1, dc.leerContratoPorId(1).getIdAerolinea());
	}
	
	@Test
	public void consultar_todos_contratos_test() {
		UtilidadesI.esTest();

		DAOContrato dc = new DAOContratoImp();

		List<TContrato> contratos = dc.leerTodosContratos();

		File carpeta = new File(UtilidadesI.ruta("contrato"));
		File[] lista = carpeta.listFiles();

		assertEquals("tiene que haber tantos contratos como ficheros", lista.length, contratos.size());
	}
	
	@Test 
	public void consultar_contratos_por_aerolinea_test(){
		UtilidadesI.esTest();

		DAOContrato dc = new DAOContratoImp();
		
		List<TContrato> contratos = dc.leerContratosPorAerolinea(1);
		
		assertEquals("tiene que haber 2 contratos con aerolinea 1", 2, contratos.size());
	}
	
	@Test
	public void modificar_contrato_test() {
		UtilidadesI.esTest();
		DAOContrato dc = new DAOContratoImp();

		TContrato contrato = new TContrato(1, 2, 100);

		assertTrue("Ha leido mal el fichero", dc.modificarContrato(contrato));
	}
}

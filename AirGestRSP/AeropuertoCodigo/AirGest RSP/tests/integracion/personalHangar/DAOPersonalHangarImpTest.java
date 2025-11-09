package integracion.personalHangar;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import integracion.UtilidadesI;
import integracion.modeloAerolinea.DAOModeloAerolinea;
import integracion.modeloAerolinea.DAOModeloAerolineaImp;

public class DAOPersonalHangarImpTest {
	@Test
	public void comprobarVinculacionTest() {
		UtilidadesI.esTest();
		DAOModeloAerolinea dma = new DAOModeloAerolineaImp();

		assertTrue("No existe vinculación", dma.comprobarVinculacion(6, 1));
	}

	@Test
	public void vincularTest() {
		UtilidadesI.esTest();
		DAOPersonalHangar dph = new DAOPersonalHangarImp();

		assertTrue("No se ha podido vincular", dph.vincular(5, 5));
	}

	@Test
	public void desvincularTest() {
		UtilidadesI.esTest();
		DAOPersonalHangar dph = new DAOPersonalHangarImp();

		assertTrue("No se ha podido desvincular", dph.desvincular(5, 5));
	}
}

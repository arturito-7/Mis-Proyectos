package negocio.contrato;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;
import integracion.UtilidadesI;
import negocio.lineaContrato.TLineaContrato;

public class SAContratoImpTest {

	@Test
	public void modificar_contrato_test() {
		UtilidadesI.esTest();

		SAContrato sc = new SAContratoImp();

		// Prueba exitosa
		TContrato contrato = new TContrato(1, 6, 346.7);
		assertTrue("debería modificarse contrato", sc.modificarContrato(contrato));

		// Prueba aerolinea no existente
		contrato = new TContrato(1, 9, 346.7);
		assertFalse("no existe la aerolinea 9", sc.modificarContrato(contrato));

		// Prueba aerolinea no activa
		contrato = new TContrato(1, 5, 456.3);
		assertFalse("la aerolinea 5 no está activa", sc.modificarContrato(contrato));
	}

	@Test
	public void consultar_contrato_por_id_test() {
		UtilidadesI.esTest();

		SAContrato sc = new SAContratoImp();

		// Prueba exitosa
		TInfoContrato info = sc.consultarContratoPorId(1);
		assertEquals("el toa deberia tener 4 hangares", 4, info.getHangares().size());

		// Prueba contrato no existente
		info = sc.consultarContratoPorId(4);
		assertNull("no existe el contrato 4", info);
	}

	@Test
	public void consultar_contratos_por_aerolinea_test() {
		UtilidadesI.esTest();

		SAContrato sc = new SAContratoImp();

		// Prueba exitosa
		List<TContrato> contratos = sc.consultarContratosPorAerolinea(6);
		assertEquals("debería tener solo un contrato", 1, contratos.size());

		// Prueba no existe aerolinea
		contratos = sc.consultarContratosPorAerolinea(9);
		assertEquals("no existe la aerolinea 9", 0, contratos.size());
	}

	@Test
	public void cerrar_contrato_test() {
		UtilidadesI.esTest();

		SAContrato sc = new SAContratoImp();

		// Prueba exitosa
		TCarrito carrito = new TCarrito(1);

		TLineaContrato linea1 = new TLineaContrato();
		linea1.setIdHangar(1);
		linea1.setFechaIni(LocalDate.of(2024, 4, 23));
		linea1.setFechaFin(LocalDate.of(2024, 4, 24));
		carrito.anyadirLinea(linea1);

		TLineaContrato linea2 = new TLineaContrato();
		linea2.setIdHangar(2);
		linea2.setFechaIni(LocalDate.of(2024, 4, 23));
		linea2.setFechaFin(LocalDate.of(2024, 4, 24));
		carrito.anyadirLinea(linea2);

		TLineaContrato linea3 = new TLineaContrato();
		linea3.setIdHangar(3);
		linea3.setFechaIni(LocalDate.of(2024, 4, 23));
		linea3.setFechaFin(LocalDate.of(2024, 4, 24));
		carrito.anyadirLinea(linea3);

		assertEquals("el id del contrato debería ser 1", -1, sc.cerrarContrato(carrito));

		// Prueba aerolinea no existente
		carrito = new TCarrito(99);
		assertEquals("la aerolinea 99 no existe", -1, sc.cerrarContrato(carrito));

		// Prueba aerolinea no activa
		carrito = new TCarrito(5);
		assertEquals("la aerolinea 55 no está activa", -1, sc.cerrarContrato(carrito));

		// Prueba hangar no existente
		carrito = new TCarrito(1);
		linea1 = new TLineaContrato();
		linea1.setIdHangar(8);
		linea1.setFechaIni(LocalDate.of(2024, 4, 23));
		linea1.setFechaFin(LocalDate.of(2024, 4, 24));
		carrito.anyadirLinea(linea1);
		assertEquals("el hangar 8 no existe", -1, sc.cerrarContrato(carrito));

		// Prueba hangar no activo
		carrito = new TCarrito(1);
		linea1 = new TLineaContrato();
		linea1.setIdHangar(6);
		linea1.setFechaIni(LocalDate.of(2024, 4, 23));
		linea1.setFechaFin(LocalDate.of(2024, 4, 24));
		carrito.anyadirLinea(linea1);
		assertEquals("el hangar 6 no esta activo", -1, sc.cerrarContrato(carrito));

		// Prueba hangar repetido en el carrito
		carrito = new TCarrito(1);
		linea1 = new TLineaContrato();
		linea1.setIdHangar(1);
		linea1.setFechaIni(LocalDate.of(2024, 4, 23));
		linea1.setFechaFin(LocalDate.of(2024, 4, 24));
		carrito.anyadirLinea(linea1);

		linea2 = new TLineaContrato();
		linea2.setIdHangar(1);
		linea2.setFechaIni(LocalDate.of(2024, 4, 23));
		linea2.setFechaFin(LocalDate.of(2024, 4, 24));
		carrito.anyadirLinea(linea2);
		assertEquals("hay hangares repetidos", -1, sc.cerrarContrato(carrito));

		// Prueba comprobar fechas
		carrito = new TCarrito(1);

		linea1 = new TLineaContrato();
		linea1.setIdHangar(1);
		linea1.setFechaIni(LocalDate.of(2024, 4, 23));
		linea1.setFechaFin(LocalDate.of(2024, 4, 24));
		carrito.anyadirLinea(linea1);

		linea2 = new TLineaContrato();
		linea2.setIdHangar(2);
		linea2.setFechaIni(LocalDate.of(2024, 4, 26));
		linea2.setFechaFin(LocalDate.of(2024, 4, 27));
		carrito.anyadirLinea(linea2);

		linea3 = new TLineaContrato();
		linea3.setIdHangar(3);
		linea3.setFechaIni(LocalDate.of(2024, 4, 26));
		linea3.setFechaFin(LocalDate.of(2024, 4, 27));
		carrito.anyadirLinea(linea3);

		assertEquals("el hangar 1 no se puede contratar", -1, sc.cerrarContrato(carrito));

		// Prueba fecha inicio posterior a fecha fin
		carrito = new TCarrito(1);

		linea1 = new TLineaContrato();
		linea1.setIdHangar(1);
		linea1.setFechaIni(LocalDate.of(2024, 5, 27));
		linea1.setFechaFin(LocalDate.of(2024, 5, 24));
		carrito.anyadirLinea(linea1);

		assertEquals("fecha inicio posterior a la de fin", -1, sc.cerrarContrato(carrito));
	}

	@Test
	public void modificar_linea_contrato_test() {
		UtilidadesI.esTest();

		SAContrato sc = new SAContratoImp();

		// Prueba exitosa
		LocalDate fecha_ini = LocalDate.of(2024, 3, 10);
		LocalDate fecha_fin = LocalDate.of(2024, 3, 14);

		TLineaContrato linea = new TLineaContrato(1, 1, fecha_ini, fecha_fin, 0);
		assertTrue("deberia modificarse linea", sc.modificarLineaContrato(linea));

		// Hangar no existente
		linea = new TLineaContrato(1, 12, fecha_ini, fecha_fin, 0);
		assertFalse("no existe el hangar 12", sc.modificarLineaContrato(linea));

		// Hangar no activo
		linea = new TLineaContrato(1, 6, fecha_ini, fecha_fin, 0);
		assertFalse("el hangar 6 esta inactivo", sc.modificarLineaContrato(linea));

		// Fechas ocupadas
		fecha_ini = LocalDate.of(2024, 3, 11);
		fecha_fin = LocalDate.of(2024, 3, 13);
		linea = new TLineaContrato(1, 1, fecha_ini, fecha_fin, 0);
		assertFalse("rango de fechas no valido", sc.modificarLineaContrato(linea));

	}
}

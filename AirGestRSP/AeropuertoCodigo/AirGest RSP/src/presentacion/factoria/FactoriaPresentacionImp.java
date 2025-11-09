
package presentacion.factoria;

import presentacion.modelo.*;
import presentacion.avion.*;
import presentacion.aerolinea.*;
import presentacion.hangar.*;
import presentacion.personal.*;
import presentacion.contrato.*;
import presentacion.Observador;
import presentacion.VistaPrincipal;

public class FactoriaPresentacionImp extends FactoriaPresentacion {

	public Observador crearVistaAltaModelo() {
		return new VistaAltaModelo();
	}

	public Observador crearVistaExitoAltaModelo() {
		return new VistaExitoAltaModelo();
	}

	public Observador crearVistaFalloAltaModelo() {
		return new VistaFalloAltaModelo();
	}

	public Observador crearVistaExitoBajaModelo() {
		return new VistaExitoBajaModelo();
	}

	public Observador crearVistaFalloBajaModelo() {
		return new VistaFalloBajaModelo();
	}

	public Observador crearVistaBajaModelo() {
		return new VistaBajaModelo();
	}

	public Observador crearVistaResultadoConsultarTodosModelos() {
		return new VistaResultadoConsultarTodosModelos();
	}

	public Observador crearVistaConsultarModeloPorId() {
		return new VistaConsultarModeloPorId();
	}

	public Observador crearVistaResultadoConsultarModeloPorId() {
		return new VistaResultadoConsultarModeloPorId();
	}

	public Observador crearVistaDesvincularModelo() {
		return new VistaDesvincularModelo();
	}

	public Observador crearVistaExitoDesvincularModelo() {
		return new VistaExitoDesvincularModelo();
	}

	public Observador crearVistaFalloDesvincularModelo() {
		return new VistaFalloDesvincularModelo();
	}

	public Observador crearVistaVincularModelo() {
		return new VistaVincularModelo();
	}

	public Observador crearVistaExitoVincularModelo() {
		return new VistaExitoVincularModelo();
	}

	public Observador crearVistaFalloVincularModelo() {
		return new VistaFalloVincularModelo();
	}

	public Observador crearVistaModificarModelo() {
		return new VistaModificarModelo();
	}

	public Observador crearVistaExitoModificarModelo() {
		return new VistaExitoModificarModelo();
	}

	public Observador crearVistaFalloModificarModelo() {
		return new VistaFalloModificarModelo();
	}

	public Observador crearVistaModelo() {
		return new VistaModelo();
	}

	public Observador crearVistaPrincipal() {
		return new VistaPrincipal();
	}

	public Observador crearVistaHangar() {
		return new VistaHangar();
	}

	public Observador crearVistaAltaHangar() {
		return new VistaAltaHangar();
	}

	public Observador crearVistaExitoAltaHangar() {
		return new VistaExitoAltaHangar();
	}

	public Observador crearVistaFalloAltaHangar() {
		return new VistaFalloAltaHangar();
	}

	public Observador crearVistaBajaHangar() {
		return new VistaBajaHangar();
	}

	public Observador crearVistaExitoBajaHangar() {
		return new VistaExitoBajaHangar();
	}

	public Observador crearVistaFalloBajaHangar() {
		return new VistaFalloBajaHangar();
	}

	public Observador crearVistaConsultarHangarPorId() {
		return new VistaConsultarHangarPorId();
	}

	public Observador crearVistaResultadoConsultarHangarPorId() {
		return new VistaResultadoConsultarHangarPorId();
	}

	public Observador crearVistaResultadoConsultarTodosHangares() {
		return new VistaResultadoConsultarTodosHangares();
	}

	public Observador crearVistaModificarHangar() {
		return new VistaModificarHangar();
	}

	public Observador crearVistaExitoModificarHangar() {
		return new VistaExitoModificarHangar();
	}

	public Observador crearVistaFalloModificarHangar() {
		return new VistaFalloModificarHangar();
	}

	public Observador crearVistaAltaAvion() {
		return new VistaAltaAvion();
	}

	public Observador crearVistaAvion() {
		return new VistaAvion();
	}

	public Observador crearVistaBajaAvion() {
		return new VistaBajaAvion();
	}

	public Observador crearVistaConsultarAvionPorId() {
		return new VistaConsultarAvionPorId();
	}

	public Observador crearVistaExitoAltaAvion() {
		return new VistaExitoAltaAvion();
	}

	public Observador crearVistaExitoBajaAvion() {
		return new VistaExitoBajaAvion();
	}

	public Observador crearVistaExitoModificarAvion() {
		return new VistaExitoModificarAvion();
	}

	public Observador crearVistaFalloAltaAvion() {
		return new VistaFalloAltaAvion();
	}

	public Observador crearVistaFalloBajaAvion() {
		return new VistaFalloBajaAvion();
	}

	public Observador crearVistaFalloModificarAvion() {
		return new VistaFalloModificarAvion();
	}

	public Observador crearVistaModficarAvion() {
		return new VistaModificarAvion();
	}

	public Observador crearVistaMostrarAvionesPorAerolinea() {
		return new VistaMostrarAvionesPorAerolinea();
	}

	public Observador crearVistaMostrarAvionesPorHangar() {
		return new VistaMostrarAvionesPorHangar();
	}

	public Observador crearVistaMostrarAvionesPorModelo() {
		return new VistaMostrarAvionesPorModelo();
	}

	public Observador crearVistaResultadoConsultarAvionPorId() {
		return new VistaResultadoConsultarAvionPorId();
	}

	public Observador crearVistaResultadoConsultarTodosAviones() {
		return new VistaResultadoConsultarTodosAviones();
	}

	public Observador crearVistaResultadoMostrarAvionesPorAerolinea() {
		return new VistaResultadoMostrarAvionesPorAerolinea();
	}

	public Observador crearVistaResultadoMostrarAvionesPorHangar() {
		return new VistaResultadoMostrarAvionesPorHangar();
	}

	public Observador crearVistaResultadoMostrarAvionesPorModelo() {
		return new VistaResultadoMostrarAvionesPorModelo();
	}

	public Observador crearVistaFalloAltaAerolinea() {
		return new VistaFalloAltaAerolinea();
	}

	public Observador crearVistaExitoAltaAerolinea() {
		return new VistaExitoAltaAerolinea();
	}

	public Observador crearVistaFalloBajaAerolinea() {
		return new VistaFalloBajaAerolinea();
	}

	public Observador crearVistaExitoBajaAerolinea() {
		return new VistaExitoBajaAerolinea();
	}

	public Observador crearVistaResultadoConsultarAerolineaPorId() {
		return new VistaResultadoConsultarAerolineaPorId();
	}

	public Observador crearVistaResultadoConsultarTodasAerolineas() {
		return new VistaResultadoConsultarTodasAerolineas();
	}

	public Observador crearVistaFalloModificarAerolinea() {
		return new VistaFalloModificarAerolinea();
	}

	public Observador crearVistaExitoModificarAerolinea() {
		return new VistaExitoModificarAerolinea();
	}

	public Observador crearVistaAerolinea() {
		return new VistaAerolinea();
	}

	public Observador crearVistaAltaAerolinea() {
		return new VistaAltaAerolinea();
	}

	public Observador crearVistaBajaAerolinea() {
		return new VistaBajaAerolinea();
	}

	public Observador crearVistaConsultarAerolineaPorId() {
		return new VistaConsultarAerolineaPorId();
	}

	public Observador crearVistaContrato() {
		return new VistaContrato();
	}

	public Observador crearVistaAbrirContrato() {
		return new VistaAbrirContrato();
	}

	public Observador crearVistaAñadirHangar() {
		return new VistaAñadirHangar();
	}

	public Observador crearVistaCarrito() {
		return new VistaCarrito();
	}

	public Observador crearVistaConsultarContratoPorId() {
		return new VistaConsultarContratoPorId();
	}

	public Observador crearVistaConsultarContratoPorAerolinea() {
		return new VistaConsultarContratosPorAerolinea();
	}

	public Observador crearVistaEliminarHangar() {
		return new VistaEliminarHangar();
	}

	public Observador crearVistaExitoCerrarContrato() {
		return new VistaExitoCerrarContrato();
	}

	public Observador crearVistaExitoModificarContrato() {
		return new VistaExitoModificarContrato();
	}

	public Observador crearVistaExitoModificarLineaContrato() {
		return new VistaExitoModificarLineaContrato();
	}

	public Observador crearVistaFalloCerrarContrato() {
		return new VistaFalloCerrarContrato();
	}

	public Observador crearVistaFalloModificarContrato() {
		return new VistaFalloModificarContrato();
	}

	public Observador crearVistaFalloModificarLineaContrato() {
		return new VistaFalloModificarLineaContrato();
	}

	public Observador crearVistaModificarContrato() {
		return new VistaModificarContrato();
	}

	public Observador crearVistaModificarLineaContrato() {
		return new VistaModificarLineaContrato();
	}

	public Observador crearVistaResultadoConsultarContratosPorAerolinea() {
		return new VistaResultadorConsultarContratosPorAerolinea();
	}

	public Observador crearVistaResultadoConsultarTodosContratos() {
		return new VistaResultadoConsultarTodosContratos();
	}

	public Observador crearVistaModificarAerolinea() {
		return new VistaModificarAerolinea();
	}

	@Override
	public Observador crearVistaResultadoConsultarContratoPorid() {
		return new VistaResultadoConsultarContratoPorId();
	}

	public Observador crearVistaPersonal() {
		return new VistaPersonal();
	}

	public Observador crearVistaAltaPersonal() {
		return new VistaAltaPersonal();
	}

	public Observador crearVistaBajaPersonal() {
		return new VistaBajaPersonal();
	}

	public Observador crearVistaModificarPersonal() {
		return new VistaModificarPersonal();
	}

	public Observador crearVistaVincularPersonal() {
		return new VistaVincularPersonal();
	}

	public Observador crearVistaDesvincularPersonal() {
		return new VistaDesvincularPersonal();
	}

	public Observador crearVistaConsultarPersonalPorId() {
		return new VistaConsultarPersonalPorId();
	}

	public Observador crearVistaExitoAltaPersonal() {
		return new VistaExitoAltaPersonal();
	}

	public Observador crearVistaFalloAltaPersonal() {
		return new VistaFalloAltaPersonal();
	}

	public Observador crearVistaExitoBajaPersonal() {
		return new VistaExitoBajaPersonal();
	}

	public Observador crearVistaFalloBajaPersonal() {
		return new VistaFalloBajaPersonal();
	}

	public Observador crearVistaExitoModificarPersonal() {
		return new VistaExitoModificarPersonal();
	}

	public Observador crearVistaFalloModificarPersonal() {
		return new VistaFalloModificarPersonal();
	}

	public Observador crearVistaExitoVincularPersonal() {
		return new VistaExitoVincularPersonal();
	}

	public Observador crearVistaFalloVincularPersonal() {
		return new VistaFalloVincularPersonal();
	}

	public Observador crearVistaExitoDesvincularPersonal() {
		return new VistaExitoDesvincularPersonal();
	}

	public Observador crearVistaFalloDesvincularPersonal() {
		return new VistaFalloDesvincularPersonal();
	}

	public Observador crearVistaResultadoConsultarPersonalPorId() {
		return new VistaResultadoConsultarPersonalPorId();
	}

	public Observador crearVistaResultadoConsultarPersonalExistente() {
		return new VistaResultadoConsultarPersonalExistente();
	}

}
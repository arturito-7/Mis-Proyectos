package negocio.contrato;

import java.util.List;

import negocio.lineaContrato.TLineaContrato;

public interface SAContrato {

	public TCarrito abrirContrato(int id_aerolinea);

	public int cerrarContrato(TCarrito tCarrito);

	public TInfoContrato consultarContratoPorId(int id);

	public List<TContrato> consultarTodosContratos();

	public boolean modificarContrato(TContrato tContrato);

	public boolean modificarLineaContrato(TLineaContrato linea);

	public List<TContrato> consultarContratosPorAerolinea(int id_aerolinea);
	
}
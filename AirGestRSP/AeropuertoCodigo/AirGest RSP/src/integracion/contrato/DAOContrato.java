package integracion.contrato;

import negocio.contrato.TContrato;
import java.util.List;

public interface DAOContrato {

	public int altaContrato(TContrato tContrato);

	public TContrato leerContratoPorId(int id);

	public List<TContrato> leerTodosContratos();

	public List<TContrato> leerContratosPorAerolinea(int id_aerolinea);

	public boolean modificarContrato(TContrato tContrato);

}
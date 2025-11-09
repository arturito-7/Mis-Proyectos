package integracion.lineaContrato;

import negocio.lineaContrato.TLineaContrato;
import java.util.List;

public interface DAOLineaContrato {

	public boolean altaLineaContrato(TLineaContrato tLineaContrato);

	public boolean modificarLineaContrato(TLineaContrato tLineaContrato);

	public List<TLineaContrato> leerLineasPorContrato(int id_contrato);

	public List<TLineaContrato> leerLineasPorHangar(int id_hangar);

	public boolean leerLineaContrato(int id_contrato, int id_hangar);

}
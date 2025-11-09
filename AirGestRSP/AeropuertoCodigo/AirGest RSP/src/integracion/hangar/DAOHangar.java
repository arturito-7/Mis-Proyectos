package integracion.hangar;

import negocio.hangar.THangar;
import java.util.List;

public interface DAOHangar {

	public THangar leerHangarPorId(int id);

	public boolean actualizarStock(int id, int stock);

	public int altaHangar(THangar tHangar);

	public boolean bajaHangar(int id);

	public List<THangar> consultarTodosHangares();

	public boolean modificarHangar(THangar tHangar);

	public THangar leerHangarPorDireccion(String direccion);

}
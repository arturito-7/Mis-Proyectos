package negocio.hangar;

import java.util.List;

public interface SAHangar {

	public int altaHangar(THangar tHangar);

	public boolean bajaHangar(int id);

	public THangar consultarHangarPorId(int id);

	public List<THangar> consultarTodosHangares();

	public boolean modificarHangar(THangar tHangar);
	
}
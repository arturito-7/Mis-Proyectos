package negocio.avion;

import java.util.List;

public interface SAAvion {

	public int altaAvion(TAvion tAvion);

	public boolean bajaAvion(int idAvion);

	public TAvion consultarAvionPorId(int idAvion);

	public List<TAvion> consultarTodosAviones();

	public boolean modificarAvion(TAvion tAvion);

	public List<TAvion> mostrarAvionesPorModelo(int idModelo);

	public List<TAvion> mostrarAvionesPorAerolinea(int idAerolinea);

	public List<TAvion> mostrarAvionesPorHangar(int idHangar);
	
}
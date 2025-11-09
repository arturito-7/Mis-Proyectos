package integracion.avion;

import java.util.List;

import negocio.avion.TAvion;

public interface DAOAvion {

	public List<TAvion> consultarAvionesPorModelo(int idModelo);

	public List<TAvion> consultarAvionesActivosPorModelo(int idModelo);

	public int altaAvion(TAvion tAvion);

	public boolean bajaAvion(int idAvion);

	public TAvion consultarAvionPorId(int idAvion);

	public TAvion consultarAvionesPorMatricula(String nombre);

	public List<TAvion> consultarTodosAviones();

	public boolean modificarAvion(TAvion tAvion);

	public List<TAvion> consultarAvionesPorAerolinea(int idAerolinea);

	public List<TAvion> consultarAvionesPorHangar(int idHangar);

	public List<TAvion> consultarAvionesActivosPorAerolinea(int idAerolinea);

	public List<TAvion> consultarAvionesActivosPorHangar(int idHangar);

}
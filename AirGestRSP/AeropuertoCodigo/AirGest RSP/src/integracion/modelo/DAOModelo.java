package integracion.modelo;

import negocio.modelo.TModelo;
import java.util.List;

public interface DAOModelo {

	public TModelo leerModeloPorNombre(String nombre);

	public int altaModelo(TModelo tModelo);

	public boolean modificarModelo(TModelo tModelo);

	public boolean bajaModelo(int id);

	public List<TModelo> consultarTodosModelos();

	public TModelo leerModeloPorId(int id);

}
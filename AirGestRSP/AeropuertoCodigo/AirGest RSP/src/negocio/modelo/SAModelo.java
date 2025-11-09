package negocio.modelo;

import java.util.List;

import negocio.modeloAerolinea.TModeloAerolinea;

public interface SAModelo {

	int altaModelo(TModelo tModelo);

	public boolean bajaModelo(int id);

	public TModelo consultarModelo(int id);

	public List<TModelo> consultarTodosModelos();

	public boolean modificarModelo(TModelo modelo);

	public boolean vincularModelo(TModeloAerolinea tModeloAerolinea);

	public boolean desvincularModelo(TModeloAerolinea tModeloAerolinea);
	
}
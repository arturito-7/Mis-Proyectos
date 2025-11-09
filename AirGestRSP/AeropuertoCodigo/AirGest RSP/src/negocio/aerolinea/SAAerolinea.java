package negocio.aerolinea;

import java.util.List;

public interface SAAerolinea {

	public int altaAerolinea(TAerolinea datos);

	public boolean bajaAerolinea(int id);

	public TAerolinea consultarAerolineaPorId(int id);

	public List<TAerolinea> consultarTodasAerolineas();

	public boolean modificarAerolinea(TAerolinea tAerolinea);
	
}
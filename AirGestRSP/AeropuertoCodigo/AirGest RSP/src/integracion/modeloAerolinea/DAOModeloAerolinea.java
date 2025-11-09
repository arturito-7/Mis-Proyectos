package integracion.modeloAerolinea;

public interface DAOModeloAerolinea {

	public boolean vincular(int idModelo, int idAerolinea);

	public boolean desvincular(int idModelo, int idAerolinea);

	public boolean comprobarVinculacion(int idModelo, int idAerolinea);

}
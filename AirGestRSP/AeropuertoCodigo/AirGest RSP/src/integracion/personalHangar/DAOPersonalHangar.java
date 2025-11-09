package integracion.personalHangar;

public interface DAOPersonalHangar {

	public boolean vincular(int idPersonal, int idHangar);

	public boolean desvincular(int idPersonal, int idHangar);

	public boolean comprobarVinculacion(int idPersonal, int idHangar);

}
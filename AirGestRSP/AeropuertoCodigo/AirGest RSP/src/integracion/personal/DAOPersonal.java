package integracion.personal;

import negocio.personal.TPersonal;
import java.util.List;

public interface DAOPersonal {

	public int altaPersonal(TPersonal tPersonal);

	public boolean bajaPersonal(int id);

	public boolean modificarPersonal(TPersonal tPersonal);

	public TPersonal consultarPersonalPorId(int id);

	public TPersonal consultarPersonalPorIdEmpleado(int idEmpleado);

	public List<TPersonal> consultarPersonalExistente();

}
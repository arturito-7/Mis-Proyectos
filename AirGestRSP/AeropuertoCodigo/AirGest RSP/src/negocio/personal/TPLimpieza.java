package negocio.personal;

public class TPLimpieza extends TPersonal {

	private String rol;

	public String getRol() {
		return this.rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public TPLimpieza(int id, int idEmpleado, String areaAsignada, boolean activo, String rol) {
		super(id, activo, idEmpleado, areaAsignada);

		this.rol = rol;
	}

	@Override
	public String toString() {
		return super.toString() + "\n rol: " + rol;
	}
}
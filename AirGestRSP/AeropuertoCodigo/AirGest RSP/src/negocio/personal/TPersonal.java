package negocio.personal;

public class TPersonal {

	private int id;

	private int idEmpleado;

	private String areaAsignada;

	private boolean activo;

	public TPersonal() {
	}

	public TPersonal(int id, boolean activo, int idEmpleado, String areaAsignada) {
		this.id = id;
		this.idEmpleado = idEmpleado;
		this.areaAsignada = areaAsignada;
		this.activo = activo;
	}

	public int getId() {
		return this.id;
	}

	public boolean getActivo() {
		return this.activo;
	}

	public int getIdEmpleado() {
		return this.idEmpleado;
	}

	public String getAreaAsignada() {
		return this.areaAsignada;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setIdEmpleado(int idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public void setAreaAsignada(String areaAsignada) {
		this.areaAsignada = areaAsignada;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	@Override
	public String toString() {
		return " id: " + id + "\n idEmpleado: " + idEmpleado + "\n Área asignada: " + areaAsignada + "\n activo: "
				+ activo + "\n";
	}
}
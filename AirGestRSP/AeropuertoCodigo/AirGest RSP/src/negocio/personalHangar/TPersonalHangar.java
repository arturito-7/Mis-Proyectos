package negocio.personalHangar;

public class TPersonalHangar {

	private int idPersonal;

	private int idHangar;

	public int getIdHangar() {
		return this.idHangar;
	}

	public int getIdPersonal() {
		return this.idPersonal;
	}

	public void setIdHangar(int id) {
		this.idHangar = id;
	}

	public void setIdPersonal(int id) {
		this.idPersonal = id;
	}

	public TPersonalHangar() {
	}

	public TPersonalHangar(int idPersonal, int idHangar) {
		this.idPersonal = idPersonal;
		this.idHangar = idHangar;
	}
}
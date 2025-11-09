package integracion.personalHangar;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import integracion.UtilidadesI;

public class DAOPersonalHangarImp implements DAOPersonalHangar {

	@Override
	public boolean vincular(int idPersonal, int idHangar) {
		try {
			FileWriter archivo = new FileWriter(UtilidadesI.ruta("personalHangar") + String.format("%05d", idPersonal)
					+ "_" + String.format("%05d", idHangar) + ".json");
			archivo.write("{\"idPersonal\":" + idPersonal + ", \"idHangar\":" + idHangar + "}");
			archivo.close();
			return true;
		} catch (IOException e) {

		}

		return false;
	}

	@Override
	public boolean desvincular(int idPersonal, int idHangar) {
		File f = new File(UtilidadesI.ruta("personalHangar") + String.format("%05d", idPersonal) + "_"
				+ String.format("%05d", idHangar) + ".json");

		if (f.exists()) {
			return f.delete();
		}

		return false;
	}

	@Override
	public boolean comprobarVinculacion(int idPersonal, int idHangar) {
		File f = new File(UtilidadesI.ruta("personalHangar") + String.format("%05d", idPersonal) + "_"
				+ String.format("%05d", idHangar) + ".json");

		return f.exists();
	}

}
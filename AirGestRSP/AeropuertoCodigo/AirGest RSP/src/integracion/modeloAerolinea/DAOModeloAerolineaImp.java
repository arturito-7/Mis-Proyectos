package integracion.modeloAerolinea;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONObject;

import integracion.UtilidadesI;
import negocio.modeloAerolinea.TModeloAerolinea;

public class DAOModeloAerolineaImp implements DAOModeloAerolinea {

	public boolean vincular(int idModelo, int idAerolinea) {
		try {
			TModeloAerolinea tma = new TModeloAerolinea(idModelo, idAerolinea);
			FileWriter archivo = new FileWriter(UtilidadesI.ruta("modeloAerolinea") + String.format("%05d", idModelo)
					+ "_" + String.format("%05d", idAerolinea) + ".json");
			archivo.write(toJSON(tma).toString());
			archivo.close();
			return true;
		} catch (IOException e) {

		}

		return false;
	}

	public boolean desvincular(int idModelo, int idAerolinea) {
		File f = new File(UtilidadesI.ruta("modeloAerolinea") + String.format("%05d", idModelo) + "_"
				+ String.format("%05d", idAerolinea) + ".json");

		if (f.exists()) {
			return f.delete();
		}

		return false;
	}

	public boolean comprobarVinculacion(int idModelo, int idAerolinea) {
		File f = new File(UtilidadesI.ruta("modeloAerolinea") + String.format("%05d", idModelo) + "_"
				+ String.format("%05d", idAerolinea) + ".json");

		return f.exists();
	}


	private JSONObject toJSON(TModeloAerolinea tma) {
		JSONObject jo = new JSONObject();

		jo.put("idModelo", tma.getIdModelo());
		jo.put("idAerolinea", tma.getIdAerolinea());

		return jo;
	}
}
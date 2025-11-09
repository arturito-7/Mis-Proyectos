package integracion.aerolinea;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONTokener;

import integracion.UtilidadesI;
import negocio.aerolinea.TAerolinea;

public class DAOAerolineaImp implements DAOAerolinea {

	public TAerolinea leerAerolineaPorId(int idAerolinea) {
		try {
			JSONObject data = new JSONObject(new JSONTokener(
					new FileReader(UtilidadesI.ruta("aerolinea") + String.format("%05d", idAerolinea) + ".json")));
			return new TAerolinea(data.getInt("id"), data.getString("nombre"), data.getBoolean("activo"));
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	public TAerolinea leerAerolineaPorNombre(String nombre) {
		File carpeta = new File(UtilidadesI.ruta("aerolinea"));
		File[] lista = carpeta.listFiles();

		int i = 0;
		while (i < lista.length) {
			JSONObject data = new JSONObject();
			try {
				data = new JSONObject(new JSONTokener(new FileReader(lista[i])));
			} catch (FileNotFoundException e) {
			}

			if (data.getString("nombre").equals(nombre)) {
				return new TAerolinea(data.getInt("id"), data.getString("nombre"), data.getBoolean("activo"));
			}

			i++;
		}
		return null;
	}

	public int altaAerolinea(TAerolinea tAerolinea) {
		File carpeta = new File(UtilidadesI.ruta("aerolinea"));
		File[] lista = carpeta.listFiles();

		try {
			int id = lista.length + 1;

			tAerolinea.setId(id);

			FileWriter archivo = new FileWriter(UtilidadesI.ruta("aerolinea") + String.format("%05d", id) + ".json");

			archivo.write(this.toJSON(tAerolinea).toString());
			archivo.close();

			return id;

		} catch (IOException e) {
		}
		return -1;
	}

	public boolean modificarAerolinea(TAerolinea tAerolinea) {
		try {
			FileWriter archivo = new FileWriter(
					UtilidadesI.ruta("aerolinea") + String.format("%05d", tAerolinea.getId()) + ".json");
			archivo.write(this.toJSON(tAerolinea).toString());
			archivo.close();

			return true;
		} catch (IOException e) {
		}
		return false;
	}

	public boolean bajaAerolinea(int id) {
		try {
			JSONObject data = new JSONObject(new JSONTokener(
					new FileReader(UtilidadesI.ruta("aerolinea") + String.format("%05d", id) + ".json")));

			data.put("activo", false);

			FileWriter archivo = new FileWriter(UtilidadesI.ruta("aerolinea") + String.format("%05d", id) + ".json");

			archivo.write(data.toString());
			archivo.close();

			return true;
		} catch (IOException e) {
		}
		return false;
	}

	public List<TAerolinea> consultarTodasAerolineas() {
		File carpeta = new File(UtilidadesI.ruta("aerolinea"));
		File[] lista = carpeta.listFiles();

		List<TAerolinea> aerolineas = new ArrayList<>();

		for (File f : lista) {
			try {
				JSONObject data = new JSONObject(new JSONTokener(new FileReader(f)));
				aerolineas.add(new TAerolinea(data.getInt("id"), data.getString("nombre"), data.getBoolean("activo")));
			} catch (FileNotFoundException e) {
			}
		}

		return aerolineas;
	}

	private JSONObject toJSON(TAerolinea taerolinea) {
		JSONObject jo = new JSONObject();

		jo.put("id", taerolinea.getId());
		jo.put("nombre", taerolinea.getNombre());
		jo.put("activo", taerolinea.getActivo());

		return jo;
	}

}
package integracion.hangar;

import negocio.hangar.THangar;

import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;
import org.json.JSONTokener;

import integracion.UtilidadesI;

public class DAOHangarImp implements DAOHangar {

	public THangar leerHangarPorId(int id) {
		try {
			JSONObject data = new JSONObject(
					new JSONTokener(new FileReader(UtilidadesI.ruta("hangar") + String.format("%05d", id) + ".json")));
			return new THangar(data.getInt("id"), data.getString("direccion"), data.getInt("stock"),
					data.getFloat("costeDia"), data.getInt("espacioAlmacenaje"), data.getBoolean("activo"));
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	public boolean actualizarStock(int id, int stock) {
		try {
			JSONObject data = new JSONObject(
					new JSONTokener(new FileReader(UtilidadesI.ruta("hangar") + String.format("%05d", id) + ".json")));
			THangar tHangar = new THangar(data.getInt("id"), data.getString("direccion"), data.getInt("stock"),
					data.getFloat("costeDia"), data.getInt("espacioAlmacenaje"), data.getBoolean("activo"));
			tHangar.setStock(stock);

			FileWriter archivo = new FileWriter(UtilidadesI.ruta("hangar") + String.format("%05d", id) + ".json");

			archivo.write(toJSON(tHangar).toString());
			archivo.close();

			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public int altaHangar(THangar tHangar) {
		File carpeta = new File(UtilidadesI.ruta("hangar"));
		File[] lista = carpeta.listFiles();

		try {
			int id = lista.length + 1;

			tHangar.setId(id);

			FileWriter archivo = new FileWriter(UtilidadesI.ruta("hangar") + String.format("%05d", id) + ".json");

			archivo.write(toJSON(tHangar).toString());
			archivo.close();

			return id;

		} catch (IOException e) {
		}
		return -1;
	}

	public boolean bajaHangar(int id) {
		try {
			JSONObject data = new JSONObject(
					new JSONTokener(new FileReader(UtilidadesI.ruta("hangar") + String.format("%05d", id) + ".json")));

			data.put("activo", false);

			FileWriter archivo = new FileWriter(UtilidadesI.ruta("hangar") + String.format("%05d", id) + ".json");

			archivo.write(data.toString());
			archivo.close();

			return true;
		} catch (IOException e) {
		}
		return false;
	}

	public List<THangar> consultarTodosHangares() {
		File carpeta = new File(UtilidadesI.ruta("hangar"));
		File[] lista = carpeta.listFiles();

		List<THangar> hangares = new ArrayList<>();

		for (File f : lista) {
			try {
				JSONObject data = new JSONObject(new JSONTokener(new FileReader(f)));
				hangares.add(new THangar(data.getInt("id"), data.getString("direccion"), data.getInt("stock"),
						data.getFloat("costeDia"), data.getInt("espacioAlmacenaje"), data.getBoolean("activo")));
			} catch (FileNotFoundException e) {
			}
		}

		return hangares;
	}

	public boolean modificarHangar(THangar tHangar) {
		try {
			FileWriter archivo = new FileWriter(
					UtilidadesI.ruta("hangar") + String.format("%05d", tHangar.getId()) + ".json");
			archivo.write(toJSON(tHangar).toString());
			archivo.close();

			return true;
		} catch (IOException e) {
		}
		return false;
	}

	public THangar leerHangarPorDireccion(String direccion) {
		File carpeta = new File(UtilidadesI.ruta("hangar"));
		File[] lista = carpeta.listFiles();

		int i = 0;
		while (i < lista.length) {
			JSONObject data = new JSONObject();
			try {
				data = new JSONObject(new JSONTokener(new FileReader(lista[i])));
			} catch (FileNotFoundException e) {
			}

			if (data.getString("direccion").equals(direccion)) {
				return new THangar(data.getInt("id"), data.getString("direccion"), data.getInt("stock"),
						data.getFloat("costeDia"), data.getInt("espacioAlmacenaje"), data.getBoolean("activo"));
			}

			i++;
		}

		return null;
	}

	private JSONObject toJSON(THangar hangar) {
		JSONObject jo = new JSONObject();

		jo.put("id", hangar.getId());
		jo.put("direccion", hangar.getDireccion());
		jo.put("stock", hangar.getStock());
		jo.put("costeDia", hangar.getCosteDia());
		jo.put("espacioAlmacenaje", hangar.getEspacioAlmacenaje());
		jo.put("activo", hangar.getActivo());

		return jo;

	}

}
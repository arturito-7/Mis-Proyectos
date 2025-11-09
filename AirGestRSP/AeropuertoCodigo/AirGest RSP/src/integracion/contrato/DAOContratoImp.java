package integracion.contrato;

import negocio.contrato.TContrato;

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

public class DAOContratoImp implements DAOContrato {

	public int altaContrato(TContrato tContrato) {
		File carpeta = new File(UtilidadesI.ruta("contrato"));
		File[] lista = carpeta.listFiles();

		try {
			int id = lista.length + 1;

			tContrato.setId(id);

			FileWriter archivo = new FileWriter(UtilidadesI.ruta("contrato") + String.format("%05d", id) + ".json");

			archivo.write(toJSON(tContrato).toString());
			archivo.close();

			return id;

		} catch (IOException e) {
		}
		return -1;
	}

	public TContrato leerContratoPorId(int id) {
		try {
			JSONObject data = new JSONObject(new JSONTokener(
					new FileReader(UtilidadesI.ruta("contrato") + String.format("%05d", id) + ".json")));
			return new TContrato(data.getInt("id"), data.getInt("id_aerolinea"), data.getDouble("precio"));
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	public List<TContrato> leerTodosContratos() {
		File carpeta = new File(UtilidadesI.ruta("contrato"));
		File[] lista = carpeta.listFiles();

		List<TContrato> contratos = new ArrayList<>();

		for (File f : lista) {
			try {
				JSONObject data = new JSONObject(new JSONTokener(new FileReader(f)));
				contratos.add(new TContrato(data.getInt("id"), data.getInt("id_aerolinea"), data.getDouble("precio")));
			} catch (FileNotFoundException e) {
			}
		}

		return contratos;
	}

	public List<TContrato> leerContratosPorAerolinea(int id_aerolinea) {
		File carpeta = new File(UtilidadesI.ruta("contrato"));
		File[] lista = carpeta.listFiles();

		List<TContrato> contratos = new ArrayList<>();

		int i = 0;
		while (i < lista.length) {
			JSONObject data = new JSONObject();
			try {
				data = new JSONObject(new JSONTokener(new FileReader(lista[i])));
			} catch (FileNotFoundException e) {
			}

			if (data.getInt("id_aerolinea") == id_aerolinea) {
				contratos.add(new TContrato(data.getInt("id"), data.getInt("id_aerolinea"), data.getDouble("precio")));
			}

			i++;
		}

		return contratos;
	}

	public boolean modificarContrato(TContrato tContrato) {
		try {
			FileWriter archivo = new FileWriter(
					UtilidadesI.ruta("contrato") + String.format("%05d", tContrato.getId()) + ".json");
			archivo.write(toJSON(tContrato).toString());
			archivo.close();

			return true;
		} catch (IOException e) {
		}
		return false;
	}

	private JSONObject toJSON(TContrato tContrato) {
		JSONObject jo = new JSONObject();

		jo.put("id", tContrato.getId());
		jo.put("id_aerolinea", tContrato.getIdAerolinea());
		jo.put("precio", tContrato.getPrecio());

		return jo;
	}

}
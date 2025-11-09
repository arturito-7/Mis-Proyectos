package integracion.lineaContrato;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONTokener;

import integracion.UtilidadesI;
import negocio.lineaContrato.TLineaContrato;

public class DAOLineaContratoImp implements DAOLineaContrato {

	@Override
	public boolean altaLineaContrato(TLineaContrato tLineaContrato) {
		try {
			FileWriter archivo = new FileWriter(
					UtilidadesI.ruta("lineaContrato") + String.format("%05d", tLineaContrato.getIdContrato()) + "_"
							+ String.format("%05d", tLineaContrato.getIdHangar()) + ".json");
			archivo.write(toJSON(tLineaContrato).toString());
			archivo.close();
			return true;
		} catch (IOException e) {

		}

		return false;
	}

	@Override
	public boolean modificarLineaContrato(TLineaContrato tLineaContrato) {
		try {
			FileWriter archivo = new FileWriter(
					UtilidadesI.ruta("lineaContrato") + String.format("%05d", tLineaContrato.getIdContrato()) + "_"
							+ String.format("%05d", tLineaContrato.getIdHangar()) + ".json");
			archivo.write(toJSON(tLineaContrato).toString());
			archivo.close();

			return true;
		} catch (IOException e) {
		}
		return false;
	}

	@Override
	public List<TLineaContrato> leerLineasPorContrato(int id_contrato) {
		File carpeta = new File(UtilidadesI.ruta("lineaContrato"));
		File[] lista = carpeta.listFiles();

		List<TLineaContrato> lineas = new ArrayList<>();

		int i = 0;
		while (i < lista.length) {
			JSONObject data = new JSONObject();
			try {
				data = new JSONObject(new JSONTokener(new FileReader(lista[i])));
			} catch (FileNotFoundException e) {
			}

			if (data.getInt("id_contrato") == id_contrato) {
				JSONObject fi = data.getJSONObject("fecha_ini");
				LocalDate fecha_ini = LocalDate.of(fi.getInt("anyo"), fi.getInt("mes"), fi.getInt("dia"));

				JSONObject ff = data.getJSONObject("fecha_fin");
				LocalDate fecha_fin = LocalDate.of(ff.getInt("anyo"), ff.getInt("mes"), ff.getInt("dia"));

				lineas.add(new TLineaContrato(data.getInt("id_contrato"), data.getInt("id_hangar"), fecha_ini,
						fecha_fin, data.getDouble("precio")));
			}

			i++;
		}

		return lineas;
	}

	@Override
	public List<TLineaContrato> leerLineasPorHangar(int id_hangar) {
		File carpeta = new File(UtilidadesI.ruta("lineaContrato"));
		File[] lista = carpeta.listFiles();

		List<TLineaContrato> lineas = new ArrayList<>();

		int i = 0;
		while (i < lista.length) {
			JSONObject data = new JSONObject();
			try {
				data = new JSONObject(new JSONTokener(new FileReader(lista[i])));
			} catch (FileNotFoundException e) {
			}

			if (data.getInt("id_hangar") == id_hangar) {
				JSONObject fi = data.getJSONObject("fecha_ini");
				LocalDate fecha_ini = LocalDate.of(fi.getInt("anyo"), fi.getInt("mes"), fi.getInt("dia"));

				JSONObject ff = data.getJSONObject("fecha_fin");
				LocalDate fecha_fin = LocalDate.of(ff.getInt("anyo"), ff.getInt("mes"), ff.getInt("dia"));

				lineas.add(new TLineaContrato(data.getInt("id_contrato"), data.getInt("id_hangar"), fecha_ini,
						fecha_fin, data.getDouble("precio")));
			}

			i++;
		}

		return lineas;
	}

	@Override
	public boolean leerLineaContrato(int id_contrato, int id_hangar) {
		File f = new File(UtilidadesI.ruta("lineaContrato") + String.format("%05d", id_contrato) + "_"
				+ String.format("%05d", id_hangar) + ".json");

		return f.exists();
	}

	private JSONObject toJSON(TLineaContrato linea) {
		JSONObject jo = new JSONObject();

		jo.put("id_contrato", linea.getIdContrato());
		jo.put("id_hangar", linea.getIdHangar());

		JSONObject fecha_ini = new JSONObject();
		fecha_ini.put("dia", linea.getFechaIni().getDayOfMonth());
		fecha_ini.put("mes", linea.getFechaIni().getMonthValue());
		fecha_ini.put("anyo", linea.getFechaIni().getYear());
		jo.put("fecha_ini", fecha_ini);

		JSONObject fecha_fin = new JSONObject();
		fecha_fin.put("dia", linea.getFechaFin().getDayOfMonth());
		fecha_fin.put("mes", linea.getFechaFin().getMonthValue());
		fecha_fin.put("anyo", linea.getFechaFin().getYear());
		jo.put("fecha_fin", fecha_fin);

		jo.put("precio", linea.getPrecio());

		return jo;

	}

}
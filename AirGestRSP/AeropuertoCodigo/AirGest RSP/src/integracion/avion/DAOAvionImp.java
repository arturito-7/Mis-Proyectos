package integracion.avion;

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
import negocio.avion.TAComercial;
import negocio.avion.TAPrivado;
import negocio.avion.TAvion;

public class DAOAvionImp implements DAOAvion {

	private JSONObject write(TAvion tAvion) {
		JSONObject jo = new JSONObject();

		JSONObject fecha = new JSONObject();
		fecha.put("dia", tAvion.getFechaFabricacion().getDayOfMonth());
		fecha.put("mes", tAvion.getFechaFabricacion().getMonthValue());
		fecha.put("anyo", tAvion.getFechaFabricacion().getYear());

		jo.put("id", tAvion.getId());
		jo.put("asientos", tAvion.getNumAsientos());
		jo.put("fechaFabricacion", fecha);
		jo.put("nombre", tAvion.getNombre());
		jo.put("matricula", tAvion.getMatricula());
		jo.put("activo", tAvion.getActivo());
		jo.put("idModelo", tAvion.getIdModelo());
		jo.put("idAerolinea", tAvion.getIdAerolinea());
		jo.put("idHangar", tAvion.getIdHangar());

		if (tAvion instanceof TAComercial)
			jo.put("trabajadores", ((TAComercial) tAvion).getTrabajadores());
		else {
			jo.put("idCarnet", ((TAPrivado) tAvion).getIdCarnet());
			jo.put("dueño", ((TAPrivado) tAvion).getNombreDuenyo());
		}

		return jo;
	}

	private TAvion read(JSONObject data) {
		TAvion avion;

		JSONObject jo = data.getJSONObject("fechaFabricacion");

		LocalDate fecha = LocalDate.of(jo.getInt("anyo"), jo.getInt("mes"), jo.getInt("dia"));
		int id = data.getInt("id");
		int asientos = data.getInt("asientos");
		String nombre = data.getString("nombre");
		String matricula = data.getString("matricula");
		Boolean activo = data.getBoolean("activo");
		int idAerolinea = data.getInt("idAerolinea");
		int idModelo = data.getInt("idModelo");
		int idHangar = data.getInt("idHangar");

		if (data.has("dueño"))
			avion = new TAPrivado(id, asientos, fecha, nombre, matricula, activo, idAerolinea, idModelo, idHangar,
					data.getString("dueño"), data.getInt("idCarnet"));
		else
			avion = new TAComercial(id, asientos, fecha, nombre, matricula, activo, idAerolinea, idModelo, idHangar,
					data.getInt("trabajadores"));

		return avion;
	}

	public List<TAvion> consultarAvionesPorModelo(int idModelo) {
		File carpeta = new File(UtilidadesI.ruta("avion"));
		File[] lista = carpeta.listFiles();

		List<TAvion> aviones = new ArrayList<>();

		for (File f : lista) {
			try {
				JSONObject data = new JSONObject(new JSONTokener(new FileReader(f)));
				if (data.getInt("idModelo") == idModelo)
					aviones.add(this.read(data));

			} catch (FileNotFoundException e) {
			}
		}

		return aviones;
	}

	public List<TAvion> consultarAvionesActivosPorModelo(int idModelo) {
		File carpeta = new File(UtilidadesI.ruta("avion"));
		File[] lista = carpeta.listFiles();

		List<TAvion> aviones = new ArrayList<>();

		for (File f : lista) {
			try {
				JSONObject data = new JSONObject(new JSONTokener(new FileReader(f)));
				if (data.getInt("idModelo") == idModelo && data.getBoolean("activo"))
					aviones.add(this.read(data));

			} catch (FileNotFoundException e) {
			}
		}

		return aviones;
	}

	@Override
	public int altaAvion(TAvion tAvion) {
		File carpeta = new File(UtilidadesI.ruta("avion"));
		File[] lista = carpeta.listFiles();

		try {
			int id = lista.length + 1;

			tAvion.setId(id);

			FileWriter archivo = new FileWriter(UtilidadesI.ruta("avion") + String.format("%05d", id) + ".json");

			archivo.write(this.write(tAvion).toString());
			archivo.close();

			return id;

		} catch (IOException e) {
			return -1;
		}
	}

	@Override
	public boolean bajaAvion(int id) {
		try {
			JSONObject data = new JSONObject(
					new JSONTokener(new FileReader(UtilidadesI.ruta("avion") + String.format("%05d", id) + ".json")));

			data.put("activo", false);

			FileWriter archivo = new FileWriter(UtilidadesI.ruta("avion") + String.format("%05d", id) + ".json");

			archivo.write(data.toString());
			archivo.close();

			return true;
		} catch (IOException e) {
		}
		return false;
	}

	@Override
	public TAvion consultarAvionPorId(int id) {
		try {
			JSONObject data = new JSONObject(
					new JSONTokener(new FileReader(UtilidadesI.ruta("avion") + String.format("%05d", id) + ".json")));
			return this.read(data);
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	@Override
	public List<TAvion> consultarTodosAviones() {
		File carpeta = new File(UtilidadesI.ruta("avion"));
		File[] lista = carpeta.listFiles();

		List<TAvion> aviones = new ArrayList<>();

		for (File f : lista) {
			try {
				JSONObject data = new JSONObject(new JSONTokener(new FileReader(f)));

				aviones.add(this.read(data));

			} catch (FileNotFoundException e) {
			}
		}

		return aviones;
	}

	@Override
	public boolean modificarAvion(TAvion tAvion) {
		try {
			FileWriter archivo = new FileWriter(
					UtilidadesI.ruta("avion") + String.format("%05d", tAvion.getId()) + ".json");
			archivo.write(this.write(tAvion).toString());
			archivo.close();

			return true;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public List<TAvion> consultarAvionesPorAerolinea(int idAerolinea) {
		File carpeta = new File(UtilidadesI.ruta("avion"));
		File[] lista = carpeta.listFiles();

		List<TAvion> aviones = new ArrayList<>();

		for (File f : lista) {
			try {
				JSONObject data = new JSONObject(new JSONTokener(new FileReader(f)));
				if (data.getInt("idAerolinea") == idAerolinea)
					aviones.add(this.read(data));

			} catch (FileNotFoundException e) {
			}
		}

		return aviones;
	}

	@Override
	public List<TAvion> consultarAvionesActivosPorAerolinea(int idAerolinea) {
		File carpeta = new File(UtilidadesI.ruta("avion"));
		File[] lista = carpeta.listFiles();

		List<TAvion> aviones = new ArrayList<>();

		for (File f : lista) {
			try {
				JSONObject data = new JSONObject(new JSONTokener(new FileReader(f)));
				if (data.getInt("idAerolinea") == idAerolinea && data.getBoolean("activo"))
					aviones.add(this.read(data));

			} catch (FileNotFoundException e) {
			}
		}

		return aviones;
	}

	@Override
	public List<TAvion> consultarAvionesPorHangar(int idHangar) {
		File carpeta = new File(UtilidadesI.ruta("avion"));
		File[] lista = carpeta.listFiles();

		List<TAvion> aviones = new ArrayList<>();

		for (File f : lista) {
			try {
				JSONObject data = new JSONObject(new JSONTokener(new FileReader(f)));
				if (data.getInt("idHangar") == idHangar)
					aviones.add(this.read(data));

			} catch (FileNotFoundException e) {
			}
		}

		return aviones;
	}

	@Override
	public List<TAvion> consultarAvionesActivosPorHangar(int idHangar) {
		File carpeta = new File(UtilidadesI.ruta("avion"));
		File[] lista = carpeta.listFiles();

		List<TAvion> aviones = new ArrayList<>();

		for (File f : lista) {
			try {
				JSONObject data = new JSONObject(new JSONTokener(new FileReader(f)));
				if (data.getInt("idHangar") == idHangar && data.getBoolean("activo"))
					aviones.add(this.read(data));

			} catch (FileNotFoundException e) {
			}
		}

		return aviones;
	}

	@Override
	public TAvion consultarAvionesPorMatricula(String matricula) {
		File carpeta = new File(UtilidadesI.ruta("avion"));
		File[] lista = carpeta.listFiles();

		int i = 0;
		while (i < lista.length) {
			JSONObject data = new JSONObject();
			try {
				data = new JSONObject(new JSONTokener(new FileReader(lista[i])));
			} catch (FileNotFoundException e) {
			}

			if (data.getString("matricula").equals(matricula)) {
				return this.read(data);
			}

			i++;
		}

		return null;

	}

}
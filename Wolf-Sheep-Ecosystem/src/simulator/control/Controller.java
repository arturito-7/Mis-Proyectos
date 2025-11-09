package simulator.control;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.Simulator;
import simulator.view.SimpleObjectViewer;
import simulator.view.SimpleObjectViewer.ObjInfo;

public class Controller {
	
	private Simulator _sim;
	
	public Controller(Simulator sim) {
		this._sim = sim;
	}
	
	public void load_data(JSONObject data) {
		
		JSONArray lista_JSONAnimals = data.getJSONArray("animals");
		
		load_data_regions(data);
        
        int j = 0;
        while (j < lista_JSONAnimals.length()) {
        	JSONObject obj = lista_JSONAnimals.getJSONObject(j);
        	int N = obj.getInt("amount");
        	JSONObject o = obj.getJSONObject("spec");
        	for(int k = 0; k < N; k++) {
        		this._sim.add_animal(o);
        	}
        	j++;
        }
		
	}
	
	private void load_data_regions(JSONObject data) {
		if(data.has("regions")) {
			JSONArray lista_JSONRegions = data.getJSONArray("regions");
			int i = 0;
	        while (i < lista_JSONRegions.length()) {
	        	
	        	JSONObject obj = lista_JSONRegions.getJSONObject(i);
	        	
	        	JSONArray row = obj.getJSONArray("row");
	        	int rf = row.getInt(0);
	        	int rt = row.getInt(1);
	        	
	        	JSONArray col = obj.getJSONArray("col");
	        	int cf = col.getInt(0);
	        	int ct = col.getInt(1);
	        	
	        	JSONObject o = obj.getJSONObject("spec");
	        	
	        	for(int k = rf; k <= rt; k++) {
	        		for(int j = cf; j <= ct; j++) {
	                	this._sim.set_region(k, j, o);
	        		}
	        	}
	        	i++;
	        }
		}
	}
	
	private List<ObjInfo> to_animals_info(List<? extends AnimalInfo> animals) { 
		List<ObjInfo> ol = new ArrayList<>(animals.size()); 
		for (AnimalInfo a : animals) 
			ol.add(new ObjInfo(a.get_genetic_code(), (int) a.get_position().getX(), (int) a.get_position().getY(),
					(int)Math.round(a.get_age())+2)); 
		return ol; 
	}
	
	public void run(double t, double dt, boolean sv, OutputStream out) {
		
		SimpleObjectViewer view = null; 
		if (sv) { 
			MapInfo m = this._sim.get_map_info();
			view = new SimpleObjectViewer("[ECOSYSTEM]", m.get_width(), m.get_height(), m.get_cols(), m.get_rows());
			view.update(to_animals_info(this._sim.get_animals()), this._sim.get_time(), dt); 
		}
		
		JSONObject o = new JSONObject();
		o.put("in", this._sim.as_JSON());
		
		while(this._sim.get_time() < t) {
			this._sim.advance(dt);
			if (sv) view.update(to_animals_info(this._sim.get_animals()), this._sim.get_time(), dt);
		}
		
		o.put("out", this._sim.as_JSON());
		
		PrintStream p = new PrintStream(out); 
		p.println(o.toString());
		
		if (sv) view.close();
	}
	
	public void reset(int cols, int rows, int width, int height) {
		this._sim.reset(cols, rows, width, height);
	}
	
	public void set_regions(JSONObject rs) {
		this.load_data_regions(rs);
	}
	
	public void advance(double dt) {
		this._sim.advance(dt);
	}
	
	public void addObserver(EcoSysObserver o) {
		this._sim.addObserver(o);
	}
	
	public void removeObserver(EcoSysObserver o) {
		this._sim.removeObserver(o);
	}
	
}

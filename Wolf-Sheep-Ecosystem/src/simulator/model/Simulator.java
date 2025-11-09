package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import simulator.factories.Factory;

public class Simulator implements JSONable, Observable<EcoSysObserver>{
	
	private Factory<Animal> _animals_factory;
	private Factory<Region> _regions_factory;
	private RegionManager _manager;
	private List<Animal> _animals;
	private double _tiempo_actual;
	private List<EcoSysObserver> _observers;

	public Simulator(int cols, int rows, int width, int height, Factory<Animal> animals_factory, Factory<Region> regions_factory) {
		
		this._animals_factory = animals_factory;
		this._regions_factory = regions_factory;
		this._manager = new RegionManager(cols, rows, width, height);
		this._animals = new ArrayList<> ();
		this._observers = new ArrayList<>();
		this._tiempo_actual = 0;
		
	}
	
	private void set_region(int row, int col, Region r) {
		this._manager.set_region(row, col, r);
		for(EcoSysObserver o : this._observers) {
			o.onRegionSet(row, col, _manager, r);
		}
	}
	
	public void set_region(int row, int col, JSONObject r_json) {
		Region r = this._regions_factory.create_instance(r_json);
		set_region(row, col, r);
	}
	
	private void add_animal(Animal a) {
		this._manager.register_animal(a);
		this._animals.add(a);
		for(EcoSysObserver o : this._observers) {
			o.onAnimalAdded(_tiempo_actual, _manager, new ArrayList<>(this._animals), a);
		}
	}
	
	public void add_animal(JSONObject a_json) {
		Animal A = this._animals_factory.create_instance(a_json);
		this.add_animal(A);
	}
	
	public MapInfo get_map_info() {
		return this._manager;
	}
	
	public List<? extends AnimalInfo> get_animals(){
		return Collections.unmodifiableList(this._animals);
	}

	public double get_time() {
		return this._tiempo_actual;
	}
	
	public void advance(double dt) {
		
		this._tiempo_actual += dt;

		List<Animal> aux = new ArrayList<>();
		for(Animal a: this._animals) {
			if(a.get_state() == State.DEAD) {
				aux.add(a);
				this._manager.unregister_animal(a);
			}
		}
		
		this._animals.removeAll(aux);

		for(Animal a: this._animals) {
			a.update(dt);
			this._manager.update_animal_region(a);
		}
		
		this._manager.update_all_regions(dt);
		
		List<Animal> aux2 = new ArrayList<>();
		for(Animal a: this._animals) {
			if(a.is_pregnant()) {
				aux2.add(a.deliver_baby());
			}
		}
		
		for(Animal a: aux2) {
			this.add_animal(a);
		}
		
		for(EcoSysObserver o : this._observers) {
			o.onAvanced(_tiempo_actual, _manager, new ArrayList<>(this._animals), dt);
		}
		
	}
	
	public void reset(int cols, int rows, int width, int height) {
		this._animals = new ArrayList<> ();
		this._manager = new RegionManager(cols, rows, width, height);
		this._tiempo_actual = 0;
		for(EcoSysObserver o : this._observers) {
			o.onReset(_tiempo_actual, _manager, new ArrayList<>(this._animals));
		}
	}
	
	public JSONObject as_JSON() {
		
		JSONObject o = new JSONObject();
		
		o.put("time", this._tiempo_actual);
		o.put("state", this._manager.as_JSON());
		
		return o;
		
	}

	@Override
	public void addObserver(EcoSysObserver o) {
		if(!this._observers.contains(o)) {
			this._observers.add(o);
			o.onRegister(_tiempo_actual, _manager, new ArrayList<>(this._animals));
		}
	}

	@Override
	public void removeObserver(EcoSysObserver o) {
		if(this._observers.contains(o)) {
			this._observers.remove(o);
		}
	}
	
}
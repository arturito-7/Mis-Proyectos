package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Region implements Entity, FoodSupplier, RegionInfo{

	protected final static double CTE_FOOD = 60.0;
	protected final static double CTE2_FOOD = 5.0;
	protected final static double CTE3_FOOD = 2.0;
	
	protected List<Animal> _animales;
	private int _herviboros_cont;
	
	public Region() {
		this._animales = new ArrayList<> ();
		this._herviboros_cont = 0;
	}
	
	public abstract String toString();
	
	final void add_animal(Animal a) {
		this._animales.add(a);
		if(a.get_diet() == Diet.HERBIVORE) this._herviboros_cont++;
	}
	
	final void remove_animal(Animal a) {
		this._animales.remove(a);
		if(a.get_diet() == Diet.HERBIVORE) this._herviboros_cont--;
	}
	
	final List<Animal> getAnimals(){
		return Collections.unmodifiableList(this._animales);
	}
	
	public List<AnimalInfo> getAnimalsInfo(){
		return new ArrayList<>(this._animales);
	}
	
	public JSONObject as_JSON() {
		
		JSONObject o = new JSONObject();
		JSONArray arr = new JSONArray();
		
		for(Animal a: this._animales) {
			arr.put(a);
		}
		
		o.put("animals", arr);
		
		return o;
		
	}
	
	public int get_herviboros_cont() {
		return this._herviboros_cont;
	}
	
}

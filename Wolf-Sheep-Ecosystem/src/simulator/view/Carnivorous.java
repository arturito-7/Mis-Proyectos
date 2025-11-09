package simulator.view;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.Diet;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.MapInfo.RegionData;
import simulator.model.RegionInfo;

public class Carnivorous implements EcoSysObserver{

	private Controller _ctrl;
	private Map<RegionData, Integer> valores;
	
	public Carnivorous(Controller ctrl) {
		this._ctrl = ctrl;
		this.valores = new TreeMap<MapInfo.RegionData, Integer>();
		this._ctrl.addObserver(this);
	}
	
	public void mostrar() {
		for(RegionData v : this.valores.keySet()) {
			System.out.println("Row: " + v.row() + ", col: " + v.col() + " nº de pasos --> " + this.valores.get(v));
		}
	}
	
	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		this.valores = new TreeMap<MapInfo.RegionData, Integer>();
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		
		int cont;
		
		for(RegionData r : map) {
			cont = 0;
			for(AnimalInfo a: r.r().getAnimalsInfo()) {
				if (a.get_diet() == Diet.CARNIVORE) {
					cont++;
				}
			}
			
			if (cont > 3) {
				if (!valores.containsKey(r)) {
					valores.put(r, 1);
				}else {
					int val = valores.get(r);
					valores.replace(r, val + 1);
				}
			}
		}	
	}
	
}

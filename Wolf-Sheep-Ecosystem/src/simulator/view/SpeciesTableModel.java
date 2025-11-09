package simulator.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;
import simulator.model.State;

class SpeciesTableModel extends AbstractTableModel implements EcoSysObserver {
	
	private static final long serialVersionUID = 1L;
	private Controller ctrl;
	private String[] columnNames;
	private Map<String,Map<State,Integer>> valores;
	
	
	SpeciesTableModel(Controller ctrl) {
		
		this.ctrl = ctrl;
		
		this.columnNames = new String[State.values().length + 1];
		this.columnNames[0] = "Species";
		int i = 1;
		for(State s : State.values()) {
			this.columnNames[i] = s.name();
			i++;
		}
		
		valores = new HashMap<String, Map<State,Integer>>();
		
		this.ctrl.addObserver(this);
		
	}

	@Override
	public int getRowCount() {
		return valores.size();
	} 

	@Override
	public int getColumnCount() {
		return columnNames.length;
	} 

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		Set<String> claves = this.valores.keySet();
		List<String> especies = new ArrayList<>(claves);
		
		if (columnIndex == 0)
			return especies.get(rowIndex);
		else {
			Integer val = valores.get(especies.get(rowIndex)).get(State.values()[columnIndex - 1]);
			if (val == null)
				return 0;
			else 
				return val;
		}		
	}
	
	@Override
	public String getColumnName(int idx) {
		return this.columnNames[idx];
	}
	
	private void añadir_animal(AnimalInfo a) {
		if (valores.containsKey(a.get_genetic_code())) {
        	Map<State,Integer> mapa = valores.get(a.get_genetic_code());
        	if(mapa.containsKey(a.get_state())) {
        		int value = mapa.get(a.get_state());
        		mapa.replace(a.get_state(), value + 1);
        	}else {
        		mapa.put(a.get_state(), 1);
        	}
        }else {
        	Map<State,Integer> nuevo = new HashMap<>();
        	nuevo.put(a.get_state(), 1);
        	valores.put(a.get_genetic_code(), nuevo);
        }
        fireTableStructureChanged();
	}
	
	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		for(AnimalInfo a: animals) añadir_animal(a);
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		valores = new HashMap<String, Map<State,Integer>>();
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
	    añadir_animal(a);        
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		valores = new HashMap<String, Map<State,Integer>>();
		for(AnimalInfo a: animals) añadir_animal(a);	
		fireTableStructureChanged();
	}
}
package simulator.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.Diet;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.MapInfo.RegionData;
import simulator.model.RegionInfo;

class RegionsTableModel extends AbstractTableModel implements EcoSysObserver {
	
	private static final long serialVersionUID = 1L;
	private Controller ctrl;
	private String[] columnNames;
	private Map<RegionData, Map<Diet, Integer>> dietas;
	private List<RegionData> misregiones;
	
	RegionsTableModel(Controller ctrl) {
		this.ctrl = ctrl;
		
		this.columnNames = new String[Diet.values().length + 3];
		this.columnNames[0] = "Row";
		this.columnNames[1] = "Col";
		this.columnNames[2] = "Desc.";
		int i = 3;
		for(Diet d : Diet.values()) {
			this.columnNames[i] = d.toString();
			i++;
		}
		
		this.dietas = new HashMap<>();
		
		this.misregiones = new ArrayList<>();
		
		this.ctrl.addObserver(this);
	}

	@Override
	public int getRowCount() {
		return this.misregiones.size();
	} 

	@Override
	public int getColumnCount() {
		return this.columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(columnIndex == 0)
			return this.misregiones.get(rowIndex).row();
		else if (columnIndex == 1)
			return this.misregiones.get(rowIndex).col();
		else if (columnIndex == 2) 
			return this.misregiones.get(rowIndex).r();
		else {
			if (this.dietas.get(this.misregiones.get(rowIndex)) == null) 
				return 0;
			else if (this.dietas.get(this.misregiones.get(rowIndex)).get(Diet.values()[columnIndex - 3]) == null)
				return 0;
			else
				return this.dietas.get(this.misregiones.get(rowIndex)).get(Diet.values()[columnIndex - 3]);
		}
	}
	
	@Override
	public String getColumnName(int idx) {
		return this.columnNames[idx];
	}
	
	private void reset() {
		this.misregiones = new ArrayList<>();
		this.dietas = new HashMap<>();
	}
	
	private void inicializar_regiones(MapInfo map) {
		if(this.misregiones.isEmpty()) {
			this.misregiones = new ArrayList<>();
			for(RegionData r: map) {
				this.misregiones.add(r);
			}
		}
	}
	
	private void llenar(MapInfo map, List<AnimalInfo> animals) {
		
		for(RegionData r: map) {
			this.misregiones.add(r);
			for(AnimalInfo a: r.r().getAnimalsInfo()) {
				if (dietas.containsKey(r)) {
		        	Map<Diet,Integer> mapa = dietas.get(r);
		        	if(mapa.containsKey(a.get_diet())){
		        		int value = mapa.get(a.get_diet());
		        		mapa.replace(a.get_diet(), value + 1);
		        	}else {
		        		mapa.put(a.get_diet(), 1);
		        	}
		        }else {
		        	Map<Diet,Integer> nuevo = new HashMap<>();
		        	nuevo.put(a.get_diet(), 1);
		        	dietas.put(r, nuevo);
		        }
			}			
		}
		
		fireTableStructureChanged();
		
	}
	
	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		llenar(map,animals);		
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		reset();
		fireTableStructureChanged();
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		
		inicializar_regiones(map);
		
		int row = (int)a.get_position().getY() / map.get_region_height();
		int col = (int)a.get_position().getX() / map.get_region_width();

		RegionData aux = this.misregiones.get(row*map.get_cols() + col);
		
		if(this.dietas.containsKey(aux)) {
			if(this.dietas.get(aux).containsKey(a.get_diet())) {
				int n = this.dietas.get(aux).get(a.get_diet());
				this.dietas.get(aux).replace(a.get_diet(), n + 1);
			}else {
				this.dietas.get(aux).put(a.get_diet(), 1);
			}
		}else {
			Map<Diet, Integer> nuevo = new HashMap<>();
			nuevo.put(a.get_diet(), 1);
			this.dietas.put(aux, nuevo);
		}
		
		fireTableStructureChanged();
		
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		
		inicializar_regiones(map);
		
		RegionData reg = new RegionData(row,col,r);
		
		RegionData aux = this.misregiones.set(row*map.get_cols() + col, reg);
		
		this.dietas.remove(aux);
		Map<Diet,Integer> mapa = new HashMap<>();
		for(AnimalInfo a: r.getAnimalsInfo()) {
			if(mapa.containsKey(a.get_diet())) {
				int value = mapa.get(a.get_diet());
				mapa.replace(a.get_diet(), value + 1);
			}else {
				mapa.put(a.get_diet(), 1);
			}
		}
		this.dietas.put(reg, mapa);
		
		fireTableStructureChanged();
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		reset();
		llenar(map,animals);
	}
	
}
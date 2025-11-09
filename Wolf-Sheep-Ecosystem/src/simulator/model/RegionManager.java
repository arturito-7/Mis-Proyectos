package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import org.json.JSONArray;
import org.json.JSONObject;

public class RegionManager implements AnimalMapView{
	
	private int _width;
	private int _height;
	private int _cols;
	private int _rows;
	private int anchura_region;
	private int altura_region;
	private Region[][] _regions;
	private Map<Animal, Region> _animal_region;
	
	public RegionManager (int cols, int rows, int width, int height) {
		
		this._width = width;
		this._height = height;
		this._cols = cols;
		this._rows = rows;
		if ( _width % _cols != 0 || _height % _rows != 0) throw new IllegalArgumentException("Parameters are not valid");
		this.anchura_region = this._width / this._cols;
		this.altura_region = this._height / this._rows;
		this._regions = new Region[_rows][_cols];
		initializeRegions();
		this._animal_region = new HashMap<Animal, Region>();
		
	}
	
	private void initializeRegions() {
		for(int i = 0; i < this._rows; i++) {
			for(int j = 0; j < this._cols; j++) {
				this._regions[i][j] = new DefaultRegion();
			}
		}
	}

	@Override
	public int get_cols() {
		return this._cols;
	}

	@Override
	public int get_rows() {
		return this._rows;
	}

	@Override
	public int get_width() {
		return this._width;
	}

	@Override
	public int get_height() {
		return this._height;
	}

	@Override
	public int get_region_width() {
		return this.anchura_region;
	}

	@Override
	public int get_region_height() {
		return this.altura_region;
	}

	@Override
	public double get_food(Animal a, double dt) {
		return this._animal_region.get(a).get_food(a, dt);
	}

	@Override
	public List<Animal> get_animals_in_range(Animal e, Predicate<Animal> filter) {
		List<Animal> aux = new ArrayList<>();
		
		for(int i = 0; i < this._rows; i++) {
			for(int j = 0; j < this._cols; j++) {
				for(Animal a: this._regions[i][j].getAnimals()) {
					if(a != e && filter.test(a) && a.get_position().distanceTo(e.get_position()) <= e.get_sight_range()) {
						aux.add(a);
					}
				}
			}
		}
		
		return aux;
	}

	void set_region(int row, int col, Region r) {
		
		for(Animal a: this._regions[row][col].getAnimals()) {
			r.add_animal(a);
			this._animal_region.put(a, r);
		}
		this._regions[row][col] = r;

	}
	
	void register_animal(Animal a) {
		
		a.init(this);
		
		int row = (int)a.get_position().getY() / this.altura_region;
		int col = (int)a.get_position().getX() / this.anchura_region;
		
		this._regions[row][col].add_animal(a);
		
		this._animal_region.put(a, this._regions[row][col]);
		
		
	}
	
	void unregister_animal(Animal a) {
		
		this._animal_region.get(a).remove_animal(a);
		
		this._animal_region.remove(a);
		
	}
	
	void update_animal_region(Animal a) {
		
		int row = (int)a.get_position().getY() / this.altura_region;
		int col = (int)a.get_position().getX() / this.anchura_region;
		
		if(this._animal_region.get(a) != this._regions[row][col]) {
			
			this._regions[row][col].add_animal(a);
			this._animal_region.get(a).remove_animal(a);
			this._animal_region.replace(a, this._regions[row][col]);
			
		}
	}
	
	void update_all_regions(double dt) {
		for(int i = 0; i < this._rows; i++) {
			for (int j = 0; j < this._cols; j++) {
				this._regions[i][j].update(dt);
			}
		}
	}
	
	public JSONObject as_JSON() {
		
		JSONObject o = new JSONObject();
		JSONArray arr = new JSONArray();
		JSONObject obj = new JSONObject();
		
		for(int i =  0; i < this._rows; i++) {
			for(int j = 0; j < this._cols; j++) {
				obj.put("row", i);
				obj.put("col", j);
				obj.put("data", this._regions[i][j].as_JSON());
				arr.put(obj);
			}
		}
		
		o.put("regiones", arr);
		
		return o;
		
	}

	@Override
	public Iterator<RegionData> iterator() {
		
		return new Iterator<MapInfo.RegionData>() {
			
			private int currentRow = 0;
	        private int currentCol = 0;
			
			@Override
			public RegionData next() {
				if(hasNext()) {
					RegionData r = new RegionData(currentRow, currentCol, _regions[currentRow][currentCol]);
					
					currentCol++;
					if (currentCol == _cols) {
						currentCol = 0;
						currentRow++;
					}
					
					return r;
				}else {
					throw new IllegalStateException("Has llegado al final");
				}
			}
			
			@Override
			public boolean hasNext() {
				return currentRow < _rows && currentCol < _cols;
			}
		};
	}

}

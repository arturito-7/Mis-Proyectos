package simulator.factories;

import org.json.JSONObject;

import simulator.model.DynamicSupplyRegion;
import simulator.model.Region;

public class DynamicSupplyRegionBuilder extends Builder<Region>{
	
	private static final double FACTOR = 2.0;
	private static final double FOOD = 100.0;

	public DynamicSupplyRegionBuilder() {
		super("dynamic", "Es un Builder de DynamicSupplyRegion");
	}

	@Override
	protected DynamicSupplyRegion create_instance(JSONObject data) {
		
		double factor = FACTOR;
		if(!data.isNull("factor")) {
			 factor = data.getDouble("factor");
			 if(factor == 0)
				 factor = FACTOR;
		}
		
		double food = FOOD;
		if(!data.isNull("food")) {
			food = data.getDouble("food");
			if (food == 0)
				food = FOOD;
		}
		
		return new DynamicSupplyRegion(factor, food);
	}
	
	@Override
	protected void fill_in_data(JSONObject o) { 
		o.put("factor", "increase factor (optional, default 2.0)");
		o.put("food", "initial amount of food (optional, default 100.0)");
	} 

}
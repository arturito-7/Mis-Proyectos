package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.Animal;
import simulator.model.SelectFirst;
import simulator.model.SelectionStrategy;
import simulator.model.Wolf;

public class WolfBuilder extends Builder<Animal>{

	private Factory<SelectionStrategy> _factoria;

	public WolfBuilder(Factory<SelectionStrategy> factoria) {
		super("wolf", "Es un Builder de Wolf");
		this._factoria = factoria;
	}

	@Override
	protected Wolf create_instance(JSONObject data) {

		return new Wolf(data.has("mate_strategy") ? this._factoria.create_instance(data.getJSONObject("mate_strategy")) : new SelectFirst(),
				data.has("hunt_strategy") ? this._factoria.create_instance(data.getJSONObject("hunt_strategy")) : new SelectFirst(),
						data.has("pos") ? position(data.getJSONObject("pos")) : null);
        
	}
	
	private Vector2D position(JSONObject JSONPos) {
		
		JSONArray x_range = JSONPos.getJSONArray("x_range");
        double x_0 = x_range.getDouble(0);
        double x_1 = x_range.getDouble(1);
        
        double pos_x = Utils._rand.nextDouble(x_0, x_1);
        
        JSONArray y_range = JSONPos.getJSONArray("y_range");
        double y_0 = y_range.getDouble(0);
        double y_1 = y_range.getDouble(1);
        
        double pos_y = Utils._rand.nextDouble(y_0, y_1);
        
        return new Vector2D(pos_x, pos_y);
		
	}

}

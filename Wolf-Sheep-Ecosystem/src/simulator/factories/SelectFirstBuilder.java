package simulator.factories;

import org.json.JSONObject;

import simulator.model.SelectionStrategy;
import simulator.model.SelectFirst;

public class SelectFirstBuilder extends Builder<SelectionStrategy> {

	public SelectFirstBuilder() {
		super("first", "Es un Builder de First");
	}

	@Override
	protected SelectionStrategy create_instance(JSONObject data) {
		return new SelectFirst();
	}
	
}

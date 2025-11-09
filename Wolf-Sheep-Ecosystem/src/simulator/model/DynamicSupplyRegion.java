package simulator.model;

import simulator.misc.Utils;

public class DynamicSupplyRegion extends Region{
	
	private double _cant;
	private double _crec;
	
	public DynamicSupplyRegion(double cant, double crec) {
		super();
		this._cant = cant;
		this._crec = crec;
	}

	@Override
	public void update(double dt) {
		if(Utils._rand.nextDouble() <= 0.5) this._cant += (dt * this._crec);
	}

	@Override
	public double get_food(Animal a, double dt) {
		if(a.get_diet() == Diet.CARNIVORE) return 0.0;
		else {
			double aux = Math.min(this._cant,CTE_FOOD*Math.exp(-Math.max(0,this.get_herviboros_cont()-CTE2_FOOD)*CTE3_FOOD)*dt);
			this._cant -= aux;
			return aux;
		}
	}

	@Override
	public String toString() {
		return "Dynamic region";
	}
}

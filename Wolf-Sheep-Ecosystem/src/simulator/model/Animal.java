package simulator.model;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import org.json.JSONObject;

public abstract class Animal implements Entity, AnimalInfo {
	
	private final static double VELOCIDAD = 0.1;
	private final static double ENERGIA = 100.0;
	private final static double TOLERANCIA = 0.2;
	private final static double FACTOR = 60.0;
	
	protected final static double MAX_DIST = 8.0;
	protected final static double CTE1_MOVE = 100.0;
	protected final static double CTE2_MOVE = 0.007;
	protected final static double MAX = 100.0;
	protected final static double MIN = 0.0;
	protected final static double LIM_DESEO = 65.0;
	protected final static double CTE_ENERGIA = 1.2;
	protected final static double PROB_BEBE = 0.9;

	protected String _genetic_code;
	protected Diet _diet;
	protected State _state;
	protected Vector2D _pos;
	protected Vector2D _dest;
	protected double _energy;
	protected double _speed;
	protected double _age;
	protected double _desire;
	protected double _sight_range;
	protected Animal _mate_target;
	protected Animal _baby;
	protected AnimalMapView _region_mngr;
	protected SelectionStrategy _mate_strategy;
	
	
	protected Animal(String genetic_code, Diet diet, double sight_range, double init_speed, SelectionStrategy mate_strategy, Vector2D pos) {

		if (!genetic_code.equalsIgnoreCase(""))
			this._genetic_code = genetic_code;
		else
			throw new IllegalArgumentException("Genetic_code tiene que ser una cadena de caracteres no vacía");
		
		if (sight_range > 0)
			this._sight_range = sight_range;
		else
			throw new IllegalArgumentException("Sight_range tiene que ser positivo");
		
		if(init_speed > 0)
			this._speed = Utils.get_randomized_parameter(init_speed, VELOCIDAD);
		else
			throw new IllegalArgumentException("Init_speed tiene que ser positivo");
		
		if(mate_strategy != null)
			this._mate_strategy = mate_strategy;
		else 
			throw new IllegalArgumentException("Mate_strategy no puede ser null");
		
		this._pos = pos;
		this._diet = diet;
		this._state = State.NORMAL;
		this._energy = ENERGIA;
		this._desire = 0.0;
		this._dest = null;
		this._mate_target = null;
		this._baby = null;
		this._region_mngr = null;
	}
	
	protected Animal(Animal p1, Animal p2) {
		this._dest = null;
		this._mate_target = null;
		this._baby = null;
		this._region_mngr = null;
		
		this._state = State.NORMAL;
		this._desire = 0.0;
		
		this._genetic_code = p1.get_genetic_code();
		this._diet = p1.get_diet();

		this._mate_strategy = p2._mate_strategy;
		
		this._energy = (p1.get_energy() + p2.get_energy()) / 2;
		
		this._pos = p1.get_position().plus(Vector2D.get_random_vector(-1,1).scale(FACTOR*(Utils._rand.nextGaussian()+1)));

		this._sight_range = Utils.get_randomized_parameter((p1.get_sight_range()+p2.get_sight_range())/2, TOLERANCIA);
		
		this._speed = Utils.get_randomized_parameter((p1.get_speed()+p2.get_speed())/2, TOLERANCIA);
		
	}
	
	void init(AnimalMapView reg_mngr) {
		double x, y;
		
		this._region_mngr = reg_mngr;
		
		if (this._pos == null) {
			x = Utils._rand.nextDouble(this._region_mngr.get_width() -1);
			y = Utils._rand.nextDouble(this._region_mngr.get_height() -1);
			this._pos = new Vector2D(x, y);
		}else 
			this._pos = ajustar(this._pos.getX(), this._pos.getY());
		
		x = Utils._rand.nextDouble(this._region_mngr.get_width()-1);
		y = Utils._rand.nextDouble(this._region_mngr.get_height()-1);
		this._dest = new Vector2D(x, y);
				
	}
	
	protected Vector2D ajustar(double x, double y) {
		while (x >= this._region_mngr.get_width()) x = (x - this._region_mngr.get_width());
		while (x < 0) x = (x + this._region_mngr.get_width());
		while (y >= this._region_mngr.get_height()) y = (y - this._region_mngr.get_height());
		while (y < 0) y = (y + this._region_mngr.get_height());
		return new Vector2D(x, y);
	}
	
	public Animal deliver_baby() {
		Animal bebe = this._baby;
		this._baby = null;
		
		return bebe;
	}
	
	protected void move(double speed) {
		this._pos = this._pos.plus(this._dest.minus(this._pos).direction().scale(speed));
	}
	
	public JSONObject as_JSON() {
		JSONObject o = new JSONObject();
		
		o.put("pos", this._pos.asJSONArray());
		o.put("gcode", this._genetic_code);
		o.put("diet", this._diet);
		o.put("state", this._state);
		
		return o;
	}
	
	@Override
	public State get_state() {
		return this._state;
	}

	@Override
	public Vector2D get_position() {
		return this._pos;
	}

	@Override
	public String get_genetic_code() {
		return this._genetic_code;
	}

	@Override
	public Diet get_diet() {
		return this._diet;
	}

	@Override
	public double get_speed() {
		return this._speed;
	}

	@Override
	public double get_sight_range() {
		return this._sight_range;
	}

	@Override
	public double get_energy() {
		return this._energy;
	}

	@Override
	public double get_age() {
		return this._age;
	}

	@Override
	public Vector2D get_destination() {
		return this._dest;
	}

	@Override
	public boolean is_pregnant() {
		return this._baby != null;
	}
	
	protected void set_desire(double d) {
		this._desire = d;
	}
	
	protected void set_state(State d) {
		this._state = d;
	}
	
}

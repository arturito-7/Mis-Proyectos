package simulator.model;

import java.util.List;

import simulator.misc.Utils;
import simulator.misc.Vector2D;

public class Sheep extends Animal {

	private final static double UNIDAD = 1.0;
	private final static double CAMPO_VISUAL = 40.0;
	private final static double VELOCIDAD = 35.0;
	private final static double MENOS_ENERGIA = 20.0;
	private final static double MAS_DESEO = 40.0;
	private final static double DOBLE_VEL = 2.0;
	private final static double MAX_EDAD = 8.0;
	

	private Animal _danger_source;
	private SelectionStrategy _danger_strategy;
	
	public Sheep(SelectionStrategy mate_strategy, SelectionStrategy danger_strategy, Vector2D pos) {
		super("Sheep", Diet.HERBIVORE, CAMPO_VISUAL, VELOCIDAD, mate_strategy, pos);
		this._danger_strategy = danger_strategy;
		this._danger_source = null;
	}
	
	protected Sheep(Sheep p1, Animal p2) {
		super(p1, p2);
		this._danger_strategy = p1._danger_strategy;
		this._danger_source = null;
	}

	private void avanzar(double dt, double vel, double cte) {
		
		this.move(vel*this._speed*dt*Math.exp((this._energy-CTE1_MOVE)*CTE2_MOVE));
		
		this._age += dt;
		
		this._energy -= (MENOS_ENERGIA * cte * dt);
		if(this._energy < MIN) this._energy = MIN;
		else if(this._energy > MAX) this._energy = MAX;
		
		this._desire += (MAS_DESEO * dt);
		if(this._desire > MAX) this._desire = MAX;
		else if(this._desire < MIN) this._desire = MIN;
	}

	private void normalMode(double dt) {
		if(this._pos.distanceTo(this._dest) < MAX_DIST) {
			double x, y;
			x = Utils._rand.nextDouble(this._region_mngr.get_width()-1);
			y = Utils._rand.nextDouble(this._region_mngr.get_height()-1);
			this._dest = new Vector2D(x, y);
		}
		
		this.avanzar(dt, UNIDAD, UNIDAD);
		
		if(this._danger_source == null) {
			List<Animal> danger = this._region_mngr.get_animals_in_range(this, (Animal e) -> {return e.get_diet() == Diet.CARNIVORE;});
			this._danger_source = this._danger_strategy.select(this, danger);
		}
		if(this._danger_source != null) {
			this._state = State.DANGER;
			this._mate_target = null;	
		}else {
			if(this._desire > LIM_DESEO)
				this._state = State.MATE;
				this._danger_source = null;
		}
	}
	
	private void dangerMode(double dt) {
		if(this._danger_source != null && this._danger_source.get_state() == State.DEAD) {
			this._danger_source = null;
		if (this._danger_source != null) {
				this._dest = this._pos.plus(this._pos.minus(this._danger_source.get_position()).direction());
				this.avanzar(dt, DOBLE_VEL, CTE_ENERGIA);
			}
		}else {
			if(this._pos.distanceTo(this._dest) < MAX_DIST) {
				double x, y;
				x = Utils._rand.nextDouble(this._region_mngr.get_width()-1);
				y = Utils._rand.nextDouble(this._region_mngr.get_height()-1);
				this._dest = new Vector2D(x, y);
			}
			
			this.avanzar(dt, UNIDAD, UNIDAD);
		}
		
		if(this._danger_source == null || this._pos.distanceTo(this._danger_source.get_position()) > this._sight_range) {
			List<Animal> danger = this._region_mngr.get_animals_in_range(this, (Animal e) -> {return e.get_diet() == Diet.CARNIVORE;});
			this._danger_source = this._danger_strategy.select(this, danger);
			if(this._danger_source == null) {
				if(this._desire > LIM_DESEO) {
					this._state = State.MATE;
					this._danger_source = null;
				}else {
					this._state = State.NORMAL;
					this._danger_source = null;
					this._mate_target = null;
				}
			}
		}
	}
	
	private void mateMode(double dt) {
		if(this._mate_target != null && (this._mate_target.get_state() == State.DEAD || this._pos.distanceTo(this._mate_target.get_position()) > this._sight_range)) {
			this._mate_target = null;
		}
		if(this._mate_target == null) {
			List<Animal> parejas = this._region_mngr.get_animals_in_range(this, (Animal e) -> {return e.get_genetic_code() == this._genetic_code;});
			this._mate_target = this._mate_strategy.select(this, parejas);
			if(this._mate_target == null) {
				if(this._pos.distanceTo(this._dest) < MAX_DIST) {
					double x, y;
					x = Utils._rand.nextDouble(this._region_mngr.get_width()-1);
					y = Utils._rand.nextDouble(this._region_mngr.get_height()-1);
					this._dest = new Vector2D(x, y);
				}
				this.avanzar(dt, UNIDAD, UNIDAD);
			}
		}else {
			this._dest = this._mate_target.get_position();
			
			this.avanzar(dt, DOBLE_VEL, CTE_ENERGIA);
			
			if(this._pos.distanceTo(this._mate_target.get_position()) < MAX_DIST) {
				this._desire = MIN;
				this._mate_target.set_desire(MIN);
				
				if(this._baby == null) {
					double aux = Utils._rand.nextDouble();
					if(aux < PROB_BEBE) this._baby = new Sheep(this,this._mate_target);
				}
				this._mate_target = null;
			}
		}
		if (this._danger_source == null) {
			List<Animal> miedo = this._region_mngr.get_animals_in_range(this, (Animal e) -> {return e.get_diet() == Diet.CARNIVORE;});
			this._danger_source = this._danger_strategy.select(this, miedo);
		}
		if(this._danger_source != null) {
			this._state = State.DANGER;
			this._mate_target = null;
		}else {
			if (this._desire < LIM_DESEO) {
				this._state = State.NORMAL;
				this._danger_source = null;
				this._mate_target = null;
			}
		}
	}
	
	@Override
	public void update(double dt) {
	
		if(this._state != State.DEAD) {			
			switch(this._state) {
			case NORMAL:
				normalMode(dt);
				break;
			case DANGER:
				dangerMode(dt);
				break;
			case MATE:
				mateMode(dt);
				break;
			default:
				break;
			}
			
			if(this._pos.getX() > this._region_mngr.get_width() || this._pos.getX() < 0 || this._pos.getY() > this._region_mngr.get_height() || this._pos.getY() < 0) {
				this._pos = super.ajustar(this._pos.getX(), this._pos.getY());
				this._state = State.NORMAL;
				this._danger_source = null;
				this._mate_target = null;
			}
			if(this._energy == MIN || this._age > MAX_EDAD) {
				this._state = State.DEAD;
			}
			if(this._state != State.DEAD) {
				this._energy += this._region_mngr.get_food(this, dt);
				if(this._energy < MIN) this._energy = MIN;
				else if(this._energy > MAX) this._energy = MAX;
			}
		}
	}
}

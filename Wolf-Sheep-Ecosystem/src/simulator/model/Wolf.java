package simulator.model;

import java.util.List;
import simulator.misc.Utils;
import simulator.misc.Vector2D;

public class Wolf extends Animal{

	private final static double UNIDAD = 1.0;
	private final static double CAMPO_VISUAL = 50.0;
	private final static double VELOCIDAD = 60.0;
	private final static double LIM_ENERGIA = 50.0;
	private final static double MAS_ENERGIA = 50.0;
	private final static double MENOS_ENERGIA = 18.0;
	private final static double MENOS_ENERGIA2 = 10.0;
	private final static double MAS_DESEO = 30.0;
	private final static double TRIPLE_VEL = 3.0;
	private final static double MAX_EDAD = 14.0;
	
	private Animal _hunt_target;
	private SelectionStrategy _hunting_strategy;

	public Wolf(SelectionStrategy mate_strategy, SelectionStrategy hunting_strategy, Vector2D pos) {
		super("Wolf", Diet.CARNIVORE, CAMPO_VISUAL, VELOCIDAD, mate_strategy, pos);
		this._hunting_strategy = hunting_strategy;
		this._hunt_target = null;
	}
	
	protected Wolf(Wolf p1, Animal p2) {
		super(p1, p2);
		this._hunting_strategy = p1._hunting_strategy;
		this._hunt_target = null;
	}
	
	private void avanzar(double dt, double vel, double cte) {
		
		this.move(vel * this._speed*dt*Math.exp((_energy-CTE1_MOVE)*CTE2_MOVE));
		
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
		
		if(this._energy < LIM_ENERGIA) {
			this._state = State.HUNGER;
			this._mate_target = null;
		}
		else {
			if(this._desire > LIM_DESEO) 
				this._state = State.MATE;
				this._hunt_target = null;
		}
	}
	
	private void hungerMode(double dt) {
		if(this._hunt_target == null || (this._hunt_target != null && (this._hunt_target.get_state() == State.DEAD || this._pos.distanceTo(this._hunt_target.get_position()) > this._sight_range))) {
			List<Animal> caza = this._region_mngr.get_animals_in_range(this, (Animal e) -> {return e.get_diet() == Diet.HERBIVORE;});
			this._hunt_target = this._hunting_strategy.select(this, caza);
		}
		if(this._hunt_target == null) {
			if(this._pos.distanceTo(this._dest) < MAX_DIST) {
				double x, y;
				x = Utils._rand.nextDouble(this._region_mngr.get_width()-1);
				y = Utils._rand.nextDouble(this._region_mngr.get_height()-1);
				this._dest = new Vector2D(x, y);
			}
			this.avanzar(dt, UNIDAD, UNIDAD);
		}
		else {
			this._dest = this._hunt_target.get_position();
			
			this.avanzar(dt, TRIPLE_VEL, CTE_ENERGIA);
			
			if(this._pos.distanceTo(this._hunt_target.get_position()) < MAX_DIST) {
				this._hunt_target.set_state(State.DEAD);
				this._hunt_target = null;
				this._energy += MAS_ENERGIA;
				if(this._energy < MIN) this._energy = MIN;
				else if(this._energy > MAX) this._energy = MAX;
			}
		}
		if(this._energy > LIM_ENERGIA) {
			if(this._desire < LIM_DESEO) {
				this._state = State.NORMAL;
				this._mate_target = null;
				this._hunt_target = null;
			}
			else {
				this._state = State.MATE; 
				this._hunt_target = null;
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
			
			this.avanzar(dt, TRIPLE_VEL, CTE_ENERGIA);
			
			if(this._pos.distanceTo(this._mate_target.get_position()) < MAX_DIST) {
				
				this._desire = MIN;
				this._mate_target.set_desire(MIN);
				if(this._baby == null){
					double aux = Utils._rand.nextDouble();
					if(aux < PROB_BEBE) this._baby = new Wolf(this,this._mate_target);
				}
				
				this._energy -= MENOS_ENERGIA2;
				if(this._energy < MIN) this._energy = MIN;
				else if(this._energy > MAX) this._energy = MAX;
				
				this._mate_target = null;
			}
		}
		
		if(this._energy < LIM_ENERGIA) {
			this._state = State.HUNGER;
			this._mate_target = null;
		}
		else 
			if(this._desire < LIM_DESEO) {
				this._state = State.NORMAL;
				this._hunt_target = null;
				this._mate_target = null;
			}
	}
	
	@Override
	public void update(double dt) {
		
		if(this._state != State.DEAD) {	
			switch(this._state) {
			case NORMAL:
				normalMode(dt);
				break;
			case HUNGER:
				hungerMode(dt);
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
				this._hunt_target = null;
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

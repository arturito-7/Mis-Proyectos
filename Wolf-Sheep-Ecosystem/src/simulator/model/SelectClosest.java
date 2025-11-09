package simulator.model;

import java.util.List;

public class SelectClosest implements SelectionStrategy{

	@Override
	public Animal select(Animal a, List<Animal> as) {

		if(as.isEmpty())
			return null;
		else {
			Animal aux = as.get(0);
			for(Animal b: as) {
				if(a.get_position().distanceTo(b.get_position()) < a.get_position().distanceTo(aux.get_position()))
					aux = b;
			}
			return aux;
		}
	}
}

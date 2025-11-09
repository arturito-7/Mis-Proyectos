package simulator.model;

import java.util.List;

public class SelectYoungest implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {
		
		if(as.isEmpty())
			return null;
		else {
			Animal aux = as.get(0);
			for(Animal b: as) {
				if(b.get_age() < aux.get_age())
					aux = b;
			}
			return aux;
		}
	}
}

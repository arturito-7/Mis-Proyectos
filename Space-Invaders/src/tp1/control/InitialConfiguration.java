package tp1.control;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InitialConfiguration {
	
	private List<String> descriptions;
	
	public static final InitialConfiguration NONE = new InitialConfiguration();
	
	private InitialConfiguration() {}
	
	private InitialConfiguration(List<String> descriptions) {
		this.descriptions = descriptions;
	}
	
	public List<String> getShipDescription(){
		return Collections.unmodifiableList(descriptions);
	}
	
	public static InitialConfiguration readFromFile(String filename) throws FileNotFoundException, IOException {
		
		List<String> lista = new ArrayList<>();
		
		try (BufferedReader in = new BufferedReader(new FileReader(filename + ".txt"));) {
			String l = in.readLine();
			while (l != null && !l.isEmpty()) {
				lista.add(l);
				l = in.readLine();
			}
		}
		return new InitialConfiguration(lista);
	}
	
}
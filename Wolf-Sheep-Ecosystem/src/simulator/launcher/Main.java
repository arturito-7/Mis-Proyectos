package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.control.Controller;
import simulator.factories.*;
import simulator.misc.Utils;
import simulator.model.Animal;
import simulator.model.Region;
import simulator.model.SelectionStrategy;
import simulator.model.Simulator;
import simulator.view.Carnivorous;
import simulator.view.MainWindow;

public class Main {

	private enum ExecMode {
		BATCH("batch", "Batch mode"), GUI("gui", "Graphical User Interface mode");

		private String _tag;
		private String _desc;

		private ExecMode(String modeTag, String modeDesc) {
			_tag = modeTag;
			_desc = modeDesc;
		}

		public String get_tag() {
			return _tag;
		}

		public String get_desc() {
			return _desc;
		}
	}

	// default values for some parameters
	//
	private final static Double _default_time = 10.0; // in seconds
	private final static Double _default_delta_time = 0.03; // in seconds

	// some attributes to stores values corresponding to command-line parameters
	//
	private static Double _time = null;
	public static Double _delta_time = null;
	private static String _in_file = null;
	private static String _out_file = null;
	private static boolean _sv = false;
	private static boolean _car = false;
	private static ExecMode _mode = ExecMode.GUI;
	
	public static Factory<Region> _region_factory;
	public static Factory<Animal> _animal_factory;
	
	private static Simulator _sim;
	private static Controller _controller;

	private static void parse_args(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = build_options();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parse_help_option(line, cmdLineOptions);
			parse_in_file_option(line);
			parse_mode_option(line);
			parse_out_file_option(line);
			parse_time_option(line);
			parse_delta_time_option(line);
			parse_sv_option(line);
			parse_car_option(line);
			

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options build_options() {
		Options cmdLineOptions = new Options();

		// delta time
		cmdLineOptions.addOption(Option.builder("dt").longOpt("delta time").hasArg()
				.desc("A double representing actual time, in seconds, per simulation step. Default value: "
						+ _default_delta_time + ".")
				.build());
		// help
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

		// input file
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Initial configuration file.").build());
		
		// mode
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Execution Mode. Possible values: 'batch' (Batch\n"
				+ "mode), 'gui' (Graphical User Interface mode).\n"
				+ "Default value: 'gui'.").build());
		
		// output
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("Output file, where output is written.").build());
		
		// simple viewer
		cmdLineOptions.addOption(Option.builder("sv").longOpt("simple viewer").desc("Show the viewer window in console mode.").build());
		
		// carnivorous
		cmdLineOptions.addOption(Option.builder("car").longOpt("Show Carnivorous").desc("Show the num of times when a regions has more than"
				+ "3 carnovorous.").build());

		// steps
		cmdLineOptions.addOption(Option.builder("t").longOpt("time").hasArg()
				.desc("An real number representing the total simulation time in seconds. Default value: "
						+ _default_time + ".")
				.build());

		return cmdLineOptions;
	}

	private static void parse_help_option(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parse_in_file_option(CommandLine line) throws ParseException {
		_in_file = line.getOptionValue("i");
		if (_mode == ExecMode.BATCH && _in_file == null) {
			throw new ParseException("In batch mode an input configuration file is required");
		}
	}
	
	private static void parse_mode_option(CommandLine line) throws ParseException {
		
		
		if (line.getOptionValue("m","gui").equals("batch")) {
			_mode = ExecMode.BATCH;
		}
		if (_mode == ExecMode.BATCH && _in_file == null) {
			throw new ParseException("In batch mode an input configuration file is required");
		}
	}
	
	private static void parse_out_file_option(CommandLine line) throws ParseException {
		_out_file = line.getOptionValue("o");
		if (_mode == ExecMode.BATCH && _out_file == null) {
			throw new ParseException("In batch mode an input configuration file is required");
		}
	}

	private static void parse_time_option(CommandLine line) throws ParseException {
		String t = line.getOptionValue("t", _default_time.toString());
		try {
			_time = Double.parseDouble(t);
			assert (_time >= 0);
		} catch (Exception e) {
			throw new ParseException("Invalid value for time: " + t);
		}
	}
	
	private static void parse_delta_time_option(CommandLine line) throws ParseException {
		String dt = line.getOptionValue("dt", _default_delta_time.toString());
		try {
			_delta_time = Double.parseDouble(dt);
			assert (_delta_time >= 0);
		} catch (Exception e) {
			throw new ParseException("Invalid value for delta time: " + dt);
		}
	}
	
	private static void parse_sv_option(CommandLine line) throws ParseException {
		if(line.hasOption("sv")) 
			_sv = true;
	}
	
	private static void parse_car_option(CommandLine line) throws ParseException {
		if(line.hasOption("car")) 
			_car = true;
	}

	private static void init_factories() {
		
		//Selection_Strategy
		List<Builder<SelectionStrategy>> selection_strategy_builders = new ArrayList<>();
		selection_strategy_builders.add(new SelectFirstBuilder()); 
		selection_strategy_builders.add(new SelectClosestBuilder()); 
		selection_strategy_builders.add(new SelectYoungestBuilder());
		Factory<SelectionStrategy> selection_strategy_factory = new BuilderBasedFactory<SelectionStrategy>(selection_strategy_builders);
		
		//Region
		List<Builder<Region>> region_builders = new ArrayList<>();
		region_builders.add(new DefaultRegionBuilder()); 
		region_builders.add(new DynamicSupplyRegionBuilder()); 
		Factory<Region> region_factory = new BuilderBasedFactory<Region>(region_builders);
		_region_factory = region_factory;
		
		//Animal
		List<Builder<Animal>> animal_builders = new ArrayList<>();
		animal_builders.add(new SheepBuilder(selection_strategy_factory)); 
		animal_builders.add(new WolfBuilder(selection_strategy_factory)); 
		Factory<Animal> animal_factory = new BuilderBasedFactory<Animal>(animal_builders);
		_animal_factory = animal_factory;
		
	}

	private static JSONObject load_JSON_file(InputStream in) {
		return new JSONObject(new JSONTokener(in));
	}

	private static void start_batch_mode() throws Exception {
		
        InputStream in = new FileInputStream(new File(_in_file));
        
        JSONObject JSONEntrada = load_JSON_file(in);

		OutputStream out = new FileOutputStream(new File(_out_file));
		
		int width = JSONEntrada.getInt("width");
		int height = JSONEntrada.getInt("height");
		int rows = JSONEntrada.getInt("rows");
		int cols = JSONEntrada.getInt("cols");
		
		_sim = new Simulator(cols, rows, width, height, _animal_factory, _region_factory);
		
		_controller = new Controller(_sim);
		
		if(_car) {
			Carnivorous c = new Carnivorous(_controller);
			_controller.load_data(JSONEntrada);
			_controller.run(_time, _delta_time, _sv, out);
			c.mostrar();
		}else {
			_controller.load_data(JSONEntrada);
			_controller.run(_time, _delta_time, _sv, out);
		}
		
		out.close();
		
	}

	private static void start_GUI_mode() throws Exception {
		
		int width, height, rows, cols;
		
		if(_in_file != null) {
			
			InputStream in = new FileInputStream(new File(_in_file));
        
			JSONObject JSONEntrada = load_JSON_file(in);
			
			width = JSONEntrada.getInt("width");
			height = JSONEntrada.getInt("height");
			rows = JSONEntrada.getInt("rows");
			cols = JSONEntrada.getInt("cols");
			
			_sim = new Simulator(cols, rows, width, height, _animal_factory, _region_factory);
			
			_controller = new Controller(_sim);
			
			_controller.load_data(JSONEntrada);
		}else {
			
			_sim = new Simulator(20, 15, 800, 600, _animal_factory, _region_factory);
			
			_controller = new Controller(_sim);
		
		}

		SwingUtilities.invokeAndWait(() -> new MainWindow(_controller));
		
	}

	private static void start(String[] args) throws Exception {
		init_factories();
		parse_args(args);
		switch (_mode) {
		case BATCH:
			start_batch_mode();
			break;
		case GUI:
			start_GUI_mode();
			break;
		}
	}

	public static void main(String[] args) {
		Utils._rand.setSeed(2147483647l);
		try {
			start(args);
		} catch (Exception e) {
			System.err.println("Something went wrong ...");
			System.err.println();
			e.printStackTrace();
		}
	}
}

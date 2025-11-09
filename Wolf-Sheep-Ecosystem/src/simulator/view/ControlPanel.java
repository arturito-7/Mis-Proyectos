package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JToolBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.control.Controller;
import simulator.launcher.Main;

class ControlPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	private ChangeRegionsDialog _changeRegionsDialog;
	private NuevaVista _newVista;
	
	private JToolBar _toolBar;
	private JFileChooser _fc;
	
	private boolean _stopped = true; // utilizado en los botones de run/stop
	private JButton _quitButton;
	private JButton _fileButton;
	private JButton _viewerButton;
	private JButton _regionsButton;
	private JButton _newButton;
	private JButton _runButton;
	private JButton _stopButton;

	private JSpinner stepsSpinner;
	private JTextField delta;
	
	ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		initGUI();
	}
	
	private void initGUI() {
		setLayout(new BorderLayout());
		_toolBar = new JToolBar();
		add(_toolBar, BorderLayout.PAGE_START);
		
		//crear los diferentes botones/atributos y añadirlos a _toolaBar.
		// Todos ellos han de tener su correspondiente tooltip. Puedes utilizar
		// _toolaBar.addSeparator() para añadir la línea de separación vertical
		// entre las componentes que lo necesiten.
		
		// File Button
		_fileButton = new JButton();
		_fileButton.setToolTipText("File");
		_fileButton.setIcon(new ImageIcon("resources/icons/open.png"));
		_fileButton.addActionListener((e) -> this.fileButton());
		_toolBar.add(_fileButton);
		
		// Viewer Button
		_toolBar.addSeparator();
		_viewerButton = new JButton();
		_viewerButton.setToolTipText("Viewer");
		_viewerButton.setIcon(new ImageIcon("resources/icons/viewer.png"));
		_viewerButton.addActionListener((e) -> this.viewerButton());
		_toolBar.add(_viewerButton);
		
		// Regions Button
		_regionsButton = new JButton();
		_regionsButton.setToolTipText("Regions");
		_regionsButton.setIcon(new ImageIcon("resources/icons/regions.png"));
		_regionsButton.addActionListener((e) -> this.regionsButton());
		_toolBar.add(_regionsButton);
		
		//New Button
		_newButton = new JButton();
		_newButton.setToolTipText("Nueva Vista");
		_newButton.addActionListener((e) -> _newVista.open(ViewUtils.getWindow(this)));
		_toolBar.add(_newButton);
		
		// Run Button
		_toolBar.addSeparator();
		_runButton = new JButton();
		_runButton.setToolTipText("Run");
		_runButton.setIcon(new ImageIcon("resources/icons/run.png"));
		_runButton.addActionListener((e) -> this.runButton());
		_toolBar.add(_runButton);
		
		// Stop Button
		_stopButton = new JButton();
		_stopButton.setToolTipText("Stop");
		_stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		_stopButton.addActionListener((e) -> this.stopButton());
		_toolBar.add(_stopButton);
		
		// Steps Label
		JLabel stepsLabel = new JLabel("Steps: ");
		_toolBar.add(stepsLabel);
		
		// Steps Spinner
		stepsSpinner = new JSpinner(new SpinnerNumberModel(100, 1, 10000, 100));
		stepsSpinner.setPreferredSize(new Dimension(80,40));
		stepsSpinner.setMaximumSize(new Dimension(80,40));
		stepsSpinner.setMinimumSize(new Dimension(80,40));
		stepsSpinner.setToolTipText("Simulation setps to run: 1-10000");
		_toolBar.add(stepsSpinner);
		
		// Delta time Label
		JLabel dtLabel = new JLabel("Delta-Time: ");
		_toolBar.add(dtLabel);
		 
		// Delta JTextField
		delta = new JTextField();
		delta.setText(Main._delta_time + "");
		delta.setToolTipText("Real time (seconds) correspomding to a step");
		delta.setPreferredSize(new Dimension(60,40));
		delta.setMaximumSize(new Dimension(60,40));
		delta.setMinimumSize(new Dimension(60,40));
		delta.setEditable(true);
		_toolBar.add(delta);
		
		// Quit Button
		_toolBar.add(Box.createGlue()); // this aligns the button to the right
		_toolBar.addSeparator();
		_quitButton = new JButton();
		_quitButton.setToolTipText("Quit");
		_quitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		_quitButton.addActionListener((e) -> ViewUtils.quit(this));
		_toolBar.add(_quitButton);
		
		//Inicializar _fc con una instancia de JFileChooser. Para que siempre
		// abre en la carpeta de ejemplos puedes usar:
		
		_fc = new JFileChooser();
		_fc.setCurrentDirectory(new File(System.getProperty("user.dir") + "/resources/examples"));
		
		//Inicializar _changeRegionsDialog con instancias del diálogo de cambio de regiones
		
		this._changeRegionsDialog = new ChangeRegionsDialog(this._ctrl);
		this._newVista = new NuevaVista(this._ctrl);
	
	}
	
	private void fileButton() {
		int opcion = _fc.showOpenDialog(ViewUtils.getWindow(this));
		
		if (opcion == JFileChooser.APPROVE_OPTION) {
			File f = _fc.getSelectedFile();
			try {
				FileInputStream in = new FileInputStream(f);
				JSONObject JSONEntrada = new JSONObject(new JSONTokener(in));
				_ctrl.reset(JSONEntrada.getInt("cols"), JSONEntrada.getInt("rows"), JSONEntrada.getInt("width"), JSONEntrada.getInt("height"));
				_ctrl.load_data(JSONEntrada);
			} catch (FileNotFoundException e) {
				ViewUtils.showErrorMsg("Error al seleccionar el fichero");
			}
		}
		
	}
	
	private void viewerButton() {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new MapWindow(null, _ctrl);
			}
		});	
	}
	
	private void regionsButton() {
		_changeRegionsDialog.open(ViewUtils.getWindow(this));
	}
	
	private void runButton() {
		_stopped = false;
		activar_botones();
		run_sim((Integer)stepsSpinner.getValue(), Double.parseDouble(delta.getText()));
	}
	
	private void stopButton() {
		_stopped = true;
	}
	
	private void activar_botones() {
		_quitButton.setEnabled(_stopped);
		_fileButton.setEnabled(_stopped);
		_viewerButton.setEnabled(_stopped);
		_regionsButton.setEnabled(_stopped);
		_runButton.setEnabled(_stopped);
	}
	
	private void run_sim(int n, double dt) {
		if (n > 0 && !_stopped) {
			try {
				_ctrl.advance(dt);
				Thread.sleep((long) (1000*dt));
				SwingUtilities.invokeLater(() -> run_sim(n - 1, dt));
			} catch (Exception e) {
				ViewUtils.showErrorMsg("Error en el run" );
				_stopped = true;
				activar_botones();
			}
		} else {
			_stopped = true;
			activar_botones();
		}
	}

}
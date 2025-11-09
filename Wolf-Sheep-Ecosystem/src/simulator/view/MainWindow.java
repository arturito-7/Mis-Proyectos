package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simulator.control.Controller;

public class MainWindow extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Controller _ctrl;
	
	public MainWindow(Controller ctrl) {
		super("[ECOSYSTEM SIMULATOR]");
		_ctrl = ctrl;
		initGUI();
	}
	
	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		setContentPane(mainPanel);
		
		//crear ControlPanel y añadirlo en PAGE_START de mainPanel
		JPanel controlPanel = new ControlPanel(this._ctrl);
		mainPanel.add(controlPanel, BorderLayout.PAGE_START);
		
		//crear StatusBar y añadirlo en PAGE_END de mainPanel
		JPanel statusBar = new StatusBar(this._ctrl);
		mainPanel.add(statusBar, BorderLayout.PAGE_END);
		
		// Definición del panel de tablas (usa un BoxLayout vertical)
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		
		//crear la tabla de especies y añadirla a contentPanel.
		// Usa setPreferredSize(new Dimension(500, 250)) para fijar su tamaño
		InfoTable species = new InfoTable("Species", new SpeciesTableModel(this._ctrl));
		species.setPreferredSize(new Dimension(500, 250));
		contentPanel.add(species);
		
		//crear la tabla de regiones.
		// Usa setPreferredSize(new Dimension(500, 250)) para fijar su tamaño
		InfoTable regions = new InfoTable("Regions", new RegionsTableModel(this._ctrl));
		regions.setPreferredSize(new Dimension(500, 250));
		contentPanel.add(regions);
		
		//llama a ViewUtils.quit(MainWindow.this) en el método windowClosing
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				ViewUtils.quit(MainWindow.this);
			}
		});
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocation(500, 100);
		pack();
		setVisible(true);
	}
	
}

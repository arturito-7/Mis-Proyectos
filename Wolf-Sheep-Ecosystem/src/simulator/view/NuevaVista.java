package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.util.HashSet;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.MapInfo.RegionData;
import simulator.model.RegionInfo;

public class NuevaVista extends JDialog implements EcoSysObserver{
	
	private Controller _ctrl;
	private DefaultComboBoxModel<String> _regionsModel;
	private MapInfo mapita;
	
	public NuevaVista(Controller ctrl) {
		super((Frame)null, true);
		this._ctrl = ctrl;
		this._ctrl.addObserver(this);
		initGUI();
	}

	private void initGUI() {
		setTitle("Nueva Vista");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);
		
		JPanel comboBox = new JPanel();
		
		_regionsModel = new DefaultComboBoxModel<String>();
			
		if(mapita != null) {
			for(RegionData r: mapita) {
				_regionsModel.addElement(r.row() + "|" + r.col() + "|" +r.r().toString());
			}
		}
		
		JComboBox<String> regionsCombo = new JComboBox<String>(_regionsModel);
		regionsCombo.setPreferredSize(new Dimension(150,20));
		
		JLabel rType = new JLabel("Regions: ");
		comboBox.add(rType);
		comboBox.add(regionsCombo);
		mainPanel.add(comboBox);
				
		JPanel buttons = new JPanel();
		JButton cancel = new JButton("Salir");
		cancel.addActionListener((e) -> this.setVisible(false));
		
		buttons.add(cancel);
		mainPanel.add(buttons);
		
		setPreferredSize(new Dimension(700, 400)); // puedes usar otro tamaño
		pack();
		setResizable(false);
		setVisible(false);
		
	}
	
	public void open(Frame parent) {
		setLocation(
		parent.getLocation().x + parent.getWidth() / 2 - getWidth() / 2, 
		parent.getLocation().y + parent.getHeight() / 2 - getHeight() / 2);
		pack();
		setVisible(true);
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		mapita = map;		
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		mapita = map;		
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		mapita = map;
		for(RegionData reg: mapita) {
			_regionsModel.addElement(reg.row() + "|" + reg.col() + "|" +reg.r().toString());
		}
		
	}

	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		mapita = map;		
	}

}

package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class StatusBar extends JPanel implements EcoSysObserver {
	
	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	private int _numAnimals;
	private JLabel _jLabelTime;
	private JLabel _jLabelNum;
	private JLabel _jLabelDim;
	
	StatusBar(Controller ctrl) {
		this._ctrl = ctrl;
		initGUI();
		this._ctrl.addObserver(this);
	}
	
	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));
		
		_jLabelTime = new JLabel();
		this.add(_jLabelTime);
		
		JSeparator s = new JSeparator(JSeparator.VERTICAL);
		s.setPreferredSize(new Dimension(10, 20));
		this.add(s);
		
		_jLabelNum = new JLabel();
		this.add(_jLabelNum);
		
		JSeparator se = new JSeparator(JSeparator.VERTICAL);
		se.setPreferredSize(new Dimension(10, 20));
		this.add(se);
		
		_jLabelDim = new JLabel();
		this.add(_jLabelDim);
		
	}
	
	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		this._jLabelTime.setText("Time: " + time);
		this._jLabelDim.setText("Dimension: " + map.get_width() + "x" + map.get_height() + " " + map.get_cols() + "x" + map.get_rows());
		this._numAnimals = animals.size();
		this._jLabelNum.setText("Total Animals: " + this._numAnimals);
	}
	
	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		this.onRegister(time, map, animals);
	}
	
	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {
		this._numAnimals++;
		_jLabelNum.setText("Total Animals: " + this._numAnimals);
	}
	
	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {}
	
	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		_numAnimals = animals.size();
		_jLabelNum.setText("Total Animals: " + this._numAnimals);
		_jLabelTime.setText("Time: " + time);
	}
}
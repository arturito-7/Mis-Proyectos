package simulator.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.launcher.Main;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

class ChangeRegionsDialog extends JDialog implements EcoSysObserver {

	private static final long serialVersionUID = 1L;
	private DefaultComboBoxModel<String> _regionsModel;
	private DefaultComboBoxModel<String> _fromRowModel;
	private DefaultComboBoxModel<String> _toRowModel;
	private DefaultComboBoxModel<String> _fromColModel;
	private DefaultComboBoxModel<String> _toColModel;
	
	private DefaultTableModel _dataTableModel;
	private Controller _ctrl;
	private List<JSONObject> _regionsInfo;
	
	private String[] _headers = { "Key", "Value", "Description" };
	
	private String help = "<html><div style='max-width: 100%;'>Select a region type, the rows/cols interval, "
			+ "and provide values for the parameters in the <b>Value column</b> (default values are used for "
			+ "parameters with no value).</div></html>";
	
	ChangeRegionsDialog(Controller ctrl) {
		super((Frame)null, true);
		_ctrl = ctrl;
		initGUI();
		this._ctrl.addObserver(this);
	}
	
	private void initGUI() {
		setTitle("Change Regions");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);
		
		//crea varios paneles para organizar los componentes visuales en el
		// dialogo, y añadelos al mainpanel. P.ej., uno para el texto de ayuda,
		// uno para la tabla, uno para los combobox, y uno para los botones.
		
		JPanel helpText = new JPanel();
		JPanel table = new JPanel();
		JPanel comboBox = new JPanel();
		JPanel buttons = new JPanel();
		
		//crear el texto de ayuda que aparece en la parte superior del diálogo y
		// añadirlo al panel correspondiente diálogo (Ver el apartado Figuras)
		
		JLabel text = new JLabel(help);
		text.setAlignmentX(Component.LEFT_ALIGNMENT);
		helpText.setLayout(new BoxLayout(helpText, BoxLayout.X_AXIS));
		helpText.add(text);
		mainPanel.add(helpText);
		
		// _regionsInfo se usará para establecer la información en la tabla
		this._regionsInfo = new ArrayList<>(Main._region_factory.get_info());
		
		
		_dataTableModel = new DefaultTableModel() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 1;
			}

		};
		
		_dataTableModel.setColumnIdentifiers(_headers);
		
		//crear un JTable que use _dataTableModel, y añadirlo al diálogo
		
		
		JTable regionsTable = new JTable(_dataTableModel);		
		JScrollPane scroll = new JScrollPane(regionsTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		table.add(scroll);
		table.setLayout(new BoxLayout(table, BoxLayout.X_AXIS));
		mainPanel.add(table);
		
		// _regionsModel es un modelo de combobox que incluye los tipos de regiones
		_regionsModel = new DefaultComboBoxModel<>();
		
		//añadir la descripción de todas las regiones a _regionsModel, para eso
		// usa la clave “desc” o “type” de los JSONObject en _regionsInfo,
		// ya que estos nos dan información sobre lo que puede crear la factoría.
		
		for(JSONObject o : this._regionsInfo) {
			this._regionsModel.addElement(o.getString("type"));
		}
		
		//crear un combobox que use _regionsModel y añadirlo al diálogo.
		
		JComboBox<String> regions = new JComboBox<>(_regionsModel);
		regions.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for(JSONObject o : _regionsInfo) {
					if(regions.getSelectedItem().equals(o.getString("type"))) {
						pintarTabla(o.getString("type"), o);
					}
				}
			}
			
			private void pintarTabla(String str, JSONObject info) {
				_dataTableModel.setRowCount(0);
				JSONObject data = info.getJSONObject("data");
				Object[] obj = new Object[3];
				for(String s: data.keySet()) {
					obj[0] = s;
					obj[2] = data.get(s);
					_dataTableModel.addRow(obj);
				}
			}
			
		});
		JLabel rType = new JLabel("Region type: ");
		comboBox.add(rType);
		comboBox.add(regions);
		
		//crear 4 modelos de combobox para _fromRowModel, _toRowModel,
		// _fromColModel y _toColModel.
	 
		_fromRowModel = new DefaultComboBoxModel<>();
		_toRowModel = new DefaultComboBoxModel<>();
		_fromColModel = new DefaultComboBoxModel<>();
		_toColModel = new DefaultComboBoxModel<>();
		
		//crear 4 combobox que usen estos modelos y añadirlos al diálogo.
		
		JLabel row = new JLabel("Row from/to: ");
		JLabel col = new JLabel("Column from/to: ");
		
		comboBox.add(row);
		JComboBox<String> fromRowBox = new JComboBox<>(_fromRowModel);
		comboBox.add(fromRowBox);
		JComboBox<String> toRowBox = new JComboBox<>(_toRowModel);
		comboBox.add(toRowBox);
		comboBox.add(col);
		JComboBox<String> fromColBox = new JComboBox<>(_fromColModel);
		comboBox.add(fromColBox);
		JComboBox<String> toColBox = new JComboBox<>(_toColModel);
		comboBox.add(toColBox);
	
		mainPanel.add(comboBox);
		
		//crear los botones OK y Cancel y añadirlos al diálogo.
		
		JButton ok = new JButton("OK");
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener((e) -> this.setVisible(false));
		ok.addActionListener(new ActionListener() {
			
			@Override 
			public void actionPerformed(ActionEvent e) {
				
				JSONObject region_data = new JSONObject();
				for(int i = 0; i < _dataTableModel.getRowCount(); i++) {
					if (_dataTableModel.getValueAt(i, 1) != null) {
						region_data.put((String) _dataTableModel.getValueAt(i, 0), _dataTableModel.getValueAt(i, 1));
					}
				}
				
				String region_type = _regionsInfo.get(regions.getSelectedIndex()).getString("type");
				
				int row_from = fromRowBox.getSelectedIndex();
				int row_to = toRowBox.getSelectedIndex();
				int col_from = fromColBox.getSelectedIndex();
				int col_to = toColBox.getSelectedIndex();
				
				JSONObject region = new JSONObject();
				region.put("row", new JSONArray().put(row_from).put(row_to));
		        region.put("col", new JSONArray().put(col_from).put(col_to));
		        region.put("spec", new JSONObject().put("type", region_type).put("data", region_data));
				
		        JSONArray array_reg = new JSONArray();
		        array_reg.put(region);
		        
		        JSONObject lleno = new JSONObject();
		        lleno.put("regions", array_reg);
		        
		        try {
		        _ctrl.set_regions(lleno);  
		        setVisible(false);
		        }catch(Exception ex) {
		        	ViewUtils.showErrorMsg("Error al hacer el set_regions");
		        }
			}
			
		});
		buttons.add(cancel);
		buttons.add(ok);
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
		for(int i = 0; i < map.get_rows(); i++) {
			_fromRowModel.addElement(i+"");
			_toRowModel.addElement(i+"");
		}
		for(int i = 0; i < map.get_cols(); i++) {
			_fromColModel.addElement(i+"");
			_toColModel.addElement(i+"");
		}
	}
	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		_fromColModel.removeAllElements();
		_toColModel.removeAllElements();
		_fromRowModel.removeAllElements();
		_toRowModel.removeAllElements();
		for(int i = 0; i < map.get_rows(); i++) {
			_fromRowModel.addElement(i+"");
			_toRowModel.addElement(i+"");
		}
		for(int i = 0; i < map.get_cols(); i++) {
			_fromColModel.addElement(i+"");
			_toColModel.addElement(i+"");
		}
	}
	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {}
	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {}
	@Override
	public void onAvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {}
}

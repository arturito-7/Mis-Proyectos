package presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import presentacion.Observador;
import presentacion.controlador.Controlador;
import presentacion.controlador.EventosControlador;

public class VistaPrincipal extends JFrame implements Observador {

	private static final long serialVersionUID = 1L;

	@Override
	public void actualizaVista(Object datos) {
		UtilidadesP.setAirGestRSP(this);	
		this.setSize(320, 300); //hace que la ventana no salga tan chiquitita
		JPanel principal = new JPanel();
		principal.setLayout(new BorderLayout());
		
		JPanel page_start_panel = new JPanel();
		page_start_panel.setLayout(new BoxLayout(page_start_panel,BoxLayout.PAGE_AXIS));
		
		JPanel panel_label_titulo = new JPanel();
		JLabel titulo = new JLabel("AIRGEST RSP"); //titulo de la ventana en la que estamos, apareceran las funciones de modelo 
		titulo.setBorder(new LineBorder(Color.BLACK,2)); 
		
		titulo.setFont(new Font("Comic Sans", Font.BOLD, 30));
		titulo.setHorizontalAlignment(SwingConstants.CENTER); //ESTO ES LO QUE LO CENTRA, SWINGCONSTANTS
		panel_label_titulo.add(titulo);
		
		JSeparator separador_titulo = new JSeparator(SwingConstants.CENTER);
		separador_titulo.setBorder(new MatteBorder(1,1,10,10, Color.BLACK));
		separador_titulo.setPreferredSize(new Dimension(0,2));
		page_start_panel.add(panel_label_titulo);
		page_start_panel.add(separador_titulo);
		principal.add(page_start_panel, BorderLayout.PAGE_START);

		JPanel botones = new JPanel();
		botones.setLayout(new GridLayout(0,1, 1, 7));

		Controlador ctrl = Controlador.getInstance();

		//-------------------------------------------

		JButton modelo = new JButton("MODELO");
		modelo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_MODELO, null);
			}
		});
		modelo.setToolTipText("MODULO MODELO");
		botones.add(modelo);

		//-------------------------------------------
		JButton avion = new JButton("AVION");
		avion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_AVION, null);
			}
		});
		avion.setToolTipText("MODULO AVION");
		botones.add(avion);

		//-------------------------------------------
		JButton aerolinea = new JButton("AEROLINEA");
		aerolinea.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_AEROLINEA, null);
			}
		});
		aerolinea.setToolTipText("MODULO AEROLINEA");
		botones.add(aerolinea);

		//-------------------------------------------
		JButton hangar = new JButton("HANGAR");
		hangar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_HANGAR, null);
			}
		});
		hangar.setToolTipText("MODULO HANGAR");
		botones.add(hangar);

		//-------------------------------------------
				JButton personal = new JButton("PERSONAL");
				personal.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
						ctrl.accion(EventosControlador.VISTA_PERSONAL, null);
					}
				});
				personal.setToolTipText("MODULO PERSONAL");
				botones.add(personal);

		//-------------------------------------------
		JButton contrato = new JButton("CONTRATO");
		contrato.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_CONTRATO, null);
			}
		});
		botones.setToolTipText("MODULO CONTRATO");
		botones.add(contrato);

		principal.add(botones, BorderLayout.CENTER);

		this.setContentPane(principal);
		this.setResizable(false); // no se puede modificar la ventana
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setLocation(200, 200);
	}
}
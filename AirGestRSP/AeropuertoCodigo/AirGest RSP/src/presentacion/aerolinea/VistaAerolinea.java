package presentacion.aerolinea;

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

import presentacion.controlador.Controlador;
import presentacion.controlador.EventosControlador;
import presentacion.Observador;
import presentacion.UtilidadesP;

public class VistaAerolinea extends JFrame implements Observador {

	private static final long serialVersionUID = 1L;

	@Override
	public void actualizaVista(Object datos) {
		UtilidadesP.setAirGestRSP(this);
		this.setSize(400, 350);
		JPanel principal = new JPanel();
		principal.setLayout(new BorderLayout());

		JPanel page_start_panel = new JPanel();
		page_start_panel.setLayout(new BoxLayout(page_start_panel,BoxLayout.PAGE_AXIS));
		
		JPanel panel_label_aerolinea = new JPanel();
		JLabel aerolinea = new JLabel("AEROLINEA"); //titulo de la ventana en la que estamos, apareceran las funciones de aerolinea 
		aerolinea.setFont(new Font("Comic Sans", Font.BOLD, 30));
		aerolinea.setHorizontalAlignment(SwingConstants.CENTER);
		aerolinea.setBorder(new LineBorder(Color.BLACK,2));
		panel_label_aerolinea.add(aerolinea);
		
		JSeparator separador_aerolinea = new JSeparator(SwingConstants.CENTER);
		separador_aerolinea.setBorder(new MatteBorder(1,1,10,10, Color.BLACK));
		separador_aerolinea.setPreferredSize(new Dimension(0,2));
		page_start_panel.add(panel_label_aerolinea);
		page_start_panel.add(separador_aerolinea);
		principal.add(page_start_panel, BorderLayout.PAGE_START);
		
		
		JPanel botones = new JPanel();
		botones.setLayout(new GridLayout(0, 1, 7, 7));

		Controlador ctrl = Controlador.getInstance();

		JButton alta = new JButton("ALTA DE AEROLINEA");
		alta.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_ALTA_AEROLINEA, null);
			}
		});
		alta.setToolTipText("Aqui das de alta una aerolinea crack");
		botones.add(alta);

		//-------------------------------------------
		JButton baja = new JButton("BAJA DE AEROLINEA");
		baja.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_BAJA_AEROLINEA, null);
			}
		});
		baja.setToolTipText("Aqui das de baja una aerolinea crack");
		botones.add(baja);

		//-------------------------------------------
		JButton consultarID = new JButton("CONSULTAR AEROLINEA POR ID");

		consultarID.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_CONSULTAR_AEROLINEA_POR_ID, null);
			}
		});
		botones.add(consultarID);

		//-------------------------------------------
		JButton consultarTodas = new JButton("CONSULTAR TODAS LAS AEROLINEAS");

		consultarTodas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ctrl.accion(EventosControlador.CONSULTAR_TODOS_AEROLINEAS, null);
			}
		});

		botones.add(consultarTodas);

		//-------------------------------------------
		JButton modificar = new JButton("MODIFICAR AEROLINEA");

		modificar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_MODIFICAR_AEROLINEA, null);
			}
		});

		botones.add(modificar);

		principal.add(botones, BorderLayout.CENTER);
		
		JPanel panel_atras = new JPanel();
		JButton atras = new JButton("ATRAS"); //boton para volver a la ventana principal
		atras.setToolTipText("Esto vuelve a la ventana anterior");
		atras.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				ctrl.accion(EventosControlador.VISTA_PRINCIPAL, null);
			}

		});
		panel_atras.add(atras);
		principal.add(panel_atras, BorderLayout.PAGE_END);

		this.setContentPane(principal);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setLocation(200, 200);
	}
}
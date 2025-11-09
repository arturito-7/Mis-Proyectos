package presentacion.avion;

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
import presentacion.UtilidadesP;
import presentacion.controlador.Controlador;
import presentacion.controlador.EventosControlador;

public class VistaAvion extends JFrame implements Observador {

	private static final long serialVersionUID = 1L;

	@Override
	public void actualizaVista(Object datos) {
		UtilidadesP.setAirGestRSP(this);
		this.setSize(430, 390); // hace que la ventana no salga tan chiquitita
		
		JPanel principal = new JPanel();
		principal.setLayout(new BorderLayout());
		
		
		JPanel page_start_panel = new JPanel();
		page_start_panel.setLayout(new BoxLayout(page_start_panel,BoxLayout.PAGE_AXIS));
		
		JPanel panel_label_avion = new JPanel();
		JLabel avion = new JLabel("AVION"); //titulo de la ventana en la que estamos, apareceran las funciones de avion 
		avion.setFont(new Font("Comic Sans", Font.BOLD, 30));
		avion.setHorizontalAlignment(SwingConstants.CENTER);
		avion.setBorder(new LineBorder(Color.BLACK,2));
		panel_label_avion.add(avion);

		JSeparator separador_avion = new JSeparator(SwingConstants.CENTER);
		separador_avion.setBorder(new MatteBorder(1,1,10,10, Color.BLACK));
		separador_avion.setPreferredSize(new Dimension(0,2));
		
		page_start_panel.add(panel_label_avion);
		page_start_panel.add(separador_avion);
		principal.add(page_start_panel, BorderLayout.PAGE_START);

		JPanel botones = new JPanel();
		botones.setLayout(new GridLayout(0, 1, 7, 7));

		Controlador ctrl = Controlador.getInstance();

		JButton alta = new JButton("ALTA DE AVION");
		alta.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_ALTA_AVION, null);
			}
		});
		alta.setToolTipText("Aqui das de alta un avion maquina");
		botones.add(alta);

		// -------------------------------------------
		JButton baja = new JButton("BAJA DE AVION");
		baja.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_BAJA_AVION, null);
			}
		});
		baja.setToolTipText("Aqui das de baja un avion maquina");
		botones.add(baja);

		// -------------------------------------------
		JButton consultarID = new JButton("CONSULTAR AVION POR ID");

		consultarID.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_CONSULTAR_AVION_POR_ID, null);
			}
		});
		botones.add(consultarID);

		// -------------------------------------------
		JButton consultarTodos = new JButton("CONSULTAR TODOS LOS AVIONES");

		consultarTodos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ctrl.accion(EventosControlador.CONSULTAR_TODOS_AVIONES, null);
			}
		});

		botones.add(consultarTodos);

		// -------------------------------------------
		JButton modificar = new JButton("MODIFICAR AVION");

		modificar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_MODIFICAR_AVION, null);
			}
		});

		botones.add(modificar);

		// -------------------------------------------
		JButton mostrarPorModelo = new JButton("MOSTRAR AVIONES POR MODELO");

		mostrarPorModelo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_MOSTRAR_AVIONES_POR_MODELO, null);
			}
		});

		botones.add(mostrarPorModelo);

		// -------------------------------------------
		JButton mostrarPorHangar = new JButton("MOSTRAR AVIONES POR HANGAR");

		mostrarPorHangar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_MOSTRAR_AVIONES_POR_HANGAR, null);
			}
		});

		botones.add(mostrarPorHangar);

		// -------------------------------------------
		JButton mostrarPorAerolinea = new JButton("MOSTRAR AVIONES POR AEROLINEA");

		mostrarPorAerolinea.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_MOSTRAR_AVIONES_POR_AEROLINEA, null);
			}
		});

		botones.add(mostrarPorAerolinea);

		// -------------------------------------------
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
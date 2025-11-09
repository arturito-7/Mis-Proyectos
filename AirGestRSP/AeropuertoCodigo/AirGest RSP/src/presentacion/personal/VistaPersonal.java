package presentacion.personal;

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

public class VistaPersonal extends JFrame implements Observador {

	private static final long serialVersionUID = 1L;

	@Override
	public void actualizaVista(Object datos) {
		UtilidadesP.setAirGestRSP(this);
		this.setSize(430, 390); // hace que la ventana no salga tan chiquitita
		
		JPanel principal = new JPanel();
		principal.setLayout(new BorderLayout());
		JPanel page_start_panel = new JPanel();
		page_start_panel.setLayout(new BoxLayout(page_start_panel,BoxLayout.PAGE_AXIS));
		
		JPanel panel_label_personal = new JPanel();
		JLabel personal = new JLabel("PERSONAL"); //titulo de la ventana en la que estamos, apareceran las funciones de personal 
		personal.setFont(new Font("Comic Sans", Font.BOLD, 30));
		personal.setHorizontalAlignment(SwingConstants.CENTER);
		personal.setBorder(new LineBorder(Color.BLACK,2));
		panel_label_personal.add(personal);
		
		JSeparator separador_personal = new JSeparator(SwingConstants.CENTER);
		separador_personal.setBorder(new MatteBorder(1,1,10,10, Color.BLACK));
		separador_personal.setPreferredSize(new Dimension(0,2));
		page_start_panel.add(panel_label_personal);
		page_start_panel.add(separador_personal);
		principal.add(page_start_panel, BorderLayout.PAGE_START);
		
		
		JPanel botones = new JPanel();
		botones.setLayout(new GridLayout(0, 1, 7, 7));


		Controlador ctrl = Controlador.getInstance();

		JButton alta = new JButton("ALTA DE PERSONAL");
		alta.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_ALTA_PERSONAL, null);
			}
		});
		alta.setToolTipText("Aqui das de alta un personal maquina");
		botones.add(alta);

		// -------------------------------------------
		JButton baja = new JButton("BAJA DE PERSONAL");
		baja.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_BAJA_PERSONAL, null);
			}
		});
		baja.setToolTipText("Aqui das de baja un personal maquina");
		botones.add(baja);

		// -------------------------------------------
		JButton consultarID = new JButton("CONSULTAR PERSONAL POR ID");

		consultarID.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_CONSULTAR_PERSONAL_POR_ID, null);
			}
		});
		botones.add(consultarID);

		// -------------------------------------------
		JButton consultarTodos = new JButton("CONSULTAR PERSONAL EXISTENTE");

		consultarTodos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ctrl.accion(EventosControlador.CONSULTAR_PERSONAL_EXISTENTE, null);
			}
		});

		botones.add(consultarTodos);

		// -------------------------------------------
		JButton modificar = new JButton("MODIFICAR PERSONAL");

		modificar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_MODIFICAR_PERSONAL, null);
			}
		});

		botones.add(modificar);

		// -------------------------------------------
		JButton vincular = new JButton("VINCULAR PERSONAL");

		vincular.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_VINCULAR_PERSONAL, null);
			}
		});

		botones.add(vincular);

		// -------------------------------------------
		JButton desvincular = new JButton("DESVINCULAR PERSONAL");

		desvincular.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_DESVINCULAR_PERSONAL, null);
			}
		});

		botones.add(desvincular);

		principal.add(botones, BorderLayout.CENTER);

		// -----------------------------------------------------
		
		JPanel panel_atras = new JPanel();
		JButton atras = new JButton("ATRAS"); // boton para volver a la ventana
												// principal
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
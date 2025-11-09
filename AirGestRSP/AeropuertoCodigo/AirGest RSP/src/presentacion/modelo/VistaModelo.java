package presentacion.modelo;

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

public class VistaModelo extends JFrame implements Observador {

	private static final long serialVersionUID = 1L;

	@Override
	public void actualizaVista(Object datos) {
		UtilidadesP.setAirGestRSP(this);
		this.setSize(430, 390); // hace que la ventana no salga tan chiquitita
		JPanel principal = new JPanel();
		principal.setLayout(new BorderLayout());

		JPanel page_start_panel = new JPanel();
		page_start_panel.setLayout(new BoxLayout(page_start_panel, BoxLayout.PAGE_AXIS));

		JPanel panel_label_modelo = new JPanel();
		JLabel modelo = new JLabel("MODELO"); // titulo de la ventana en la que
												// estamos, apareceran las
												// funciones de modelo
		// modelo.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1),
		// new MatteBorder(5,3,7,10, Color.ORANGE))); // OPCION 2
		modelo.setBorder(new LineBorder(Color.BLACK, 2));
		modelo.setFont(new Font("Comic Sans", Font.BOLD, 30));
		modelo.setHorizontalAlignment(SwingConstants.CENTER);

		panel_label_modelo.add(modelo);

		JSeparator separador_modelo = new JSeparator(SwingConstants.CENTER);
		separador_modelo.setBorder(new MatteBorder(1, 1, 10, 10, Color.BLACK));
		separador_modelo.setPreferredSize(new Dimension(0, 2));
		page_start_panel.add(panel_label_modelo);
		page_start_panel.add(separador_modelo);
		principal.add(page_start_panel, BorderLayout.PAGE_START);

		JPanel botones = new JPanel();
		botones.setLayout(new GridLayout(0, 1, 7, 7));

		Controlador ctrl = Controlador.getInstance();

		JButton alta = new JButton("ALTA DE MODELO");
		alta.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_ALTA_MODELO, null);
			}
		});
		alta.setToolTipText("Aqui das de alta un modelo maquina");
		botones.add(alta);

		// -------------------------------------------
		JButton baja = new JButton("BAJA DE MODELO");
		baja.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_BAJA_MODELO, null);
			}
		});
		baja.setToolTipText("Aqui das de baja un modelo maquina");
		botones.add(baja);

		// -------------------------------------------
		JButton consultarID = new JButton("CONSULTAR MODELO POR ID");

		consultarID.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_CONSULTAR_MODELO_POR_ID, null);
			}
		});
		botones.add(consultarID);

		// -------------------------------------------
		JButton consultarTodos = new JButton("CONSULTAR TODOS LOS MODELOS");

		consultarTodos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ctrl.accion(EventosControlador.CONSULTAR_TODOS_MODELOS, null);
			}
		});

		botones.add(consultarTodos);

		// -------------------------------------------
		JButton modificar = new JButton("MODIFICAR MODELO");

		modificar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_MODIFICAR_MODELO, null);
			}
		});

		botones.add(modificar);

		// -------------------------------------------
		JButton vincular = new JButton("VINCULAR MODELO");

		vincular.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_VINCULAR_MODELO, null);
			}
		});

		botones.add(vincular);

		// -------------------------------------------
		JButton desvincular = new JButton("DESVINCULAR MODELO");

		desvincular.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_DESVINCULAR_MODELO, null);
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
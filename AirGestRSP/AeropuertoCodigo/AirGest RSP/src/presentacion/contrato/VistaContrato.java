package presentacion.contrato;

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

public class VistaContrato extends JFrame implements Observador{

	private static final long serialVersionUID = 1L;

	@Override
	public void actualizaVista(Object datos) {
		UtilidadesP.setAirGestRSP(this);
		this.setSize(400, 370);
		JPanel principal = new JPanel();
		principal.setLayout(new BorderLayout());

		JPanel page_start_panel = new JPanel();
		page_start_panel.setLayout(new BoxLayout(page_start_panel,BoxLayout.PAGE_AXIS));
		
		JPanel panel_label_contrato = new JPanel();
		JLabel contrato = new JLabel("CONTRATO"); //titulo de la ventana en la que estamos, apareceran las funciones de contrato 
		contrato.setFont(new Font("Comic Sans", Font.BOLD, 30));
		contrato.setHorizontalAlignment(SwingConstants.CENTER);
		contrato.setBorder(new LineBorder(Color.BLACK,2));
		panel_label_contrato.add(contrato);
		
		JSeparator separador_contrato= new JSeparator(SwingConstants.CENTER);
		separador_contrato.setBorder(new MatteBorder(1,1,10,10, Color.BLACK));
		separador_contrato.setPreferredSize(new Dimension(0,2));
		page_start_panel.add(panel_label_contrato);
		page_start_panel.add(separador_contrato);
		principal.add(page_start_panel, BorderLayout.PAGE_START);
		
		
		JPanel botones = new JPanel();
		botones.setLayout(new GridLayout(0, 1, 7, 7));

		Controlador ctrl = Controlador.getInstance();

		JButton abrir = new JButton("ABRIR CONTRATO");
		abrir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_ABRIR_CONTRATO, null);
			}
		});
		botones.add(abrir);

		//-------------------------------------------
		JButton consultar = new JButton("CONSULTAR CONTRATO POR ID");
		consultar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_CONSULTAR_CONTRATO_POR_ID, null);
			}
		});
		botones.add(consultar);

		//-------------------------------------------
		JButton consultarTodos = new JButton("CONSULTAR CONTRATOS");

		consultarTodos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.CONSULTAR_TODOS_CONTRATOS, null);
			}
		});
		botones.add(consultarTodos);

		//-------------------------------------------
		JButton modificarContrato = new JButton("MODIFICAR CONTRATO");

		modificarContrato.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_MODIFICAR_CONTRATO, null);
			}
		});

		botones.add(modificarContrato);

		//-------------------------------------------
		JButton modificarLinea = new JButton("MODIFICAR LINEA CONTRATO");

		modificarLinea.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_MODIFICAR_LINEA_CONTRATO, null);
			}
		});

		botones.add(modificarLinea);

		//-------------------------------------------
		JButton consultarPorAerolinea = new JButton("CONSULTAR CONTRATOS POR AEROLINEA");

		consultarPorAerolinea.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				ctrl.accion(EventosControlador.VISTA_MOSTRAR_CONTRATOS_POR_AEROLINEA, null);
			}
		});

		botones.add(consultarPorAerolinea);

		principal.add(botones, BorderLayout.CENTER);

		//-----------------------------------------------------
		
		JPanel panel_atras = new JPanel();
		JButton atras = new JButton("ATRAS"); //boton para volver a la ventana principal
		atras.setToolTipText("Esto vuelve a la ventana anterior");
		atras.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				ctrl.accion(EventosControlador.VISTA_PRINCIPAL, null);
				dispose();
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


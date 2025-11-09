package presentacion.modelo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import negocio.modelo.TModelo;
import presentacion.Observador;
import presentacion.UtilidadesP;
import presentacion.controlador.Controlador;
import presentacion.controlador.EventosControlador;

public class VistaAltaModelo extends JFrame implements Observador {

	private static final long serialVersionUID = 1L;

	@Override
	public void actualizaVista(Object datos) {
		UtilidadesP.setAirGestRSP(this);
		this.setSize(320, 200);

		JPanel principal = new JPanel();
		principal.setLayout(new BoxLayout(principal, BoxLayout.PAGE_AXIS));

		JPanel funcion = new JPanel();
		funcion.setLayout(new BoxLayout(funcion,BoxLayout.PAGE_AXIS));
		
		JPanel panel_titulo = new JPanel();
		JLabel titulo = new JLabel("Alta Modelo");
		titulo.setFont(new Font("Tahoma", Font.BOLD, 30));
		titulo.setBorder(new LineBorder(Color.BLACK, 2));
		titulo.setHorizontalAlignment(SwingConstants.CENTER);
		panel_titulo.add(titulo);
		
		funcion.add(panel_titulo);
		
		SpringLayout layout = new SpringLayout();
		JPanel centro = new JPanel();
		centro.setLayout(layout);// BoxLayout(centro, BoxLayout.PAGE_AXIS)

		// JPanel nombre = new JPanel();
		// nombre.setLayout(new BoxLayout(nombre, BoxLayout.LINE_AXIS));
		JLabel etiquetaNombre = new JLabel("nombre:");
		etiquetaNombre.setFont(new Font("Times New Roman", Font.BOLD, 25));
		JTextField textoNombre = new JTextField();
		textoNombre.setToolTipText("letras-numeros");
		
		textoNombre.setMaximumSize(new Dimension(200, 30));
		textoNombre.setMinimumSize(new Dimension(200, 30));
		textoNombre.setPreferredSize(new Dimension(200, 30));
		textoNombre.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		centro.add(etiquetaNombre);
		centro.add(textoNombre);
		layout.putConstraint(SpringLayout.WEST, textoNombre, 5, SpringLayout.EAST, etiquetaNombre);

		JLabel etiquetaMotor = new JLabel("motor:");
		etiquetaMotor.setFont(new Font("Times New Roman", Font.BOLD, 25));
		JTextField textoMotor = new JTextField();
		textoMotor.setToolTipText("{3}MAYUS-{2}NUM");
		textoMotor.setMaximumSize(new Dimension(200, 30));
		textoMotor.setMinimumSize(new Dimension(200, 30));
		textoMotor.setPreferredSize(new Dimension(200, 30));
		textoMotor.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		
		layout.putConstraint(SpringLayout.NORTH, etiquetaMotor, 2, SpringLayout.SOUTH, etiquetaNombre);
		layout.putConstraint(SpringLayout.NORTH, textoMotor, 2, SpringLayout.SOUTH, textoNombre);
		layout.putConstraint(SpringLayout.WEST, textoMotor, 24, SpringLayout.EAST, etiquetaMotor);
		
		centro.add(etiquetaMotor);
		centro.add(textoMotor);
		// centro.add(motor);

		Controlador controlador = Controlador.getInstance();

		principal.add(funcion);
		principal.add(centro);
		JSplitPane botones = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JButton aceptar = new JButton("ACEPTAR");
		aceptar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String nombreLeido = textoNombre.getText();
				String motorLeido = textoMotor.getText();
				TModelo transfer = new TModelo(0, nombreLeido, motorLeido, true);
				controlador.accion(EventosControlador.ALTA_MODELO, transfer);
			}

		});

		aceptar.setMaximumSize(new Dimension(100, 30));
		aceptar.setPreferredSize(new Dimension(100, 30));
		botones.setMaximumSize(new Dimension(190, 30));
		botones.setPreferredSize(new Dimension(190, 30));

		JButton atras = new JButton("ATRAS"); // boton para volver a la ventana
												// principal
		atras.setToolTipText("Esto vuelve a la ventana anterior");
		atras.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				controlador.accion(EventosControlador.VISTA_MODELO, null);
			}

		});

		atras.setMaximumSize(new Dimension(90, 30));
		atras.setPreferredSize(new Dimension(90, 30));

		botones.add(aceptar);
		botones.add(atras);
		principal.add(botones, BorderLayout.PAGE_END);

		this.setContentPane(principal);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setVisible(true);
		this.setLocation(200, 200);
	}

}
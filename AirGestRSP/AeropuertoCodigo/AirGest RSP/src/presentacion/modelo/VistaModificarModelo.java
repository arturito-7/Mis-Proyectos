package presentacion.modelo;


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
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import negocio.modelo.TModelo;
import presentacion.Observador;
import presentacion.UtilidadesP;
import presentacion.controlador.Controlador;
import presentacion.controlador.EventosControlador;

public class VistaModificarModelo extends JFrame implements Observador {

	private static final long serialVersionUID = 1L;

	public void actualizaVista(Object datos) {
		UtilidadesP.setAirGestRSP(this);

		JPanel principal = new JPanel();
		principal.setLayout(new BoxLayout(principal, BoxLayout.PAGE_AXIS));

		JPanel funcion = new JPanel();
		funcion.setLayout(new BoxLayout(funcion,BoxLayout.PAGE_AXIS));
		
		JPanel centro = new JPanel();
		
		JPanel panel_titulo = new JPanel();
		JLabel titulo = new JLabel("Modificar Modelo");
		titulo.setFont(new Font("Tahoma", Font.BOLD, 30));
		titulo.setBorder(new LineBorder(Color.BLACK, 2));
		titulo.setHorizontalAlignment(SwingConstants.CENTER);
		panel_titulo.add(titulo);
		
		funcion.add(panel_titulo);
		
		JPanel panel_etiquetas = new JPanel();
		panel_etiquetas.setLayout(new BoxLayout(panel_etiquetas, BoxLayout.PAGE_AXIS));
		
		JPanel panel_textfield = new JPanel();
		panel_textfield.setLayout(new BoxLayout(panel_textfield, BoxLayout.PAGE_AXIS));
		
		JLabel etiquetaId = new JLabel("id: ");
		etiquetaId.setFont(new Font("Tahoma", Font.BOLD, 25));
		JTextField textoId = new JTextField();
		textoId.setMaximumSize(new Dimension(200, 30));
		textoId.setMinimumSize(new Dimension(200, 30));
		textoId.setPreferredSize(new Dimension(200, 30));
		textoId.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		panel_etiquetas.add(etiquetaId);
		panel_textfield.add(textoId);
		
		centro.add(panel_etiquetas);
		centro.add(panel_textfield);

		
		JLabel etiquetaNombre = new JLabel("nombre:");
		etiquetaNombre.setFont(new Font("Tahoma", Font.BOLD, 25));
		JTextField textoNombre = new JTextField();
		textoNombre.setMaximumSize(new Dimension(200, 30));
		textoNombre.setMinimumSize(new Dimension(200, 30));
		textoNombre.setPreferredSize(new Dimension(200, 30));
		textoNombre.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		panel_etiquetas.add(etiquetaNombre);
		panel_textfield.add(textoNombre);
		
		centro.add(panel_etiquetas);
		centro.add(panel_textfield);


		JLabel etiquetaMotor = new JLabel("motor:");
		etiquetaMotor.setFont(new Font("Tahoma", Font.BOLD, 25));
		JTextField textoMotor = new JTextField();
		textoMotor.setMaximumSize(new Dimension(200, 30));
		textoMotor.setMinimumSize(new Dimension(200, 30));
		textoMotor.setPreferredSize(new Dimension(200, 30));
		textoMotor.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		panel_etiquetas.add(etiquetaMotor);
		panel_textfield.add(textoMotor);
		
		centro.add(panel_etiquetas);
		centro.add(panel_textfield);
		
		
		principal.add(funcion);
		principal.add(centro);
		
		Controlador controlador = Controlador.getInstance();

		JSplitPane botones = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JButton aceptar = new JButton("ACEPTAR");
		aceptar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int idLeido = Integer.parseInt(textoId.getText());
					String nombreLeido = textoNombre.getText();
					String motorLeido = textoMotor.getText();
					TModelo transfer = new TModelo(idLeido, nombreLeido, motorLeido, true);
					controlador.accion(EventosControlador.MODIFICAR_MODELO, transfer);
				} catch (NumberFormatException n) {
					controlador.accion(EventosControlador.MODIFICAR_MODELO, new TModelo());
				}

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
		principal.add(botones);

		this.setContentPane(principal);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setVisible(true);
		this.setLocation(200, 200);
		this.pack();
		this.setResizable(false);
	}
}
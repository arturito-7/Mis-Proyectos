
package presentacion.hangar;

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

import negocio.hangar.THangar;
import presentacion.Observador;
import presentacion.UtilidadesP;
import presentacion.controlador.Controlador;
import presentacion.controlador.EventosControlador;

public class VistaAltaHangar extends JFrame implements Observador {

	private static final long serialVersionUID = 1L;

	public void actualizaVista(Object datos) {
		UtilidadesP.setAirGestRSP(this);

		JPanel principal = new JPanel();
		principal.setLayout(new BoxLayout(principal, BoxLayout.PAGE_AXIS));

		JPanel funcion = new JPanel();
		funcion.setLayout(new BoxLayout(funcion, BoxLayout.PAGE_AXIS));

		JPanel centro = new JPanel();

		JPanel panel_titulo = new JPanel();
		
		JLabel titulo = new JLabel("Alta Hangar");
		titulo.setFont(new Font("Tahoma", Font.BOLD, 30));
		titulo.setBorder(new LineBorder(Color.BLACK, 2));
		titulo.setHorizontalAlignment(SwingConstants.CENTER);
		panel_titulo.add(titulo);

		funcion.add(panel_titulo);

		JPanel panel_etiquetas = new JPanel();
		panel_etiquetas.setLayout(new BoxLayout(panel_etiquetas, BoxLayout.PAGE_AXIS));
		
		JPanel panel_textfield = new JPanel();
		panel_textfield.setLayout(new BoxLayout(panel_textfield, BoxLayout.PAGE_AXIS));
		
		JLabel etiquetaDir = new JLabel("direccion: ");
		etiquetaDir.setFont(new Font("Tahoma", Font.BOLD, 23));
		JTextField textoDir = new JTextField();
		textoDir.setMaximumSize(new Dimension(200, 30));
		textoDir.setMinimumSize(new Dimension(200, 30));
		textoDir.setPreferredSize(new Dimension(200, 30));
		textoDir.setFont(new Font("Tahoma", Font.BOLD, 18));
		textoDir.setToolTipText("letraSy/oNumeros");
		
		panel_etiquetas.add(etiquetaDir);
		panel_textfield.add(textoDir);
		
		centro.add(panel_etiquetas);
		centro.add(panel_textfield);

		
		JLabel etiquetaStock = new JLabel("stock: ");
		etiquetaStock.setFont(new Font("Tahoma", Font.BOLD, 23));
		JTextField textoStock = new JTextField();
		textoStock.setMaximumSize(new Dimension(200, 30));
		textoStock.setMinimumSize(new Dimension(200, 30));
		textoStock.setPreferredSize(new Dimension(200, 30));
		textoStock.setFont(new Font("Tahoma", Font.BOLD, 18));
		textoStock.setToolTipText("numero natural con 0");
		
		panel_etiquetas.add(etiquetaStock);
		panel_textfield.add(textoStock);
		
		centro.add(panel_etiquetas);
		centro.add(panel_textfield);

		
		JLabel etiquetacosteDia = new JLabel("costeDia: ");
		etiquetacosteDia.setFont(new Font("Tahoma", Font.BOLD, 23));
		JTextField textocosteDia = new JTextField();
		textocosteDia.setMaximumSize(new Dimension(200, 30));
		textocosteDia.setMinimumSize(new Dimension(200, 30));
		textocosteDia.setPreferredSize(new Dimension(200, 30));
		textocosteDia.setFont(new Font("Tahoma", Font.BOLD, 18));
		textocosteDia.setToolTipText("numero double");
		
		panel_etiquetas.add(etiquetacosteDia);
		panel_textfield.add(textocosteDia);
		
		centro.add(panel_etiquetas);
		centro.add(panel_textfield);

		
		JLabel etiquetaespacioAlmacenaje = new JLabel("espacio almacenaje:    ");
		etiquetaespacioAlmacenaje.setFont(new Font("Tahoma", Font.BOLD, 23));
		JTextField textoespacioAlmacenaje = new JTextField();
		textoespacioAlmacenaje.setMaximumSize(new Dimension(200, 30));
		textoespacioAlmacenaje.setMinimumSize(new Dimension(200, 30));
		textoespacioAlmacenaje.setPreferredSize(new Dimension(200, 30));
		textoespacioAlmacenaje.setFont(new Font("Tahoma", Font.BOLD, 18));
		textoespacioAlmacenaje.setToolTipText("numero natural sin 0");
		
		panel_etiquetas.add(etiquetaespacioAlmacenaje);
		panel_textfield.add(textoespacioAlmacenaje);
		
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
					String dirLeido = textoDir.getText();
					int stockLeido = Integer.parseInt(textoStock.getText());
					double costeLeido = Double.parseDouble(textocosteDia.getText());// tiene
																					// que
																					// ser
																					// con
																					// .,
																					// no
																					// con
																					// ,
																					// xq
																					// sino
																					// da
																					// error
					int espacioLeido = Integer.parseInt(textoespacioAlmacenaje.getText());
					THangar transfer = new THangar(0, dirLeido, stockLeido, costeLeido, espacioLeido, true);
					controlador.accion(EventosControlador.ALTA_HANGAR, transfer);
				} catch (NumberFormatException n) {
					THangar hangar = new THangar(0, "", 0, 0, 0, false);
					controlador.accion(EventosControlador.ALTA_HANGAR, hangar);
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
				controlador.accion(EventosControlador.VISTA_HANGAR, null);
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
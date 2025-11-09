
package presentacion.contrato;

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

import negocio.contrato.TCarrito;
import negocio.contrato.TContrato;
import presentacion.Observador;
import presentacion.UtilidadesP;
import presentacion.controlador.Controlador;
import presentacion.controlador.EventosControlador;

public class VistaModificarContrato extends JFrame implements Observador {

	private static final long serialVersionUID = 1L;

	public void actualizaVista(Object datos) {
		UtilidadesP.setAirGestRSP(this);
		this.setSize(420, 250);

		Controlador controlador = Controlador.getInstance();
		TCarrito carrito = (TCarrito) datos;

		JPanel principal = new JPanel();
		principal.setLayout(new BoxLayout(principal, BoxLayout.PAGE_AXIS));

		JPanel funcion = new JPanel();
		funcion.setLayout(new BoxLayout(funcion,BoxLayout.PAGE_AXIS));
		
		JPanel centro = new JPanel();
		
		JPanel panel_titulo = new JPanel();
		JLabel titulo = new JLabel("Modificar Contrato");
		titulo.setFont(new Font("Tahoma", Font.BOLD, 30));
		titulo.setBorder(new LineBorder(Color.BLACK, 2));
		titulo.setHorizontalAlignment(SwingConstants.CENTER);
		panel_titulo.add(titulo);
		
		funcion.add(panel_titulo);

		JPanel panel_etiquetas = new JPanel();
		panel_etiquetas.setLayout(new BoxLayout(panel_etiquetas, BoxLayout.PAGE_AXIS));
		
		JPanel panel_textfield = new JPanel();
		panel_textfield.setLayout(new BoxLayout(panel_textfield, BoxLayout.PAGE_AXIS));
		
		JLabel etiquetaId = new JLabel("id contrato: ");
		etiquetaId.setFont(new Font("Tahoma", Font.BOLD, 23));
		JTextField textoId = new JTextField();
		textoId.setMaximumSize(new Dimension(200, 30));
		textoId.setMinimumSize(new Dimension(200, 30));
		textoId.setPreferredSize(new Dimension(200, 30));
		textoId.setFont(new Font("Tahoma", Font.BOLD, 18));
		panel_etiquetas.add(etiquetaId);
		panel_textfield.add(textoId);
		
		centro.add(panel_etiquetas);
		centro.add(panel_textfield);

		
		JLabel etiquetaAerolinea = new JLabel("id aerolinea: ");
		etiquetaAerolinea.setFont(new Font("Tahoma", Font.BOLD, 23));
		JTextField textoAerolinea = new JTextField();
		textoAerolinea.setMaximumSize(new Dimension(200, 30));
		textoAerolinea.setMinimumSize(new Dimension(200, 30));
		textoAerolinea.setPreferredSize(new Dimension(200, 30));
		textoId.setFont(new Font("Tahoma", Font.BOLD, 18));
		panel_etiquetas.add(etiquetaAerolinea);
		panel_textfield.add(textoAerolinea);
		
		centro.add(panel_etiquetas);
		centro.add(panel_textfield);

		JPanel precio = new JPanel();
		precio.setLayout(new BoxLayout(precio, BoxLayout.LINE_AXIS));
		JLabel etiquetaPrecio = new JLabel("precio: ");
		etiquetaPrecio.setFont(new Font("Tahoma", Font.BOLD, 23));
		JTextField textoPrecio = new JTextField();
		textoPrecio.setMaximumSize(new Dimension(200, 30));
		textoPrecio.setMinimumSize(new Dimension(200, 30));
		textoPrecio.setPreferredSize(new Dimension(200, 30));
		textoId.setFont(new Font("Tahoma", Font.BOLD, 18));
		panel_etiquetas.add(etiquetaPrecio);
		panel_textfield.add(textoPrecio);
		
		centro.add(panel_etiquetas);
		centro.add(panel_textfield);
		
		principal.add(funcion);
		principal.add(centro);

		JSplitPane botones = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JButton aceptar = new JButton("ACEPTAR");
		aceptar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int id_contrato = Integer.parseInt(textoId.getText());
					int id_aerolinea = Integer.parseInt(textoAerolinea.getText());
					double precio = Double.parseDouble(textoPrecio.getText());
					TContrato c = new TContrato(id_contrato, id_aerolinea, precio);
					controlador.accion(EventosControlador.MODIFICAR_CONTRATO, c);
				} catch (NumberFormatException n) {

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
				controlador.accion(EventosControlador.VISTA_CONTRATO, carrito);
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
		this.setResizable(false);
	}
}
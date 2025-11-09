package presentacion.personal;

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

import negocio.modeloAerolinea.TModeloAerolinea;
import negocio.personalHangar.TPersonalHangar;
import presentacion.Observador;
import presentacion.UtilidadesP;
import presentacion.controlador.Controlador;
import presentacion.controlador.EventosControlador;

public class VistaVincularPersonal extends JFrame implements Observador {

	private static final long serialVersionUID = 1L;

	public void actualizaVista(Object datos) {
		UtilidadesP.setAirGestRSP(this);

		this.setSize(470, 250);

		JPanel principal = new JPanel();
		principal.setLayout(new BoxLayout(principal, BoxLayout.PAGE_AXIS));

		JPanel funcion = new JPanel();
		funcion.setLayout(new BoxLayout(funcion,BoxLayout.PAGE_AXIS));
		
		JPanel centro = new JPanel();
		
		JPanel panel_titulo = new JPanel();
		JLabel titulo = new JLabel("Vincular Personal");
		titulo.setFont(new Font("Tahoma", Font.BOLD, 30));
		titulo.setBorder(new LineBorder(Color.BLACK, 2));
		titulo.setHorizontalAlignment(SwingConstants.CENTER);
		panel_titulo.add(titulo);
		
		funcion.add(panel_titulo);
		

		JPanel panel_etiquetas = new JPanel();
		panel_etiquetas.setLayout(new BoxLayout(panel_etiquetas, BoxLayout.PAGE_AXIS));
		
		JPanel panel_textfield = new JPanel();
		panel_textfield.setLayout(new BoxLayout(panel_textfield, BoxLayout.PAGE_AXIS));
		
		JLabel etiquetaIdPersonal = new JLabel("idPersonal: ");
		etiquetaIdPersonal.setFont(new Font("Tahoma", Font.BOLD, 24));
		JTextField textoIdPersonal = new JTextField();
		textoIdPersonal.setMaximumSize(new Dimension(200, 30));
		textoIdPersonal.setMinimumSize(new Dimension(200, 30));
		textoIdPersonal.setPreferredSize(new Dimension(200, 30));
		textoIdPersonal.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		panel_etiquetas.add(etiquetaIdPersonal);
		panel_textfield.add(textoIdPersonal);
		
		centro.add(panel_etiquetas);
		centro.add(panel_textfield);

		JPanel idHangar = new JPanel();
		idHangar.setLayout(new BoxLayout(idHangar, BoxLayout.LINE_AXIS));
		JLabel etiquetaIdHangar = new JLabel("idHangar: ");
		etiquetaIdHangar.setFont(new Font("Tahoma", Font.BOLD, 24));
		JTextField textoIdHangar = new JTextField();
		textoIdHangar.setMaximumSize(new Dimension(200, 30));
		textoIdHangar.setMinimumSize(new Dimension(200, 30));
		textoIdHangar.setPreferredSize(new Dimension(200, 30));
		textoIdHangar.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		panel_etiquetas.add(etiquetaIdHangar);
		panel_textfield.add(textoIdHangar);
		
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
					int idPersonalLeido = Integer.valueOf(textoIdPersonal.getText());
					int idHangarLeido = Integer.valueOf(textoIdHangar.getText());
					TPersonalHangar transfer = new TPersonalHangar(idPersonalLeido, idHangarLeido);
					controlador.accion(EventosControlador.VINCULAR_PERSONAL, transfer);
				} catch (NumberFormatException n) {
					controlador.accion(EventosControlador.VINCULAR_PERSONAL, new TModeloAerolinea());
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
				controlador.accion(EventosControlador.VISTA_PERSONAL, null);
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
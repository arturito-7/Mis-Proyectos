
package presentacion.contrato;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import negocio.lineaContrato.TLineaContrato;
import presentacion.Observador;
import presentacion.UtilidadesP;
import presentacion.controlador.Controlador;
import presentacion.controlador.EventosControlador;

public class VistaModificarLineaContrato extends JFrame implements Observador {

	private static final long serialVersionUID = 1L;

	public void actualizaVista(Object datos) {
		UtilidadesP.setAirGestRSP(this);
		this.setSize(450, 300);

		Controlador controlador = Controlador.getInstance();

		JPanel principal = new JPanel();
		principal.setLayout(new BoxLayout(principal, BoxLayout.PAGE_AXIS));

		JPanel funcion = new JPanel();
		funcion.setLayout(new BoxLayout(funcion,BoxLayout.PAGE_AXIS));
		
		JPanel centro = new JPanel();
		
		JPanel panel_titulo = new JPanel();
		JLabel titulo = new JLabel("Modificar linea de Contrato");
		titulo.setFont(new Font("Tahoma", Font.BOLD, 30));
		titulo.setBorder(new LineBorder(Color.BLACK, 2));
		titulo.setHorizontalAlignment(SwingConstants.CENTER);
		panel_titulo.add(titulo);
		
		funcion.add(panel_titulo);

		JPanel panel_etiquetas = new JPanel();
		panel_etiquetas.setLayout(new BoxLayout(panel_etiquetas, BoxLayout.PAGE_AXIS));
		
		JPanel panel_textfield = new JPanel();
		panel_textfield.setLayout(new BoxLayout(panel_textfield, BoxLayout.PAGE_AXIS));
		
		JLabel etiquetaContrato = new JLabel("id contrato: ");
		etiquetaContrato.setFont(new Font("Tahoma", Font.BOLD, 23));
		JTextField textoContrato = new JTextField();
		textoContrato.setMaximumSize(new Dimension(200, 30));
		textoContrato.setMinimumSize(new Dimension(200, 30));
		textoContrato.setPreferredSize(new Dimension(200, 30));
		textoContrato.setFont(new Font("Tahoma", Font.BOLD, 18));
		panel_etiquetas.add(etiquetaContrato);
		panel_textfield.add(textoContrato);
		
		centro.add(panel_etiquetas);
		centro.add(panel_textfield);


		
		JLabel etiquetaId = new JLabel("id hangar: ");
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

		
		JLabel etiquetaIni = new JLabel("fecha inicio: ");
		etiquetaIni.setFont(new Font("Tahoma", Font.BOLD, 23));
		LocalDate currentDate = LocalDate.now();
		Date initialDate = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());// pasarlo
																									// a
																									// Date
																									// para
																									// el
																									// spinner
		SpinnerDateModel model = new SpinnerDateModel(initialDate, null, null, java.util.Calendar.DAY_OF_MONTH);
		JSpinner spinnerIni = new JSpinner(model);
		spinnerIni.setMaximumSize(new Dimension(200, 30));
		spinnerIni.setMinimumSize(new Dimension(200, 30));
		spinnerIni.setPreferredSize(new Dimension(200, 30));
		JSpinner.DateEditor editor = new JSpinner.DateEditor(spinnerIni, "dd/MM/yyyy");// formato
																						// de
																						// la
																						// fecha
		spinnerIni.setEditor(editor);
		
		panel_etiquetas.add(etiquetaIni);
		panel_textfield.add(spinnerIni);
		
		centro.add(panel_etiquetas);
		centro.add(panel_textfield);

		JPanel fecha_fin = new JPanel();
		fecha_fin.setLayout(new BoxLayout(fecha_fin, BoxLayout.LINE_AXIS));
		JLabel etiquetaFin = new JLabel("fecha fin: ");
		etiquetaFin.setFont(new Font("Tahoma", Font.BOLD, 23));
		SpinnerDateModel model2 = new SpinnerDateModel(initialDate, null, null, java.util.Calendar.DAY_OF_MONTH);
		JSpinner spinnerFin = new JSpinner(model2);
		spinnerFin.setMaximumSize(new Dimension(200, 30));
		spinnerFin.setMinimumSize(new Dimension(200, 30));
		spinnerFin.setPreferredSize(new Dimension(200, 30));
		JSpinner.DateEditor editor2 = new JSpinner.DateEditor(spinnerFin, "dd/MM/yyyy");// formato
																						// de
																						// la
																						// fecha
		spinnerFin.setEditor(editor2);
		panel_etiquetas.add(etiquetaFin);
		panel_textfield.add(spinnerFin);
		
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
					int id_contrato = Integer.parseInt(textoContrato.getText());
					int id_hangar = Integer.parseInt(textoId.getText());
					Date seleccion = (Date) spinnerIni.getValue();
					ZonedDateTime zonedDateTime = seleccion.toInstant().atZone(ZoneId.systemDefault());// para
																										// transformar
																										// date
																										// a
																										// localDate
					LocalDate fecha_ini = zonedDateTime.toLocalDate();
					seleccion = (Date) spinnerFin.getValue();
					zonedDateTime = seleccion.toInstant().atZone(ZoneId.systemDefault());
					LocalDate fecha_fin = zonedDateTime.toLocalDate();
					TLineaContrato linea = new TLineaContrato(id_contrato, id_hangar, fecha_ini, fecha_fin, 0);
					controlador.accion(EventosControlador.MODIFICAR_LINEA_CONTRATO, linea);
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
				controlador.accion(EventosControlador.VISTA_CONTRATO, null);
			}

		});

		atras.setMaximumSize(new Dimension(90, 30));
		atras.setPreferredSize(new Dimension(90, 30));

		botones.add(aceptar);
		botones.add(atras);
		principal.add(botones);

		this.setContentPane(principal);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setLocation(200, 200);
	}
}
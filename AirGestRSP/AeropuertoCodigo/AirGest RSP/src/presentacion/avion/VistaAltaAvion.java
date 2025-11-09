
package presentacion.avion;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
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
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import negocio.avion.TAComercial;
import negocio.avion.TAPrivado;
import negocio.avion.TAvion;
import presentacion.Observador;
import presentacion.UtilidadesP;
import presentacion.controlador.Controlador;
import presentacion.controlador.EventosControlador;

public class VistaAltaAvion extends JFrame implements Observador {

	private static final long serialVersionUID = 1L;

	public void actualizaVista(Object datos) {
		UtilidadesP.setAirGestRSP(this);
		this.setSize(450, 250);
		
		Controlador ctrl = Controlador.getInstance();


		JPanel principal = new JPanel();
		principal.setLayout(new BoxLayout(principal, BoxLayout.PAGE_AXIS));

		JPanel funcion = new JPanel();
		funcion.setLayout(new BoxLayout(funcion,BoxLayout.PAGE_AXIS));
		
		JPanel panel_titulo = new JPanel();
		JLabel titulo = new JLabel("Alta Avion");
		titulo.setFont(new Font("Tahoma", Font.BOLD, 30));
		titulo.setBorder(new LineBorder(Color.BLACK, 2));
		titulo.setHorizontalAlignment(SwingConstants.CENTER);
		panel_titulo.add(titulo);
		
		funcion.add(panel_titulo);
		principal.add(funcion);

		JPanel centro = new JPanel();
		centro.setLayout(new BoxLayout(centro, BoxLayout.LINE_AXIS));
		centro.setAlignmentX(CENTER_ALIGNMENT);
		principal.add(centro);

		JPanel panelBotones = new JPanel();
		principal.add(panelBotones);
		panelBotones.setAlignmentX(CENTER_ALIGNMENT);

		JButton atras = new JButton("ATRAS"); // boton para volver a la ventana
												// principal
		atras.setToolTipText("Esto vuelve a la ventana anterior");
		atras.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				if (datos == null)
					ctrl.accion(EventosControlador.VISTA_AVION, null);
				else
					ctrl.accion(EventosControlador.VISTA_ALTA_AVION, null);
			}

		});
		panelBotones.add(atras);

		if (datos == null) {
			JPanel botones = new JPanel();
			botones.setLayout(new GridLayout(0,1,8,8));

			// -------------------------------------------
			JButton comercial = new JButton("AVION COMERCIAL");

			comercial.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
					ctrl.accion(EventosControlador.VISTA_ALTA_AVION, "COMERCIAL");
				}
			});

			comercial.setAlignmentX(CENTER_ALIGNMENT);
			botones.add(comercial);

			// -------------------------------------------
			JButton privado = new JButton("AVION PRIVADO");

			privado.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
					ctrl.accion(EventosControlador.VISTA_ALTA_AVION, "PRIVADO");
				}
			});

			privado.setAlignmentX(CENTER_ALIGNMENT);
			botones.setAlignmentX(CENTER_ALIGNMENT);
			botones.add(privado);
			botones.add(panelBotones);

			principal.add(botones);

			// -----------------------------------------------------
		} else { //
			JPanel panelEtiquetas = new JPanel();
			panelEtiquetas.setLayout(new BoxLayout(panelEtiquetas, BoxLayout.PAGE_AXIS));
			panelEtiquetas.setAlignmentX(CENTER_ALIGNMENT);

			JPanel panelTexto = new JPanel();
			panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.PAGE_AXIS));
			panelTexto.setAlignmentX(CENTER_ALIGNMENT);

			JLabel etiquetaNombre = new JLabel("Nombre: ");
			etiquetaNombre.setFont(new Font("Tahoma", Font.BOLD, 25));
			JTextField textoNombre = new JTextField();
			textoNombre.setMaximumSize(new Dimension(200, 30));
			textoNombre.setMinimumSize(new Dimension(200, 30));
			textoNombre.setPreferredSize(new Dimension(200, 30));
			textoNombre.setFont(new Font("Tahoma", Font.BOLD, 18));
			panelEtiquetas.add(etiquetaNombre);
			panelTexto.add(textoNombre);
			centro.add(panelEtiquetas);
			centro.add(panelTexto);

			JLabel etiquetaMatricula = new JLabel("Matricula:    ");
			etiquetaMatricula.setFont(new Font("Tahoma", Font.BOLD, 25));
			JTextField textoMatricula = new JTextField();
			textoMatricula.setMaximumSize(new Dimension(200, 30));
			textoMatricula.setMinimumSize(new Dimension(200, 30));
			textoMatricula.setPreferredSize(new Dimension(200, 30));
			textoMatricula.setFont(new Font("Tahoma", Font.BOLD, 18));
			textoMatricula.setToolTipText("EC-LeTrasNumeros");
			panelEtiquetas.add(etiquetaMatricula);
			panelTexto.add(textoMatricula);

			// SPINNER O COMBOBOX PARA LA FECHA
			JLabel etiquetaFecha = new JLabel("Fecha de Fabricación: ");
			etiquetaFecha.setFont(new Font("Tahoma", Font.BOLD, 25));
			LocalDate currentDate = LocalDate.now();
			Date initialDate = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());// pasarlo
																										// a
																										// Date
																										// para
																										// el
																										// spinner
			SpinnerDateModel model = new SpinnerDateModel(initialDate, null, initialDate,
					java.util.Calendar.DAY_OF_MONTH);
			JSpinner spinner = new JSpinner(model);
			spinner.setMaximumSize(new Dimension(200, 30));
			spinner.setMinimumSize(new Dimension(200, 30));
			spinner.setPreferredSize(new Dimension(200, 30));
			JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd/MM/yyyy");// formato
																						// de
																						// la
																						// fecha
			spinner.setEditor(editor);
			panelEtiquetas.add(etiquetaFecha);
			panelTexto.add(spinner);

			JLabel etiquetaAsientos = new JLabel("Número de asientos:    ");
			etiquetaAsientos.setFont(new Font("Tahoma", Font.BOLD, 25));
			JTextField textoAsientos = new JTextField();
			textoAsientos.setMaximumSize(new Dimension(200, 30));
			textoAsientos.setMinimumSize(new Dimension(200, 30));
			textoAsientos.setPreferredSize(new Dimension(200, 30));
			textoAsientos.setFont(new Font("Tahoma", Font.BOLD, 18));
			textoAsientos.setToolTipText("numero natural sin 0");
			panelEtiquetas.add(etiquetaAsientos);
			panelTexto.add(textoAsientos);

			JLabel etiquetaAerolinea = new JLabel("Id aerolínea:    ");
			etiquetaAerolinea.setFont(new Font("Tahoma", Font.BOLD, 25));
			JTextField textoAerolinea = new JTextField();
			textoAerolinea.setMaximumSize(new Dimension(200, 30));
			textoAerolinea.setMinimumSize(new Dimension(200, 30));
			textoAerolinea.setPreferredSize(new Dimension(200, 30));
			textoAerolinea.setFont(new Font("Tahoma", Font.BOLD, 18));
			textoAerolinea.setToolTipText("numero natural sin 0");
			panelEtiquetas.add(etiquetaAerolinea);
			panelTexto.add(textoAerolinea);

			JLabel etiquetaModelo = new JLabel("Id modelo:    ");
			etiquetaModelo.setFont(new Font("Tahoma", Font.BOLD, 25));
			JTextField textomodelo = new JTextField();
			textomodelo.setMaximumSize(new Dimension(200, 30));
			textomodelo.setMinimumSize(new Dimension(200, 30));
			textomodelo.setPreferredSize(new Dimension(200, 30));
			textomodelo.setFont(new Font("Tahoma", Font.BOLD, 18));
			textomodelo.setToolTipText("numero natural sin 0");
			panelEtiquetas.add(etiquetaModelo);
			panelTexto.add(textomodelo);

			JLabel etiquetaHangar = new JLabel("Id hangar:    ");
			etiquetaHangar.setFont(new Font("Tahoma", Font.BOLD, 25));
			JTextField textoHangar = new JTextField();
			textoHangar.setMaximumSize(new Dimension(200, 30));
			textoHangar.setMinimumSize(new Dimension(200, 30));
			textoHangar.setPreferredSize(new Dimension(200, 30));
			textoHangar.setFont(new Font("Tahoma", Font.BOLD, 18));
			textoHangar.setToolTipText("numero natural sin 0");
			panelEtiquetas.add(etiquetaHangar);
			panelTexto.add(textoHangar);

			JLabel etiquetaTrabajadores = new JLabel("Número de trabajadores:    ");
			etiquetaTrabajadores.setFont(new Font("Tahoma", Font.BOLD, 25));
			JTextField textoTrabajadores = new JTextField();
			textoTrabajadores.setMaximumSize(new Dimension(200, 30));
			textoTrabajadores.setMinimumSize(new Dimension(200, 30));
			textoTrabajadores.setPreferredSize(new Dimension(200, 30));
			textoTrabajadores.setFont(new Font("Tahoma", Font.BOLD, 18));
			textoTrabajadores.setToolTipText("numero natural sin 0");

			JLabel etiquetaDuenyo = new JLabel("Dueño:    ");
			etiquetaDuenyo.setFont(new Font("Tahoma", Font.BOLD, 25));
			JTextField textoDuenyo = new JTextField();
			textoDuenyo.setMaximumSize(new Dimension(200, 30));
			textoDuenyo.setMinimumSize(new Dimension(200, 30));
			textoDuenyo.setPreferredSize(new Dimension(200, 30));
			textoDuenyo.setFont(new Font("Tahoma", Font.BOLD, 18));

			JLabel etiquetaCarnet = new JLabel("Id carnet:    ");
			etiquetaCarnet.setFont(new Font("Tahoma", Font.BOLD, 25));
			JTextField textoCarnet = new JTextField();
			textoCarnet.setMaximumSize(new Dimension(200, 30));
			textoCarnet.setMinimumSize(new Dimension(200, 30));
			textoCarnet.setPreferredSize(new Dimension(200, 30));
			textoCarnet.setFont(new Font("Tahoma", Font.BOLD, 18));
			textoCarnet.setToolTipText("numero natural sin 0");

			if ("COMERCIAL" == datos) {
				panelEtiquetas.add(etiquetaTrabajadores);
				panelTexto.add(textoTrabajadores);
			} else {
				panelEtiquetas.add(etiquetaDuenyo);
				panelTexto.add(textoDuenyo);

				panelEtiquetas.add(etiquetaCarnet);
				panelTexto.add(textoCarnet);
			}

			JButton aceptar = new JButton("ACEPTAR");
			aceptar.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						String nombre = textoNombre.getText();
						String matricula = textoMatricula.getText();
						int numAsientos = Integer.valueOf(textoAsientos.getText());
						int idAerolinea = Integer.valueOf(textoAerolinea.getText());
						int idHangar = Integer.valueOf(textoHangar.getText());
						int idModelo = Integer.valueOf(textomodelo.getText());
						Date seleccion = (Date) spinner.getValue();
						ZonedDateTime zonedDateTime = seleccion.toInstant().atZone(ZoneId.systemDefault());// para
																											// transformar
																											// date
																											// a
																											// localDate
						LocalDate fecha = zonedDateTime.toLocalDate();

						TAvion transfer;
						if (datos == "COMERCIAL") {
							int trabajadores = Integer.valueOf(textoTrabajadores.getText());
							transfer = new TAComercial(0, numAsientos, fecha, nombre, matricula, true, idAerolinea,
									idModelo, idHangar, trabajadores);
						} else {
							String nombre_duenyo = textoDuenyo.getText();
							int idCarnet = Integer.valueOf(textoCarnet.getText());
							transfer = new TAPrivado(0, numAsientos, fecha, nombre, matricula, true, idAerolinea,
									idModelo, idHangar, nombre_duenyo, idCarnet);
						}
						ctrl.accion(EventosControlador.ALTA_AVION, transfer);
					} catch (Exception ex) {
						ctrl.accion(EventosControlador.ALTA_AVION, null);
					}
				}

			});

			panelBotones.add(aceptar);

		}

		this.setContentPane(principal);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setVisible(true);
		this.setLocation(200, 200);
		this.setResizable(false);
		this.pack();
	}

}
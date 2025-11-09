
package presentacion.contrato;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import negocio.contrato.TInfoContrato;
import presentacion.Observador;
import presentacion.UtilidadesP;

public class VistaResultadoConsultarContratoPorId extends JFrame implements Observador {
	
	private static final long serialVersionUID = 1L;

	public void actualizaVista(Object datos) {
		UtilidadesP.setAirGestRSP(this);
		
		JPanel principal = new JPanel();
		principal.setLayout(new BorderLayout());

		String contrato = datos == null ? "" : ((TInfoContrato) datos).toString();

		JTextArea exito = new JTextArea(contrato);
		exito.setFont(new Font("Tahoma", Font.PLAIN, 20));
		exito.setLineWrap(true); // Habilita el ajuste automático de línea
		exito.setWrapStyleWord(true); // Ajusta el texto en palabras completas
		exito.setEditable(false); // Hace que el JTextArea sea de solo lectura

		JScrollPane scroll = new JScrollPane(exito);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(500, 300)); // Ajusta el tamaño
															// del JScrollPane
		principal.add(scroll, BorderLayout.CENTER);

		this.setContentPane(principal);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setVisible(true);
		this.setLocation(200, 200);
		this.pack();
	}
}
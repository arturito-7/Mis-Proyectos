
package presentacion.hangar;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import negocio.hangar.THangar;
import presentacion.Observador;
import presentacion.UtilidadesP;

public class VistaResultadoConsultarHangarPorId extends JFrame implements Observador {

	private static final long serialVersionUID = 1L;

	public void actualizaVista(Object datos) {
		UtilidadesP.setAirGestRSP(this);

		JPanel principal = new JPanel();
		principal.setLayout(new BorderLayout());

		String hangar = datos == null ? "" : ((THangar) datos).toString();

		JTextArea exito = new JTextArea(hangar);
		exito.setEditable(false);
		exito.setFont(new Font("Tahoma", Font.PLAIN, 26));
		exito.setAlignmentX(CENTER_ALIGNMENT);
		principal.add(exito, BorderLayout.PAGE_START);

		this.setContentPane(principal);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setVisible(true);
		this.setLocation(200, 200);
		this.pack();

	}
}
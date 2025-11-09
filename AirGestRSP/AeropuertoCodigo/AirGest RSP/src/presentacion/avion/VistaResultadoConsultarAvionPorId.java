package presentacion.avion;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import negocio.avion.TAvion;
import presentacion.Observador;
import presentacion.UtilidadesP;

public class VistaResultadoConsultarAvionPorId extends JFrame implements Observador {

	private static final long serialVersionUID = 1L;

	public void actualizaVista(Object datos) {
		UtilidadesP.setAirGestRSP(this);
		
		JPanel principal = new JPanel();
		principal.setLayout(new BorderLayout());

		String avion = datos == null ? "" : ((TAvion) datos).toString();

		JTextArea exito = new JTextArea(avion);
		exito.setFont(new Font("Tahoma", Font.PLAIN, 20));
		exito.setAlignmentX(CENTER_ALIGNMENT);
		exito.setEditable(false);
		principal.add(exito, BorderLayout.PAGE_START);

		this.setContentPane(principal);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setVisible(true);
		this.setLocation(200, 200);
		this.pack();

	}

}
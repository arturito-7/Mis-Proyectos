package presentacion;

import java.awt.Toolkit;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class UtilidadesP {

	public static void setAirGestRSP(JFrame frame) {
		frame.setTitle(Mensajes.AIRGEST_RSP);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("recursos/iconos/avion.png"));
	}

	public static Icon getIcon(String name) {
		return new ImageIcon("recursos/iconos/" + name + ".png");
	}
}

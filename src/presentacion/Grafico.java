package presentacion;

import java.awt.BorderLayout;
import java.awt.Canvas;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Grafico extends JFrame {
	
	private JPanel panel;

	public Grafico(JPanel panel) {
		this.panel = panel;
		initComponents();
	}

	private void initComponents() {
		lienzo = new Canvas();

		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        
        getContentPane().add(panel, BorderLayout.CENTER);
		
		pack();
	}

	public Canvas getLienzo() {
		return lienzo;
	}

	private Canvas lienzo;
	
}

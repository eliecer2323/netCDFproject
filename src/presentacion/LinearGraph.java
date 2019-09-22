package presentacion;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class LinearGraph extends JPanel {
	
	private String[] data;
    
    public LinearGraph(String[] data) {
    	super();
    	
    	this.data = data;
    }
    
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		int width = this.getWidth();
		int height = this.getHeight();

		this.setOpaque(true);

		// clear the panel
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, width, height);
		
		double numero = Double.parseDouble(data[0]);
		double escala  = 1;
		if(numero>1000) {
			while(numero/escala > height) {
				escala *= 10;
			}
		} else {
			while(numero/escala < height/10) {
				escala /= 10;
			}
		}
		
		g2d.setColor(Color.black);
		int escalar = height;
		for(int i=1; escalar>0; i++) {
			escalar = (int) (height-i*100-20);
			g2d.drawString(escala*i*100+"", 5, (int) escalar);
		}
		
		g2d.fillRect(36, 10, 2, height-20);
		g2d.fillRect(16, height-20, width-30, 2);
		
		g2d.setColor(Color.red);
		for(int i=0; i<data.length; i++) {
			double datoDouble = Double.parseDouble(data[i]) / escala;
			int datoInt = height - (int) datoDouble - 30;
			g2d.fillOval(i*2+40, datoInt, 5, 5);
		}
    }

}

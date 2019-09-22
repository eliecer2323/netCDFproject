package presentacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;

class Controlador implements ActionListener {

	private Vista ventana;
	private Modelo modelo;

	public Controlador(Vista aThis) {
		ventana = aThis;
		modelo = ventana.getModelo();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Cargar":
			JFileChooser fileOpener = new JFileChooser(); 
            int r = fileOpener.showOpenDialog(null); 
            if (r == JFileChooser.APPROVE_OPTION) { 
                String ruta = fileOpener.getSelectedFile().getAbsolutePath(); 
                modelo.abrirNetcdf(ruta);
            } 
			break;
		case "Ver Datos":
			modelo.leerDatos();
			break;
		case "Ver Grafica":
			modelo.graficarDatos();
			break;
		default:
			break;
		}
	}

}

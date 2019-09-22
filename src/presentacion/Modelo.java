package presentacion;

import java.awt.Color;
import java.io.IOException;
import java.util.Arrays;

import logica.ArchivoNetcdf;
import logica.Gradiente;
import ucar.netcdf.DimensionIterator;
import ucar.netcdf.Netcdf;
import ucar.netcdf.NetcdfFile;
import ucar.netcdf.Variable;
import ucar.netcdf.VariableIterator;

public class Modelo {

	private Vista ventana;
	private ArchivoNetcdf nc;


	public Vista getVentana() {
		if (ventana == null) {
			ventana = new Vista(this);
		}
		return ventana;
	}

	public void iniciar() {
		getVentana().setSize(1350, 540);
		getVentana().setVisible(true);
	}

	public void abrirNetcdf(String fileName) {
		try {
			nc = new ArchivoNetcdf(fileName);
			VariableIterator vi = nc.getFile().iterator();
			getVentana().resetTable();
			while (vi.hasNext()) {
				Variable var = vi.next();
				String descripcion = var.getAttribute("long_name") != null ? var.getAttribute("long_name").getStringValue() : "";
				String unidades = var.getAttribute("units") != null ? var.getAttribute("units").getStringValue() : "";
				String nombre = var.getName();
				int[] sombra = var.getLengths();
				StringBuilder sombraText = new StringBuilder();
				for (int i = 0; i < sombra.length; i++) {
					sombraText.append(sombra[i]);
					if (i < sombra.length - 1) {
						sombraText.append(", ");
					}
				}
				DimensionIterator di = var.getDimensionIterator();
				StringBuilder dimensiones = new StringBuilder();
				while (di.hasNext()) {
					ucar.netcdf.Dimension dimension = di.next();
					dimensiones.append(dimension.getName());
					if (di.hasNext()) {
						dimensiones.append(", ");
					}
				}
				String[] rowData = { var.getComponentType().getName(), descripcion, dimensiones.toString(), nombre, sombraText.toString(), unidades };
				getVentana().getDataTable().addRow(rowData);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String leerDatos() {
		try {
			int rowIndex = getVentana().getTable().getSelectedRow();
			String name = getVentana().getTable().getValueAt(rowIndex, 3).toString();
			String dataVar = nc.cargarVar(name);
			
			getVentana().getTextArea().setText(name+" = ");
			getVentana().getTextArea().append(dataVar);
			
			return dataVar;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	

	public void graficarDatos() {
		String datos = leerDatos();
		String datosAjustados = datos.substring(datos.indexOf("[")+1, datos.lastIndexOf(", ]"));
		
		int rowIndex = getVentana().getTable().getSelectedRow();
		String name = getVentana().getTable().getValueAt(rowIndex, 3).toString();
		int[] nVar = nc.getVarLengths(name);
		
		if(nVar.length==1) {
			String[] data = datosAjustados.split(", ");
			graficarUnidimensional(data);
		} else if(nVar.length==2) {
			datosAjustados = datosAjustados.substring(datosAjustados.indexOf("[")+1, datosAjustados.lastIndexOf(", ]"));
			datosAjustados = datosAjustados.replace("[", "");
			String[] dataString = datosAjustados.split(", ],\n");
			double[][] data = new double[nVar[0]][nVar[1]];
			for(int i=0; i<dataString.length; i++) {
				data[i] = Arrays.stream(dataString[i].split(", "))
	                .mapToDouble(Double::parseDouble).toArray();
			}
			graficarBidimensional(data);		
		}
	}
	
	private void graficarUnidimensional(String[] data) {
		LinearGraph panel = new LinearGraph(data);
		
		Grafico grafico = new Grafico(panel);
		grafico.setSize(1024, 768);
		grafico.setLocation(100, 60);
		grafico.setVisible(true);
	}
	
	private void graficarBidimensional(double[][] data) {
		HeatMap panel = new HeatMap(data, true, Gradiente.createGradient(Color.BLACK, Color.WHITE, 500));
        
        panel.setDrawLegend(true);

        panel.setTitle("Height");
        panel.setDrawTitle(true);

        panel.setXAxisTitle("X-Distance (m)");
        panel.setDrawXAxisTitle(true);

        panel.setYAxisTitle("Y-Distance (m)");
        panel.setDrawYAxisTitle(true);

        panel.setCoordinateBounds(0, 6.28, 0, 6.28);
        panel.setDrawXTicks(true);
        panel.setDrawYTicks(true);

        panel.setColorForeground(Color.black);
        panel.setColorBackground(Color.white);
		
		Grafico grafico = new Grafico(panel);
		grafico.setSize(1024, 768);
		grafico.setLocation(100, 60);
		grafico.setVisible(true);
	}
	
}

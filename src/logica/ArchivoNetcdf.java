package logica;

import java.io.IOException;

import ucar.netcdf.Netcdf;
import ucar.netcdf.NetcdfFile;
import ucar.netcdf.Variable;

public class ArchivoNetcdf {
	
	private Netcdf file;
	private StringBuffer datos;
	
	public ArchivoNetcdf(String fileName) throws IOException {
		file = new NetcdfFile(fileName, true);
	}
	
	public void iterar(Variable var, int[] ix, int index) {
		try {
			datos.append("[");
			for (int i = 0; i < var.getLengths()[index]; i++) {
				ix[index] = i;
				if (index < ix.length - 1) {
					iterar(var, ix, index + 1);
				} else {
					datos.append(var.get(ix) + ", ");
				}
			}
			datos.append("],\n");
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}
	
	public String cargarVar(String varName) throws IOException {
		Variable var = file.get(varName);
		int[] nVar = var.getLengths();
		int[] ix = new int[nVar.length];
		datos = new StringBuffer();

		if (nVar.length == 0) {
			datos.append(var.get(nVar)+"");
		} else if (nVar.length > 0) {
			iterar(var, ix, 0);
		}
		return datos.toString();
	}
	
	public int[] getVarLengths(String varName) {
		Variable var = file.get(varName);
		int[] nVar = var.getLengths();
		return nVar;
	}
	
	public Netcdf getFile() {
		return file;
	}

}

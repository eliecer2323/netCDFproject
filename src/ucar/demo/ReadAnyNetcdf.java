/*
 * Note: These examples use the old version 1 Java netCDF interface,
 * which we do not recommend for new development. Instead, please use
 * NetCDF Java Library (Version 2), which is more efficient, simpler,
 * and provides better support for remote access using HTTP or
 * DODS. Similar examples are available in the NetCDF Java (version 2)
 * User's Manual.
 */

package ucar.demo;

import ucar.netcdf.Attribute;
import ucar.netcdf.AttributeIterator;
import ucar.netcdf.AttributeSet;
import ucar.netcdf.Netcdf;
import ucar.netcdf.NetcdfFile;
import ucar.netcdf.Variable;
import ucar.netcdf.VariableIterator;

/**
 * Read all the data from an existing netCDF file of unknown structure. This
 * only outputs variable names as they are being read, without doing anything
 * with the data, so it may useful for timing and tuning.
 *
 * @author: Russ Rew
 * @version: $Id: ReadAnyNetcdf.java,v 1.9 1998/07/17 15:24:32 russ Exp $
 */
public class ReadAnyNetcdf {

	static String fileName;

	/**
	 * Reads an existing netCDF file with a specified file name.
	 *
	 * @param args name of netCDF file to be read.
	 */
	public static void main(String[] args) {
		fileName = "outN.netcdf";

		try {
			Netcdf nc = new NetcdfFile(fileName, true); // open it readonly
			VariableIterator vi = nc.iterator();
			while (vi.hasNext()) {
				Variable var = vi.next();
				System.out.println(var.getName() + " ...");
				
				AttributeSet as = var.getAttributes();
				AttributeIterator ai = as.iterator();
				while (ai.hasNext()) {
					Attribute a = ai.next();
					System.out.println("\t"+a.getValue());
				}
				
				int[] nVar = var.getLengths();
				int[] ix = new int[nVar.length];
				
				if(nVar.length==0) {
					System.out.println(var.get(nVar));
				} else if(nVar.length>0){
					iterar(var, ix, 0);
				}
				
					
//				if(nVar.length==0) {
//					System.out.println(var.get(nVar));
//				} else if(nVar.length==1){
//					for (int i=0; i<nVar[0]; i++) {
//						int[] index = new int[1];
//						index[0] = i;
//						System.out.println(var.get(index));
//					}
//				} else if(nVar.length==2) {
//					int[] ix = new int[nVar.length];
//					for (int i=0; i<nVar[0]; i++) {
//						ix[0] = i;
//						System.out.print("{\n\t");
//						for (int j=0; j<nVar[1]; j++) {
//							ix[1] = j;
//							System.out.print(var.get(ix)+", ");
//						}
//						System.out.println("\n},");
//					}
//				}
//				System.out.println(nVar.toString());
				
				
				// just throw away the data, read it in for timing and tuning
				var.copyout(new int[var.getRank()], var.getLengths());
			}
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void iterar(Variable var, int[] ix, int index) {
		try {
			System.out.print("[\n\t");
			for (int i=0; i<var.getLengths()[index]; i++) {
				ix[index] = i;
				if(index<ix.length-1) {
					iterar(var, ix, index+1);
				} else {
					System.out.print(var.get(ix)+", ");
				}
			}
			System.out.println("\n],");
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}
	
}
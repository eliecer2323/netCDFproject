/*
 * Note: These examples use the old version 1 Java netCDF interface,
 * which we do not recommend for new development. Instead, please use
 * NetCDF Java Library (Version 2), which is more efficient, simpler,
 * and provides better support for remote access using HTTP or
 * DODS. Similar examples are available in the NetCDF Java (version 2)
 * User's Manual.
 */

package ucar.demo;

import ucar.multiarray.ArrayMultiArray;
import ucar.multiarray.IndexIterator;
import ucar.multiarray.MultiArray;
import ucar.multiarray.MultiArrayImpl;
import ucar.netcdf.Attribute;
import ucar.netcdf.Netcdf;
import ucar.netcdf.NetcdfFile;
import ucar.netcdf.Variable;

/**
 * Simple example to read data from an existing netCDF file of known structure,
 * corresponding to the following CDL (created by the CreateNetcdf.java demo):
 * 
 * <pre>
 *  netcdf example1 {
 *  dimensions:
 *  	lat = 2 ;
 *  	lon = 3 ;
 *  	time = UNLIMITED ;
 *  variables:
 *  	int rh(time, lat, lon) ;
 *              T:long_name="relative humidity" ;
 *  		T:units = "percent" ;
 *  	double T(time, lat, lon) ;
 *              T:long_name="surface temperature" ;
 *  		T:units = "degC" ;
 *  	float lat(lat) ;
 *  		lat:units = "degrees_north" ;
 *  	float lon(lon) ;
 *  		lon:units = "degrees_east" ;
 *  	int time(time) ;
 *  		time:units = "hours" ;
 *  // global attributes:
 *  		:title = "Example Data" ;
 *  data:
 *  }
 * </pre>
 *
 * @author: Russ Rew
 * @version: $Id: ReadNetcdf.java,v 1.10 1999/02/03 21:18:01 russ Exp $
 */
public class ReadNetcdf {

	static String fileName = "example.nc"; // name of existing file to read

	/**
	 * Reads an existing netCDF file with a specified file name or the default if no
	 * file name is specified.
	 *
	 * @param args name of netCDF file to be read, if other than default
	 */
	public static void main(String[] args) {
		fileName = "example.nc";

//		if (args.length > 0)
//			fileName = args[0];

		try {
			Netcdf nc = new NetcdfFile(fileName, true // open it readonly
			);

			/* Get the value of the global attribute named "title" */
			Attribute titleA = nc.getAttribute("title");
			String title = titleA.getStringValue();
			System.out.println("global title attribute: " + title);

			/*
			 * Read the latitudes into an array of double, reading one value at a time. This
			 * works regardless of the external type of the "lat" variable.
			 */
			Variable lat = nc.get("lat");
			assert (lat.getRank() == 1); // make sure it's 1-dimensional
			int nlats = lat.getLengths()[0]; // number of latitudes
			double[] lats = new double[nlats]; // where to put them
			int[] index = new int[1]; // index array to specify which value
			for (int ilat = 0; ilat < nlats; ilat++) {
				index[0] = ilat;
				lats[ilat] = lat.getDouble(index);
				System.out.println("lats[" + ilat + "]: " + lats[ilat]);
			}
			/* Read units attribute of lat variable */
			String latUnits = lat.getAttribute("units").getStringValue();
			System.out.println("attribute lats:units: " + latUnits);

			/*
			 * Read the longitudes. This illustrates a different approach, using a
			 * MultiArray to read them all at once.
			 */
			Variable lon = nc.get("lon");
			int[] origin = new int[lon.getRank()]; // where to start
			int[] extent = lon.getLengths(); // how many along each dimension
			MultiArray lonMa = lon.copyout(origin, extent);

			/*
			 * Now we can just use the MultiArray to access values, or we can copy the
			 * MultiArray elements to another array with toArray(), or we can get access to
			 * the MultiArray storage without copying. Each of these approaches to accessing
			 * the data are illustrated below.
			 */
			System.out.println("lons[3]: " + lonMa.getFloat(new int[] { 3 }));

			/* Read the times */
			Variable time = nc.get("time");
			origin = new int[time.getRank()];
			extent = time.getLengths();
			MultiArray timeMa = time.copyout(origin, extent);
			System.out.println("time[1]: " + timeMa.getFloat(new int[] { 1 }));

			/* Read the relative humidity data */
			Variable rh = nc.get("rh");
			int[] rhShape = rh.getLengths();
			/* assume we know rank is 3 */
			int[][][] rhData = new int[rhShape[0]][rhShape[1]][rhShape[2]];
			/* reading a single value is simple */
			rhData[1][0][2] = rh.getInt(new int[] { 1, 0, 2 });
			System.out.println("rh[1][0][2]: " + rhData[1][0][2]);

			/* Reading all the values, one at a time, is similar */
			int[] ix = new int[3];
			for (int itime = 0; itime < rhData.length; itime++) {
				ix[0] = itime;
				for (int ilat = 0; ilat < rhData[0].length; ilat++) {
					ix[1] = ilat;
					for (int ilon = 0; ilon < rhData[0][0].length; ilon++) {
						ix[2] = ilon;
						rhData[itime][ilat][ilon] = rh.getInt(ix);
					}
				}
			}
			System.out.println("rh[0][0][0]: " + rhData[0][0][0]);

			/*
			 * Read the relative humidity data one value at a time again, but this time use
			 * an IndexIterator instead to avoid the nested loops.
			 */

			/*
			 * First create a MultiArray wrapper for rhData array. Note this does not copy
			 * storage, but shares the same storage as rhData.
			 */
			MultiArray rhMa = new ArrayMultiArray(rhData);
			/*
			 * Now read the data into the MultiArray, which also reads it into the shared
			 * storage of the array.
			 */
			for (IndexIterator rhIx = new IndexIterator(rhShape); rhIx.notDone(); rhIx.incr()) {
				rhMa.setInt(rhIx.value(), rh.getInt(rhIx.value()));
			}
			System.out.println("rh[1][1][1]: " + rhData[1][1][1]);

			/* Read the temperature data all at once, as a MultiArray */
			Variable temperature = nc.get("T");
			origin = new int[temperature.getRank()];
			extent = temperature.getLengths();
			MultiArray tMa = temperature.copyout(origin, extent);
			/* Access the value of the [0][0][0] temperature as a double. */
			double t000 = tMa.getDouble(new int[] { 0, 0, 0 });
			System.out.println("T[0][0][0]: " + t000);

			/*
			 * Now access a value as a float. Note that we can't use getFloat() to get the
			 * value as a float, because temperature's external type is double and
			 * converting from double to float is not a "widening conversion" (it can lose
			 * information). But we can use getDouble() and then explicitly cast the double
			 * result to float, which accomplishes the same thing ...
			 */
			float t001 = (float) tMa.getDouble(new int[] { 0, 0, 1 });
			System.out.println("T[0][0][1]: " + t001);

			int[] tLengths = tMa.getLengths();

			/*
			 * Use the toArray() method of a MultiArray to convert it into a 1-dimensional
			 * array of doubles, then access the [1][2][3] element by computing where it
			 * would be in the resulting 1-dimensional array. Note: the toArray() method is
			 * only available in versions of MultiArray after 1998-04-19.
			 */
			double[] t1d = (double[]) tMa.toArray();
			double t123 = t1d[(1 * tLengths[1] + 2) * tLengths[2] + 3];
			System.out.println("T[1][2][3]: " + t123);

			/*
			 * Use the public storage member of an ArrayMultiArray to get at the array
			 * storage and then access the [0][1][2] element by computing where it would be
			 * in a 1-dimensional array.
			 */
			MultiArrayImpl tAma = (MultiArrayImpl) tMa;
			double[] tD = (double[]) tAma.storage;
			double t012 = tD[(0 * tLengths[1] + 1) * tLengths[2] + 2];
			System.out.println("T[0][1][2]: " + t012);

			System.out.println("read " + fileName + " successfully");

		} catch (java.io.IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Rudimentary assertion checker.
	 *
	 * @param pred assertion to be checked. If false, stacktrace is printed and
	 *             program exits.
	 */
//    static void assert(boolean pred) {
//	if (pred)
//	    return;
//	try {
//	    throw new Exception("assertion violated");
//	} catch (Exception e) {
//	    e.printStackTrace();
//	}
//	System.exit(1);
//    }

	/**
	 * true to see debug info printed, false for silent running
	 */
	static boolean DEBUG = false;

	/**
	 * @param s String to print
	 */
	static void debug(String s) {
		if (DEBUG)
			System.out.println(s);
	}

}
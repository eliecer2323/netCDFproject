/*
 * Note: These examples use the old version 1 Java netCDF interface,
 * which we do not recommend for new development. Instead, please use
 * NetCDF Java Library (Version 2), which is more efficient, simpler,
 * and provides better support for remote access using HTTP or
 * DODS. Similar examples are available in the NetCDF Java (version 2)
 * User's Manual.
 */

package ucar.demo;

import java.io.IOException;
import ucar.multiarray.*;

/**
 * Simple example program demonstrating some of the things you can do with
 * MultiArrays.
 *
 * @author Russ Rew
 * @version $Id: DemoMultiArrays.java,v 1.4 1999/02/03 22:03:37 russ Exp $
 */
public class DemoMultiArrays {

	public static void main(String[] args) throws java.io.IOException { // no I/O here, so this won't really happen

		double[][][] dd = new double[][][] {
				{ { 0.00, 0.01, 0.02, 0.03, 0.04 }, { 0.10, 0.11, 0.12, 0.13, 0.14 }, { 0.20, 0.21, 0.22, 0.23, 0.24 },
						{ 0.30, 0.31, 0.32, 0.33, 0.34 } },
				{ { 1.00, 1.01, 1.02, 1.03, 1.04 }, { 1.10, 1.11, 1.12, 1.13, 1.14 }, { 1.20, 1.21, 1.22, 1.23, 1.24 },
						{ 1.30, 1.31, 1.32, 1.33, 1.34 } },
				{ { 2.00, 2.01, 2.02, 2.03, 2.04 }, { 2.10, 2.11, 2.12, 2.13, 2.14 }, { 2.20, 2.21, 2.22, 2.23, 2.24 },
						{ 2.30, 2.31, 2.32, 2.33, 2.34 } } };

		/* Create a new 3 x 4 x 5 MultiArray from a Java 3D array. */
		MultiArray ma = new ArrayMultiArray(dd);
		System.out.println("ma = " + MultiArrayToString(ma));

		/*
		 * This MultiArray is a "wrapper" around the Java multidimensional array sharing
		 * the same storage for values, so setting a value of this MultiArray will
		 * change the underlying value.
		 */
		ma.setDouble(new int[] { 0, 0, 0 }, -99.0);
//	assert (dd[0][0][0] == -99, "should have changed dd[0][0][0]!");
		/*
		 * Restore value so it's easy to recognize element indices from values in
		 * subsequent operations
		 */
		ma.setDouble(new int[] { 0, 0, 0 }, 0.);

		/*
		 * A MultiArrayProxy is a MultiArray that is a "view" of another underlying
		 * MultiArray to make it look clipped or flipped or transposed or sliced or ....
		 * MultiArrayProxys involve no copying of values; the underlying storage is
		 * still shared.
		 */

		/*
		 * Create a MultiArrayProxy that behaves as if the values are flipped along the
		 * zeroth dimension.
		 */
		MultiArray maFlipped = new MultiArrayProxy(ma, new FlipMap(0));
		System.out.println("maFlipped = " + MultiArrayToString(maFlipped));

		/* Flip it along the first dimension */
		maFlipped = new MultiArrayProxy(ma, new FlipMap(1));
		System.out.println("maFlipped = " + MultiArrayToString(maFlipped));

		/* Flip it along the second dimension */
		maFlipped = new MultiArrayProxy(ma, new FlipMap(2));
		System.out.println("maFlipped = " + MultiArrayToString(maFlipped));

		/*
		 * Flip it along the zeroth dimension and then flip the result along the second
		 * dimension
		 */
		maFlipped = new MultiArrayProxy(ma, new FlipMap(new FlipMap(0), 2));
		System.out.println("maFlipped = " + MultiArrayToString(maFlipped));

		/*
		 * Clip the 3 x 4 x 5 MultiArray along the zeroth dimension to make a 2 x 4 x 5
		 * MultiArray
		 */
		MultiArray maClipped = new MultiArrayProxy(ma, new ClipMap(0, 1, 2));
		System.out.println("maClipped = " + MultiArrayToString(maClipped));

		/*
		 * Clip it along the first dimension to make a 3 x 2 x 5 MultiArray
		 */
		maClipped = new MultiArrayProxy(ma, new ClipMap(1, 1, 2));
		System.out.println("maClipped = " + MultiArrayToString(maClipped));

		/*
		 * Clip it along the second dimension to make a 3 x 4 x 2 MultiArray
		 */
		maClipped = new MultiArrayProxy(ma, new ClipMap(2, 1, 2));
		System.out.println("maClipped = " + MultiArrayToString(maClipped));

		/*
		 * Clip along the zeroth and then second dimension to make a 2 x 4 x 2
		 * MultiArray
		 */
		maClipped = new MultiArrayProxy(ma, new ClipMap(new ClipMap(0, 1, 2), 2, 1, 2));
		System.out.println("maClipped = " + MultiArrayToString(maClipped));

		/*
		 * Clip along the second and then zeroth dimension to make a 2 x 4 x 2
		 * MultiArray
		 */
		maClipped = new MultiArrayProxy(ma, new ClipMap(new ClipMap(2, 1, 2), 0, 1, 2));
		System.out.println("maClipped = " + MultiArrayToString(maClipped));

		boolean[] everyOther = new boolean[] { true, false };
		/*
		 * Decimate along the zeroth dimension to take every other slab to make a 2 x 2
		 * x 4 MultiArray
		 */
		MultiArray maDecimated = new MultiArrayProxy(ma, new DecimateMap(0, everyOther));
		System.out.println("maDecimated = " + MultiArrayToString(maDecimated));

		/*
		 * Decimate along the first dimension taking every other slab to make a 3 x 1 x
		 * 4 MultiArray
		 */
		maDecimated = new MultiArrayProxy(ma, new DecimateMap(1, everyOther));
		System.out.println("maDecimated = " + MultiArrayToString(maDecimated));

		/*
		 * Decimate along the second dimension taking every other value to make a 3 x 2
		 * x 2 MultiArray
		 */
		maDecimated = new MultiArrayProxy(ma, new DecimateMap(2, everyOther));
		System.out.println("maDecimated = " + MultiArrayToString(maDecimated));

		/*
		 * Decimate along the first and then second dimensions taking every other value
		 * to make a 3 x 1 x 2 MultiArray
		 */
		maDecimated = new MultiArrayProxy(ma, new DecimateMap(new DecimateMap(1, everyOther), 2, everyOther));
		System.out.println("maDecimated = " + MultiArrayToString(maDecimated));

		/*
		 * Flatten it along the zeroth dimension to make a 12 x 5 MultiArray
		 */
		MultiArray maFlattened = new MultiArrayProxy(ma, new FlattenMap(0));
		System.out.println("maFlattened = " + MultiArrayToString(maFlattened));

		/*
		 * Flatten it along the first dimension to make a 3 x 20 MultiArray
		 */
		maFlattened = new MultiArrayProxy(ma, new FlattenMap(1));
		System.out.println("maFlattened = " + MultiArrayToString(maFlattened));

		/*
		 * Flatten along the zeroth dimension and then the zeroth dimension of the
		 * result to make a 60-element MultiArray
		 */
		maFlattened = new MultiArrayProxy(ma, new FlattenMap(new FlattenMap(0), 0));
		System.out.println("maFlattened = " + MultiArrayToString(maFlattened));

		/*
		 * Slice it along the zeroth dimension at index 1 to make a 4 x 5 MultiArray
		 */
		MultiArray maSliced = new MultiArrayProxy(ma, new SliceMap(0, 1));
		System.out.println("maSliced = " + MultiArrayToString(maSliced));

		/*
		 * Slice it along the first dimension at index 1 to make a 3 x 5 MultiArray
		 */
		maSliced = new MultiArrayProxy(ma, new SliceMap(1, 1));
		System.out.println("maSliced = " + MultiArrayToString(maSliced));

		/*
		 * Slice it along the second dimension at index 1 to make a 3 x 4 MultiArray
		 */
		maSliced = new MultiArrayProxy(ma, new SliceMap(2, 1));
		System.out.println("maSliced = " + MultiArrayToString(maSliced));

		/*
		 * Slice it along the zeroth dimension at index 1, then the result along the
		 * first dimension at index 1 to make a 1-dimensional MultiArray of length 4
		 */
		maSliced = new MultiArrayProxy(ma, new SliceMap(new SliceMap(0, 1), 1, 1));
		System.out.println("maSliced = " + MultiArrayToString(maSliced));

		/*
		 * Get the [2][1][3] element by slicing 3 times, along the zeroth dimension at
		 * index 2, then the result along its zeroth dimension at index 1, then the
		 * result along its zeroth dimension at index 3 to yield a scalar MultiArray.
		 * Note we can get this element a lot easier with just ma.getDouble(new int[]
		 * {2,1,3})
		 */
		maSliced = new MultiArrayProxy(ma, new SliceMap(new SliceMap(new SliceMap(0, 2), 0, 1), 0, 3));
		System.out.println("maSliced = " + MultiArrayToString(maSliced));

		/*
		 * Transpose it by swapping the zeroth and second dimensions to make a 5 x 4 x 3
		 * MultiArray
		 */
		MultiArray maTransposed = new MultiArrayProxy(ma, new TransposeMap(0, 2));
		System.out.println("maTransposed = " + MultiArrayToString(maTransposed));

		/*
		 * Flip along the first dimension, then clip it along the zeroth dimension to
		 * make a 2 x 4 x 5 MultiArray
		 */
		MultiArray maClipFlip = new MultiArrayProxy(ma, new ClipMap(new FlipMap(1), 0, 1, 2));
		System.out.println("maClipFlip = " + MultiArrayToString(maClipFlip));
	}

	public static String MultiArrayToString(MultiArray ma) {
		StringBuffer buf = new StringBuffer(((Object) ma).toString() + " (" + ma.getComponentType() + ", " + "[");
		int[] shape = ma.getLengths();
		int rank = ma.getRank();
		for (int i = 0; i < rank - 1; i++) {
			buf.append(shape[i] + ", ");
		}
		if (rank > 0) {
			buf.append(shape[rank - 1]);
		}
		try {
			buf.append("])" + MultiArrayToStringHelper(ma, new IndentLevel()));
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		return buf.toString();
	}

	/**
	 * Maintains indentation level for printing nested structures.
	 */
	static class IndentLevel {
		private int level = 0;
		private int indentation;
		private StringBuffer indent;
		private StringBuffer blanks;

		public IndentLevel() {
			this(4);
		}

		public IndentLevel(int indentation) {
			if (indentation > 0)
				this.indentation = indentation;
			indent = new StringBuffer();
			blanks = new StringBuffer();
			for (int i = 0; i < indentation; i++)
				blanks.append(" ");
		}

		public void incr() {
			level += indentation;
			indent.append(blanks);
		}

		public void decr() {
			level -= indentation;
			indent.setLength(level);
		}

		public String getIndent() {
			return indent.toString();
		}
	}

	private static String MultiArrayToStringHelper(MultiArray ma, IndentLevel ilev) throws java.io.IOException { // no
																													// I/O
																													// here,
																													// so
																													// this
																													// won't
																													// really
																													// happen

		final int rank = ma.getRank();
		if (rank == 0) {
			try {
				return ma.get((int[]) null).toString();
			} catch (IOException ee) {
			}
		}
		StringBuffer buf = new StringBuffer();
		buf.append("\n" + ilev.getIndent() + "{");
		ilev.incr();
		final int[] dims = ma.getLengths();
		final int last = dims[0];
		for (int ii = 0; ii < last; ii++) {
			final MultiArray inner = new MultiArrayProxy(ma, new SliceMap(0, ii));
			buf.append(MultiArrayToStringHelper(inner, ilev));
			if (ii != last - 1)
				buf.append(", ");
		}
		ilev.decr();
		if (rank > 1) {
			buf.append("\n" + ilev.getIndent());
		}
		buf.append("}");

		return buf.toString();
	}

//	public static final void assert(
//
//	boolean b, String s)
//	{
//		if (!b)
//			throw new AssertionException(s);
//	}

}

class AssertionException extends Error {
	public AssertionException(String s) {
		super(s);
	}
}

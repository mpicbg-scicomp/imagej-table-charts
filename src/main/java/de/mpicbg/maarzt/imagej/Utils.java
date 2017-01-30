package de.mpicbg.maarzt.imagej;

import net.imagej.table.Column;
import net.imagej.table.DoubleColumn;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Matthias Arzt
 */
public class Utils {

	static public Iterable<Column<Double>> filterDoubleColumns(Iterable<Column<?>> input) {
		List<Column<Double>> output = new LinkedList<>();
		if(input != null)
			for(Column<?> col : input)
				if(checkColumnType(col, Double.class))
					output.add(unsafeColumnTypeConversion(col));
		return output;
	}

	static public double[] toArray(Column<Double> column) {
		if(column instanceof DoubleColumn)
			return ((DoubleColumn) column).getArray();

		double[] result = new double[column.size()];
		for(int i = 0, n = column.size(); i < n; i++)
			result[i] = column.get(i);
		return result;
	}

	public static <T> Column<T> castColumnType(Column<?> column, Class<T> clazz) {
		if(checkColumnType(column, clazz))
			return unsafeColumnTypeConversion(column);
		throw new ClassCastException();
	}

	public static boolean checkColumnType(Column<?> column, Class<?> clazz) {
		return column.getType().equals(clazz);
	}

	@SuppressWarnings("unchecked")
	private static <T> Column<T> unsafeColumnTypeConversion(Column<?> column) {
		return (Column<T>) column;
	}

}

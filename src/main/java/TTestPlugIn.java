import net.imagej.table.Column;
import net.imagej.table.Table;
import org.apache.commons.math3.stat.inference.TTest;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import widgets.MutableChoices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Plugin(type = Command.class, menuPath="Table>Calculate t-Test")
public class TTestPlugIn implements Command {

	@Parameter(callback = "tableChanged")
	public Table<Column<?>, ?> table;

	@Parameter(initializer = "tableChanged")
	public MutableChoices<Column<Double>> firstColumn;

	@Parameter(initializer = "tableChanged")
	public MutableChoices<Column<Double>> secondColumn;

	@Parameter(label = "p-value", type = ItemIO.OUTPUT)
	public double p_value;

	private void tableChanged() {
		if(table == null)
			return;
		List<Column<Double>> l = new ArrayList<>(table.size());
		for(Column<?> c : table) {
			if (c.getType().equals(Double.class)){
				l.add((Column<Double>) c);
			}
		}
		if(firstColumn != null)
			firstColumn.setChoices(l, c -> c.getHeader());
		if(secondColumn != null)
			secondColumn.setChoices(l, c -> c.getHeader());
	}

	@Override
	public void run() {
		p_value = new TTest().tTest(getArray(firstColumn.get()), getArray(secondColumn.get()));
	}

	static private double[] getArray(Collection<Double> values) {
		double[] r = new double[values.size()];
		int i = 0;
		for(double v : values)
			r[i++] = v;
		return r;
	}

}

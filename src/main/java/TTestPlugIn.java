import net.imagej.table.Column;
import net.imagej.table.Table;
import org.apache.commons.math3.stat.inference.TTest;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import widgets.MutableChoices;

import java.util.Collection;

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
		Iterable<Column<Double>> l = Utils.filterDoubleColumns(table);
		if(firstColumn != null)
			firstColumn.setChoices(l, c -> c.getHeader());
		if(secondColumn != null)
			secondColumn.setChoices(l, c -> c.getHeader());
	}

	@Override
	public void run() {
		p_value = new TTest().tTest(Utils.toArray(firstColumn.get()), Utils.toArray(secondColumn.get()));
	}

}

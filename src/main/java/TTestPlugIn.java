import net.imagej.table.Column;
import org.apache.commons.math3.stat.inference.TTest;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import java.util.Collection;

@Plugin(type = Command.class, menuPath="Table>Calculate t-Test")
public class TTestPlugIn implements Command {

	@Parameter
	public Column<Double> firstColumn;

	@Parameter
	public Column<Double> secondColumn;

	@Parameter(label = "p-value", type = ItemIO.OUTPUT)
	public double p_value;

	@Override
	public void run() {
		p_value = new TTest().tTest(getArray(firstColumn), getArray(secondColumn));
	}

	static private double[] getArray(Collection<Double> values) {
		double[] r = new double[values.size()];
		int i = 0;
		for(double v : values)
			r[i++] = v;
		return r;
	}

}

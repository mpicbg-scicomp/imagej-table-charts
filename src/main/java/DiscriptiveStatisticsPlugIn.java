import net.imagej.table.*;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.display.Display;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Command.class, menuPath="Table>Calculate Statistic Properties")
public class DiscriptiveStatisticsPlugIn implements Command {

	@Parameter
	public Display<Table<?,?>> activeTableDisplay;

	@Parameter(type = ItemIO.OUTPUT)
	public Table<?,?> output;

	private DefaultGenericTable result_table;

	@Override
	public void run() {
		result_table = new DefaultGenericTable();
		addPropertyColumn();
		for(Column<?> column : activeTableDisplay.get(0))
			if(column instanceof DoubleColumn)
				addStatisticsColumn((DoubleColumn) column);
		output = result_table;
	}

	private void addPropertyColumn() {
		GenericColumn c = new GenericColumn();
		c.add("mean");
		c.add("standard deviation");
		c.add("variance");
		c.add("min");
		c.add("max");
		c.add("sum");
		c.add("median");
		c.add("N");
		result_table.add(c);
	}

	private void addStatisticsColumn(DoubleColumn column) {
		SummaryStatistics stat = calculateSummaryStatistics(column);
		double median = new Median().evaluate(column.getArray());
		GenericColumn c = new GenericColumn();
		c.setHeader(column.getHeader());
		c.add(stat.getMean());
		c.add(stat.getStandardDeviation());
		c.add(stat.getVariance());
		c.add(stat.getMin());
		c.add(stat.getMax());
		c.add(stat.getSum());
		c.add(median);
		c.add(stat.getN());
		result_table.add(c);
	}

	static private SummaryStatistics calculateSummaryStatistics(DoubleColumn column) {
		SummaryStatistics stat = new SummaryStatistics();
		for(double x : column)
			stat.addValue(x);
		return stat;
	}

}

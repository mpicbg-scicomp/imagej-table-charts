import net.imagej.table.*;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Command.class, menuPath="Table>Calculate Statistic Properties")
public class DiscriptiveStatisticsPlugIn extends ChartPluginBase {

	@Parameter(type = ItemIO.OUTPUT)
	private Object output;

	private DefaultGenericTable result_table;

	@Override
	protected void runWithTable(String table_title, GenericTable table) {
		result_table = new DefaultGenericTable();
		addPropertyColumn();
		for(Column<?> column : table)
			if(column instanceof DoubleColumn)
				addStatisticsColumn((DoubleColumn) column);
		output = result_table;
	}

	void addPropertyColumn() {
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

	void addStatisticsColumn(DoubleColumn column) {
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

	static SummaryStatistics calculateSummaryStatistics(DoubleColumn column) {
		SummaryStatistics stat = new SummaryStatistics();
		for(double x : column)
			stat.addValue(x);
		return stat;
	}

}

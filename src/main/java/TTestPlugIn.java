import net.imagej.table.GenericTable;
import org.apache.commons.math3.stat.inference.TTest;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Command.class, menuPath="Table>Calculate t-Test")
public class TTestPlugIn extends ChartPluginBase {

	@Parameter(label = "p-value", type = ItemIO.OUTPUT)
	double p_value;

	private GenericTable table;
	private String tableTitle;

	@Override
	protected void runWithTable(String table_title, GenericTable table) {
		this.table = table;
		this.tableTitle = table_title;
		runDialog();
		p_value = new TTest().tTest(dialog.getXColumn().getArray(), dialog.getYColumn().getArray());
	}

	private ScatterPlotDialog dialog;

	private void runDialog() {
		dialog = new ScatterPlotDialog(tableTitle, table);
		dialog.showDialog();
		if(!dialog.wasOked())
			throw new AbortRun(null);
	}

}

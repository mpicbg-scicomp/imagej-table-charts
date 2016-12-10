import net.imagej.table.DoubleColumn;
import net.imagej.table.GenericTable;

class ScatterPlotDialog extends PlotDialogBase {
	private DoubleColumn x_column = null;
	private DoubleColumn y_column = null;

	ScatterPlotDialog(String title, GenericTable table) {
		super("ScatterPlot - " + title, table);
	}

	@Override
	protected void addControls() {
		dialog.AddDoubleColumnChoice("X Columns", table, 0);
		dialog.AddDoubleColumnChoice("Y Columns", table, 1);
	}

	@Override
	protected void readControls() {
		x_column = dialog.getChosenDoubleColumn();
		y_column = dialog.getChosenDoubleColumn();
	}

	public DoubleColumn getXColumn() { return x_column; }
	public DoubleColumn getYColumn() { return y_column; }
}

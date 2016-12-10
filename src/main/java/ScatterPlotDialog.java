import net.imagej.table.DoubleColumn;
import net.imagej.table.GenericTable;

class ScatterPlotDialog {
	private GenericTable table;
	private Boolean was_oked = false;
	private DoubleColumn x_column = null;
	private DoubleColumn y_column = null;

	ScatterPlotDialog(String title, GenericTable table) {
		this.table = table;
	}

	void run() {
		final TableColumnSelectorDialog d = new TableColumnSelectorDialog("Hello World!");
		d.AddDoubleColumnChoice("X Columns", table, 0);
		d.AddDoubleColumnChoice("Y Columns", table, 1);
		d.showDialog();
		was_oked = d.wasOKed();
		x_column = d.getChosenDoubleColumn();
		y_column = d.getChosenDoubleColumn();
		d.dispose();
	}

	public Boolean wasOked() { return was_oked; }
	public DoubleColumn getXColumn() { return x_column; }
	public DoubleColumn getYColumn() { return y_column; }
}

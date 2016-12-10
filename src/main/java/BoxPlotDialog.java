import net.imagej.table.Column;
import net.imagej.table.DoubleColumn;
import net.imagej.table.GenericTable;

import java.util.Collection;
import java.util.LinkedList;

class BoxPlotDialog extends PlotDialogBase {
	private Column<?> key_column = null;
	private Collection<DoubleColumn> value_columns = null;
	private Boolean use_key_column = false;

	BoxPlotDialog(String title, GenericTable table) {
		super("BoxPlot - " + title, table);
	}

	public Column<?> getKeyColumn() { return key_column; }
	public Collection<DoubleColumn> getValueColumns() { return value_columns; }
	public Boolean getUseKeyColumn() { return use_key_column; }

	protected void addControls() {
		dialog.addCheckbox("Use key column", false);
		dialog.AddNonDoubleColumnChoice("Key Column", table, 0);
		for(Column<?> column : table)
			if(column instanceof DoubleColumn)
				dialog.addCheckbox(column.getHeader(), true);
	}

	protected void readControls() {
		use_key_column = dialog.getNextBoolean();
		key_column = dialog.getChosenNonDoubleColumn();
		value_columns = new LinkedList<>();
		for(Column<?> column : table)
			if(column instanceof DoubleColumn)
				if(dialog.getNextBoolean())
					value_columns.add((DoubleColumn) column);
	}
}

import net.imagej.table.Column;
import net.imagej.table.DoubleColumn;
import net.imagej.table.GenericTable;

import java.util.Collection;
import java.util.LinkedList;

public class BoxPlotDialog {
	private String title;
	private GenericTable table;
	private Column<?> key_column = null;
	private Collection<DoubleColumn> value_columns = null;
	private Boolean was_oked = false;
	private Boolean use_key_column = false;
	private TableColumnSelectorDialog dialog = null;

	BoxPlotDialog(final String title, final GenericTable table) {
		this.title = title;
		this.table = table;
	}

	void run() {
		create();
		addControls();
		show();
		readControls();
	}

	private void create() {
		dialog = new TableColumnSelectorDialog("BoxPlot - " + title);
	}

	private void addControls() {
		dialog.addCheckbox("Use key column", false);
		dialog.AddNonDoubleColumnChoice("Key Column", table, 0);
		for(Column<?> column : table)
			if(column instanceof DoubleColumn)
				dialog.addCheckbox(column.getHeader(), true);
	}

	private void show() {
		dialog.showDialog();
		was_oked = dialog.wasOKed();
	}

	private void readControls() {
		use_key_column = dialog.getNextBoolean();
		key_column = dialog.getChosenNonDoubleColumn();
		value_columns = new LinkedList<>();
		for(Column<?> column : table)
			if(column instanceof DoubleColumn)
				if(dialog.getNextBoolean())
					value_columns.add((DoubleColumn) column);
	}

	public Column<?> getKeyColumn() { return key_column; }
	public Collection<DoubleColumn> getValueColumns() { return value_columns; }
	public Boolean wasOked() { return was_oked; }
	public Boolean getUseKeyColumn() { return use_key_column; }
}

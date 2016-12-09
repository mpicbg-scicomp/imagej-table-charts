import fiji.util.gui.GenericDialogPlus;
import net.imagej.table.Column;
import net.imagej.table.DoubleColumn;
import net.imagej.table.GenericColumn;
import net.imagej.table.GenericTable;

import java.util.LinkedList;
import java.util.Vector;

/**
 * Created by arzt on 08/12/2016.
 */
class TableColumnSelectorDialog extends GenericDialogPlus {

	static private final String splitter = " - ";
	private LinkedList<Vector<Column<?>>> list_of_list_of_columns;

	TableColumnSelectorDialog(String title) {
		super(title);
		list_of_list_of_columns = new LinkedList<>();
	}

	void AddDoubleColumnChoice(String label, GenericTable table, int default_index) {
		AddGenericColumnChoice(new DoubleColumnFilter(), label, table, default_index);
	}

	void AddNonDoubleColumnChoice(String label, GenericTable table, int default_index) {
		AddGenericColumnChoice(new NonDoubleColumnFilter(), label, table, default_index);
	}

	void AddGenericColumnChoice(ColumnFilter filter, String label, GenericTable table, int default_index) {
		int cc  = table.getColumnCount();
		Vector<String> result = new Vector<>(cc);
		Vector<Column<?>> list_of_columns = new Vector<>(cc);
		for(int i = 0; i < cc; i++) {
			Column<?> column = table.get(i);
			if(filter.get(column)) {
				result.add((i + 1) + splitter + column.getHeader());
				list_of_columns.add(column);
			}
		}
		if(result.isEmpty())
			throw new NoDoubleColumnsException();
		addChoice(label,
				result.toArray(new String[result.size()]),
				(default_index > 0 && default_index < result.size()) ? result.get(default_index) : null);
		list_of_list_of_columns.add(list_of_columns);
	}

	Column<?> getChoosenColumn() {
		Vector<Column<?>> list_of_columns = list_of_list_of_columns.removeFirst();
		return list_of_columns.get(getNextChoiceIndex());
	}

	DoubleColumn getChoosenDoubleColumn() {
		return (DoubleColumn) getChoosenColumn();
	}

	Column<?> getChoosenNonDoubleColumn() {
		return getChoosenColumn();
	}

	class NoDoubleColumnsException extends RuntimeException {}
	interface ColumnFilter { boolean get(Column<?> column); }
	class DoubleColumnFilter implements ColumnFilter {
		public boolean get(Column<?> column) { return column instanceof DoubleColumn; }
	}
	class NonDoubleColumnFilter implements ColumnFilter {
		public boolean get(Column<?> column) { return !(column instanceof DoubleColumn); }
	}
}

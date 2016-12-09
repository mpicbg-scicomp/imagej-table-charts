import fiji.util.gui.GenericDialogPlus;
import net.imagej.table.Column;
import net.imagej.table.DoubleColumn;
import net.imagej.table.GenericTable;

import java.util.LinkedList;
import java.util.Vector;

/**
 * Created by arzt on 08/12/2016.
 */
class TableColumnSelectorDialog extends GenericDialogPlus {

	static private final String splitter = " - ";
	private LinkedList<Vector<DoubleColumn>> list_of_list_of_columns;

	TableColumnSelectorDialog(String title) {
		super(title);
		list_of_list_of_columns = new LinkedList<>();
	}

	void AddDoubleColumnChoice(String label, GenericTable table, int default_index) {
		int cc  = table.getColumnCount();
		Vector<String> result = new Vector<>(cc);
		Vector<DoubleColumn> list_of_columns = new Vector<>(cc);
		for(int i = 0; i < cc; i++) {
			Column<?> column = table.get(i);
			if(column instanceof DoubleColumn) {
				result.add((i + 1) + splitter + column.getHeader());
				list_of_columns.add((DoubleColumn) column);
			}
		}
		if(result.isEmpty())
			throw new NoDoubleColumnsException();
		addChoice(label,
				result.toArray(new String[result.size()]),
				(default_index > 0 && default_index < result.size()) ? result.get(default_index) : null);
		list_of_list_of_columns.add(list_of_columns);
	}

	DoubleColumn getChoosenDoubleColumn() {
		Vector<DoubleColumn> list_of_columns = list_of_list_of_columns.removeFirst();
		return list_of_columns.get(getNextChoiceIndex());
	}

	class NoDoubleColumnsException extends RuntimeException {}
}

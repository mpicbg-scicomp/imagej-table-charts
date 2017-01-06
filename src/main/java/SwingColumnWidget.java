import net.imagej.table.Column;
import net.imagej.table.Table;
import net.imagej.table.TableDisplay;
import org.scijava.display.DisplayService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.swing.widget.SwingInputWidget;
import org.scijava.widget.InputWidget;
import org.scijava.widget.WidgetModel;

import javax.swing.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.Vector;

/**
 * Created by arzt on 06/01/2017.
 */
@Plugin(type = InputWidget.class)
public class SwingColumnWidget extends SwingInputWidget<Column<?>> {

	@Parameter
	DisplayService displayService;
	private JComboBox<ColumnItem> columnComboBox;
	private JComboBox<TableItem> tableComboBox;
	private DefaultComboBoxModel<ColumnItem> columnComboBoxModel;
	private ColumnFilter filter;

	@Override
	protected void doRefresh() {
		get().setValue(getValue());
	}

	@Override
	public void set(final WidgetModel model) {
		super.set(model);
		filter = new ColumnFilter(model);
		initializeTableComboBox();
		initializeColumnComboBox();
		updateColumnComboBoxModel();
	}

	private void initializeTableComboBox() {
		List<TableDisplay> tableDisplays = displayService.getDisplaysOfType(TableDisplay.class);
		Vector<TableItem> tables = new Vector<>();
		for(TableDisplay tableDisplay : tableDisplays)
			tables.add(new TableItem(tableDisplay));
		tableComboBox = new JComboBox<>(tables);
		tableComboBox.addActionListener(e -> updateColumnComboBoxModel());
		getComponent().add(new JLabel("Table"));
		getComponent().add(tableComboBox);
	}

	private void initializeColumnComboBox() {
		columnComboBoxModel = new DefaultComboBoxModel<>();
		columnComboBox = new JComboBox<>(columnComboBoxModel);
		columnComboBox.addActionListener(e -> updateSelectedColumn());
		getComponent().add(new JLabel("Column"));
		getComponent().add(columnComboBox);
	}

	private void updateColumnComboBoxModel() {
		columnComboBoxModel.removeAllElements();
		Table<?,?> selectedTable = tableComboBox.getItemAt(tableComboBox.getSelectedIndex()).get();
		for(Column<?> col : selectedTable)
			if(filter.isValid(col))
				columnComboBoxModel.addElement(new ColumnItem(col));
	}

	private void updateSelectedColumn() {
		WidgetModel model = get();
		model.setValue(getValue());
	}

	@Override
	public boolean supports(final WidgetModel model) { return model.isType(Column.class); }

	@Override
	public Column<?> getValue() {
		ColumnItem selected = (ColumnItem) columnComboBox.getSelectedItem();
		return (selected != null) ? selected.get() : null;
	}

	static private class ComboBoxItem<E> {

		final String label;
		final E value;

		ComboBoxItem(String label, E value) {
			this.label = label;
			this.value = value;
		}

		@Override
		public String toString() { return label; }

		public E get() { return value; }

	}

	static private class TableItem extends ComboBoxItem<Table<?,?>> {
		TableItem(TableDisplay display) { super(display.getName(), display.get(0)); }
	}

	static private class ColumnItem extends ComboBoxItem<Column<?>> {
		ColumnItem(Column<?> column) { super(column.getHeader(), column); }
	}

	static private class ColumnFilter {
		Type elementType;

		ColumnFilter(WidgetModel model) {
			ParameterizedType pt = (ParameterizedType) model.getItem().getGenericType();
			elementType = pt.getActualTypeArguments()[0];
		}

		private boolean isValid(Column<?> col) {
			Class<?> t = col.getType();
			if(elementType instanceof WildcardType)
				return classFitsWildcard(t, (WildcardType) elementType);
			return col.getType().equals(elementType);
		}

		static private boolean classFitsWildcard(Class<?> clazz, WildcardType elementType) {
			boolean result = true;
			for(Type bound : elementType.getLowerBounds())
				result &= classFitsLowerBound(clazz, bound);
			for(Type bound : elementType.getUpperBounds())
				result &= classFitsUpperBound(clazz, bound);
			return result;
		}

		static private boolean classFitsUpperBound(Class<?> clazz, Type bound) {
			if(!(bound instanceof Class))
				return false;
			return ((Class<?>) bound).isAssignableFrom(clazz);
		}

		static private boolean classFitsLowerBound(Class<?> clazz, Type bound) {
			if(!(bound instanceof Class))
				return false;
			return clazz.isAssignableFrom((Class<?>) bound);
		}
	}
}

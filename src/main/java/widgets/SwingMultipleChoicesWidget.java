package widgets;

import org.scijava.module.MethodCallException;
import org.scijava.plugin.Plugin;
import org.scijava.ui.swing.widget.SwingInputWidget;
import org.scijava.widget.InputWidget;
import org.scijava.widget.WidgetModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Matthias Arzt
 */
@Plugin(type = InputWidget.class)
public class SwingMultipleChoicesWidget<T> extends SwingInputWidget<MultipleChoices<T>> {

	Map<JCheckBox, T> checkBoxes = new HashMap<>();

	MyMultipleChoices<T> result;

	@Override
	public void set(final WidgetModel model) {
		super.set(model);
		result = new MyMultipleChoices<T>(this);
		get().setValue(result);
		try {
			model.getItem().initialize(model.getModule());
		} catch (MethodCallException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doRefresh() {
		// FIXME
	}

	@Override
	public MultipleChoices<T> getValue() {
		return result;
	}

	@Override
	public boolean supports(final WidgetModel model) {
		return model.isType(MultipleChoices.class);
	}

	private void setChoices(Iterable<T> choices, MultipleChoices.PrettyPrinter<T> prettyPrinter) {
		for(JCheckBox oldCheckBox : checkBoxes.keySet())
			getComponent().remove(oldCheckBox);
		checkBoxes.clear();
		result.selected.clear();
		if(choices != null)
			for(T choice : choices) {
				String text = prettyPrinter.toString(choice);
				JCheckBox checkBox = new JCheckBox(text);
				checkBoxes.put(checkBox, choice);
				checkBox.addActionListener(e -> updateChoice(e));
				getComponent().add(checkBox);
			}
		this.getComponent().revalidate();
	}

	private void updateChoice(ActionEvent e) {
		JCheckBox source = (JCheckBox) e.getSource();
		T choice = checkBoxes.get(source);
		boolean value = source.isSelected();
		if(value)
			result.selected.add(choice);
		else
			result.selected.remove(choice);
		get().setValue(result);
	}

	static private class MyMultipleChoices<T> implements MultipleChoices<T> {

		final SwingMultipleChoicesWidget<T> widget;

		Set<T> selected;

		private MyMultipleChoices(SwingMultipleChoicesWidget<T> widget) {
			this.widget = widget;
			selected = new HashSet<T>();
		}

		@Override
		public void setChoices(Iterable<T> choices, PrettyPrinter<T> prettyPrinter) {
			widget.setChoices(choices, prettyPrinter);
		}

		@Override
		public boolean get(T value) {
			return selected.contains(value);
		}

		@Override
		public Set<T> get() {
			return selected;
		}

	}

}

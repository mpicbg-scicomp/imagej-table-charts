import net.imagej.table.Column;
import net.imagej.table.Table;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import widgets.MultipleChoices;
import widgets.MutableChoices;

/**
 * @author Matthias Arzt
 */
@Plugin(type = Command.class, menuPath="Table>Test")
public class TestPlugIn implements Command {

	@Parameter(callback = "init")
	public Table<Column<?>,?> table;

	@Parameter(initializer = "init")
	public MultipleChoices<Column<?>> value;

	@Override
	public void run() {

	}

	void init() {
		if(value != null)
			value.setChoices(table, t -> t.getHeader());
	}
}

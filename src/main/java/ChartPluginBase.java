import net.imagej.plot.AbstractPlot;
import net.imagej.plot.PlotService;
import net.imagej.table.GenericTable;
import net.imagej.table.TableDisplay;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.display.DisplayService;
import org.scijava.plugin.Parameter;

import javax.swing.*;

abstract class ChartPluginBase implements Command {

	@Parameter(label = "plot", type = ItemIO.OUTPUT)
	public AbstractPlot output;

	@Parameter
	public PlotService plotService;

	@Parameter
	public DisplayService displayService;
	
	@Override
	public void run() {
		try {
			Pair<GenericTable, String> tableAndItsName = getActiveTable();
			runWithTable(tableAndItsName.getB(), tableAndItsName.getA());
		}
		catch (AbortRun e) {
			e.showMessage();
		}
	}

	protected abstract void runWithTable(String table_table, GenericTable table);

	protected Pair<GenericTable, String> getActiveTable() {
		try {
			TableDisplay display = (TableDisplay) displayService.getActiveDisplay();
			return new ValuePair<>((GenericTable) checkNotNull(display.get(0)), display.getName());
		} catch(NullPointerException | ClassCastException | IndexOutOfBoundsException e) {
			throw new AbortRun("No table window is active.");
		}
	}

	static protected class AbortRun extends RuntimeException {
		private String message;
		protected AbortRun(String message) { this.message = message; }
		protected void showMessage() {
			if(!message.isEmpty())
				JOptionPane.showMessageDialog(null, message, "ScatterPlot",
						JOptionPane.INFORMATION_MESSAGE);
		}
	}

	protected <T> T checkNotNull(T value) {
		if(value == null)
			throw new NullPointerException();
		return value;
	}
	
}

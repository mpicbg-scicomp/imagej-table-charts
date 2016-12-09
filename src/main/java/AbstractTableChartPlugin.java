import net.imagej.table.GenericTable;
import net.imagej.table.TableDisplay;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.scijava.command.Command;
import org.scijava.display.DisplayService;
import org.scijava.plugin.Parameter;

import javax.swing.*;
import java.awt.*;

abstract class AbstractTableChartPlugin implements Command {

	@Parameter
	private DisplayService displayService;
	
	@Override
	public void run() {
		try {
			Pair<GenericTable, String> tableAndItsName = getActiveTable();
			JFreeChart chart = generateChartFromTable(tableAndItsName.getB(), tableAndItsName.getA());
			showChartWindow(tableAndItsName.getB(), chart);
		}
		catch (AbortRun e) {
			e.showMessage();
		}
	}

	protected abstract JFreeChart generateChartFromTable(String b, GenericTable a);

	private static void showChartWindow(String title, JFreeChart chart) {
		JFrame frame = new ChartFrame(title, chart);
		frame.pack();
		frame.setVisible(true);
	}

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
				JOptionPane.showMessageDialog(null,
						message,
						"ScatterPlot",
						JOptionPane.INFORMATION_MESSAGE);
		}
	}

	protected <T> T checkNotNull(T value) {
		if(value == null)
			throw new NullPointerException();
		return value;
	}
	
	protected static JFreeChart beautifyChart(JFreeChart chart) {
		chart.setBackgroundPaint(Color.white);
		chart.getLegend().setFrame(BlockBorder.NONE);
		return chart;
	}
}

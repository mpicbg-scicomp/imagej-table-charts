/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */

import net.imagej.plot.*;
import net.imagej.table.*;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.util.Colors;

import java.util.ArrayList;
import java.util.Collection;

@Plugin(type = Command.class, menuPath="Table>ScatterPlot")
public class ScatterPlotPlugin extends ChartPluginBase {

	private GenericTable table;
	private String tableTitle;
	private ScatterPlotDialog dialog;

	protected void runWithTable(String table_title, GenericTable table) {
		this.table = table;
		this.tableTitle = table_title;
		runDialog();
		generateChart();
	}

	private void runDialog() {
		dialog = new ScatterPlotDialog(tableTitle, table);
		dialog.showDialog();
		if(!dialog.wasOked())
			throw new AbortRun("");
		if(dialog.getXColumn() == null || dialog.getYColumn() == null)
			throw new AbortRun("Necessary columns where not chosen.");
	}

	private void generateChart() {
		final String xLabel = dialog.getXColumn().getHeader();
		final String yLabel = dialog.getYColumn().getHeader();
		final String chart_title = "ScatterPlot - " + xLabel + " - " + yLabel;
		XYPlot plot = plotService.createXYPlot();
		plot.setTitle("Line Styles");
		Collection<Double> xs = new ArrayList<>(dialog.getXColumn());
		Collection<Double> ys = new ArrayList<>(dialog.getYColumn());
		XYSeries series = plot.createXYSeries("", xs, ys);
		series.setStyle(plot.createSeriesStyle(Colors.RED, LineStyle.NONE, MarkerStyle.CIRCLE));
		plot.setTitle(chart_title);
		plot.getItems().add(series);
		plot.getXAxis().setLabel(xLabel);
		plot.getYAxis().setLabel(yLabel);
		output = plot;
	}

}

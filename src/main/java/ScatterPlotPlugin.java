/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */

import net.imagej.plot.*;
import net.imagej.table.Column;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.util.Colors;

import java.util.ArrayList;
import java.util.Collection;

@Plugin(type = Command.class, menuPath="Table>ScatterPlot")
public class ScatterPlotPlugin implements Command {

	@Parameter
	public Column<Double> xColumn;

	@Parameter
	public Column<Double> yColumn;

	@Parameter(label = "plot", type = ItemIO.OUTPUT)
	public AbstractPlot output;

	@Parameter
	private PlotService plotService;

	@Override
	public void run() {
		generateChart();
	}

	private void generateChart() {
		final String xLabel = xColumn.getHeader();
		final String yLabel = yColumn.getHeader();
		final String chart_title = "ScatterPlot - " + xLabel + " - " + yLabel;
		XYPlot plot = plotService.createXYPlot();
		plot.setTitle("Line Styles");
		Collection<Double> xs = new ArrayList<>(xColumn);
		Collection<Double> ys = new ArrayList<>(yColumn);
		XYSeries series = plot.createXYSeries("", xs, ys);
		series.setStyle(plot.createSeriesStyle(Colors.RED, LineStyle.NONE, MarkerStyle.CIRCLE));
		plot.setTitle(chart_title);
		plot.getItems().add(series);
		plot.getXAxis().setLabel(xLabel);
		plot.getYAxis().setLabel(yLabel);
		output = plot;
	}
}

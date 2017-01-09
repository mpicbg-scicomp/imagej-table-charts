/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */

import net.imagej.plot.*;
import net.imagej.table.Column;
import net.imagej.table.Table;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.util.Colors;
import widgets.MutableChoices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Plugin(type = Command.class, menuPath="Table>ScatterPlot")
public class ScatterPlotPlugin implements Command {

	@Parameter(callback = "tableChanged")
	public Table<Column<?>, ?> table;

	@Parameter(initializer = "tableChanged")
	public MutableChoices<Column<Double>> xColumn;

	@Parameter(initializer = "tableChanged")
	public MutableChoices<Column<Double>> yColumn;

	@Parameter(label = "plot", type = ItemIO.OUTPUT)
	public AbstractPlot output;

	@Parameter
	private PlotService plotService;

	@Override
	public void run() {
		generateChart();
	}

	private void tableChanged() {
		Iterable<Column<Double>> l = Utils.filterDoubleColumns(table);
		if(xColumn != null)
			xColumn.setChoices(l, c -> c.getHeader());
		if(yColumn != null)
			yColumn.setChoices(l, c -> c.getHeader());
	}

	private void generateChart() {
		final String xLabel = xColumn.get().getHeader();
		final String yLabel = yColumn.get().getHeader();
		final String chart_title = "ScatterPlot - " + xLabel + " - " + yLabel;
		XYPlot plot = plotService.createXYPlot();
		plot.setTitle("Line Styles");
		Collection<Double> xs = new ArrayList<>(xColumn.get());
		Collection<Double> ys = new ArrayList<>(yColumn.get());
		XYSeries series = plot.createXYSeries("", xs, ys);
		series.setStyle(plot.createSeriesStyle(Colors.RED, LineStyle.NONE, MarkerStyle.CIRCLE));
		plot.setTitle(chart_title);
		plot.getItems().add(series);
		plot.getXAxis().setLabel(xLabel);
		plot.getYAxis().setLabel(yLabel);
		output = plot;
	}
}

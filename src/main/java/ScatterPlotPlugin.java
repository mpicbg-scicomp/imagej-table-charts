/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */

import net.imagej.table.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.scijava.command.Command;
import org.scijava.plugin.Plugin;

import java.util.Iterator;

@Plugin(type = Command.class, menuPath="Table>ScatterPlot")
public class ScatterPlotPlugin extends ChartPluginBase {

	private GenericTable table;
	private String tableTitle;
	private ScatterPlotDialog dialog;
	private XYSeriesCollection chartDataset;
	private JFreeChart chart;

	protected void runWithTable(String table_title, GenericTable table) {
		this.table = table;
		this.tableTitle = table_title;
		runDialog();
		generateChartDataset();
		generateChart();
		showChartWindow(tableTitle, chart);
	}

	private void runDialog() {
		dialog = new ScatterPlotDialog(tableTitle, table);
		dialog.showDialog();
		if(!dialog.wasOked())
			throw new AbortRun("");
		if(dialog.getXColumn() == null || dialog.getYColumn() == null)
			throw new AbortRun("Necessary columns where not chosen.");
	}

	private void generateChartDataset() {
		final XYSeries series = new XYSeries( "chartDataset" );
		Iterator<Double> xiter = dialog.getXColumn().iterator();
		Iterator<Double> yiter = dialog.getYColumn().iterator();
		while(xiter.hasNext() && yiter.hasNext()) {
			double x = xiter.next();
			double y = yiter.next();
			series.add(x, y);
		}
		chartDataset = new XYSeriesCollection( );
		chartDataset.addSeries( series );
	}

	private void generateChart() {
		final String xlabel = dialog.getXColumn().getHeader();
		final String ylabel = dialog.getYColumn().getHeader();
		final String chart_title = "ScatterPlot - " + xlabel + " - " + ylabel;
		chart = beautifyChart(ChartFactory.createScatterPlot(chart_title, xlabel, ylabel, chartDataset));
	}

}

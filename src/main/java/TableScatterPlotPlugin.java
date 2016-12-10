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

/**
 * This tutorial shows how to work with tables using ImageJ API
 *
 * Author: Robert Haase, Scientific Computing Facility, MPI-CBG Dresden, rhaase@mpi-cbg.de
 * Date: September 2016
 */

@Plugin(type = Command.class, menuPath="Table>ScatterPlot")
public class TableScatterPlotPlugin extends AbstractTableChartPlugin {

	protected JFreeChart generateChartFromTable(String table_title, GenericTable table) {
		ScatterPlotDialog dialog = runScatterPlotDialog(table_title, table);
		final String xlabel = dialog.getXColumn().getHeader();
		final String ylabel = dialog.getYColumn().getHeader();
		final String chart_title = "ScatterPlot - " + xlabel + " - " + ylabel;
		final XYSeriesCollection series = xySeriesCollectionFromColumns(dialog.getXColumn(), dialog.getYColumn());
		return beautifyChart(ChartFactory.createScatterPlot(chart_title, xlabel, ylabel, series));
	}

	private static ScatterPlotDialog runScatterPlotDialog(String table_title, GenericTable table) {
		final ScatterPlotDialog d = new ScatterPlotDialog(table_title, table);
		d.showDialog();
		if(!d.wasOked())
			throw new AbortRun("");
		if(d.getXColumn() == null || d.getYColumn() == null)
			throw new AbortRun("Necessary columns where not chosen.");
		return d;
	}

	private static XYSeriesCollection xySeriesCollectionFromColumns(DoubleColumn xcolumn, DoubleColumn ycolumn) {
		final XYSeries series = new XYSeries( "series" );
		Iterator<Double> xiter = xcolumn.iterator();
		Iterator<Double> yiter = ycolumn.iterator();
		while(xiter.hasNext() && yiter.hasNext()) {
			double x = xiter.next();
			double y = yiter.next();
			series.add(x, y);
		}
		final XYSeriesCollection dataset = new XYSeriesCollection( );
		dataset.addSeries( series );
		return dataset;
	}

}

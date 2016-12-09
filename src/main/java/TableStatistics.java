/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */


import net.imagej.ImageJ;
import net.imagej.table.*;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.scijava.command.Command;
import org.scijava.display.DisplayService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import java.awt.*;
import java.util.Iterator;
import javax.swing.*;

/**
 * This tutorial shows how to work with tables using ImageJ API
 *
 * Author: Robert Haase, Scientific Computing Facility, MPI-CBG Dresden, rhaase@mpi-cbg.de
 * Date: September 2016
 */

@Plugin(type = Command.class, menuPath="Table>ScatterPlot")
public class TableStatistics implements Command {

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

	private Pair<GenericTable, String> getActiveTable() {
		try {
			TableDisplay display = (TableDisplay) displayService.getActiveDisplay();
			return new ValuePair<>((GenericTable) checkNotNull(display.get(0)), display.getName());
		} catch(NullPointerException | ClassCastException | IndexOutOfBoundsException e) {
			throw new AbortRun("No table window is active.");
		}
	}

	private static JFreeChart generateChartFromTable(String table_title, GenericTable table) {
		final Pair<DoubleColumn, DoubleColumn> columns = chooseColumns(table_title, table);
		final String xlabel = columns.getA().getHeader();
		final String ylabel = columns.getB().getHeader();
		final String chart_title = "ScatterPlot - " + xlabel + " - " + ylabel;
		final XYSeriesCollection series = xySeriesCollectionFromColumns(columns.getA(), columns.getB());
		return beautifyChart(ChartFactory.createScatterPlot(chart_title, xlabel, ylabel, series));
	}

	private static void showChartWindow(String title, JFreeChart chart) {
		JFrame frame = new ChartFrame(title, chart);
		frame.pack();
		frame.setVisible(true);
	}

	private static Pair<DoubleColumn, DoubleColumn> chooseColumns(String title, GenericTable table) {
		TableColumnSelectorDialog d = new TableColumnSelectorDialog("Hello World!");
		try {
			d.AddDoubleColumnChoice("X Columns", table, 0);
			d.AddDoubleColumnChoice("Y Columns", table, 1);
		}
		catch (TableColumnSelectorDialog.NoDoubleColumnsException e) {
			throw new AbortRun("There is no column in the table" + title + ", containing numbers.");
		}
		d.showDialog();
		if(! d.wasOKed())
			throw new AbortRun(null);
		return new ValuePair<>(d.getChoosenDoubleColumn(), d.getChoosenDoubleColumn());
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

	private static JFreeChart beautifyChart(JFreeChart chart) {
		chart.setBackgroundPaint(Color.white);
		chart.getLegend().setFrame(BlockBorder.NONE);
		return chart;
	}

	private static <T> T checkNotNull(T value) {
		if(value == null)
			throw new NullPointerException();
		return value;
	}

	static private class AbortRun extends RuntimeException {
		private String message;
		private AbortRun(String message) { this.message = message; }
		private void showMessage() {
			if(!message.isEmpty())
				JOptionPane.showMessageDialog(null,
						message,
						"ScatterPlot",
						JOptionPane.INFORMATION_MESSAGE);
		}
	}
}

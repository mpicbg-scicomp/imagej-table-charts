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
import java.util.List;
import java.util.LinkedList;
import javax.swing.JFrame;

/**
 * This tutorial shows how to work with tables using ImageJ API
 *
 * Author: Robert Haase, Scientific Computing Facility, MPI-CBG Dresden, rhaase@mpi-cbg.de
 * Date: September 2016
 */

@Plugin(type = Command.class, menuPath="Table>ScatterPlot")
public class TableStatistics implements Command {

	@Override
	public void run() {
		System.out.println("Hello World!");
		Pair<GenericTable, String> tableAndItsName = getActiveTable();
		JFreeChart chart = generateChartFromTable(tableAndItsName.getA());
		showChartWindow(tableAndItsName.getB(), chart);
	}
	static ImageJ ij;

	@Parameter
	private DisplayService displayService;

	private Pair<GenericTable, String> getActiveTable() {
		try {
			TableDisplay display = (TableDisplay) displayService.getActiveDisplay();
			return new ValuePair((GenericTable) display.get(0), display.getName());
		} catch(NullPointerException | ClassCastException | IndexOutOfBoundsException e) {
			return null;
		}
	}

	private static void showChartWindow(String title, JFreeChart chart) {
		JFrame frame = new ChartFrame(title, chart);
		frame.pack();
		frame.setVisible(true);
	}

	private static JFreeChart generateChartFromTable(GenericTable table) {
		Pair<DoubleColumn, DoubleColumn> columnpair = getFirstTwoDoubleColumns(table);
		final XYSeriesCollection dataset = xySeriesCollectionFromColumns(columnpair.getA(), columnpair.getB());
		JFreeChart chart = ChartFactory.createScatterPlot(
				"ScatterPlot - " + columnpair.getA().getHeader() + " - " + columnpair.getB().getHeader(),
				columnpair.getA().getHeader(), columnpair.getB().getHeader(), dataset);
		//chart.addSubtitle(new TextTitle("subtitle"));
		chart.setBackgroundPaint(Color.white);
		chart.getLegend().setFrame(BlockBorder.NONE);
		return chart;
	}

	private static Pair<DoubleColumn, DoubleColumn> getFirstTwoDoubleColumns(GenericTable table) {
		List<DoubleColumn> l = new LinkedList<>();
		for(Column<?> column : table)
			if(column instanceof DoubleColumn)
				l.add((DoubleColumn) column);

		return new ValuePair<>((DoubleColumn) table.get(1), (DoubleColumn) table.get(2));
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

	private static void logTableStatistics(GenericTable table) {
		for(Object column : table) {
			if(column instanceof DoubleColumn) {
				final DoubleColumn doublecolumn = (DoubleColumn) column;
				final SummaryStatistics stats = new SummaryStatistics();
				for(double cellvalue : doublecolumn) 
					stats.addValue(cellvalue);
				final double mean = stats.getMean();
				final double sd = stats.getStandardDeviation();
				ij.log().info("column " + doublecolumn.getHeader() + ":   mean is " + mean + "   standard deviation is " + sd);
			}
		}
	}

}

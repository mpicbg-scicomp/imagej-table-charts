/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */


import fiji.util.gui.GenericDialogPlus;
import ij.WindowManager;
import javafx.scene.chart.Chart;
import net.imagej.ImageJ;
import net.imagej.display.WindowService;
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
import org.scijava.display.Display;
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

@Plugin(type = Command.class, menuPath="Tutorials>HelloWorld")
public class TableStatistics implements Command {

	static ImageJ ij;

	public static void main(final String... args)
	{
		ij = new ImageJ();
		ij.ui().showUI();

		ij.ui().show("Population of largest towns", createSampleTable(" A"));
		ij.ui().show("Population of largest towns", createSampleTable(" B"));
	}


	public TableStatistics()
	{
	}
	
	private static void showChartWindow(String title, JFreeChart chart) {
		JFrame frame = new ChartFrame(title, chart);
		frame.pack();
		frame.setVisible(true);
	}
	
	private static Pair<DoubleColumn, DoubleColumn> getFirstTwoDoubleColumns(GenericTable table) {
		List<DoubleColumn> l = new LinkedList<>();
		for(Column<?> column : table)
			if(column instanceof DoubleColumn)
				l.add((DoubleColumn) column);

		return new ValuePair<>((DoubleColumn) table.get(1), (DoubleColumn) table.get(2));
	}

	private static JFreeChart generateChartFromTable(GenericTable table) {
		Pair<DoubleColumn, DoubleColumn> columnpair = getFirstTwoDoubleColumns(table);
		final XYSeriesCollection dataset = datasetFromColumns(columnpair.getA(), columnpair.getB());
		JFreeChart chart = ChartFactory.createScatterPlot(
		   "ScatterPlot - " + columnpair.getA().getHeader() + " - " + columnpair.getB().getHeader(), 
		   columnpair.getA().getHeader(), columnpair.getB().getHeader(), dataset);
		//chart.addSubtitle(new TextTitle("subtitle"));
		chart.setBackgroundPaint(Color.white);
		chart.getLegend().setFrame(BlockBorder.NONE);
		return chart;
	}

	private static XYSeriesCollection datasetFromColumns(DoubleColumn xcolumn, DoubleColumn ycolumn) {
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

	private void showSampleGenericDialogPlus() {
		GenericDialogPlus gdp = new GenericDialogPlus("Dialog");
		
		gdp.addNumericField("Beschreibung ", 5 , 2);
		gdp.addChoice("Beschreibung laben", new String[]{"T-test, Wilcoxon"}, "T-test");
		gdp.addImageChoice("Beschreibung bild", ""); 
		gdp.showDialog();
		if (gdp.wasCanceled())
			return;
		
		int numericValue = (int)gdp.getNextNumber();
		String choice = gdp.getNextString();
		//ImagePlus imp = gdp.getNextImage();
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


	/**
	 * This function shows how to create a table with information
	 * about the largest towns in the world.
	 *
	 * @return a table with strings and numbers
	 */
	private static GenericTable createSampleTable(String header_postfix)
	{
		// we create two columns
		GenericColumn nameColumn = new GenericColumn("Town" + header_postfix);
		DoubleColumn populationColumn = new DoubleColumn("Population" + header_postfix);
		DoubleColumn sizeColumn = new DoubleColumn("Size" + header_postfix);

		// we fill the columns with information about the largest towns in the world.
		nameColumn.add("Karachi");
		populationColumn.add(23500000.0);
		sizeColumn.add(45.2);

		nameColumn.add("Bejing");
		populationColumn.add(21516000.0);
		sizeColumn.add(20.0);

		nameColumn.add("Sao Paolo");
		populationColumn.add(21292893.0);
		sizeColumn.add(42.0);

		// but actually, the largest town is Shanghai,
		// so let's add it at the beginning of the table.
		nameColumn.add(0, "Shanghai");
		populationColumn.add(0, 24256800.0);
		sizeColumn.add(0, 10.0);

		// After filling the columns, you can create a table
		GenericTable table = new DefaultGenericTable();

		// and add the columns to that table
		table.add(nameColumn);
		table.add(populationColumn);
		table.add(sizeColumn);

		return table;
	}

	@Parameter
	DisplayService displayService;

	private Pair<GenericTable, String> getActiveTable() {
		try {
			TableDisplay display = (TableDisplay) displayService.getActiveDisplay();
			return new ValuePair((GenericTable) display.get(0), display.getName());
		} catch(NullPointerException | ClassCastException | IndexOutOfBoundsException e) {
			return null;
		}
	}

	@Override
	public void run() {
		System.out.println("Hello World!");
		Pair<GenericTable, String> tableAndItsName = getActiveTable();
		JFreeChart chart = generateChartFromTable(tableAndItsName.getA());
		showChartWindow(tableAndItsName.getB(), chart);
	}
}

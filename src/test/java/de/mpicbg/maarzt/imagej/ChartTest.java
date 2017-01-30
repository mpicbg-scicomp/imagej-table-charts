package de.mpicbg.maarzt.imagej;

import net.imagej.ImageJ;
import net.imagej.plot.AbstractPlot;
import net.imagej.plot.PlotService;
import net.imagej.table.*;
import org.junit.Test;

import java.util.Arrays;

/**
 * Author: Robert Haase, Scientific Computing Facility, MPI-CBG Dresden, rhaase@mpi-cbg.de
 * Date: January 2017
 */
public class ChartTest extends TableTest {
    @Test
    public void testIfPlottingTablesWorks() {
        final ImageJ ij = new ImageJ();
        final PlotService plotService = ij.getContext().getService(PlotService.class);

        final GenericTable table = createSampleTable("");
        final Column<Double> population2015Column = Utils.castColumnType(table.get("Population 2015"), Double.class);
        final Column<Double> population2016Column = Utils.castColumnType(table.get("Population 2016"), Double.class);

        final AbstractPlot boxPlot = BoxPlotPlugin.generateChart(plotService,
                Arrays.asList(population2015Column, population2016Column));
        ij.ui().show(boxPlot);

        final AbstractPlot scatterPlot = ScatterPlotPlugin.generateChart(plotService, population2015Column, population2016Column);
        ij.ui().show(boxPlot);
    }

}

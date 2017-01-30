package de.mpicbg.maarzt.imagej;

import net.imagej.table.Column;
import net.imagej.table.GenericTable;
import org.junit.Test;

/**
 * Author: Robert Haase, Scientific Computing Facility, MPI-CBG Dresden, rhaase@mpi-cbg.de
 * Date: January 2017
 */
public class StatisticsTest extends TableTest {
    @Test
    public void testIfStatisticsWork() {
        GenericTable table = createSampleTable("");

        Column<Double> population2015Column = Utils.castColumnType(table.get("Population 2015"), Double.class);
        Column<Double> population2016Column = Utils.castColumnType(table.get("Population 2016"), Double.class);

        double pvalue = TTestPlugIn.pairedTTest(population2015Column, population2016Column);
    }

}

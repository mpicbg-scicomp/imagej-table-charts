package de.mpicbg.maarzt.imagej;

import net.imagej.table.GenericTable;
import org.junit.Test;

/**
 * Author: Robert Haase, Scientific Computing Facility, MPI-CBG Dresden, rhaase@mpi-cbg.de
 * Date: January 2017
 */
public class StatisticsTest extends TableTest {
    @Test
    public void testIfStatisticsWork() {
        GenericTable table = createSampleTable("2");

        int indexPopulation2015Column = table.getColumnIndex("Population 2015");
        int indexPopulation2016Column = table.getColumnIndex("Population 2016");


        double pvalue = TTestPlugIn.pairedTTest(table.get(indexPopulation2015Column), table.get(indexPopulation2016Column));




    }
}

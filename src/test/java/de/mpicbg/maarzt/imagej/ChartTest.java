package de.mpicbg.maarzt.imagej;

import de.mpicbg.maarzt.imagej.BoxPlotPlugin;
import de.mpicbg.maarzt.imagej.ScatterPlotPlugin;
import de.mpicbg.maarzt.imagej.TableTest;
import net.imagej.table.DefaultGenericTable;
import net.imagej.table.DoubleColumn;
import net.imagej.table.GenericColumn;
import net.imagej.table.GenericTable;
import org.junit.Test;
import scala.tools.jline_embedded.internal.TestAccessible;

/**
 * Author: Robert Haase, Scientific Computing Facility, MPI-CBG Dresden, rhaase@mpi-cbg.de
 * Date: January 2017
 */
public class ChartTest extends TableTest {
    @Test
    public void testIfPlottingTablesWorks() {
        GenericTable table = createSampleTable("1");

        BoxPlotPlugin bpp = new BoxPlotPlugin();
        // how can I do this now? I have a table and I have a plugin...


        ScatterPlotPlugin spp = new ScatterPlotPlugin();
        // see above...


    }


}

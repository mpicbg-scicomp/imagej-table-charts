package de.mpicbg.maarzt.imagej;

import net.imagej.table.DefaultGenericTable;
import net.imagej.table.DoubleColumn;
import net.imagej.table.GenericColumn;
import net.imagej.table.GenericTable;

/**
 * Author: Robert Haase, Scientific Computing Facility, MPI-CBG Dresden, rhaase@mpi-cbg.de
 * Date: January 2017
 */
public abstract class TableTest {


    protected static GenericTable createSampleTable(String header_postfix) {
        GenericColumn nameColumn = new GenericColumn("Town" + header_postfix);
        DoubleColumn population2015Column = new DoubleColumn("Population 2015" + header_postfix);
        DoubleColumn population2016Column = new DoubleColumn("Population 2016" + header_postfix);
        DoubleColumn sizeColumn = new DoubleColumn("Size" + header_postfix);

        nameColumn.add("Karachi");
        population2015Column.add(21300000.0);
        population2016Column.add(23500000.0);
        sizeColumn.add(45.2);

        nameColumn.add("Bejing");
        population2015Column.add(19232000.0);
        population2016Column.add(21516000.0);
        sizeColumn.add(20.0);

        nameColumn.add("Sao Paolo");
        population2015Column.add(21928223.0);
        population2016Column.add(21292893.0);
        sizeColumn.add(42.0);

        nameColumn.add(0, "Shanghai");
        population2015Column.add(0, 28635800.0);
        population2016Column.add(0, 24256800.0);
        sizeColumn.add(0, 10.0);

        GenericTable table = new DefaultGenericTable();
        table.add(nameColumn);
        table.add(population2015Column);
        table.add(population2016Column);
        table.add(sizeColumn);

        return table;
    }
}

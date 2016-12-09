import fiji.util.gui.GenericDialogPlus;
import net.imagej.ImageJ;
import net.imagej.table.DefaultGenericTable;
import net.imagej.table.DoubleColumn;
import net.imagej.table.GenericColumn;
import net.imagej.table.GenericTable;

/**
 * Created by arzt on 08/12/2016.
 */
public class TableStatisticsApp {

	static ImageJ ij;

	public static void main(final String... args) {
		ij = new ImageJ();
		ij.ui().showUI();

		GenericTable table = createSampleTable(" A");
		ij.ui().show("Population of largest towns", table);
		ij.ui().show("Population of largest towns", createSampleTable(" B"));
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

	private static GenericTable createSampleTable(String header_postfix) {
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
}

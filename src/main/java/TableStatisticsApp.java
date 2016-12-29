import fiji.util.gui.GenericDialogPlus;
import net.imagej.ImageJ;
import net.imagej.table.*;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import java.util.Random;

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
		ij.ui().show("Random data", createSampleTable2());
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

	private static GenericTable createSampleTable2() {
		Random random = new Random();
		GenericTable table = new DefaultGenericTable();
		GenericColumn nameColumn = new GenericColumn("Town");
		DoubleColumn populationColumn = new DoubleColumn("Population");
		DoubleColumn sizeColumn = new DoubleColumn("Size");
		for(String town : new String[]{"sadf","sdf","C","D"}) {
			for(int i = 0; i < 20; i++) {
				nameColumn.add(town);
				populationColumn.add(random.nextGaussian() * 2 + 3);
				sizeColumn.add(random.nextGaussian() + 1);
			}
		}
		table.add(nameColumn);
		table.add(populationColumn);
		table.add(sizeColumn);
		return table;
	}

	private static GenericTable createSampleTable(String header_postfix) {
		GenericColumn nameColumn = new GenericColumn("Town" + header_postfix);
		DoubleColumn populationColumn = new DoubleColumn("Population" + header_postfix);
		DoubleColumn sizeColumn = new DoubleColumn("Size" + header_postfix);

		nameColumn.add("Karachi"); populationColumn.add(23500000.0); sizeColumn.add(45.2);
		nameColumn.add("Bejing"); populationColumn.add(21516000.0); sizeColumn.add(20.0);
		nameColumn.add("Sao Paolo"); populationColumn.add(21292893.0); sizeColumn.add(42.0);
		nameColumn.add(0, "Shanghai"); populationColumn.add(0, 24256800.0); sizeColumn.add(0, 10.0);

		GenericTable table = new DefaultGenericTable();
		table.add(nameColumn);
		table.add(populationColumn);
		table.add(sizeColumn);

		return table;
	}

}

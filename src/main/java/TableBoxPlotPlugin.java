import net.imagej.table.Column;
import net.imagej.table.DoubleColumn;
import net.imagej.table.GenericTable;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.scijava.command.Command;
import org.scijava.plugin.Plugin;

import java.awt.Font;
import java.util.*;

@Plugin(type = Command.class, menuPath="Table>BoxPlot")
public class TableBoxPlotPlugin extends AbstractTableChartPlugin {

	protected JFreeChart generateChartFromTable(final String title, final GenericTable table) {
		BoxPlotDialog dialog = runDialog(title, table);
		final BoxAndWhiskerCategoryDataset dataset = dialog.getUseKeyColumn() ?
						createDataset(dialog.getKeyColumn(), dialog.getValueColumns()) :
						createDataset(dialog.getValueColumns());
		return createChart(title, dataset);
	}

	private BoxPlotDialog runDialog(String title, GenericTable table) {
		BoxPlotDialog dialog = new BoxPlotDialog(title, table);
		dialog.showDialog();
		if(!dialog.wasOked())
			throw new AbortRun(null);
		return dialog;
	}

	static private BoxAndWhiskerCategoryDataset createDataset(Collection<DoubleColumn> value_columns) {
		final DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
		for(DoubleColumn value_column : value_columns)
			dataset.add(value_column, "", value_column.getHeader());
		return dataset;
	}

	static private <K> BoxAndWhiskerCategoryDataset createDataset(Column<K> key_column, Collection<DoubleColumn> value_columns) {
		final DefaultBoxAndWhiskerCategoryDataset dataset
				= new DefaultBoxAndWhiskerCategoryDataset();
		for(DoubleColumn value_column : value_columns) {
			String column_title = value_column.getHeader();
			MyMultiMap<K, Double> map = new MyMultiMap<>(key_column, value_column);
			for (Map.Entry<K, List<Double>> entry : map.entrySet())
				dataset.add(entry.getValue(), entry.getKey().toString(), column_title);
		}
		return dataset;
	}

	static private JFreeChart createChart(String title, BoxAndWhiskerCategoryDataset dataset) {
		final CategoryAxis xAxis = new CategoryAxis("Type");
		final NumberAxis yAxis = new NumberAxis("Value");
		yAxis.setAutoRangeIncludesZero(false);
		final BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
		renderer.setFillBox(false);
		final CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);
		final Font font = new Font("SansSerif", Font.BOLD, 14);
		return new JFreeChart(title ,font , plot, true );
	}

	static private class MyMultiMap<K,V> {

		private Map<K, List<V>> map;

		MyMultiMap(Collection<K> keys, Collection<V> values) {
			map = new HashMap<>();
			Iterator<K> kIterator = keys.iterator();
			Iterator<V> vIterator = values.iterator();
			while(kIterator.hasNext() && vIterator.hasNext())
				add(kIterator.next(), vIterator.next());
		}

		void add(K k, V v) { get(k).add(v); }

		List<V> get(K k) {
			List<V> list = map.get(k);
			if(list == null) {
				list = new ArrayList<>();
				map.put(k, list);
			}
			return list;
		}

		Set<Map.Entry<K, List<V>>> entrySet() { return map.entrySet(); }
	}

}

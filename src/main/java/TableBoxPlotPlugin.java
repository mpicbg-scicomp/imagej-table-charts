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

	protected JFreeChart generateChartFromTable(String b, GenericTable a) {
		Collection<DoubleColumn> l = new LinkedList<>();
		l.add((DoubleColumn)a.get(1));
		l.add((DoubleColumn)a.get(2));
		final BoxAndWhiskerCategoryDataset dataset = createDataset(a.get(0), l); // TODO

		final CategoryAxis xAxis = new CategoryAxis("Type");
		final NumberAxis yAxis = new NumberAxis("Value");
		yAxis.setAutoRangeIncludesZero(false);
		final BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
		renderer.setFillBox(false);
		final CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);
		final String chart_title = b;
		final Font font = new Font("SansSerif", Font.BOLD, 14);
		return new JFreeChart(chart_title ,font , plot, true );
	}

	private <K> BoxAndWhiskerCategoryDataset createDataset(Column<K> key_column, Collection<DoubleColumn> value_columns) {
		final DefaultBoxAndWhiskerCategoryDataset dataset
				= new DefaultBoxAndWhiskerCategoryDataset();

		for(DoubleColumn value_column : value_columns) {
			String column_title = value_column.getHeader();
			MyMultiMap<K, Double> map = new MyMultiMap<>(key_column, value_column);
			for(Map.Entry<K, List<Double>> entry : map.entrySet())
				dataset.add(entry.getValue(), entry.getKey().toString(), column_title);
		}

		return dataset;
	}


	private class MyMultiMap<K,V> {

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
			return (list == null) ? new ArrayList<>() : list;
		}

		Set<Map.Entry<K, List<V>>> entrySet() { return map.entrySet(); }
	}

}

import net.imagej.table.DoubleColumn;
import net.imagej.table.GenericTable;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.scijava.command.Command;
import org.scijava.plugin.Plugin;

import java.awt.Font;
import java.util.*;

@Plugin(type = Command.class, menuPath="Table>BoxPlot")
public class BoxPlotPlugin extends ChartPluginBase {

	private GenericTable table;
	private String tableTitle;
	private BoxPlotDialog dialog;
	private DefaultBoxAndWhiskerCategoryDataset chartDataset;
	private JFreeChart chart;

	protected void runWithTable(final String title, final GenericTable table) {
		this.table = table;
		this.tableTitle = title;
		runDialog();
		createDataset();
		createChart();
		showChartWindow(tableTitle, chart);
	}

	private void runDialog() {
		dialog = new BoxPlotDialog(tableTitle, table);
		dialog.showDialog();
		if(!dialog.wasOked())
			throw new AbortRun(null);
	}

	private void createDataset() {
		chartDataset = new DefaultBoxAndWhiskerCategoryDataset();
		for(DoubleColumn value_column : dialog.getValueColumns()) {
			String column_title = value_column.getHeader();
			if(dialog.getUseKeyColumn()) {
				MyMultiMap<?, Double> map = new MyMultiMap<>(dialog.getKeyColumn(), value_column);
				for (Map.Entry<?, List<Double>> entry : map.entrySet())
					chartDataset.add(entry.getValue(), entry.getKey().toString(), column_title);
			}
			else
				chartDataset.add(value_column, "", value_column.getHeader());
		}
	}

	private void createChart() {
		final CategoryAxis xAxis = new CategoryAxis("Type");
		final NumberAxis yAxis = new NumberAxis("Value");
		yAxis.setAutoRangeIncludesZero(false);
		final BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
		renderer.setFillBox(false);
		final CategoryPlot plot = new CategoryPlot(chartDataset, xAxis, yAxis, renderer);
		final Font font = new Font("SansSerif", Font.BOLD, 14);
		chart = new JFreeChart(tableTitle ,font , plot, true );
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

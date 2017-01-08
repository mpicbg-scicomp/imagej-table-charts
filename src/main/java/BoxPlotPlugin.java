import net.imagej.plot.AbstractPlot;
import net.imagej.plot.CategoryChart;
import net.imagej.plot.PlotService;
import net.imagej.plot.RangeStrategy;
import net.imagej.table.Column;
import net.imagej.table.Table;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Attr;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import java.util.*;

@Plugin(type = Command.class, menuPath="Table>BoxPlot")
public class BoxPlotPlugin implements Command {

	@Parameter
	public Table<?,?> table;

	@Parameter
	public boolean useKeyColumn;

	@Parameter(attrs={@Attr(name="elementOf", value="table")})
	public Column<?> keyColumn;

	@Parameter(attrs={@Attr(name="subsetOf", value="table")})
	public Set<Column<Double>> valueColumns;

	@Parameter(type = ItemIO.OUTPUT)
	public AbstractPlot output;

	@Parameter
	private PlotService plotService;

	private CategoryChart chart;

	private String tableTitle = "table title";

	@Override
	public void run() {
		createChart();
	}

	private void createChart() {
		chart = plotService.createCategoryChart();
		chart.setTitle(tableTitle);
		chart.getCategoryAxis().setLabel("Type");
		chart.getNumberAxis().setLabel("Value");
		chart.getNumberAxis().setAutoRange(RangeStrategy.AUTO);
		createDataset();
		output = chart;
	}

	private void createDataset() {
		if (useKeyColumn)
			createDatasetWithKeys();
		else
			createDatasetWithoutKeys();
	}

	private void createDatasetWithoutKeys() {
		chart.getCategoryAxis().setCategories(Collections.singletonList(""));
		for (Column<Double> valueColumn : valueColumns) {
			String column_title = valueColumn.getHeader();
			List<Collection<Double>> values = new ArrayList<>(1);
			values.add(valueColumn);
			chart.getItems().add(chart.createBoxSeries(column_title, values));
		}
	}

	private void createDatasetWithKeys() {
		List<?> keys = new ArrayList<>(new TreeSet<>(keyColumn));
		chart.getCategoryAxis().setCategories(elementsToString(keys));
		for (Column<Double> valueColumn : valueColumns) {
			String column_title = valueColumn.getHeader();
			List<Collection<Double>> values = new ArrayList<>(keys.size());
			MyMultiMap<Object, Double> map = new MyMultiMap<>(keyColumn, valueColumn);
			for (Object key : keys)
				values.add(map.get(key));
			chart.getItems().add(chart.createBoxSeries(column_title, values));
		}
	}

	static private List<String> elementsToString(List<? extends Object> input) {
		List<String> result = new ArrayList<>(input.size());
		for(Object o : input)
			result.add(o.toString());
		return result;
	}

	static private class MyMultiMap<K,V> {

		private Map<K, List<V>> map;

		MyMultiMap(Collection<? extends K> keys, Collection<? extends V> values) {
			map = new HashMap<>();
			Iterator<? extends K> kIterator = keys.iterator();
			Iterator<? extends V> vIterator = values.iterator();
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

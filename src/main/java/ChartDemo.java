import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * A simple demonstration application showing how to create a bar chart.
 */
public class ChartDemo extends JFrame {

	private static final long serialVersionUID = 1L;

	static {
		// set a theme using the new shadow generator feature available in
		// 1.0.14 - for backwards compatibility it is not enabled by default
		ChartFactory.setChartTheme(new StandardChartTheme("JFree/Shadow",
				true));
	}

	public ChartDemo(String title) {
		super(title);
		JFreeChart chart = createChart();
		ChartPanel chartPanel = new ChartPanel(chart, false);
		chartPanel.setFillZoomRectangle(true);
		chartPanel.setMouseWheelEnabled(true);
		chartPanel.setPreferredSize(new Dimension(500, 270));
		setContentPane(chartPanel);
	}

	private static JFreeChart createChart() {
		final XYSeries firefox = new XYSeries( "Firefox" );
		firefox.add( 1.0 , 1.0 );
		firefox.add( 2.0 , 4.0 );
		firefox.add( 3.0 , 3.0 );
		final XYSeries chrome = new XYSeries( "Chrome" );
		chrome.add( 1.0 , 4.0 );
		chrome.add( 2.0 , 5.0 );
		chrome.add( 3.0 , 6.0 );
		final XYSeries iexplorer = new XYSeries( "InternetExplorer" );
		iexplorer.add( 3.0 , 4.0 );
		iexplorer.add( 4.0 , 5.0 );
		iexplorer.add( 5.0 , 4.0 );
		final XYSeriesCollection dataset = new XYSeriesCollection( );
		dataset.addSeries( firefox );
		dataset.addSeries( chrome );
		dataset.addSeries( iexplorer );

		JFreeChart chart = ChartFactory.createXYLineChart(
		   "Browser usage statastics", 
		   "Category",
		   "Score", 
		   dataset,
		   PlotOrientation.VERTICAL, 
		   true, true, false);
		chart.addSubtitle(new TextTitle("subtitle"));
		chart.setBackgroundPaint(Color.white);
		chart.getLegend().setFrame(BlockBorder.NONE);
		return chart;
	}

	/**
	 * Starting point for the demonstration application.
	 *
	 * @param args  ignored.
	 */
	public static void main(String[] args) {
		showSampleChartFrame();
		showFrame(new ChartDemo("ChartDemo"));
	}
	
	public static void showSampleChartFrame() {
		showFrame(new ChartFrame("ChartFrame", createChart()));
	}
	
	public static void showFrame(JFrame frame) {
		frame.pack();
		frame.setVisible(true);
	}

}
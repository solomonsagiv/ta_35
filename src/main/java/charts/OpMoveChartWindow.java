package charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

import api.ApiObject;
import api.Manifest;

public class OpMoveChartWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	DataUpdater updater1;
	DataUpdater updater2;
	DataUpdater updater3;

	JFreeChart chart1 = null;
	JFreeChart chart2 = null;
	JFreeChart chart3 = null;

	// Index series array
	XYSeries[] series1 = { new XYSeries("index")};
	
	XYSeries[] series2 = { new XYSeries("futreBidAskCounter")};

	XYSeries[] series3 = { new XYSeries("futureRatio")};

	XYPlot plot;

	// Colors
	Color blue = new Color(0, 51, 180);
	Color green = new Color(0, 150, 48);
	Color red = new Color(190, 23, 0);

	int timeInSecondes;

	public static void main(String[] args) {
		OpMoveChartWindow chart = new OpMoveChartWindow(2000);
		chart.pack();
		chart.setVisible(true);
	}

	// Constructor
	public OpMoveChartWindow(int timeInSecondes) {
		this.timeInSecondes = timeInSecondes;
		init();
	}

	String strike;

	// Init function
	private void init() {
		showOnScreen(Manifest.screen, this);
		setBounds(778, 0, 477, 830);
		setPreferredSize(new Dimension(470, 833));
		
		// On Close
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onClose(e);
			}
		});

		setLayout(new GridLayout(3, 0));

		chart1 = getChart("index", series1, Color.BLACK, null);
		chart2 = getChart("futreBidAskCounter" + strike, series2, green, blue);
		chart3 = getChart("pesimi" + strike, series3, red, blue);

		ChartPanel chartPanel1 = new ChartPanel(chart1);
		ChartPanel chartPanel2 = new ChartPanel(chart2);
		ChartPanel chartPanel3 = new ChartPanel(chart3);

		add(chartPanel1);
		add(chartPanel2);
		add(chartPanel3);

		// Updaters
		updater1 = new DataUpdater(series1, chart1, chart1.getXYPlot(), "index", .5, timeInSecondes);
		updater1.start();

		updater2 = new DataUpdater(series2, chart2, chart2.getXYPlot(), "futreBidAskCounter" + strike, .1, timeInSecondes);
		updater2.start();

		updater3 = new DataUpdater(series3, chart3, chart3.getXYPlot(), "pesimi" + strike, .1, timeInSecondes);
		updater3.start();
	}

	// On close
	public void onClose(WindowEvent e) {
		updater1.close();
		updater2.close();
		updater3.close();
		dispose();
	}

	// Show on screen
	public static void showOnScreen(int screen, JFrame frame) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gd = ge.getScreenDevices();
		if (screen > -1 && screen < gd.length) {
			frame.setLocation(gd[screen].getDefaultConfiguration().getBounds().x + frame.getX(), frame.getY());
		} else if (gd.length > 0) {
			frame.setLocation(gd[0].getDefaultConfiguration().getBounds().x + frame.getX(), frame.getY());
		} else {
			throw new RuntimeException("No Screens Found");
		}
	}

	// Create complete chart
	@SuppressWarnings("deprecation")
	public JFreeChart getChart(String seriesName, XYSeries[] series, Color seriesColor, Color seriesColor2) {

		XYSeriesCollection data = new XYSeriesCollection();

		for (XYSeries xySeries : series) {
			data.addSeries(xySeries);
		}

		JFreeChart chart = ChartFactory.createXYLineChart(null, null, null, data, PlotOrientation.VERTICAL, false, true,
				false);

		// Style
		plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setDomainGridlinePaint(Color.black);
		plot.setRangeGridlinesVisible(false);
		plot.setRangeGridlinePaint(Color.blue);

		ValueAxis range = plot.getRangeAxis();
		range.setVisible(true);

		// Style lines
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesPaint(0, seriesColor);
		renderer.setSeriesPaint(1, seriesColor2);

		renderer.setSeriesStroke(0, new BasicStroke(1.5f));
		renderer.setSeriesStroke(1, new BasicStroke(1.5f));
		renderer.setSeriesStroke(2, new BasicStroke(1.5f));
		renderer.setShapesVisible(false);
		plot.setRenderer(renderer);
		return chart;
	}

}

// The updater for the chart
class DataUpdater extends Thread {

	ApiObject apiObject = ApiObject.getInstance();

	XYSeries[] series;
	JFreeChart chart;
	XYPlot plot;
	NumberAxis range;

	double[] prices;

	boolean run = true;

	String data;

	ArrayList<Double> dots = new ArrayList<>();
	Font ariel14 = new Font("Ariel", Font.PLAIN, 14);

	double margin = 1;
	int timeInSecondes;

	ValueMarker marker;
	ValueMarker markerZero;

	int x = 0;

	// Constructor
	public DataUpdater(XYSeries[] series, JFreeChart chart, XYPlot plot, String data, double margin,
			int timeInSecondes) {
		this.chart = chart;
		this.plot = plot;
		this.data = data;
		this.margin = margin;
		this.timeInSecondes = timeInSecondes;
		this.prices = new double[series.length];

		// Series
		this.series = series;

		marker = createMarker(0, Color.GRAY, 0.5f);
		markerZero = createMarker(0, Color.BLACK, 1.5f);
		plot.addRangeMarker(marker);

		if (data.contains("futreBidAskCounter")) {
			
			if (apiObject.getOptimiMoveList().size() > 0) {
				for (int i = 0; i < apiObject.getOptimiMoveList().size(); i++) {
					double price = apiObject.getOptimiMoveList().get(i);
					series[0].add(x, price);
					dots.add(price);
					x++;
				}
			}
		}

		if (data.contains("pesimi")) {

			if (apiObject.getPesimiMoveList().size() > 0) {
				for (int i = 0; i < apiObject.getPesimiMoveList().size(); i++) {
					double price = apiObject.getPesimiMoveList().get(i);
					series[0].add(x, price);
					dots.add(price);
					x++;
				}
			}
		}

		if (data.equals("index")) {
			if (apiObject.getIndexList().size() > 0) {
				for (int i = 0; i < apiObject.getIndexList().size(); i++) {
					double price = apiObject.getIndexList().get(i);
					series[0].add(x, price);
					dots.add(price);
					x++;
				}
			}
		}
	}

	@Override
	public void run() {

		while (run) {
			try {
				// Sleep
				Thread.sleep(1000);

				// Get data from the api
				getDataFromApi(data);

				// Update the chart
				updateChart();

			} catch (InterruptedException e) {
				run = false;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}

	// Get the data from the api
	private void getDataFromApi(String data) throws IOException {
		
		if (data.equals("index")) {
			prices[0] = apiObject.getFuture();
		}
		
		if (data.contains("optimi")) {
			prices[0] = apiObject.getOptimiLiveMove();
		}

		if (data.contains("pesimi")) {
			prices[0] = apiObject.getPesimiLiveMove();
		}
	}

	// Floor
	private double floor(double d) {
		return Math.floor(d * 1000) / 1000;
	}

	// Update data
	private void updateChart() {

		for (int i = 0; i < prices.length; i++) {
			dots.add(prices[i]);
		}
		
		// Update the range
		range = (NumberAxis) plot.getRangeAxis();
		range.setRange(Collections.min(dots) - margin, Collections.max(dots) + margin);

		// Append series
		for (int i = 0; i < prices.length; i++) {
			// if (series[i].getItemCount() > timeInSecondes) {
			// series[i].remove(0);
			// }
			series[i].add(x, prices[i]);
		}

		x++;
	}

	// Create marker
	public ValueMarker createMarker(double price, Color color, float stroke) {
		ValueMarker marker = new ValueMarker(price, color, new BasicStroke(stroke));
		marker.setLabelFont(ariel14);
		marker.setLabelOffset(new RectangleInsets(10d, 25d, 0d, 0d));
		return marker;
	}

	public void close() {
		run = false;
	}
}

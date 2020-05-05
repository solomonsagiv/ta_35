package charts;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import org.jfree.data.xy.XYSeries;

import api.ApiObject;
import locals.Themes;

public class INDEX_FUTURECOUNTER_FUTURERATIO_CHART implements IChartCreator {
	
	public static void main(String[] args) {
		int[] bounds = {2697, 2, 465, 837}; 
		INDEX_FUTURECOUNTER_FUTURERATIO_CHART chart = new INDEX_FUTURECOUNTER_FUTURERATIO_CHART(bounds);
		chart.createChart();
		
		Thread thread = new Thread(()->{
			
			while (true) {
				try {
					
					ApiObject.getInstance().getIndexList().add(new Random().nextDouble() * 100);
					ApiObject.getInstance().getFutureBidAskCounterList().add(new Random().nextDouble() * 100);
					
					// Sleep 
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		thread.start();
		
	}
	
	MySingleFreeChart[] singleFreeCharts;
	MySingleFreeChart chart;
	XYSeries[] series;
	Color[] colors;
	ArrayList<ArrayList<Double>> lists;
	
	ApiObject apiObject;
	
	int[] bounds;
	
	// Constructor
	public INDEX_FUTURECOUNTER_FUTURERATIO_CHART(int[] bounds) {
		this.bounds = bounds;
		
		singleFreeCharts = new MySingleFreeChart[2];
		apiObject = ApiObject.getInstance();
		
	}
	
	@Override
	public void createChart() {
		
		// ---------- Index ---------- //
		
		// Params
		series = new XYSeries[1];
		series[0] = new XYSeries("index");
		
		colors = new Color[1];
		colors[0] = Color.BLACK;
		
		lists = new ArrayList<>();
		lists.add(apiObject.getIndexList());
		
		// Create chart
		chart = new MySingleFreeChart(series, colors, 1, lists, 0, true);
		singleFreeCharts[0] = chart;
		
		// ---------- Future counter ---------- //
		// Params
		series = new XYSeries[1];
		series[0] = new XYSeries("future counter");
		
		colors = new Color[1];
		colors[0] = Themes.BLUE;
		
		lists = new ArrayList<>();
		lists.add(apiObject.getFutureBidAskCounterList());
		
		// Create chart
		chart = new MySingleFreeChart(series, colors, 1, lists, 0, true);
		
		singleFreeCharts[1] = chart;
		
		// Display chart
		MyFreeChart myFreeChart = new MyFreeChart(singleFreeCharts, bounds);
		myFreeChart.pack();
		myFreeChart.setVisible(true);
	}
	
}

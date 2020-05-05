package counter;

import api.ApiObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.util.ArrayList;

public class Updater extends Thread {

	ApiObject apiObject = ApiObject.getInstance();

	// local variables
	int count = 0;
	double avgDay;
	double avg;
	double madadPresentDay;

	LocalTime pre_trading_time = LocalTime.parse("09:31:00");
	LocalTime start_trading_time = LocalTime.parse("09:45:00");
	LocalTime current_time;

	// Avg list
	public ArrayList<Double> avg_day = new ArrayList<>();

	// test
	double efresh;
	JSONArray madadNetunim;
	double text;
	boolean run = true;

	Color lightGreen = new Color(12, 135, 0);
	Color lightRed = new Color(229, 19, 0);

	Elements pay;
	Elements company_for_israel;

	WindowTA35 window;

	long startTime;
	long endTime;

	int sleepCounter = 0;
	int sleep = 200;

	// Constructor
	public Updater(WindowTA35 window) {
		this.window = window;
	}

	@Override
	public void run() {
		while (run) {
			try {
				// Write the data to the window
				write();
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				run = false;
				System.out.println("Updater is stopped ");
			}
		}
	}

	private void opTimerCalculator() {
		if (apiObject.getOptimiSell() > 0) {
			apiObject.setOptimiTimer(apiObject.getOptimiTimer() + sleep);
		}

		if (apiObject.getPesimiBuy() > 0) {
			apiObject.setPesimiTimer(apiObject.getPesimiTimer() + sleep);
		}
	}

	// Write the data to the window
	private void write() throws InterruptedException {

		String streamMarket = "רצ";

		try {
			count++;
			current_time = LocalTime.now();
			// LAST
			text = apiObject.getIndex();
			setColor(window.index, text, Color.BLACK);

			// OP
			efresh = apiObject.getFuture() - apiObject.getIndex();
			text = floor(efresh, 100);

			// FUTURE
			setColor(window.future, floor(apiObject.getFuture(), 100), lightGreen);

			// EXP
			text = floor(((apiObject.getIndex() - apiObject.getStartExp()) / apiObject.getStartExp()) * 100, 100);
			setColorPresent(window.monthStartExpField, text);

			// EXP
			text = floor(((apiObject.getIndex() - apiObject.getStartWeekExp()) / apiObject.getStartWeekExp()) * 100, 100);
			setColorPresent(window.weekStartExpField, text);	
			
			// After start trading
			if (!window.start.isEnabled()) {
				if (apiObject.getStatus().contains(streamMarket)) {
					// OP Timer calculator
					opTimerCalculator();
				}

				// AVG OP
				text = floor(avg(), 10);
				setColor(window.op_avg, text, lightGreen);

				// RACES
				window.f_up.setText(String.valueOf(apiObject.getFuture_up()));
				window.f_down.setText(String.valueOf(apiObject.getFuture_down()));
				window.index_up.setText(String.valueOf(apiObject.getIndex_up()));
				window.index_down.setText(String.valueOf(apiObject.getIndex_down()));

				// Future counter
				setColorInt(window.futureCounterField, apiObject.getFutureCounter());
				setColorInt(window.futureRatioField, apiObject.futureRatioCalculationByFuture());

				// counts sum
				setColorInt(window.f_sum, apiObject.getFuture_up() - apiObject.getFuture_down());
				setColorInt(window.index_sum, apiObject.getIndex_up() - apiObject.getIndex_down());

				// Optimi Pesimi move
				window.pesimiBasketField.setText(str(apiObject.getPesimiLiveMove()));

				setColor(window.optimiMoveField, floor(apiObject.getOptimiLiveMove(), 10), lightGreen);
				setColor(window.pesimiMoveField, floor(apiObject.getPesimiLiveMove(), 10), lightGreen);

				// Baskets
				window.optimiBasketField.setText(str(apiObject.getBasketUp()));
				window.pesimiBasketField.setText(str(apiObject.getBasketDown()));
				setColorInt(window.basketsSumField, (apiObject.getBasketUp() - apiObject.getBasketDown()));

				// EqualMove
				setColor(window.equalMoveField, floor(apiObject.getEqualMove(), 10), lightGreen);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() {
		run = false;
	}

	private double avg() {
		efresh = apiObject.getFuture() - apiObject.getIndex();
		avg_day.add(efresh);
		double f = 0;
		for (int i = 0; i < avg_day.size(); i++) {
			f += avg_day.get(i);
		}
		return f / avg_day.size();
	}

	// avg
	public void avgFunction(JTextField avgMax, ArrayList<Double> list, int listLength) {
		if (list.size() < listLength) {
			list.add(efresh);
		} else {
			list.remove(0);
			list.add(efresh);
		}

		// for loop
		double sum = 0;
		for (int i = 0; i < list.size(); i++) {
			sum += list.get(i);
		}
		avg = sum / list.size();

		if (list.size() >= listLength) {
			text = floor(avg, 10);
			setColor(avgMax, text, Color.blue);
		}
	}

	// min
	public void lastMin(JTextField madadTEXT, ArrayList<Double> list, int listLength) throws JSONException {
		double madad = 0;

		if (list.size() < listLength) {
			list.add(apiObject.getIndex());
		} else {
			madad = list.get(0);
			list.remove(0);
			list.add(apiObject.getIndex());
			double madadPresent = ((apiObject.getIndex() / madad) * 100) - 100;

			text = floor(madadPresent, 100);
			setColorPresent(madadTEXT, text);
		}
	}

	// color setting function();
	public void setColor(JTextField textField, double text, Color color) {

		if (text >= 0.0) {
			textField.setForeground(color);
			textField.setText(String.valueOf(text));
		} else {
			textField.setForeground(Color.red);
			textField.setText(String.valueOf(text));
		}
	}

	// color setting function();
	public void setColorInt(JTextField textField, int text) {
		if (text >= 0.0) {
			textField.setForeground(lightGreen);
			textField.setText(String.valueOf(text));
		} else {
			textField.setForeground(lightRed);
			textField.setText(String.valueOf(text));
		}
	}

	// color setting function();
	public void setColorPresent(JTextField textField, double text) {

		if (text >= 0.0) {
			textField.setBackground(lightGreen);
			textField.setText(String.valueOf(text) + "% ");
		} else {
			textField.setBackground(lightRed);
			textField.setText(String.valueOf(text) + "% ");
		}
	}

	// pars double function();
	public double dbl(String string) {
		return Double.parseDouble(string);
	}

	// floor function();
	public double floor(double d, int zeros) {
		return Math.floor(d * zeros) / zeros;
	}

	// To string
	public String str(Object o) {
		return String.valueOf(o);
	}

}

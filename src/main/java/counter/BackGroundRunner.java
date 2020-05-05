package counter;

import java.awt.Color;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.JTextField;

import org.json.JSONArray;

import api.ApiObject;
import arik.Arik;
import book.BookWindow;

public class BackGroundRunner extends Thread {

	double rando_start;
	double rando_end;
	double rando;
	boolean Rando = false;
	double bid;
	double ask;
	String status;
	LocalTime current_time;

	String preOpen = "טפ";
	String streamMarket = "רצ";
	String randomally = "מנ";
	String endMarket = "ספ";
	
	boolean preTradingBool = false;
	boolean streamMarketBool = false;
	boolean randomallyBool = false;
	boolean endMarketBool = false;
	boolean reset = false;

	Color lightGreen = new Color(12, 135, 0);
	Color lightRed = new Color(229, 19, 0);
	
	// Rando
	LocalTime start_rando = LocalTime.of(17, 20, 0);
	LocalTime end_rando = LocalTime.of(17, 27, 0);
	LocalTime end_day = LocalTime.of(17, 45, 0);

	JSONArray j;

	ApiObject apiObject = ApiObject.getInstance();

	WindowTA35 window;
	Updater updater;

	public BackGroundRunner(WindowTA35 window) {
		this.window = window;
	}

	@Override
	public void run() {
		runner();
	}

	int i = 0;

	// Runner
	@SuppressWarnings("static-access")
	public void runner() {
		try {

			if (LocalDate.now().getDayOfWeek().equals("SUNDAY")) {
				end_day = LocalTime.of(16, 45, 0);
				start_rando = LocalTime.of(16, 20, 0);
				end_rando = LocalTime.of(16, 27, 0);
			}

			while (true) {
				try {
					Thread.sleep(1000);

					bid = apiObject.getIndex_bid();
					ask = apiObject.getIndex_ask();
					current_time = LocalTime.now();

					// Pre trading
					if (apiObject.getStatus().contains(preOpen) && !preTradingBool) {
						
						window.startUpdater();

						int futureINT = ((int) apiObject.getFuture() / 10) * 10;
						
						// Open call option window
						BookWindow call_window = new BookWindow(window.updater_id, true);
						call_window.frame.setVisible(true);
						window.updater_id++;
						// call_window.openOption(str(futureINT - 10), true);

						// Open put option window
						BookWindow put_window = new BookWindow(window.updater_id, false);
						put_window.frame.setVisible(true);
						window.updater_id++;
						// put_window.openOption(str(futureINT + 10), false);

						preTradingBool = true;
					}
					
					// Auto start
					if (apiObject.getStatus().contains(streamMarket) && !streamMarketBool
							&& current_time.isAfter(LocalTime.of(9, 44, 0))) {
						
						apiObject.setFutureOpen(apiObject.getFuture());
						window.start.doClick();
						streamMarketBool = true;
					}

					// Rando
					if (apiObject.getStatus().contains(randomally) && !randomallyBool) {
						randomallyBool = true;
						startRando();
						window.closeLogic();
						// Lamp
					}
					
					// End of rando
					if (apiObject.getStatus().contains(endMarket) && randomallyBool) {
						randomallyBool = false;
						endRando();
					}

					// End of Day
					if (apiObject.getStatus().contains(endMarket) && current_time.isAfter(end_day) && !endMarketBool) {
						endMarketBool = true;
						endRando();
						window.export.doClick();
						window.updater.close();
						window.stopDbUpdater();
						
						Arik.getInstance().sendMessage(Arik.sagivID, "TA35: Export complited", null);
					}
					
					if (apiObject.getStatus().contains(endMarket) && current_time.isAfter(LocalTime.of(18, 0, 0)) && endMarketBool && !reset) {
						window.restBtn.doClick();
						reset = true;
					}
					
					if (reset) {
						System.exit(0);
					}
				
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String str(Object o) {
		return String.valueOf(o);
	}

	// floor
	private static double floorDouble(double d) {
		return Math.floor(d * 1000) / 1000;
	}

	// Start rando
	public void startRando() throws Exception {
		rando_start = apiObject.getLast();
	}

	// End rando
	public void endRando() throws Exception {
		rando_end = apiObject.getLast();
		rando = floorDouble(((rando_end - rando_start) / rando_end) * 100);
		setColorPresent(window.rando, floorDouble(rando));
	}

	public double dbl(String string) {
		return Double.parseDouble(string);
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
}

package options;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Options {

	public static final int MONTH = 0;
	public static final int WEEK = 1;

	private double totalDelta = 0;

	List<Strike> strikes;
	HashMap<Integer, Option> optionsMap;
	private int type;

	public Options( int type ) {
		strikes = new ArrayList<Strike>();
		optionsMap = new HashMap<Integer, Option>();
		this.type = type;
	}

	public Option getOption(String name) {
		
		double targetStrike = Double.parseDouble(name.substring(1));
		
		for (Strike strike : strikes) {
			if (strike.getStrike() == targetStrike) {
				if (name.toLowerCase().contains("c")) {
					return strike.getCall();
				} else {
					return strike.getPut();
				}
			}
		}
		return null;
	}
	
	public Strike getStrikeInMoney(double index, int strikeFarLevel) {
		
		double margin = 100000;
		int returnIndex = 0;
		
		List<Strike> strikes = getStrikes();
		
		for (int i = 0; i < strikes.size(); i++) {
			
			Strike strike = strikes.get(i);
			double newMargin = absolute(strike.getStrike() - index);
			
			if (newMargin < margin) {
				margin = newMargin;
				returnIndex = i;
			} else {
				return strikes.get(returnIndex + strikeFarLevel);
			}
		}
		return null;
	}
	
	public ArrayList<Option> getOptionsList() {
		
		ArrayList<Option> optionsList = new ArrayList<>();
		
		for (Strike strike : strikes) {
			optionsList.add(strike.getCall());
			optionsList.add(strike.getPut());
		}

		return optionsList;
	}
	
	public Option getOption(String side, double targetStrike) {
		for (Strike strike : strikes) {
			if (strike.getStrike() == targetStrike) {
				if (side.toLowerCase().contains("c")) {
					return strike.getCall();
				} else {
					return strike.getPut();
				}
			}
		}
		return null;
	}

	// Return single strike by strike price (double)
	public Strike getStrike(double strikePrice) {
		for (Strike strike : strikes) {
			if (strikePrice == strike.getStrike()) {
				return strike;
			}
		}
		return null;
	}

	// Return list of strikes prices
	public ArrayList<Double> getStrikePricesList() {
		ArrayList<Double> list = new ArrayList<>();
		strikes.forEach(strike -> list.add(strike.getStrike()));
		return list;
	}

	// Remove strike from strikes arr by strike price (double)
	public void removeStrike(double strike) {
		int indexToRemove = 0;

		for (int i = 0; i < strikes.size(); i++) {
			if (strikes.get(i).getStrike() == strike) {
				indexToRemove = i;
			}
		}
		strikes.remove(indexToRemove);
	}

	// Remove strike from strikes arr by strike class
	public void removeStrike(Strike strike) {
		strikes.remove(strike);
	}

	// Add strike to strikes arr
	public void addStrike(Strike strike) {

		boolean contains = getStrikePricesList().contains(strike.getStrike());

		// Not inside
		if (!contains) {
			strikes.add(strike);
		}
	}

	public Option getOptionById(int id) {
		return optionsMap.get(id);
	}
	
	// Set option in strikes arr
	public void setOption(Option option) {
		// HashMap
		optionsMap.put(option.getId(), option);
		
		// Strikes list 
		boolean callPut = option.getSide().toLowerCase().contains("c") ? true : false;

		Strike strike = getStrike(option.getStrike());

		if (strike != null) {

			if (callPut) {
				if (strike.getCall() == null) {
					strike.setCall(option);
				}
			} else {
				if (strike.getPut() == null) {
					strike.setPut(option);
				}
			}
		} else {
			
			// Create new if doesn't exist
			strike = new Strike();
			strike.setStrike(option.getStrike());

			if (callPut) {
				strike.setCall(option);
			} else {
				strike.setPut(option);
			}

			// Add strike
			addStrike(strike);
		}
	}
	
	public List<Strike> getStrikes() {
		return strikes;
	}

	public void setStrikes(List<Strike> strikes) {
		this.strikes = strikes;
	}
	
	public String toStringVertical() {
		String string = "";
		for (Strike strike : strikes) {
			string += strike.toString() + "\n\n";
		}
		return string;
	}
	
	public JSONObject getOptionsAsJson() {
		JSONObject json = new JSONObject();
		
		JSONObject callJson;
		JSONObject putJson;
		JSONObject strikeJson;
		
		for (Strike strike : strikes) {
			
			callJson = new JSONObject();
			putJson = new JSONObject();
			strikeJson = new JSONObject();
			
			Option call = strike.getCall();
			callJson.put("bid", call.getBid());
			callJson.put("ask", call.getAsk());
			callJson.put("bid_ask_counter", call.getBidAskCounter());
			callJson.put("high", call.getHigh());
			callJson.put("low", call.getLow());
			callJson.put("base", call.getBase());
			callJson.put("last", call.getLast());
			callJson.put("levels", call.getLevels());
			
			Option put = strike.getPut();
			putJson.put("bid", put.getBid());
			putJson.put("ask", put.getAsk());
			putJson.put("bid_ask_counter", put.getBidAskCounter());
			putJson.put("high", put.getHigh());
			putJson.put("low", put.getLow());
			putJson.put("base", put.getBase());
			putJson.put("last", put.getLast());
			putJson.put("levels", put.getLevels());
			
			strikeJson.put("call", callJson);
			strikeJson.put("put", putJson);
			
			json.put(str(strike.getStrike()), strikeJson);
		}
		return json;
	}
	
	
	public String str(Object o) {
		return String.valueOf(o);
	}

	public double getTotalDelta() {
		return totalDelta;
	}

	public void appendDelta( double delta ) {
		totalDelta += delta;
	}

	public void setTotalDelta( double totalDelta ) {
		this.totalDelta = totalDelta;
	}

	public int getType() {
		return type;
	}

	public double absolute( double d) {
		return Math.abs(d);
	}
}

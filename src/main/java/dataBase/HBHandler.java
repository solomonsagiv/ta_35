package dataBase;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import api.ApiObject;
import arik.Arik;
import counter.WindowTA35;
import options.Option;
import options.Strike;
import tables.ArraysTable;
import tables.StatusTable;
import tables.TA35Data;
import tables.TA35_sum;

public class HBHandler {

	public static void main(String[] args) {
		HBHandler bHandler = new HBHandler();
		bHandler.getStatusHandler().resetDataToHB();
		bHandler.getArraysHandler().resetDataToHB();
	}

	StatusHandler statusHandler;
	ArraysHandler arraysHandler;
	SumHandler sumHandler;

	public HBHandler() {

		statusHandler = new StatusHandler();
		arraysHandler = new ArraysHandler();
		sumHandler = new SumHandler();

	}

	ApiObject apiObject = ApiObject.getInstance();

	public void appendLineData() {
		try {
			// Append data line
			TA35Data data = getDayLineObject();

			HB.save(data);
		} catch (Exception e) {
			e.printStackTrace();
			Arik.getInstance().sendMessage(Arik.sagivID, " TA35: Append lind HB faild \n" + e.getCause(), null);
		}
	}

	public TA35Data getDayLineObject() {

		TA35Data data = new TA35Data();

		try {
			// Append data line
			data.setDate(LocalDate.now().toString());
			data.setTime(LocalTime.now().toString());
			data.setFuture_up(apiObject.getFuture_up());
			data.setFuture(apiObject.getFuture());
			data.setIndex(apiObject.getIndex());
			data.setFuture_down(apiObject.getFuture_down());
			data.setIndex_up(apiObject.getIndex_up());
			data.setIndex_down(apiObject.getIndex_down());
			data.setOptimi_counter(apiObject.getOptimiTimer());
			data.setPesimi_counter(apiObject.getPesimiTimer());
			data.setBasket_up(apiObject.getBasketUp());
			data.setBasket_down(apiObject.getBasketDown());
			data.setOptimiMove(apiObject.getOptimiLiveMove());
			data.setPesimiMove(apiObject.getPesimiLiveMove());
			data.setFutureBidAskCounter(apiObject.getFutureCounter());
			data.setIndexBidAskCounter(apiObject.getIndexCounter());
			data.setOpAvg(apiObject.getOp_avg());
			data.setOptions(apiObject.getOptionsMonth().getOptionsAsJson().toString());
			data.setIndexBid(apiObject.getIndex_bid());
			data.setIndexAsk(apiObject.getIndex_ask());
			data.setBase(apiObject.getBase());

			return data;
		} catch (Exception e) {
			e.printStackTrace();
			Arik.getInstance().sendMessage(Arik.sagivID, " TA35: Append lind HB faild \n" + e.getCause(), null);
		}

		return data;
	}

	public void appendMultipleLinesData(ArrayList<TA35Data> lines) {

	}

	// Convert json array to arrayList<Double>
	private void appendDataFromJsonArrayToDoubleArray(JSONArray jsonArray, ArrayList<Double> list) {

		for (int i = 0; i < jsonArray.length(); i++) {

			list.add(jsonArray.getDouble(i));

		}
	}

	private void updateOptionsFromJsonToClient(String optionsString) {

		if (!optionsString.equals("")) {
			JSONObject optionsJson = new JSONObject(optionsString);

			for (Strike strike : apiObject.getOptionsMonth().getStrikes()) {
				try {

					Option call = strike.getCall();
					Option put = strike.getPut();

					JSONObject callJson = optionsJson.getJSONObject(str(strike.getStrike())).getJSONObject("call");
					JSONObject putJson = optionsJson.getJSONObject(str(strike.getStrike())).getJSONObject("put");

					call.setBidAskCounter(callJson.getInt("bid_ask_counter"));
					put.setBidAskCounter(putJson.getInt("bid_ask_counter"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String str(Object o) {
		return String.valueOf(o);
	}

	// ---------- Status handler class ----------- /
	public class StatusHandler {

		ApiObject apiObject = ApiObject.getInstance();

		public void loadDataFromHB() {

			try {
				StatusTable status = (StatusTable) HB.get_line_by_id(StatusTable.class, 1);

				apiObject.setFuture_up(status.getFuture_up());
				apiObject.setFuture_down(status.getFuture_down());
				apiObject.setIndex_up(status.getIndex_up());
				apiObject.setIndex_down(status.getIndex_down());
				apiObject.setOptimiMoveFromOutSide(status.getOptimiMove());
				apiObject.setPesimiMoveFromOutSide(status.getPesimiMove());
				apiObject.setFutureCounter(status.getFutureBidAskCounter());
				apiObject.setBasketUp(status.getBasket_up());
				apiObject.setBasketDown(status.getBasket_down());
				apiObject.setEqualMove(status.getEqualMove());

				// Exp
				apiObject.setFutureExpRaces(status.getExpConRaces());
				apiObject.setIndexExpRaces(status.getExpIndRaces());
				apiObject.setStartExp(status.getExpStart());
				apiObject.setStartWeekExp(status.getExpWeekStart());

				// Options
				updateOptionsFromJsonToClient(status.getOptions());

				// DB loaded true
				apiObject.setDbLoaded(true);
			} catch (JSONException e) {
				e.printStackTrace();
				Arik.getInstance().sendMessage(Arik.sagivID, " TA35: Load status HB faild \n" + e.getCause(), null);
			}

		}

		public void resetDataToHB() {

			try {

				StatusTable status = (StatusTable) HB.get_line_by_id(StatusTable.class, 1);

				status.setTime(LocalTime.now().toString());
				status.setIndex(0);
				status.setFuture(0);
				status.setFuture_up(0);
				status.setFuture_down(0);
				status.setIndex_up(0);
				status.setIndex_down(0);
				status.setBasket_up(0);
				status.setBasket_down(0);
				status.setOptimi_counter(0);
				status.setPesimi_counter(0);
				status.setOptimiMove(0);
				status.setPesimiMove(0);
				status.setOptions(new JSONObject().toString());
				status.setOpen(0);
				status.setHigh(0);
				status.setLow(0);
				status.setBase(0);
				status.setFutureBidAskCounter(0);
				status.setOpAvg(0);
				status.setIndexBid(0);
				status.setIndexAsk(0);
				status.setEqualMove(0);

				HB.update(status);
			} catch (Exception e) {
				e.printStackTrace();
				Arik.getInstance().sendMessage(Arik.sagivID, " TA35: Reset status HB faild \n" + e.getCause(), null);
			}

		}

		public void updateDataToHB() {
			Thread thread = new Thread(() -> {
				try {

					StatusTable status = new StatusTable();
					status.setId(1);
					status.setName("ta35");
					status.setTime(LocalTime.now().toString());
					status.setIndex(apiObject.getIndex());
					status.setFuture(apiObject.getFuture());
					status.setFuture_up(apiObject.getFuture_up());
					status.setFuture_down(apiObject.getFuture_down());
					status.setIndex_up(apiObject.getIndex_up());
					status.setIndex_down(apiObject.getIndex_down());
					status.setBasket_up(apiObject.getBasketUp());
					status.setBasket_down(apiObject.getBasketDown());
					status.setOptimi_counter(apiObject.getOptimiTimer());
					status.setPesimi_counter(apiObject.getPesimiTimer());
					status.setOptimiMove(apiObject.getOptimiLiveMove());
					status.setPesimiMove(apiObject.getPesimiLiveMove());
					status.setOptions(apiObject.getOptionsMonth().getOptionsAsJson().toString());
					status.setOpen(apiObject.getOpen());
					status.setHigh(apiObject.getHigh());
					status.setLow(apiObject.getLow());
					status.setBase(apiObject.getBase());
					status.setFutureBidAskCounter(apiObject.getFutureCounter());
					status.setOpAvg(apiObject.getOp_avg());
					status.setIndexBid(apiObject.getIndex_bid());
					status.setIndexAsk(apiObject.getIndex_ask());
					status.setExpConRaces(apiObject.getFutureExpRaces());
					status.setExpIndRaces(apiObject.getIndexExpRaces());
					status.setExpStart(apiObject.getStartExp());
					status.setExpWeekStart(apiObject.getStartWeekExp());
					status.setEqualMove(apiObject.getEqualMove());
					HB.update(status);

				} catch (Exception e) {
					e.printStackTrace();
					Arik.getInstance().sendMessage(Arik.sagivID, " TA35: Update status HB faild \n" + e.getCause(),
							null);
				}

			});

			thread.start();
		}
	}

	// ---------- Arrays handler class ----------- //
	public class ArraysHandler {

		ApiObject apiObject = ApiObject.getInstance();

		public void updateDataToHB() {
			Thread thread = new Thread(() -> {
				try {
					ArraysTable arrays = new ArraysTable(1, LocalTime.now().toString(),
							new JSONArray(apiObject.getFutureBidAskCounterList()).toString(),
							new JSONArray(apiObject.getIndexList()).toString(),
							new JSONArray(apiObject.getOpList()).toString());

					HB.update(arrays);
				} catch (JSONException e) {
					e.printStackTrace();
					Arik.getInstance().sendMessage(Arik.sagivID, " TA35: Update arrays HB faild \n" + e.getCause(),
							null);
				}

			});

			thread.start();
		}

		public void loadDataFromHB() {

			try {
				ArraysTable arrays = (ArraysTable) HB.get_line_by_id(ArraysTable.class, 1);
				// Lists
				appendDataFromJsonArrayToDoubleArray(new JSONArray(arrays.getConBdCounterList()),
						apiObject.getFutureBidAskCounterList());
				appendDataFromJsonArrayToDoubleArray(new JSONArray(arrays.getIndexList()), apiObject.getIndexList());
				appendDataFromJsonArrayToDoubleArray(new JSONArray(arrays.getOpList()), apiObject.getOpList());
			} catch (Exception e) {
				e.printStackTrace();
				Arik.getInstance().sendMessage(Arik.sagivID, " TA35: Load arrays HB faild \n" + e.getCause(), null);
			}

		}

		public void resetDataToHB() {

			try {

				ArraysTable arrays = new ArraysTable(1, LocalTime.now().toString(), new JSONArray().toString(),
						new JSONArray().toString(), new JSONArray().toString());

				arrays.setIndexList(new JSONArray().toString());
				arrays.setConBdCounterList(new JSONArray().toString());

				HB.update(arrays);
			} catch (Exception e) {
				e.printStackTrace();
				Arik.getInstance().sendMessage(Arik.sagivID, " TA35: Reset arrays HB faild \n" + e.getCause(), null);
			}
		}

	}

	public class SumHandler {

		public void insetrLine() {

			try {

				String date = LocalDate.now().toString();
				String open = String.valueOf(apiObject.getOpen());
				String close = String.valueOf(apiObject.getIndex());
				String low = String.valueOf(apiObject.getLow());
				String high = String.valueOf(apiObject.getHigh());
				String f_up = String.valueOf(apiObject.getFuture_up());
				String f_down = String.valueOf(apiObject.getFuture_down());
				String index_up = String.valueOf(apiObject.getIndex_up());
				String index_down = String.valueOf(apiObject.getIndex_down());
				String op_avg = String.valueOf(apiObject.getOp_avg());
				String srando = WindowTA35.rando.getText();
				String basketUp = String.valueOf(apiObject.getBasketUp());
				String basketDown = String.valueOf(apiObject.getBasketDown());
				String optimiPress = String.valueOf(apiObject.getOptimiTimer());
				String pesimiPress = String.valueOf(apiObject.getPesimiTimer());
				String tomorrowFuture = String.valueOf(apiObject.getFuture() - apiObject.getIndex());
				double optimiMove = apiObject.getOptimiLiveMove();
				double pesimiMove = apiObject.getPesimiLiveMove();
				int futureCounter = apiObject.getFutureCounter();
				double equalMove = apiObject.getEqualMove();

				TA35_sum ta35_sum = new TA35_sum(date, open, close, high, low, f_up, f_down, index_up, index_down,
						op_avg, srando, tomorrowFuture, basketUp, basketDown, optimiPress, pesimiPress, optimiMove,
						pesimiMove, futureCounter, equalMove);

				HB.save(ta35_sum);

			} catch (Exception e) {
				e.printStackTrace();
				Arik.getInstance().sendMessage(Arik.sagivID, " TA35: Insert sum HB faild \n" + e.getCause(), null);
			}

		}

	}

	// ---------- Getters and setters ----------- //
	public StatusHandler getStatusHandler() {
		if (statusHandler == null) {
			statusHandler = new StatusHandler();
		}
		return statusHandler;
	}

	public void setStatusHandler(StatusHandler statusHandler) {
		this.statusHandler = statusHandler;
	}

	public ArraysHandler getArraysHandler() {
		if (arraysHandler == null) {
			arraysHandler = new ArraysHandler();
		}
		return arraysHandler;
	}

	public void setArraysHandler(ArraysHandler arraysHandler) {
		this.arraysHandler = arraysHandler;
	}

	public SumHandler getSumHandler() {
		return sumHandler;
	}

	public void setSumHandler(SumHandler sumHandler) {
		this.sumHandler = sumHandler;
	}

}

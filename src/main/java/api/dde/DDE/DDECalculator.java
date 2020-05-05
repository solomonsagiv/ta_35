package api.dde.DDE;

import api.ApiObject;

public class DDECalculator extends Thread {

	String[] state = { "", "" };
	String streamMarket = "רצ";
	private ApiObject apiObject = ApiObject.getInstance();
	private boolean run = true;
	
	int mySleep = 0;

	@Override
	public void run() {
		
		while (run) {
			try {
				// Calculate data
				calculateData();
				
				// Api start
				if (apiObject.getStatus().contains(streamMarket)) {
					// Optimi Pesimi behavior
					calculateOptimiPesimiBehavior();
				}
				
				// Update data to apiObject
				updateDataToClient();
				
				// Sleep
				sleep(1000);
			} catch (InterruptedException e) {
				close();
			} catch (NullPointerException e) {
				
			}
		}
	}

	private void updateDataToClient() {
		apiObject.setOptimiLiveMove(optimiLiveMove);
		apiObject.setPesimiLiveMove(pesimiLiveMove);
	}

	boolean optimiStart = false;
	boolean pesimiStart = false;

	double optimiMoveSum = 0;
	double pesimiMoveSum = 0;

	double optimiLiveMove = 0;
	double pesimiLiveMove = 0;

	double lastMove = 0;

	double startPrice = 0;
	double endPrice = 0;

	int LAST_STATE = 1;
	int PRE_STATE = 0;

	String OPTIMI = "optimi";
	String PESIMI = "pesimi";

	double future = 0;
	double index = 0;

	void calculateOptimiPesimiBehavior() {

		future = apiObject.getFuture();
		index = apiObject.getIndex();
		double op = future - index;

		// Optimi
		if (op > 0.0) {

			if (!optimiStart) {

				// Change state
				state[PRE_STATE] = state[LAST_STATE];
				state[LAST_STATE] = OPTIMI;

				// Reset pesimi
				if (state[PRE_STATE].equals(PESIMI)) {
					pesimiStart = false;

					endPrice = index;
					pesimiMoveSum += endPrice - startPrice;
					pesimiLiveMove = pesimiMoveSum;
				}

				// Optimi set data
				optimiStart = true;
				startPrice = index;
			}

			lastMove = index - startPrice;
			optimiLiveMove = optimiMoveSum + lastMove;
		} else

		// Pesimi
		if (op < 0.0) {

			if (!pesimiStart) {

				// Change state
				state[PRE_STATE] = state[LAST_STATE];
				state[LAST_STATE] = PESIMI;

				// Reset pesimi
				if (state[PRE_STATE].equals(OPTIMI)) {
					optimiStart = false;

					endPrice = index;
					optimiMoveSum += endPrice - startPrice;
					optimiLiveMove = optimiMoveSum;
				}

				// Optimi set data
				pesimiStart = true;
				startPrice = index;
			}

			lastMove = index - startPrice;
			pesimiLiveMove = pesimiMoveSum + lastMove;
		} else

		if (op == 0) {
			if (state[LAST_STATE].equals(OPTIMI)) {
				lastMove = index - startPrice;

				optimiLiveMove = optimiMoveSum + lastMove;
			}

			if (state[LAST_STATE].equals(PESIMI)) {
				lastMove = index - startPrice;
				pesimiLiveMove = pesimiMoveSum + lastMove;
			}
		}
	}

	public String str(Object o) {
		return String.valueOf(o);
	}

	public double floor(double d) {
		return Math.floor(d * 100) / 100;
	}
	
	// Calculate data
	private void calculateData() {
		try {
			
			mySleep += 1000;
			
			if (apiObject.getStatus().contains("רצ") && apiObject.isDbLoaded() && mySleep % 5000 == 0) {
				// OP
				double op = apiObject.getFuture() - apiObject.getIndex();
				apiObject.getOpList().add(op);
				apiObject.getOptimiMoveList().add(apiObject.getOptimiLiveMove());
				apiObject.getPesimiMoveList().add(apiObject.getPesimiLiveMove());
				apiObject.getFutureBidAskCounterList().add((double) apiObject.getFutureCounter());
				apiObject.getIndexBidAskCounterList().add(apiObject.getIndexCounter());
				apiObject.getFutureRatioList().add((double) apiObject.futureRatioCalculationByFuture());
				apiObject.getIndexList().add(apiObject.getIndex());
				apiObject.getFutureList().add(apiObject.getFuture());
				
				mySleep = 0;
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	// Close
	public void close() {
		run = false;
	}

	public double dbl(String s) {
		return Double.parseDouble(s);
	}
}

package options;

import api.ApiObject;
import blackScholes.MyBlackScholes;

public class OptionsDataCalculator {

	private Runner runner;

	ApiObject apiObject;

	public OptionsDataCalculator() {
		apiObject = ApiObject.getInstance();
	}

	public void startRunner() {
		if (runner == null) {
			runner = new Runner();
			if (!runner.isAlive()) {
				runner.start();
			}
		}
	}

	public void closeRunner() {
		runner.close();
		runner = null;
	}

	// Runner thread
	private class Runner extends Thread {

		private boolean run = true;

		@Override
		public void run() {

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			while (run) {

				// Bid ask counter
				calculators();

				// StDev
				calculateStDev();

				try {
					// Sleep
					sleep(500);
				} catch (InterruptedException e) {
					run = false;
				}
			}
		}

		private void calculateStDev() {
			for (Option option : apiObject.getOptionsMonth().getOptionsList()) {
				if (option.getStDev() == 0) {
					initStDev(option);
				}
				updateStDev(option);
			}
		}

		private void updateStDev(Option option) {

			boolean callPut;

			if (option.getSide().contains("c")) {
				callPut = true;
			} else {
				callPut = false;
			}

			double[] data = MyBlackScholes.getGreeaks(option.getStrike(), callPut, apiObject.getFuture(),
					apiObject.getInterest(), apiObject.getDaysToExp(), option.getStDev());

			option.setCalcPrice(data[0]);
		}

		private void initStDev(Option option) {
			try {
				double[] arr;

				if (option.getSide().contains("c")) {
					if (option.getStrike() > apiObject.getFuture()) {
						arr = MyBlackScholes.getStDev(option);

						// Update stDev
						option.setStDev(arr[4]);
						Option opositeOption = apiObject.getOptionsMonth().getOption("p" + option.getStrike());
						opositeOption.setStDev(arr[4]);
					}
				} else {
					if (option.getStrike() <= apiObject.getFuture()) {
						arr = MyBlackScholes.getStDev(option);

						// Update stDev
						option.setStDev(arr[4]);
						Option opositeOption = apiObject.getOptionsMonth().getOption("c" + option.getStrike());
						opositeOption.setStDev(arr[4]);
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

//		public void calculateBidAskCounter() {
//			for (Option option : apiObject.getOptionsClass().getOptionsList()) {
//				try {
//					// Cycle change
//					// if (option.lastCycle() > option.preCycle()) {
//
//					// Bid
//					if (option.getLast() >= option.pretBid()) {
//						option.decreaseCalcCounter();
//					}
//
//					// Ask
//					if (option.getLast() <= option.preAsk()) {
//						option.increaseCalcCounter();
//					}
//					// }
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}

		double margin = 35;

		private void calculators() {
			// For each option
			for (Option option : apiObject.getOptionsMonth().getOptionsList()) {
				try {
					
					// Bid Ask Counter 
					bdCounter(option);
					
					// Delta counter 
					deltaCounter(option);
					
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		}

		private void bdCounter(Option option) {
			// Bid
			if (option.getBidStateList().size() >= 2) {
				// Bid
				if (option.lastBid() > option.pretBid()) {
					option.setBidAskCounter(option.getBidAskCounter() + 1);
					option.addBidState(option.lastBid());
				}
				
				// Ask
				if (option.getAskStateList().size() >= 2) {
					// Ask
					if (option.lastAsk() < option.preAsk()) {
						option.setBidAskCounter(option.getBidAskCounter() - 1);
						option.addAskState(option.lastAsk());

					}
				}
				
				option.getBidAskCounterList().add(option.getBidAskCounter());
			}
		}
		
		
		private void deltaCounter(Option option) {
			
			double preAvg = (option.pretBid() + option.preAsk()) / 2;
			
			// Bigger 
			if (option.getLast() > preAvg) {
				
				option.increaseCalcCounter();
				
			}
			
			// Smaller
			if (option.getLast() < preAvg) {
				
				option.decreaseCalcCounter();
				
			}
			
		}
		
		public void close() {
			run = false;
		}
	}
}

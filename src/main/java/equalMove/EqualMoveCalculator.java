package equalMove;

import api.ApiObject;
import threads.MyThread;

public class EqualMoveCalculator extends MyThread implements Runnable {

	// Variables
	int sleep = 200;
	double opPlag;
	boolean equalStatus = false;
	double startPrice = 0;
	double endPrice = 0;

	// Constructor
	public EqualMoveCalculator() {
		super(ApiObject.getInstance());
		setRunnable(this);
		setName("EqualMoveCalculator");
	}

	@Override
	public void run() {

		while (isRun()) {
			try {

				// Calculate
				calculate();

				// Sleep
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				setRun(false);
				getHandler().close();
			}

		}

	}

	private void calculate() {

		double optimiMargin = getApiObject().getOptimiMargin();
		double pesimiMargin = getApiObject().getPesimiMargin();
		
		if (optimiMargin < 0 && pesimiMargin < 0) {

			// Start of the move
			if (!equalStatus) {

				// Set start price
				startPrice = getApiObject().getIndex();

			}

			// Set equalLiveMove
			endPrice = getApiObject().getIndex();
			double equalLiveMove = endPrice - startPrice;
			getApiObject().setEqualLiveMove(equalLiveMove);

			// Set status true
			equalStatus = true;

		} else {

			// End of the move
			if (equalStatus) {

				// Reset live move
				getApiObject().setEqualLiveMove(0);

				// Set end price
				endPrice = getApiObject().getIndex();

				// Get the move
				double move = floor(endPrice - startPrice);

				// Append the move
				getApiObject().appendEqualMove(move);

			}

			// Set status false
			equalStatus = false;

		}
	}

	private double oposite(double d) {
		return d * -1;
	}

	private double floor(double d) {
		return Math.floor(d * 100) / 100;
	}
}

package dataBase;

import api.ApiObject;
import arik.Arik;

public class DbUpdater extends Thread {

	ApiObject apiObject = ApiObject.getInstance();
	
	int sleep = 500;
	int sleepCount = 0;
	
	public DbUpdater() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		init();
	}
	
	private void init() {
		
		apiObject.getHbHandler().getStatusHandler().loadDataFromHB();
		apiObject.getHbHandler().getArraysHandler().loadDataFromHB();
		
		while (!this.isInterrupted()) {
			try {
				sleep(sleep);

//				apiObject.getHbHandler().appendLineData();
//
//				// Update status data
//				apiObject.getHbHandler().getStatusHandler().updateDataToHB();
//
//				// Update Lists
//				if (sleepCount % 30000 == 0) {
//					apiObject.getHbHandler().getArraysHandler().updateDataToHB();
//				}
				
				// Sleep
				sleepCount += sleep;
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				close();
			} catch (Exception e) {
				e.printStackTrace();
				Arik.getInstance().sendMessage(Arik.sagivID, "TA35: DB runner exception \n" + e.getCause(), null);
			}
		}
	}
	
	
	public void close() {
		interrupt();
	}

	public String str(Object o) {
		return String.valueOf(o);
	}

}

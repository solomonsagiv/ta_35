package tables;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.json.simple.JSONObject;

@Entity
@Table(name = "status")
public class StatusTable {
	
	@Id
	@Column(name = "id")
	private int id;
	@Column(name = "name")
	private String name;
	@Column(name = "time")
	private String time = LocalTime.now().toString();
	@Column(name = "con")
	private double con = 0;
	@Column(name = "ind")
	private double index = 0;
	@Column(name = "conUp")
	private int future_up = 0;
	@Column(name = "conDown")
	private int future_down = 0;
	@Column(name = "indUp")
	private int index_up = 0;
	@Column(name = "indDown")
	private int index_down = 0;
	@Column(name = "optimiCounter")
	private int optimi_counter = 0;
	@Column(name = "pesimiCounter")
	private int pesimi_counter = 0;
	@Column(name = "basketUp")
	private int basket_up = 0;
	@Column(name = "basketDown")
	private int basket_down = 0;
	@Column(name = "optimiMove")
	private double optimiMove = 0;
	@Column(name = "pesimiMove")
	private double pesimiMove = 0;
	@Column(name = "conBdCounter")
	private int futureBidAskCounter = 0;
	@Column(name = "opAvg")
	private double opAvg = 0;
	@Column(name = "options")
	private String options = new JSONObject().toString();
	@Column(name = "base")
	private double base = 0;
	@Column(name = "indBid")
	private double indexBid = 0;
	@Column(name = "indAsk")
	private double indexAsk = 0;
	@Column(name = "high")
	private double high = 0;
	@Column(name = "low")
	private double low = 0;
	@Column(name = "open")
	private double open = 0;
	@Column(name = "expStart")
	private double expStart;
	@Column(name = "expConRaces")
	private int expConRaces;
	@Column(name = "expIndRaces")
	private int expIndRaces;
	@Column(name = "expWeekStart")
	private double expWeekStart;
	@Column(name = "equalMove")
	private double equalMove;

	// Empty constructor
	public StatusTable() {}

	public StatusTable(int id, String name, String time, double con, double index, int future_up, int future_down,
			int index_up, int index_down, int optimi_counter, int pesimi_counter, int basket_up, int basket_down,
			double optimiMove, double pesimiMove, int futureBidAskCounter, double opAvg, String options, double base,
			double indexBid, double indexAsk, double high, double low,
			double open, double expStart, int expConRaces, int expIndRaces, double expWeekStart, double equalMove) {
		this.id = id;
		this.name = name;
		this.time = time;
		this.con = con;
		this.index = index;
		this.future_up = future_up;
		this.future_down = future_down;
		this.index_up = index_up;
		this.index_down = index_down;
		this.optimi_counter = optimi_counter;
		this.pesimi_counter = pesimi_counter;
		this.basket_up = basket_up;
		this.basket_down = basket_down;
		this.optimiMove = optimiMove;
		this.pesimiMove = pesimiMove;
		this.futureBidAskCounter = futureBidAskCounter;
		this.opAvg = opAvg;
		this.options = options;
		this.base = base;
		this.indexBid = indexBid;
		this.indexAsk = indexAsk;
		this.high = high;
		this.low = low;
		this.open = open;
		this.expStart = expStart;
		this.expConRaces = expConRaces;
		this.expIndRaces = expIndRaces;
		this.expWeekStart = expWeekStart;
		this.equalMove = equalMove;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getBase() {
		return base;
	}

	public void setBase(double base) {
		this.base = base;
	}

	public double getIndexBid() {
		return indexBid;
	}

	public void setIndexBid(double indexBid) {
		this.indexBid = indexBid;
	}

	public double getIndexAsk() {
		return indexAsk;
	}

	public void setIndexAsk(double indexAsk) {
		this.indexAsk = indexAsk;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public double getContract() {
		return con;
	}

	public void setFuture(double con) {
		this.con = con;
	}

	public double getIndex() {
		return index;
	}

	public void setIndex(double index) {
		this.index = index;
	}

	public int getFuture_up() {
		return future_up;
	}

	public void setFuture_up(int future_up) {
		this.future_up = future_up;
	}

	public int getFuture_down() {
		return future_down;
	}

	public void setFuture_down(int future_down) {
		this.future_down = future_down;
	}

	public int getIndex_up() {
		return index_up;
	}

	public void setIndex_up(int index_up) {
		this.index_up = index_up;
	}

	public int getIndex_down() {
		return index_down;
	}

	public void setIndex_down(int index_down) {
		this.index_down = index_down;
	}

	public int getOptimi_counter() {
		return optimi_counter;
	}

	public void setOptimi_counter(int optimi_counter) {
		this.optimi_counter = optimi_counter;
	}

	public int getPesimi_counter() {
		return pesimi_counter;
	}

	public void setPesimi_counter(int pesimi_counter) {
		this.pesimi_counter = pesimi_counter;
	}

	public int getBasket_up() {
		return basket_up;
	}

	public void setBasket_up(int basket_up) {
		this.basket_up = basket_up;
	}

	public int getBasket_down() {
		return basket_down;
	}

	public void setBasket_down(int basket_down) {
		this.basket_down = basket_down;
	}

	public double getOptimiMove() {
		return optimiMove;
	}

	public void setOptimiMove(double optimiMove) {
		this.optimiMove = optimiMove;
	}

	public double getPesimiMove() {
		return pesimiMove;
	}

	public void setPesimiMove(double pesimiMove) {
		this.pesimiMove = pesimiMove;
	}

	public int getFutureBidAskCounter() {
		return futureBidAskCounter;
	}

	public void setFutureBidAskCounter(int futureBidAskCounter) {
		this.futureBidAskCounter = futureBidAskCounter;
	}

	public double getOpAvg() {
		return opAvg;
	}

	public void setOpAvg(double opAvg) {
		this.opAvg = opAvg;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getCon() {
		return con;
	}

	public void setCon(double con) {
		this.con = con;
	}

	public double getExpStart() {
		return expStart;
	}

	public void setExpStart(double expStart) {
		this.expStart = expStart;
	}

	public int getExpConRaces() {
		return expConRaces;
	}

	public void setExpConRaces(int expConRaces) {
		this.expConRaces = expConRaces;
	}

	public int getExpIndRaces() {
		return expIndRaces;
	}

	public void setExpIndRaces(int expIndRaces) {
		this.expIndRaces = expIndRaces;
	}
	
	public double getExpWeekStart() {
		return expWeekStart;
	}

	public void setExpWeekStart(double expWeekStart) {
		this.expWeekStart = expWeekStart;
	}
	
	public double getEqualMove() {
		return equalMove;
	}

	public void setEqualMove(double equalMove) {
		this.equalMove = equalMove;
	}

	@Override
	public String toString() {
		return "TA35Daily [id=" + id + ", time=" + time + ", future=" + con + ", index=" + index + ", future_up="
				+ future_up + ", future_down=" + future_down + ", index_up=" + index_up + ", index_down=" + index_down
				+ ", optimi_counter=" + optimi_counter + ", pesimi_counter=" + pesimi_counter + ", basket_up="
				+ basket_up + ", basket_down=" + basket_down + "]";
	}

}

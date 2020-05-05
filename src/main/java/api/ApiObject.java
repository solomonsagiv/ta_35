package api;

import dataBase.HBHandler;
import options.Options;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class ApiObject {

    static ApiObject apiObject;

    private String status = null;
    private String streamMarket = "רצ";

    private int futureStartStrike = 0;
    private int futureEndStrike = 0;

    private double interest = 0.006;

    private boolean dbLoaded = false;

    private HBHandler hbHandler;

    // Ticker
    private double futureOpen = 0;
    private double open = 0;
    private double high = 0;
    private double low = 0;
    private double last = 0;
    private double future = 0;
    private double index = 0;
    private double base = 0;
    private double index_bid = 0;
    private double index_ask = 0;
    private double futureBid = 0;
    private double futureAsk = 0;
    private Options optionsMonth;
    private Options optionsWeek;
    private double equalLiveMove = 0;
    private double equalMove = 0;
    private double optimiMargin = 0;
    private double pesimiMargin = 0;

    private int futureCounter = 0;
    private int indexCounter = 0;
    private ArrayList< Double > futureRatioList = new ArrayList<>( );
    private ArrayList< Double > futureBidAskCounterList = new ArrayList<>( );
    private ArrayList< Integer > indexBidAskCounterList = new ArrayList<>( );

    private double optimiSell = 0;
    private double pesimiBuy = 0;

    private int optimiTimer = 0;
    private int pesimiTimer = 0;

    private int basketUp = 0;
    private int basketDown = 0;

    private double optimiLiveMove = 0;
    private double pesimiLiveMove = 0;
    private ArrayList< Double > optimiMoveList = new ArrayList<>( );
    private ArrayList< Double > pesimiMoveList = new ArrayList<>( );
    private double optimiMoveFromOutSide = 0;
    private double pesimiMoveFromOutSide = 0;

    // Rcaes
    private int future_up = 0;
    private int future_down = 0;
    private int index_up = 0;
    private int index_down = 0;

    // Exp
    private int futureExpRaces = 0;
    private int indexExpRaces = 0;
    private int expOptimiTimer = 0;
    private int expPesimiTimer = 0;
    private int expBasketUp = 0;
    private int expBasketDown = 0;
    private double daysToExp = 0;

    private double startExp = 0;
    private double startWeekExp = 0;

    // Lists
    private ArrayList< Double > opList = new ArrayList<>( );
    private ArrayList< Double > indexList = new ArrayList<>( );
    private ArrayList< Double > futureList = new ArrayList<>( );

    private String export_dir = "C://Users/user/Desktop/Work/Data history/TA35/";

    // Private constructor
    private ApiObject() {
        optionsMonth = new Options( Options.MONTH );
        optionsWeek = new Options( Options.WEEK );
    }

    // Get instance
    public static ApiObject getInstance() {
        if ( apiObject == null ) {
            apiObject = new ApiObject( );
        }
        return apiObject;
    }

    public double floor( double d ) {
        return Math.floor( d * 100 ) / 100;
    }

    @SuppressWarnings( "unchecked" )
    public JSONObject getData() {

        JSONObject json = new JSONObject( );
        json.put( "open", open );
        json.put( "high", high );
        json.put( "low", low );
        json.put( "last", last );
        json.put( "future", future );
        json.put( "base", base );
        json.put( "index_bid", index_bid );
        json.put( "index_ask", index_ask );
        json.put( "op_avg", getOp_avg( ) );

        JSONObject races = new JSONObject( );
        races.put( "future_up", future_up );
        races.put( "future_down", future_down );
        races.put( "index_up", index_up );
        races.put( "index_down", index_down );
        json.put( "races", races );

        return json;
    }


    public int futureRatioCalculation() {

        int ratio = ( int ) ( ( ( getIndex( ) - getOpen( ) ) / ( getIndex( ) / 100 ) ) * 100 );
        int res = getFutureCounter( ) - ratio;

        return res;

    }

    public int futureRatioCalculationByFuture() {

        int ratio = ( int ) ( ( ( getFuture( ) - getFutureOpen( ) ) / ( getFuture( ) / 100 ) ) * 100 );
        int res = getFutureCounter( ) - ratio;

        return res;

    }


    public double getOptimiSell() {
        return optimiSell;
    }

    public void setOptimiSell( double optimiSell ) {
        this.optimiSell = optimiSell;
    }

    public double getPesimiBuy() {
        return pesimiBuy;
    }

    public void setPesimiBuy( double pesimiBuy ) {
        this.pesimiBuy = pesimiBuy;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen( double open ) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh( double high ) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow( double low ) {
        this.low = low;
    }

    public double getLast() {
        return last;
    }

    public void setLast( double last ) {
        this.last = last;
    }

    public double getFuture() {
        return future;
    }

    public void setFuture( double future ) {
        this.future = future;
    }

    public double getBase() {
        return base;
    }

    public void setBase( double base ) {
        this.base = base;
    }

    public double getIndex_bid() {
        return index_bid;
    }

    public void setIndex_bid( double newBid ) {

        if ( index_bid != 0 && newBid > index_bid && getStatus( ).contains( getStreamMarket( ) ) ) {
            increasIndexCounter( );
        }

        this.index_bid = newBid;
    }

    public double getIndex_ask() {
        return index_ask;
    }

    public void setIndex_ask( double newAsk ) {

        if ( index_ask != 0 && newAsk < index_ask && getStatus( ).contains( getStreamMarket( ) ) ) {
            decreasIndexCounter( );
        }

        this.index_ask = newAsk;
    }

    public int getFuture_up() {
        return future_up;
    }

    public void setFuture_up( int future_up ) {
        this.future_up = future_up;
    }

    public int getFuture_down() {
        return future_down;
    }

    public void setFuture_down( int future_down ) {
        this.future_down = future_down;
    }

    public int getIndex_up() {
        return index_up;
    }

    public void setIndex_up( int index_up ) {
        this.index_up = index_up;
    }

    public int getIndex_down() {
        return index_down;
    }

    public void setIndex_down( int index_down ) {
        this.index_down = index_down;
    }

    public double getOp_avg() {

        if ( opList.size( ) > 0 ) {

            double avg = 0;
            for ( Double price : opList ) {
                avg += price;
            }
            return floor( avg / opList.size( ) );
        } else {
            return 0;
        }


    }

    public double getIndex() {
        return index;
    }

    public void setIndex( double index ) {
        this.index = index;
    }

    public ArrayList< Double > getOpList() {
        return opList;
    }

    public void setOpList( ArrayList< Double > opList ) {
        this.opList = opList;
    }

    public int getFutureExpRaces() {
        return futureExpRaces;
    }

    public void setFutureExpRaces( int futureExpRaces ) {
        this.futureExpRaces = futureExpRaces;
    }

    public int getIndexExpRaces() {
        return indexExpRaces;
    }

    public void setIndexExpRaces( int indexExpRaces ) {
        this.indexExpRaces = indexExpRaces;
    }

    public double getStartExp() {
        return startExp;
    }

    public void setStartExp( double startExp ) {
        this.startExp = startExp;
    }


    public String getExport_dir() {
        return export_dir;
    }

    public void setExport_dir( String export_dir ) {
        this.export_dir = export_dir;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus( String status ) {
        this.status = status;
    }

    public int getBasketUp() {
        return basketUp;
    }

    public void setBasketUp( int basketUp ) {
        this.basketUp = basketUp;
    }

    public int getBasketDown() {
        return basketDown;
    }

    public void incrementBasketUp() {
        this.basketUp += 1;
    }

    public void incrementBasketDown() {
        this.basketDown += 1;
    }

    public void setBasketDown( int basketDown ) {
        this.basketDown = basketDown;
    }

    public int getOptimiTimer() {
        return optimiTimer;
    }

    public void setOptimiTimer( long optimiTimer ) {
        this.optimiTimer = ( int ) ( optimiTimer / 1000 );
    }

    public int getPesimiTimer() {
        return pesimiTimer;
    }

    public void setPesimiTimer( long pesimiTimer ) {
        this.pesimiTimer = ( int ) ( pesimiTimer / 1000 );
    }

    public int getExpOptimiTimer() {
        return expOptimiTimer;
    }

    public void setExpOptimiTimer( int expOptimiTimer ) {
        this.expOptimiTimer = expOptimiTimer;
    }

    public int getExpPesimiTimer() {
        return expPesimiTimer;
    }

    public void setExpPesimiTimer( int expPesimiTimer ) {
        this.expPesimiTimer = expPesimiTimer;
    }

    public int getExpBasketUp() {
        return expBasketUp;
    }

    public void setExpBasketUp( int expBasketUp ) {
        this.expBasketUp = expBasketUp;
    }

    public int getExpBasketDown() {
        return expBasketDown;
    }

    public void setExpBasketDown( int expBasketDown ) {
        this.expBasketDown = expBasketDown;
    }

    public void setOptimiTimer( int optimiTimer ) {
        this.optimiTimer = optimiTimer;
    }

    public void setPesimiTimer( int pesimiTimer ) {
        this.pesimiTimer = pesimiTimer;
    }

    public int getFutureStartStrike() {
        return futureStartStrike;
    }

    public void setFutureStartStrike( int futureStartStrike ) {
        this.futureStartStrike = futureStartStrike;
    }

    public int getFutureEndStrike() {
        return futureEndStrike;
    }

    public void setFutureEndStrike( int futureEndStrike ) {
        this.futureEndStrike = futureEndStrike;
    }

    public ArrayList< Double > getIndexList() {
        return indexList;
    }

    public void setIndexList( ArrayList< Double > indexList ) {
        this.indexList = indexList;
    }

    @Override
    public String toString() {
        return "ApiObject [open=" + open + ", high=" + high + ", low=" + low + ", last=" + last + ", future=" + future
                + ", index=" + index + ", base=" + base + ", index_bid=" + index_bid + ", index_ask=" + index_ask
                + ", future_up=" + future_up + ", future_down=" + future_down + ", index_up=" + index_up
                + ", index_down=" + index_down + "]";
    }

    public double getDaysToExp() {
        return daysToExp;
    }

    public void setDaysToExp( double daysToExp ) {
        this.daysToExp = daysToExp;
    }

    public ArrayList< Double > getFutureList() {
        return futureList;
    }

    public void setFutureList( ArrayList< Double > futureList ) {
        this.futureList = futureList;
    }

    public double getOptimiLiveMove() {
        return floor( optimiLiveMove + optimiMoveFromOutSide );
    }

    public void setOptimiLiveMove( double optimiLiveMove ) {
        this.optimiLiveMove = optimiLiveMove;
    }

    public double getPesimiLiveMove() {
        return floor( pesimiLiveMove + pesimiMoveFromOutSide );
    }

    public void setPesimiLiveMove( double pesimiLiveMove ) {
        this.pesimiLiveMove = pesimiLiveMove;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest( double interest ) {
        this.interest = interest;
    }

    public double getFutureBid() {
        return futureBid;
    }

    public void setFutureBid( double newBid ) {

        if ( futureBid != 0 && newBid > futureBid && getStatus( ).contains( getStreamMarket( ) ) ) {
            increasFutureCounter( );
        }

        this.futureBid = newBid;
    }

    public double getFutureAsk() {
        return futureAsk;
    }

    public void setFutureAsk( double newAsk ) {

        if ( futureAsk != 0 && newAsk < futureAsk && getStatus( ).contains( getStreamMarket( ) ) ) {
            decreasFutureCounter( );
        }

        this.futureAsk = newAsk;
    }

    public int getFutureCounter() {
        return futureCounter;
    }

    public void setFutureCounter( int futureCounter ) {
        this.futureCounter = futureCounter;
    }

    public synchronized void increasFutureCounter() {
        this.futureCounter++;
    }

    public synchronized void decreasFutureCounter() {
        this.futureCounter--;
    }


    public synchronized void increasIndexCounter() {
        this.setIndexCounter( this.getIndexCounter( ) + 1 );
    }

    public synchronized void decreasIndexCounter() {
        this.setIndexCounter( this.getIndexCounter( ) - 1 );
    }

    public int getIndexCounter() {
        return indexCounter;
    }

    public void setIndexCounter( int indexCounter ) {
        this.indexCounter = indexCounter;
    }

    public String getStreamMarket() {
        return streamMarket;
    }

    public void setStreamMarket( String streamMarket ) {
        this.streamMarket = streamMarket;
    }

    public ArrayList< Double > getOptimiMoveList() {
        return optimiMoveList;
    }

    public void setOptimiMoveList( ArrayList< Double > optimiMoveList ) {
        this.optimiMoveList = optimiMoveList;
    }

    public ArrayList< Double > getPesimiMoveList() {
        return pesimiMoveList;
    }

    public void setPesimiMoveList( ArrayList< Double > pesimiMoveList ) {
        this.pesimiMoveList = pesimiMoveList;
    }

    public ArrayList< Double > getFutureBidAskCounterList() {
        return futureBidAskCounterList;
    }

    public void setFutureBidAskCounterList( ArrayList< Double > futureBidAskCounterList ) {
        this.futureBidAskCounterList = futureBidAskCounterList;
    }

    public ArrayList< Integer > getIndexBidAskCounterList() {
        return indexBidAskCounterList;
    }

    public void setIndexBidAskCounterList( ArrayList< Integer > indexBidAskCounterList ) {
        this.indexBidAskCounterList = indexBidAskCounterList;
    }

    public Options getOptionsMonth() {
        return optionsMonth;
    }

    public void setOptionsMonth( Options optionsMonth ) {
        this.optionsMonth = optionsMonth;
    }

    public Options getOptionsWeek() {
        return optionsWeek;
    }

    public void setOptionsWeek( Options optionsWeek ) {
        this.optionsWeek = optionsWeek;
    }

    public boolean isDbLoaded() {
        return dbLoaded;
    }

    public void setDbLoaded( boolean dbLoaded ) {
        this.dbLoaded = dbLoaded;
    }

    public double getFutureOpen() {
        return futureOpen;
    }

    public void setFutureOpen( double futureOpen ) {
        this.futureOpen = futureOpen;
    }

    public ArrayList< Double > getFutureRatioList() {
        return futureRatioList;
    }

    public void setFutureRatioList( ArrayList< Double > futureRatioList ) {
        this.futureRatioList = futureRatioList;
    }

    public double getOptimiMoveFromOutSide() {
        return optimiMoveFromOutSide;
    }

    public void setOptimiMoveFromOutSide( double optimiMoveFromOutSide ) {
        this.optimiMoveFromOutSide = optimiMoveFromOutSide;
    }

    public double getPesimiMoveFromOutSide() {
        return pesimiMoveFromOutSide;
    }

    public void setPesimiMoveFromOutSide( double pesimiMoveFromOutSide ) {
        this.pesimiMoveFromOutSide = pesimiMoveFromOutSide;
    }

    public HBHandler getHbHandler() {
        if ( hbHandler == null ) {
            hbHandler = new HBHandler( );
        }
        return hbHandler;
    }

    public void setHbHandler( HBHandler hbHandler ) {
        this.hbHandler = hbHandler;
    }

    public double getEqualLiveMove() {
        return equalLiveMove;
    }

    public void setEqualLiveMove( double equalLiveMove ) {
        this.equalLiveMove = equalLiveMove;
    }

    public void appendEqualMove( double move ) {
        setEqualMove( getEqualMove( ) + move );
    }

    public double getEqualMove() {
        return equalMove;
    }

    public void setEqualMove( double equalMove ) {
        this.equalMove = equalMove;
    }

    public double getOptimiMargin() {
        return optimiMargin;
    }

    public void setOptimiMargin( double optimiMargin ) {
        this.optimiMargin = optimiMargin;
    }

    public double getPesimiMargin() {
        return pesimiMargin;
    }

    public void setPesimiMargin( double pesimiMargin ) {
        this.pesimiMargin = pesimiMargin;
    }

    public double getStartWeekExp() {
        return startWeekExp;
    }

    public void setStartWeekExp( double startWeekExp ) {
        this.startWeekExp = startWeekExp;
    }
}

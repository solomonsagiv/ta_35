package api.dde.DDE;

import api.ApiObject;
import api.deltaTest.Calulator;
import com.pretty_tools.dde.DDEException;
import com.pretty_tools.dde.client.DDEClientConversation;
import counter.WindowTA35;
import dataBase.HB;
import locals.L;
import logger.MyLogger;
import options.Level;
import options.Option;
import options.Options;
import options.Strike;
import tables.OptionsDataTable;

import java.util.HashMap;
import java.util.logging.Logger;

public class DDEReader {

    private DDEConnection ddeConnection;
    private DataSheet dataSheet;
    private OptionsSheet optionsSheet;

    // Constructor
    public DDEReader( DDEConnection ddeConnection ) {
        this.ddeConnection = ddeConnection;
    }

    // Start
    public void start() {

        dataSheet = new DataSheet( ddeConnection );
        optionsSheet = new OptionsSheet( ddeConnection );

        dataSheet.startRunners( );
        optionsSheet.start( );
    }

    // Close
    public void close() {
        dataSheet.closeRunners( );
        optionsSheet.close( );
    }
}

// Get ticker data from excel
class DataSheet {

    private String excelPath = "C://Users/user/Desktop/DDE/[DDE.xlsm]Data";
    private boolean run = true;
    ApiObject apiObject = ApiObject.getInstance( );
    DDEClientConversation conversation;
    DataSheetThread dataSheetThread;
    BasketRunner basketRunner;

    // Start runner
    public void startRunners() {

        dataSheetThread = new DataSheetThread( );
        dataSheetThread.start( );

        // Basket runner
        basketRunner = new BasketRunner( );
        basketRunner.start( );

    }

    // Close runners
    public void closeRunners() {

        dataSheetThread.close( );
        basketRunner.close( );

        try {
            conversation.disconnect( );
        } catch ( DDEException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace( );
        }

    }

    // Cells
    String futureCell = "R2C1";
    String indexCell = "R2C2";
    String indexBidCell = "R2C3";
    String indexAskCell = "R2C4";
    String openCell = "R2C5";
    String highCell = "R2C6";
    String lowCell = "R2C7";
    String baseCell = "R2C8";
    String lastCell = "R2C9";
    String statusCell = "R7C1";
    String optimiSellCell = "R2C11";
    String pesimiBuyCell = "R2C10";
    String daysToExpCell = "R4C1";
    String futureBidCell = "R7C2";
    String futureAskCell = "R7C3";
    String optimiMarginCell = "R7C5";
    String pesimiMarginCell = "R7C6";


    // Constructor
    public DataSheet( DDEConnection ddeConnection ) {
        this.conversation = ddeConnection.createNewConversation( excelPath );
    }

    // Basket class
    private class Basket {

        private boolean upDown;

        public Basket() {
        }

        public boolean isUpDown() {
            return upDown;
        }

        public void setUpDown( boolean upDown ) {
            this.upDown = upDown;
        }
    }

    // Data sheet thread
    private class DataSheetThread extends Thread {

        @Override
        public void run() {

            while ( run ) {
                try {

                    // Update data to apiObject
                    update( );

                    // Sleep
                    sleep( 200 );

                } catch ( InterruptedException e ) {
                    // TODO Auto-generated catch block
                    e.printStackTrace( );
                }
            }

        }

        public void update() {
            try {
                // Ticker data
                apiObject.setStatus( conversation.request( statusCell ) );

                if ( apiObject.getStatus( ) != "טפ" ) {
                    apiObject.setDaysToExp( dbl( conversation.request( daysToExpCell ) ) );
                    apiObject.setFuture( dbl( conversation.request( futureCell ) ) );
                    apiObject.setIndex( dbl( conversation.request( indexCell ) ) );
                    apiObject.setIndex_bid( dbl( conversation.request( indexBidCell ) ) );
                    apiObject.setIndex_ask( dbl( conversation.request( indexAskCell ) ) );
                    apiObject.setHigh( dbl( conversation.request( highCell ) ) );
                    apiObject.setLow( dbl( conversation.request( lowCell ) ) );
                    apiObject.setBase( dbl( conversation.request( baseCell ) ) );
                    apiObject.setOpen( dbl( conversation.request( openCell ) ) );
                    apiObject.setLast( dbl( conversation.request( lastCell ) ) );
                    apiObject.setOptimiSell( dbl( conversation.request( optimiSellCell ) ) );
                    apiObject.setPesimiBuy( dbl( conversation.request( pesimiBuyCell ) ) );
                    apiObject.setFutureBid( dbl( conversation.request( futureBidCell ) ) );
                    apiObject.setFutureAsk( dbl( conversation.request( futureAskCell ) ) );
                    apiObject.setOptimiMargin( dbl( conversation.request( optimiMarginCell ) ) );
                    apiObject.setPesimiMargin( dbl( conversation.request( pesimiMarginCell ) ) );
                }

            } catch ( Exception e ) {
                e.printStackTrace( );
            } finally {
            }
        }

        public void close() {
            run = false;
        }

        // To double
        private double dbl( String s ) {
            return Double.parseDouble( s );
        }

    }

    // Basket runner thread
    private class BasketRunner extends Thread {

        private boolean run = true;

        double[] prices_0 = new double[ 35 ];
        double index_0 = 0;

        @Override
        public void run() {
            try {
                init( );
            } catch ( DDEException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace( );
            }
        }

        public void init() throws DDEException {

            try {
                updatePrices( getThePrices( ) );
            } catch ( DDEException e1 ) {
                WindowTA35.popup( e1.getMessage( ).toString( ), e1 );
            } catch ( Exception e ) {
                // TODO: handle exception
            }

            while ( isRun( ) ) {
                try {

                    // Get the prices
                    double[] prices = getThePrices( );

                    // Calculate
                    Basket basket = null;
                    try {
                        basket = isBasket( prices );
                    } catch ( NullPointerException e ) {
                        updatePrices( prices );
                        sleep( 5000 );
                        continue;
                    }

                    System.out.println( basket );

                    // Update apiObject
                    updateApiObject( basket );

                    // Update the prices
                    updatePrices( prices );

                    // Sleep
                    sleep( 1000 );
                } catch ( InterruptedException e ) {
                    close( );
                } catch ( DDEException e ) {
                    // TODO Auto-generated catch block
                    e.printStackTrace( );
                } catch ( Exception e ) {

                    e.printStackTrace( );
                }
            }
        }

        private void updateApiObject( Basket basket ) {
            if ( basket.isUpDown( ) ) {
                apiObject.incrementBasketUp( );
            } else {
                apiObject.incrementBasketDown( );
            }
        }

        private Basket isBasket( double[] prices ) throws NullPointerException {

            int numOfChange = 0;

            // Num of changes
            for ( int i = 0; i < prices.length; i++ ) {
                if ( prices_0[ i ] != prices[ i ] ) {
                    numOfChange++;
                }
            }

            WindowTA35.log.setText( "Baket changes: " + numOfChange );

            // If under 30 throw exception
            if ( numOfChange <= 30 ) {
                throw new NullPointerException( );
            }

            Basket basket = new Basket( );
            if ( apiObject.getIndex( ) > index_0 ) {
                basket.setUpDown( true );
                System.out.println( "up" );
            }
            if ( apiObject.getIndex( ) < index_0 ) {
                basket.setUpDown( false );
                System.out.println( "down" );
            }
            return basket;
        }

        private double[] getThePrices() throws DDEException {

            double[] prices = null;
            try {
                prices = new double[ 35 ];
                for ( int i = 2; i < 37; i++ ) {
                    String cell = "R" + i + "C13";
                    prices[ i - 2 ] = dbl( conversation.request( cell ) );
                }
                return prices;

            } catch ( Exception e ) {
                // TODO: handle exception
            }
            return prices;
        }

        private void updatePrices( double[] prices ) throws DDEException {
            index_0 = apiObject.getIndex( );
            for ( int i = 0; i < prices.length; i++ ) {
                prices_0[ i ] = prices[ i ];
            }
        }

        void close() {
            setRun( false );
        }

        boolean isRun() {
            return run;
        }

        void setRun( boolean run ) {
            this.run = run;
        }

        double dbl( String s ) {
            return Double.parseDouble( s );
        }
    }

}

// Get ticker data from excel
class OptionsSheet extends Thread {

    Logger logger = MyLogger.createLogger( );

    private String excelPath = "C://Users/user/Desktop/DDE/[DDE.xlsm]Import options";
    private boolean run = true;
    ApiObject apiObject = ApiObject.getInstance( );
    DDEClientConversation conversation;
    Calulator calulator;

    final String weekBidCol = "32";
    final String weekAskCol = "33";
    final String weekLastCol = "34";
    final String weekVolumeCol = "35";
    final String weekDeltaCol = "36";

    // Constructor
    public OptionsSheet( DDEConnection ddeConnection ) {
        this.conversation = ddeConnection.createNewConversation( excelPath );
        calulator = new Calulator( );
    }

    @Override
    public void run() {

        setUpOptions( apiObject.getOptionsMonth( ) );
        setUpOptions( apiObject.getOptionsWeek( ) );

        while ( run ) {
            try {
                // Update data
                update( );

                // Sleep
                sleep( 500 );
            } catch ( InterruptedException e ) {
                run = false;
            }
        }
    }

    public void update() {

        // Options month
        for ( Strike strike : apiObject.getOptionsMonth( ).getStrikes( ) ) {
            updateOptionData( strike.getCall( ), apiObject.getOptionsMonth( ) );
            updateOptionData( strike.getPut( ), apiObject.getOptionsMonth( ) );
        }

        // Options week
        for ( Strike strike : apiObject.getOptionsWeek( ).getStrikes( ) ) {
            updateOptionData( strike.getCall( ), apiObject.getOptionsWeek( ) );
            updateOptionData( strike.getPut( ), apiObject.getOptionsWeek( ) );
        }
    }

    private void updateOptionData( Option option, Options options ) {
        int row = option.getCellRow( );

        HashMap< Integer, Level > levels = option.getLevels( );

        int bidPrice1 = 0;
        int askPrice1 = 0;
        int high = 0;
        int low = 0;
        int last = 0;
        int base = 0;
        int cycle = 0;
        int volume = 0;
        double delta = 0;


        // Options month
        if ( options.getType( ) == Options.MONTH ) {
            try {

                bidPrice1 = requestInt( cell( row, 2 ) );
                askPrice1 = requestInt( cell( row, 3 ) );
                high = requestInt( cell( row, 24 ) );
                low = requestInt( cell( row, 23 ) );
                last = requestInt( cell( row, 25 ) );
                base = requestInt( cell( row, 22 ) );
                cycle = requestInt( cell( row, 28 ) );
                volume = requestInt( cell( row, 20 ) );
                delta = requestDouble( cell( row, 30 ) );

                // Bid price
                levels.get( 1 ).setBidPrice( requestInt( cell( row, 2 ) ) );
                levels.get( 2 ).setBidPrice( requestInt( cell( row, 4 ) ) );
                levels.get( 3 ).setBidPrice( requestInt( cell( row, 5 ) ) );
                levels.get( 4 ).setBidPrice( requestInt( cell( row, 6 ) ) );
                levels.get( 5 ).setBidPrice( requestInt( cell( row, 7 ) ) );

                // Bid quantity
                levels.get( 1 ).setBidQuantity( requestInt( cell( row, 8 ) ) );
                levels.get( 2 ).setBidQuantity( requestInt( cell( row, 9 ) ) );
                levels.get( 3 ).setBidQuantity( requestInt( cell( row, 10 ) ) );
                levels.get( 4 ).setBidQuantity( requestInt( cell( row, 11 ) ) );
                levels.get( 5 ).setBidQuantity( requestInt( cell( row, 12 ) ) );

                // Ask price
                levels.get( 1 ).setAskPrice( requestInt( cell( row, 3 ) ) );
                levels.get( 2 ).setAskPrice( requestInt( cell( row, 13 ) ) );
                levels.get( 3 ).setAskPrice( requestInt( cell( row, 14 ) ) );
                levels.get( 4 ).setAskPrice( requestInt( cell( row, 15 ) ) );
                levels.get( 5 ).setAskPrice( requestInt( cell( row, 16 ) ) );

                // Ask quantity
                levels.get( 1 ).setAskQuantity( requestInt( cell( row, 17 ) ) );
                levels.get( 2 ).setAskQuantity( requestInt( cell( row, 18 ) ) );
                levels.get( 3 ).setAskQuantity( requestInt( cell( row, 19 ) ) );
                levels.get( 4 ).setAskQuantity( requestInt( cell( row, 20 ) ) );
                levels.get( 5 ).setAskQuantity( requestInt( cell( row, 21 ) ) );

            } catch ( Exception e ) {
                e.printStackTrace( );
            }

            // Options week
        } else if ( options.getType( ) == Options.WEEK ) {
            try {

                String bidCell = "R" + row + "C" + weekBidCol;
                String askCell = "R" + row + "C" + weekAskCol;
                String lastCell = "R" + row + "C" + weekLastCol;
                String volumeCell = "R" + row + "C" + weekVolumeCol;
                String deltaCell = "R" + row + "C" + weekDeltaCol;

                bidPrice1 = requestInt( bidCell );
                askPrice1 = requestInt( askCell );
                last = requestInt( lastCell );
                volume = requestInt( volumeCell );
                delta = requestDouble( deltaCell );

            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }

        updateTickerData( options, option, levels, bidPrice1, askPrice1, high, low, last, base, volume, delta );


    }

    private void updateTickerData( Options options, Option option, HashMap< Integer, Level > levels, int bidPrice1, int askPrice1, int high,
                                   int low, int last, int base, int volume, double delta ) {

        if ( options.getType( ) == Options.WEEK ) {
        }

        // Calc
        calulator.calc( options, option, last, volume, delta );

        // Update option data
        option.setLevels( levels );

        if ( last != 0 ) {
            option.setLast( last );
        }

        option.setHigh( high );
        option.setLow( low );
        option.setBase( base );

        option.setBid( bidPrice1 );
        option.setAsk( askPrice1 );
        option.getLastList( ).add( ( bidPrice1 + askPrice1 ) / 2 );
        option.addBidState( bidPrice1 );
        option.addAskState( askPrice1 );
        option.setVolume( volume );
        option.setDelta( delta );
    }

    private void setUpOptions( Options options ) {
        try {
            // Wait for future to update
            do {
                sleep( 1000 );
            } while ( apiObject.getFuture( ) == 0 );

            double currentFuture = apiObject.getFuture( );
            int future0 = ( ( ( int ) ( currentFuture / 10 ) ) * 10 ) - 100;

            // Update strikes
            apiObject.setFutureStartStrike( future0 );
            apiObject.setFutureEndStrike( future0 + 200 );

            String cell = "R%sC%s";

            // Update the options map
            for ( int strike = future0; strike <= future0 + 200; strike += 10 ) {

                Option call = new Option( "c", strike );
                Option put = new Option( "p", strike );

                // Get the option cell
                for ( int row = 1; row < 100; row++ ) {
                    String currentStrike = conversation.request( String.format( cell, row, 26 ) );
                    if ( currentStrike.contains( String.valueOf( strike ) ) ) {
                        call.setCellRow( row );
                        put.setCellRow( row + 1 );
                    }
                }

                options.setOption( call );
                options.setOption( put );

                // Logger
                logger.info( call.getName( ) + " " + call.getCellRow( ) + ",  " + put.getName( ) + " " + put.getCellRow( ) );

                // updateOptionsToDB(call, put);
            }
        } catch ( Exception e ) {
            e.printStackTrace( );
        }
    }

    private void updateOptionsToDB( Option call, Option put ) {
        // Update the DB
        OptionsDataTable dataTable = new OptionsDataTable( call.getName( ), 0 );
        HB.save( dataTable );
        dataTable = new OptionsDataTable( put.getName( ), 0 );
        HB.save( dataTable );

        System.out.println( "Saved: " + call.getName( ) + " " + put.getName( ) );
    }

    public double requestDouble( String cell ) throws DDEException {
        try {
            return L.dbl( conversation.request( cell ).replaceAll( "\\s+", "" ) );
        } catch ( Exception e ) {
            return 0;
        }
    }

    public int requestInt( String cell ) throws DDEException {
        try {
            return L.INT( conversation.request( cell ).replaceAll( "\\s+", "" ) );
        } catch ( Exception e ) {
            return 0;
        }
    }

    public String reques( String cell ) throws DDEException {
        return conversation.request( cell ).replaceAll( "\\s+", "" );
    }

    public String cell( int row, int col ) {
        return "R" + row + "C" + col;
    }

    public void close() {
        run = false;
        try {
            conversation.disconnect( );
        } catch ( DDEException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace( );
        }
    }

    // To double
    public double dbl( String s ) {
        return Double.parseDouble( s );
    }

}

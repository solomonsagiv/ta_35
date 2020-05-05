package api.deltaTest;

import api.ApiObject;
import api.dde.DDE.DDEConnection;
import com.pretty_tools.dde.DDEException;
import com.pretty_tools.dde.client.DDEClientConversation;
import locals.L;
import options.Option;
import options.Options;
import options.Strike;

public class DDEReaderTest {

    String path = "C://Users/user/Desktop/DDE/[testDelta.xlsx]נגזרים";
    DDEClientConversation conversation;
    final String paperNameCol = "1";
    final String strikeCol = "2";
    final String bidCol = "3";
    final String askCol = "4";
    final String lastCol = "5";
    final String volumeCol = "6";
    final String deltaCol = "7";
    int startStrike = 1350;
    int endStrike = 1450;
    final int startRow = 2;
    final int endRow = 24;
    Options options;
    boolean init = false;
    DDEConnection connection;
    Calulator calulator;

    public DDEReaderTest( ApiObject apiObject ) {
        connection = new DDEConnection( );
        this.conversation = connection.createNewConversation( path );
        options = apiObject.getOptionsMonth( );
        calulator = new Calulator( );
    }

    public void read() {

        if ( !init ) {
            initOptions( );
            init = true;
        }

        for ( int i = startRow; i < endRow; i += 2 ) {
            try {
                String strikeCell = "R" + i + "C" + strikeCol;
                Strike strike = options.getStrike( requestDouble( strikeCell ) );

                Option call = strike.getCall( );
                Option put = strike.getPut( );
                readOption( call, i );
                readOption( put, i + 1 );

            } catch ( Exception e ) {
                e.printStackTrace( );
            }
        }

    }

    public void readOption( Option option, int row ) throws DDEException {

        String bidCell = "R" + row + "C" + bidCol;
        String askCell = "R" + row + "C" + askCol;
        String lastCell = "R" + row + "C" + lastCol;
        String volumeCell = "R" + row + "C" + volumeCol;
        String deltaCell = "R" + row + "C" + deltaCol;

        int bid = requestInt( bidCell );
        int ask = requestInt( askCell );
        int last = requestInt( lastCell );
        int volume = requestInt( volumeCell );
        double delta = requestDouble( deltaCell );

        calulator.calc( options, option, last, volume, delta );

        option.setBid( bid );
        option.setAsk( ask );
        option.setLast( last );
        option.setVolume( volume );
        option.setDelta( delta );

    }


    public double requestDouble( String cell ) throws DDEException {
        return L.dbl( conversation.request( cell ).replaceAll( "\\s+", "" ) );
    }

    public int requestInt( String cell ) throws DDEException {
        return L.INT( conversation.request( cell ).replaceAll( "\\s+", "" ) );
    }

    public String reques( String cell ) throws DDEException {
        return conversation.request( cell ).replaceAll( "\\s+", "" );
    }

    private void initOptions() {

        for ( int i = startRow; i < endRow; i++ ) {
            try {
                String strikeCell = "R" + i + "C" + strikeCol;
                String side = i % 2 == 0 ? "C" : "p";
                String cell = conversation.request( strikeCell ).replaceAll( "\\s+", "" );
                int strike = L.INT( cell );

                options.setOption( new Option( side, strike ) );
            } catch ( Exception e ) {
                e.printStackTrace( );
            }
        }

    }

}

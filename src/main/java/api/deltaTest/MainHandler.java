package api.deltaTest;

import api.ApiObject;
import threads.MyThread;

public class MainHandler extends MyThread implements Runnable {

    // Variables
    DDEReaderTest reader;

    // Reader test
    public MainHandler( ApiObject apiObject ) {
        super( apiObject );
        setRunnable( this );
        reader = new DDEReaderTest( apiObject );
    }

    @Override
    public void run() {

        while ( isRun( ) ) {
            try {
                // Sleep
                Thread.sleep( 500 );

                // Read
                reader.read( );

            } catch ( InterruptedException e ) {
                break;
            }
        }
    }

}

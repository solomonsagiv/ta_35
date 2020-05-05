package api.deltaTest;

import api.ApiObject;
import com.pretty_tools.dde.DDEException;

public class DDEConnectionTestDelta {

    public static void main( String[] args ) throws DDEException {
        DDEConnectionTestDelta testDelta = new DDEConnectionTestDelta();
    }

    // Variables
    ApiObject apiObject = ApiObject.getInstance();
    MainHandler mainHandler;

    // Constructor
    public DDEConnectionTestDelta() throws DDEException {
         mainHandler = new MainHandler( apiObject );
         mainHandler.getHandler().start();
    }
}

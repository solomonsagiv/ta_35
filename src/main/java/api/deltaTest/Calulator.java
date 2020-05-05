package api.deltaTest;

import api.ApiObject;
import options.Option;
import options.Options;

public class Calulator {

    ApiObject apiObject;

    public Calulator() {
        apiObject = ApiObject.getInstance( );
    }

    public void calc( Options options, Option option, int newLast, int newVolume, double newDelta ) {


        // Volume check
        if ( newVolume > option.getVolume( ) ) {

            double quantity = newVolume - option.getVolume( );
            double delta = 0;

            // Buy ( Last == pre ask )
            if ( newLast == option.getAsk( ) ) {
                delta = quantity * newDelta;
            }

            // Buy ( Last == pre bid )
            if ( newLast == option.getBid( ) ) {
                delta = quantity * newDelta * -1;
            }

            if ( options.getType() == Options.MONTH ) {
                apiObject.getOptionsMonth().appendDelta( delta );
//                System.out.println( "Month : " + apiObject.getOptionsMonth().getTotalDelta());
            } else if ( options.getType() == Options.WEEK ) {
                apiObject.getOptionsWeek().appendDelta( delta );
//                System.out.println( "Week : " + apiObject.getOptionsWeek().getTotalDelta() );
            }
        }
    }

}

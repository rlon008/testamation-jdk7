package nz.co.testamation.common.time;

import org.joda.time.DateTime;
import org.joda.time.Duration;


public class DateTimeProviderImpl implements DateTimeProvider {

    @Override
    public DateTime getDateTime() {
        return new DateTime();
    }

    @Override
    public void waitFor( Duration duration, Clock clock ) {
        try {
            Thread.sleep( duration.getMillis() );
        } catch ( InterruptedException e ) {
            Thread.currentThread().interrupt();
            throw new RuntimeException( "Clock wait interrupted" );
        }
    }
}

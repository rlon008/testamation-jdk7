package nz.co.testamation.common.time;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public interface DateTimeProvider {
    DateTime getDateTime();

    void waitFor( Duration duration, Clock clock );
}

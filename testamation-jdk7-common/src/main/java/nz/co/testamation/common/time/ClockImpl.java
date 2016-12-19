/*
 * Copyright 2016 Ratha Long
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nz.co.testamation.common.time;


import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class ClockImpl implements Clock {

    private final DateTimeProvider dateTimeProvider;
    private final String timeZoneSuffix;

    public ClockImpl() {
        this( new DateTimeProviderImpl(), "" );
    }

    public ClockImpl( String timeZoneSuffix ) {
        this( new DateTimeProviderImpl(), timeZoneSuffix );
    }

    public ClockImpl( DateTimeProvider dateTimeProvider, String timeZoneSuffix ) {
        this.dateTimeProvider = dateTimeProvider;
        this.timeZoneSuffix = timeZoneSuffix;
    }

    public DateTime now() {
        return dateTimeProvider.getDateTime();
    }

    @Override
    public long currentTimestampMillis() {
        return now().getMillis();
    }

    @Override
    public LocalDate today() {
        return now().toLocalDate();
    }

    public String getTimeZoneSuffix() {
        return timeZoneSuffix;
    }

    public DateTime todayAt( LocalTime time ) {
        return now().withFields( time );
    }

    public void waitFor( Duration duration ) {
        dateTimeProvider.waitFor( duration, this );
    }

}

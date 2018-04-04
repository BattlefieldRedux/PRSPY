/*
 * Copyright (c) 2018 uturista.pt
 *
 * Licensed under the Attribution-NonCommercial 4.0 International (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://creativecommons.org/licenses/by-nc/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pt.uturista.prspy.model;

import java.util.Locale;

/**
 * Representation of an exact time interval, with the smallest unit being the
 * milliseconds and the largest days.<br>
 * <br>
 * Months and years are not represented due to the its time's ambiguity (
 * <code>eg: a month can have 28, 29, 30 or 31 days</code>). <br>
 * <br>
 * The TimeSpan class also offers operations to convert units.
 *
 */
public class TimeSpan {
    private final long milliseconds;
    private final long seconds;
    private final long minutes;
    private final long hours;
    private final long days;
    private Locale locale = Locale.US;

    /**
     * Constructs a new TimeSpan object with the given values, where the it
     * follows a order from the small to largest, meaning the first value will
     * define the number of milliseconds and the 6th will define the number of
     * days.<br>
     * <br>
     * <ul>
     * <li><code>getInstance(1000), sets an interval of 1000ms</code></li>
     * <li>
     * <code>getInstance(1000, 10), sets an interval of 1000ms and 10s</code></li>
     * <li>
     * <code>getInstance(1000, -10, 10), sets an interval of 1000ms, 0s and 10m</code>
     * </li>
     * </ul>
     *
     * @param values Array of values to construct the time span.
     * @return
     */

    private TimeSpan(Builder builder) {

        this.milliseconds = builder.milliseconds % 1000;
        this.seconds = (builder.milliseconds / 1000) % 60;
        this.minutes = (builder.milliseconds / (1000 * 60)) % 60;
        this.hours = (builder.milliseconds / (1000 * 60 * 60)) % 24;
        this.days = builder.milliseconds / (1000 * 60 * 60 * 24);

    }

    public static ConvertWrapper convert(long value, Time unit) {
        TimeSpan time;

        switch (unit) {
            case millisecond:
                time = new Builder().milliseconds(value).build();
                break;
            case day:
                time = new Builder().days(value).build();
                break;
            case hour:
                time = new Builder().hours(value).build();
                break;
            case minute:
                time = new Builder().minutes(value).build();
                break;
            case second:
                time = new Builder().seconds(value).build();
                break;
            default:
                return null;
        }

        return new ConvertWrapper(time);
    }

    /**
     * @param unit
     * @return
     */
    public long convertTo(Time unit) {
        switch (unit) {
            case millisecond:
                return toMiliseconds();
            case day:
                return toDays();
            case hour:
                return toHours();
            case minute:
                return toMinutes();
            case second:
                return toSeconds();
            default:
                return 0;

        }
    }

    public long getDays() {
        return this.days;
    }

	/*
     * public TimeSpan tick() { return this.tick(Time.second); }
	 * 
	 * public TimeSpan tick(Time unit) { return this.tick(Time.second, 1); }
	 * 
	 * public TimeSpan tick(Time unit, long rate) { switch (unit) { case
	 * millisecond: return new TimeSpan(getMilliSeconds() - rate, getSeconds(),
	 * getMinutes(), getHours(), getDays()); case day: return new
	 * TimeSpan(getMilliSeconds(), getSeconds(), getMinutes(), getHours(),
	 * getDays() - rate); case hour: return new TimeSpan(getMilliSeconds(),
	 * getSeconds(), getMinutes(), getHours() - rate, getDays()); case minute:
	 * return new TimeSpan(getMilliSeconds(), getSeconds(), getMinutes() - rate,
	 * getHours(), getDays()); case second: return new
	 * TimeSpan(getMilliSeconds(), getSeconds() - rate, getMinutes(),
	 * getHours(), getDays()); } return null; }
	 */
    // ********************************
    // ********** Getters *************
    // ********************************

    public long getHours() {
        return this.hours;
    }

    public long getMinutes() {
        return this.minutes;
    }

    public long getSeconds() {
        return this.seconds;
    }

    public long getMilliSeconds() {
        return this.milliseconds;
    }

    public String getDays(String format) {
        return String.format(locale, format, this.days);
    }

    public String getHours(String format) {
        return String.format(locale, format, this.hours);
    }

    public String getMinutes(String format) {
        return String.format(locale, format, this.minutes);
    }

    public String getSeconds(String format) {
        return String.format(locale, format, this.seconds);
    }

    public String getMilliSeconds(String format) {
        return String.format(locale, format, this.milliseconds);
    }

    private long toDays() {
        return days;
    }

    // ********************************
    // ********* Converters ***********
    // ** From <TimeSpan> to <value> **
    // ********************************

    private long toHours() {
        long days = this.days * 24;
        return days + hours;
    }

    private long toMinutes() {
        long days = this.days * 24 * 60;
        long hours = this.hours * 60;
        return days + hours + minutes;
    }

    private long toSeconds() {
        long days = this.days * 24 * 60 * 60;
        long hours = this.hours * 60 * 60;
        long minutes = this.minutes * 60;
        long seconds = this.seconds;
        return days + hours + minutes + seconds;
    }

    private long toMiliseconds() {
        long days = this.days * 24 * 60 * 60 * 1000;
        long hours = this.hours * 60 * 60 * 1000;
        long minutes = this.minutes * 60 * 1000;
        long seconds = this.seconds * 1000;

        return days + hours + minutes + seconds;
    }

    @Override
    public String toString() {
        return "TimeSpan(" + getDays() + "d " + getHours() + "h "
                + getMinutes() + "m " + getSeconds() + "s " + getMilliSeconds()
                + "ms)";
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null || obj.getClass() != this.getClass())
            return false;

        TimeSpan span = (TimeSpan) obj;

        boolean hasSameDays = this.days == span.days;
        boolean hasSameHours = this.hours == span.hours;
        boolean hasSameMinutes = this.minutes == span.minutes;
        boolean hasSameSeconds = this.seconds == span.seconds;
        boolean hasSameMilliseconds = this.milliseconds == span.milliseconds;

        return hasSameDays && hasSameHours && hasSameMinutes && hasSameSeconds
                && hasSameMilliseconds;
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    /**
     * Different units available in the class TimeSpan.<br>
     * <ul>
     * <li>millisecond</li>
     * <li>second</li>
     * <li>minute</li>
     * <li>hour</li>
     * <li>day</li>
     * </ul>
     */
    public enum Time {
        millisecond, second, minute, hour, day
    }

    public static class ConvertWrapper {
        TimeSpan timespan;

        private ConvertWrapper(TimeSpan timespan) {
            this.timespan = timespan;
        }

        public long to(Time unit) {
            return timespan.convertTo(unit);
        }
    }

    public static class Builder {
        private long milliseconds = 0;

        public Builder milliseconds(long val) {
            this.milliseconds += val;
            return this;
        }

        public Builder seconds(long val) {
            this.milliseconds += val * 1000;
            return this;
        }

        public Builder minutes(long val) {
            this.milliseconds += val * 60 * 1000;
            return this;
        }

        public Builder hours(long val) {
            this.milliseconds += val * 60 * 60 * 1000;
            return this;
        }

        public Builder days(long val) {
            this.milliseconds += val * 24 * 60 * 60 * 1000;
            return this;
        }

        public TimeSpan build() {
            return new TimeSpan(this);
        }

    }
}

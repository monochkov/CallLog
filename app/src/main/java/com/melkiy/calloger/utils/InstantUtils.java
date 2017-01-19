package com.melkiy.calloger.utils;

import org.joda.time.Instant;

public class InstantUtils {

    public static boolean isToday(Instant instant) {
        return instant.toDateTime().dayOfYear() != Instant.now().toDateTime().dayOfYear();
    }

    public static boolean isDayEquals(Instant oldDay, Instant newDay) {
        return newDay.toDateTime().dayOfYear().get() != oldDay.toDateTime().dayOfYear().get();
    }
}

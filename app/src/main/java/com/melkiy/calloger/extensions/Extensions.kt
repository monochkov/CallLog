package com.melkiy.calloger.extensions

import android.view.View
import org.joda.time.Instant

var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

fun Instant.isNotToday(): Boolean = toDateTime().dayOfYear() != Instant.now().toDateTime().dayOfYear()

fun Instant.isDayNotEquals(newDay: Instant?): Boolean = newDay?.toDateTime()?.dayOfYear()?.get() != toDateTime().dayOfYear().get()
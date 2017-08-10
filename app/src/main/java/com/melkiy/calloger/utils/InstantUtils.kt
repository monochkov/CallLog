/*
    Copyright (C) 2015 Ihor Monochkov

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.melkiy.calloger.utils

import org.joda.time.Instant

object InstantUtils {

    fun isToday(instant: Instant?): Boolean {
        return instant?.toDateTime()?.dayOfYear() !== Instant.now().toDateTime().dayOfYear()
    }

    fun isDayEquals(oldDay: Instant?, newDay: Instant?): Boolean {
        return newDay?.toDateTime()?.dayOfYear()?.get() != oldDay?.toDateTime()?.dayOfYear()?.get()
    }
}

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

package com.melkiy.calloger.models

import org.joda.time.Instant

import java.io.Serializable

class Call : Serializable {

    var name: String? = null
    var number: String? = null
    var type: Type? = null
    var date: Instant? = null
    var durationInSeconds: Int = 0
    var message: String? = null

    override fun toString(): String {
        return "Call{" +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", type=" + type +
                ", date=" + date +
                ", durationInSeconds=" + durationInSeconds +
                ", message='" + message + '\'' +
                '}'
    }

    enum class Type(val value: Int) {
        INCOMING(1), OUTGOING(2), MISSED(3), DISMISSED(5);

        companion object {

            fun fromValue(value: Int): Type {
                for (type in values()) {
                    if (type.value == value) {
                        return type
                    }
                }
                throw IllegalStateException("There is no type with value " + value)
            }
        }
    }
}

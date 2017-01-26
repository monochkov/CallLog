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

package com.melkiy.calloger.models;

import org.joda.time.Instant;

import java.io.Serializable;

public class Call implements Serializable {

    private String name;
    private String number;
    private Type type;
    private Instant date;
    private int durationInSeconds;
    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(int durationInSecends) {
        this.durationInSeconds = durationInSecends;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Call{" +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", type=" + type +
                ", date=" + date +
                ", durationInSeconds=" + durationInSeconds +
                ", message='" + message + '\'' +
                '}';
    }

    public enum Type {
        INCOMING(1), OUTGOING(2), MISSED(3), DISMISSED(5);

        private final int value;

        Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Type fromValue(int value) {
            for (Type type : values()) {
                if (type.getValue() == value) {
                    return type;
                }
            }
            throw new IllegalStateException("There is no type with value " + value);
        }
    }
}

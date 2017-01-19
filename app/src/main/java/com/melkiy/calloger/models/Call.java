package com.melkiy.calloger.models;

import org.joda.time.Instant;

import java.io.Serializable;

public class Call implements Serializable {

    private String name;
    private String number;
    private int type;
    private Instant date;
    private int durationInSec;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public int getDurationInSec() {
        return durationInSec;
    }

    public void setDurationInSec(int durationInSec) {
        this.durationInSec = durationInSec;
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
                ", durationInSec=" + durationInSec +
                ", message='" + message + '\'' +
                '}';
    }
}

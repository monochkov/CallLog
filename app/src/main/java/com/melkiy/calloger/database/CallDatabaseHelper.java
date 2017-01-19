package com.melkiy.calloger.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.melkiy.calloger.models.Call;

import org.joda.time.Instant;

import java.util.ArrayList;
import java.util.List;

public class CallDatabaseHelper extends DatabaseHelper {

    //Call table
    public static final String TABLE_NAME = "calls";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String NUMBER = "number";
    public static final String TYPE = "type";
    public static final String DATE = "date";
    public static final String DURATION = "duration";
    public static final String MESSAGE = "message";

    public CallDatabaseHelper(Context context) {
        super(context);
    }

    public long insert(Call call) {
        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(TABLE_NAME, null, toContentValues(call));
        db.close();
        return id;
    }

    public List<Call> getAll() {
        SQLiteDatabase db = getReadableDatabase();
        List<Call> list = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, "id DESC");
        while (cursor.moveToNext()) {
            list.add(fromCursor(cursor));
        }
        cursor.close();
        db.close();
        return list;
    }

    public static Call fromCursor(Cursor cursor) {
        int i = 1;
        Call call = new Call();
        call.setName(cursor.getString(i++));
        call.setNumber(cursor.getString(i++));
        call.setType(cursor.getInt(i++));
        call.setDate(new Instant(cursor.getString(i++)));
        call.setDurationInSec(cursor.getInt(i++));
        call.setMessage(cursor.getString(i));
        System.out.println(call.toString());
        return call;
    }

    public static ContentValues toContentValues(Call call) {
        ContentValues cv = new ContentValues();
        cv.put(NAME, call.getName());
        cv.put(NUMBER, call.getNumber());
        cv.put(TYPE, call.getType());
        cv.put(DATE, String.valueOf(call.getDate()));
        cv.put(DURATION, call.getDurationInSec());
        cv.put(MESSAGE, call.getMessage());
        System.out.println(cv.toString());
        return cv;
    }
}

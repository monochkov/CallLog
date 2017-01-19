package com.melkiy.calloger.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.melkiy.calloger.models.Call;

import org.joda.time.Instant;

import java.util.ArrayList;
import java.util.List;

public class CallDatabase extends SQLiteOpenHelper {

    public static final String QUERY_CREATE_DATABASE = "CREATE TABLE calls\n"+
            "    (id INTEGER PRIMARY KEY,\n"+
            "    name TEXT,\n"+
            "    number TEXT,\n"+
            "    type INTEGER,\n"+
            "    date TEXT,\n"+
            "    duration INTEGER,\n"+
            "    message TEXT);\n";

    public static final String DATABASE_NAME = "calloger.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "calls";

    public CallDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUERY_CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

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
        call.setType(Call.Type.fromValue(cursor.getInt(i++)));
        call.setDate(new Instant(cursor.getString(i++)));
        call.setDurationInSeconds(cursor.getInt(i++));
        call.setMessage(cursor.getString(i));
        System.out.println(call.toString());
        return call;
    }

    public static ContentValues toContentValues(Call call) {
        ContentValues cv = new ContentValues();
        cv.put(Columns.NAME, call.getName());
        cv.put(Columns.NUMBER, call.getNumber());
        cv.put(Columns.TYPE, call.getType().getValue());
        cv.put(Columns.DATE, String.valueOf(call.getDate()));
        cv.put(Columns.DURATION, call.getDurationInSeconds());
        cv.put(Columns.MESSAGE, call.getMessage());
        System.out.println(cv.toString());
        return cv;
    }
}

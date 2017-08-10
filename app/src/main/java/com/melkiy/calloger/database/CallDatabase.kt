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

package com.melkiy.calloger.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.melkiy.calloger.models.Call
import org.joda.time.Instant
import java.util.*

class CallDatabase private constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(QUERY_CREATE_DATABASE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    fun insert(call: Call?): Long {
        val db = writableDatabase
        val id = db.insert(TABLE_NAME, null, toContentValues(call))
        db.close()
        return id
    }

    val all: MutableList<Call>
        get() {
            val db = readableDatabase
            val list = ArrayList<Call>()

            val cursor = db.query(TABLE_NAME, null, null, null, null, null, "id DESC")
            while (cursor.moveToNext()) {
                list.add(fromCursor(cursor))
            }
            cursor.close()
            db.close()
            return list
        }

    companion object {

        private var instance : CallDatabase? = null

        fun getInstance(context: Context) : CallDatabase {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = CallDatabase(context)
                    }
                }
            }
            return instance!!
        }

        val QUERY_CREATE_DATABASE = "CREATE TABLE calls\n" +
                "    (id INTEGER PRIMARY KEY,\n" +
                "    name TEXT,\n" +
                "    number TEXT,\n" +
                "    type INTEGER,\n" +
                "    date TEXT,\n" +
                "    duration INTEGER,\n" +
                "    message TEXT);\n"

        val DATABASE_NAME = "calloger.db"
        val DATABASE_VERSION = 1
        val TABLE_NAME = "calls"

        fun fromCursor(cursor: Cursor): Call {
            var i = 1
            val call = Call()
            call.name = cursor.getString(i++)
            call.number = cursor.getString(i++)
            call.type = Call.Type.fromValue(cursor.getInt(i++))
            call.date = Instant(cursor.getString(i++))
            call.durationInSeconds = cursor.getInt(i++)
            call.message = cursor.getString(i)
            println(call.toString())
            return call
        }

        fun toContentValues(call: Call?): ContentValues {
            val cv = ContentValues()
            cv.put(Columns.NAME, call?.name)
            cv.put(Columns.NUMBER, call?.number)
            cv.put(Columns.TYPE, call?.type?.value)
            cv.put(Columns.DATE, call?.date.toString())
            cv.put(Columns.DURATION, call?.durationInSeconds)
            cv.put(Columns.MESSAGE, call?.message)
            println(cv.toString())
            return cv
        }
    }
}

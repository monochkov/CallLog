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

    override fun onCreate(db: SQLiteDatabase) = db.execSQL(QUERY_CREATE_DATABASE)

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun insert(call: Call): Long {
        val db = writableDatabase
        val id = db.insert(TABLE_NAME, null, call.toContentValues())
        db.close()
        return id
    }

    fun getAllCalls() : List<Call> {
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

        private var instance: CallDatabase? = null

        fun getInstance(context: Context): CallDatabase {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = CallDatabase(context)
                    }
                }
            }
            return instance!!
        }

        const val QUERY_CREATE_DATABASE = """CREATE TABLE calls\n
                    (id INTEGER PRIMARY KEY,\n
                    name TEXT,\n
                    number TEXT,\n
                    value INTEGER,\n
                    date TEXT,\n
                    duration INTEGER,\n
                    message TEXT);\n"""

        const val DATABASE_NAME = "calloger.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "calls"

        private fun fromCursor(cursor: Cursor): Call {
            var i = 1
            return Call(name = cursor.getString(i++),
                    number = cursor.getString(i++),
                    type = Call.Type.fromValue(cursor.getInt(i++)),
                    date = Instant(cursor.getString(i++)),
                    durationInSeconds = cursor.getInt(i++),
                    message = cursor.getString(i))
        }

        private fun Call.toContentValues() = ContentValues().apply {
            put(Columns.NAME, name)
            put(Columns.NUMBER, number)
            put(Columns.TYPE, type.value)
            put(Columns.DATE, date.toString())
            put(Columns.DURATION, durationInSeconds)
            put(Columns.MESSAGE, message)
        }
    }
}

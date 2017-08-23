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

package com.melkiy.calloger.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.CallLog
import android.support.v4.app.ActivityCompat
import android.widget.EditText
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick

import com.melkiy.calloger.R
import com.melkiy.calloger.database.CallDatabase
import com.melkiy.calloger.models.Call

import org.greenrobot.eventbus.EventBus
import org.joda.time.Instant

class PopupActivity : Activity() {

    @BindView(R.id.comment)
    lateinit var comment: EditText

    private lateinit var databaseHelper: CallDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup)
        ButterKnife.bind(this)

        databaseHelper = CallDatabase.getInstance(this.applicationContext)
    }

    private fun getLastCall(): Call? {
        if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            val cursor = applicationContext.contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER)
            val call = fromCursor(cursor)
            cursor.close()
            return call
        }
        return null
    }

    private fun fromCursor(cursor: Cursor): Call {
        return Call(name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)),
                number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)),
                type = Call.Type.fromValue(cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE))),
                date = Instant(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE))),
                durationInSeconds = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION)))
    }

    @OnClick(R.id.ok)
    fun onOkClick() {
        val message = comment.text.toString()
        if (message.isNotEmpty()) {
            val call = getLastCall()
            if (call != null) {
                val newCall = call.copy(message = message)
                databaseHelper.insert(newCall)
                EventBus.getDefault().post(newCall)
            }
        }
        finish()
    }

    @OnClick(R.id.cancel)
    fun onCancelClick() = finish()

    companion object {

        fun show(context: Context) {
            val intent = Intent(context, PopupActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
            context.startActivity(intent)
        }
    }
}

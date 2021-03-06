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

package com.melkiy.calloger.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.widget.EditText;

import com.melkiy.calloger.R;
import com.melkiy.calloger.database.CallDatabase;
import com.melkiy.calloger.models.Call;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.Instant;

public class PopupActivity extends Activity {

    private CallDatabase databaseHelper;

    public static void show(Context context) {
        context.startActivity(new Intent(context, PopupActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        databaseHelper = new CallDatabase(this);

        EditText comment = (EditText) findViewById(R.id.comment);

        findViewById(R.id.ok).setOnClickListener(v -> {
            String message = comment.getText().toString();
            if (!message.isEmpty()) {
                Call call = getLastCall();
                call.setMessage(message);
                databaseHelper.insert(call);
                EventBus.getDefault().post(call);
            }
            finish();
        });

        findViewById(R.id.cancel).setOnClickListener(v -> {
            finish();
        });
    }

    private Call getLastCall() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            Cursor cursor = getApplicationContext().getContentResolver().query(CallLog.Calls.CONTENT_URI,
                    null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
            Call call = fromCursor(cursor);
            cursor.close();
            return call;
        }
        return null;
    }

    private Call fromCursor(Cursor cursor) {
        if (cursor.moveToNext()) {
            Call call = new Call();
            call.setName(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
            call.setNumber(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
            call.setType(Call.Type.fromValue(cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE))));
            String callDate = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
            call.setDate(new Instant(Long.valueOf(callDate)));
            call.setDurationInSeconds(cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION)));
            return call;
        } else {
            return null;
        }
    }
}

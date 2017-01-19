package com.melkiy.calloger.activities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.widget.Button;
import android.widget.EditText;

import com.melkiy.calloger.R;
import com.melkiy.calloger.database.CallDatabaseHelper;
import com.melkiy.calloger.models.Call;

import org.joda.time.Instant;

public class PopupActivity extends Activity {

    private Button ok, cancel;
    private EditText comment;

    private CallDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        ok = (Button) findViewById(R.id.ok);
        cancel = (Button) findViewById(R.id.cancel);
        comment = (EditText) findViewById(R.id.comment);

        databaseHelper = new CallDatabaseHelper(this);

        ok.setOnClickListener(v -> {
            String message = comment.getText().toString();
            if (!message.isEmpty()) {
                Call call = getLastCall();
                call.setMessage(message);
                databaseHelper.insert(call);
                finish();
            }
        });

        cancel.setOnClickListener(v -> {
            finish();
        });
    }

    private Call getLastCall() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PopupActivity.this,
                    new String[]{Manifest.permission.READ_CALL_LOG},
                    1);
        }
        Cursor cursor = getApplicationContext().getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        Call call = fromCursor(cursor);
        cursor.close();
        return call;
    }

    private Call fromCursor(Cursor cursor) {
        Call call = new Call();
        while (cursor.moveToNext()) {
            call.setName(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
            call.setNumber(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
            call.setType(cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)));
            String callDate = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
            call.setDate(new Instant(Long.valueOf(callDate)));
            call.setDurationInSec(cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION)));
            break;
        }
        return call;
    }
}

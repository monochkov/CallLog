package com.melkiy.calloger.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.melkiy.calloger.R;
import com.melkiy.calloger.models.Call;
import com.melkiy.calloger.utils.StringUtils;

public class CallActivity extends AppCompatActivity {

    public static final String CALL_EXTRA = "CALL_EXTRA";

    private TextView name;
    private TextView number;
    private TextView type;
    private TextView date;
    private TextView duration;
    private TextView message;

    private Call call;

    public static void show(Context context, Call call) {
        Intent intent = new Intent(context, CallActivity.class);
        intent.putExtra(CALL_EXTRA, call);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = (TextView) findViewById(R.id.name);
        number = (TextView) findViewById(R.id.number);
        type = (TextView) findViewById(R.id.type);
        date = (TextView) findViewById(R.id.date);
        duration = (TextView) findViewById(R.id.duration);
        message = (TextView) findViewById(R.id.comment);

        call = (Call) getIntent().getSerializableExtra(CALL_EXTRA);
        initializeFields();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeFields() {
        if (StringUtils.isNullOrEmpty(call.getName())) {
            name.setText(R.string.label_name_unknown);
        } else {
            name.setText(getString(R.string.template_name, call.getName()));
        }
        number.setText(getString(R.string.template_number, call.getNumber()));
        switch (call.getType()) {
            case INCOMING:
                type.setText(R.string.label_type_incoming);
                break;
            case OUTGOING:
                type.setText(R.string.label_type_outgoing);
                break;
            case MISSED:
                type.setText(R.string.label_type_missed);
                break;
            case DISMISSED:
                type.setText(R.string.label_type_dismissed);
                break;
        }
        date.setText(getString(R.string.template_date, call.getDate()));
        duration.setText(getString(R.string.template_duration, call.getDurationInSeconds()));
        message.setText(getString(R.string.template_message, call.getMessage()));
    }
}

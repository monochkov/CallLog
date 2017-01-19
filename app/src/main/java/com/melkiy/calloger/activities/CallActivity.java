package com.melkiy.calloger.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.melkiy.calloger.R;
import com.melkiy.calloger.models.Call;

public class CallActivity extends AppCompatActivity {

    private TextView name, number, type, date, duration, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        name = (TextView) findViewById(R.id.name);
        number = (TextView) findViewById(R.id.number);
        type = (TextView) findViewById(R.id.type);
        date = (TextView) findViewById(R.id.date);
        duration = (TextView) findViewById(R.id.duration);
        message = (TextView) findViewById(R.id.comment);

        Call call = (Call) getIntent().getSerializableExtra("Call");
        initializeFields(call);
    }

    private void initializeFields(Call call) {
        if (call.getName() == null || call.getName().isEmpty()) {
            name.setText("Name: unknown");
        } else {
            name.setText("Name: " + call.getName());
        }
        number.setText("Number: " + call.getNumber());
        switch (call.getType()) {
            case 1:
                type.setText("Type: incoming");
                break;
            case 2:
                type.setText("Type: outgoing");
                break;
            case 3:
                type.setText("Type: missing");
                break;
            case 5:
                type.setText("Type: dismiss");
                break;
        }
        date.setText("Date: " + call.getDate());
        duration.setText("Duration: " + call.getDurationInSec() + " sec");
        message.setText("Message: " + call.getMessage());
    }
}

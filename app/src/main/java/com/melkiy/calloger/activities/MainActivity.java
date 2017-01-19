package com.melkiy.calloger.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.melkiy.calloger.R;
import com.melkiy.calloger.adapters.CallRecyclerViewAdapter;
import com.melkiy.calloger.database.CallDatabaseHelper;
import com.melkiy.calloger.models.Call;

import org.joda.time.Instant;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private CallRecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private CallDatabaseHelper databaseHelper;

    private List<Object> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new CallDatabaseHelper(this);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        recyclerView = (RecyclerView) findViewById(R.id.caller_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new CallRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(
                Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);

        getCallList();
    }

    @Override
    public void onRefresh() {
        getCallList();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void getCallList() {
        /*if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_CALL_LOG},
                    1);
        }
        List<Call> calls = new ArrayList<>();
        Cursor cursor = getApplicationContext().getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        while (cursor.moveToNext()) {
            Call call = new Call();
            call.setName(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
            call.setNumber(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
            call.setType(cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)));
            String callDate = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
            call.setDate(new Instant(Long.valueOf(callDate)));
            call.setDurationInSec(cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION)));
            calls.add(call);
        }*/
        List<Call> calls = new ArrayList<>(databaseHelper.getAll());
        createCallList(calls);
    }

    private void createCallList(List<Call> calls) {
        list.clear();
        if (!calls.isEmpty()) {
            if (calls.get(0).getDate().toDateTime().dayOfYear() != Instant.now().toDateTime().dayOfYear())
                list.add(calls.get(0).getDate());
            list.add(calls.get(0));
            for (int i = 1; i < calls.size(); i++) {
                if (calls.get(i).getDate().toDateTime().dayOfYear().get() !=
                        calls.get(i - 1).getDate().toDateTime().dayOfYear().get()) {
                    list.add(calls.get(i).getDate());
                }
                list.add(calls.get(i));
            }
            updateList(list);
        }
    }

    private void updateList(List<Object> callList) {
        adapter.setCalls(callList);
    }
}

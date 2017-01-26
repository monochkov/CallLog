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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.melkiy.calloger.R;
import com.melkiy.calloger.adapters.CallRecyclerViewAdapter;
import com.melkiy.calloger.database.CallDatabase;
import com.melkiy.calloger.models.Call;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener,
        CallRecyclerViewAdapter.OnCallClickListener {

    public static final int PERMISSIONS_REQUEST_READ_CALL_LOG = 1;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayout settingsLayout;
    private CallRecyclerViewAdapter adapter;

    private CallDatabase databaseHelper;

    private List<Call> calls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new CallDatabase(this);

        settingsLayout = (LinearLayout) findViewById(R.id.settings_layout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        recyclerView = (RecyclerView) findViewById(R.id.caller_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new CallRecyclerViewAdapter();
        adapter.setOnCallClickListener(this);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(
                Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);

        findViewById(R.id.open_settings_button).setOnClickListener(v -> {
            openSettingsActivity();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        checkPermissions();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh() {
        checkPermissions();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Call call) {
        calls.add(0, call);
        adapter.setCalls(calls);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CALL_LOG: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showRecyclerView();
                    loadCalls();
                } else {
                    showSettingsLayout();
                }
                break;
            }
        }
    }

    private void loadCalls() {
        calls = databaseHelper.getAll();
        adapter.setCalls(calls);
    }

    private void openSettingsActivity() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) !=
                PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_CALL_LOG},
                    PERMISSIONS_REQUEST_READ_CALL_LOG);
        } else {
            showRecyclerView();
            loadCalls();
        }
    }

    private void showRecyclerView() {
        settingsLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showSettingsLayout() {
        recyclerView.setVisibility(View.GONE);
        settingsLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCallClicked(Call call) {
        CallActivity.show(this, call);
    }
}

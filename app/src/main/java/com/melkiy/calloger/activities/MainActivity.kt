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
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.melkiy.calloger.R
import com.melkiy.calloger.adapters.CallRecyclerViewAdapter
import com.melkiy.calloger.database.CallDatabase
import com.melkiy.calloger.models.Call
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*

class MainActivity : AppCompatActivity(),
        SwipeRefreshLayout.OnRefreshListener, CallRecyclerViewAdapter.OnCallClickListener {

    @BindView(R.id.swipe_refresh)
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    @BindView(R.id.caller_recycler_view)
    lateinit var recyclerView: RecyclerView
    @BindView(R.id.settings_layout)
    lateinit var settingsLayout: LinearLayout
    @BindView(R.id.open_settings_button)
    lateinit var openSettingButton: Button

    private var adapter: CallRecyclerViewAdapter = CallRecyclerViewAdapter()
    private var databaseHelper: CallDatabase? = null
    private var calls: MutableList<Call> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        initRecyclerView()
        initSwipeRefreshLayout()
        setListeners()
        databaseHelper = CallDatabase.getInstance(this)
    }

    override fun onStart() {
        super.onStart()
        checkPermissions()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onRefresh() {
        checkPermissions()
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_CALL_LOG -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showRecyclerView()
                    loadCalls()
                } else {
                    showSettingsLayout()
                }
            }
        }
    }

    override fun onCallClicked(call: Call) {
        CallActivity.show(this, call)
    }

    @Subscribe
    fun onMessageEvent(call: Call) {
        calls.add(0, call)
        adapter.setCalls(calls)
    }

    /*
    Private methods
    */
    private fun initRecyclerView() {
        recyclerView.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
        }
        adapter.setOnCallClickListener(this)
    }

    private fun initSwipeRefreshLayout() {
        swipeRefreshLayout.let {
            it.setOnRefreshListener(this)
            it.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN)
        }
    }

    private fun setListeners() {
        openSettingButton.setOnClickListener { _ -> openSettingsActivity() }
    }

    private fun loadCalls() {
        calls = databaseHelper!!.all
        adapter.setCalls(calls)
    }

    private fun openSettingsActivity() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.READ_CALL_LOG),
                    PERMISSIONS_REQUEST_READ_CALL_LOG)
        } else {
            showRecyclerView()
            loadCalls()
        }
    }

    private fun showRecyclerView() {
        settingsLayout.visibility(false)
        recyclerView.visibility(true)
    }

    private fun showSettingsLayout() {
        recyclerView.visibility(false)
        settingsLayout.visibility(true)
    }

    /*
    Extensions methods
    */
    fun LinearLayout.visibility(isVisible: Boolean) {
        if (isVisible)
            this.visibility = View.VISIBLE
        else
            this.visibility = View.GONE
    }

    fun RecyclerView.visibility(isVisible: Boolean) {
        if (isVisible)
            this.visibility = View.VISIBLE
        else
            this.visibility = View.GONE
    }

    companion object {

        val PERMISSIONS_REQUEST_READ_CALL_LOG = 1
    }
}

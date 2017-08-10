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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.melkiy.calloger.R
import com.melkiy.calloger.models.Call

class CallActivity : AppCompatActivity() {

    @BindView(R.id.name)
    lateinit var name: TextView
    @BindView(R.id.number)
    lateinit var number: TextView
    @BindView(R.id.type)
    lateinit var type: TextView
    @BindView(R.id.date)
    lateinit var date: TextView
    @BindView(R.id.duration)
    lateinit var duration: TextView
    @BindView(R.id.comment)
    lateinit var message: TextView

    private var call: Call? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call)
        ButterKnife.bind(this)

        initToolbar()
        getExtras()
        initializeFields()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initToolbar() {
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun getExtras() {
        call = intent.getSerializableExtra(CALL_EXTRA) as Call
    }

    private fun initializeFields() {
        if (call?.name.isNullOrEmpty()) {
            name.text = getString(R.string.label_name_unknown)
        } else {
            name.text = getString(R.string.template_name, call?.name)
        }
        number.text = getString(R.string.template_number, call?.number)
        when (call?.type) {
            Call.Type.INCOMING -> type.text = getString(R.string.label_type_incoming)
            Call.Type.OUTGOING -> type.text = getString(R.string.label_type_outgoing)
            Call.Type.MISSED -> type.text = getString(R.string.label_type_missed)
            Call.Type.DISMISSED -> type.text = getString(R.string.label_type_dismissed)
        }
        date.text = getString(R.string.template_date, call?.date)
        duration.text = getString(R.string.template_duration, call?.durationInSeconds)
        message.text = getString(R.string.template_message, call?.message)
    }

    companion object {

        val CALL_EXTRA = "CALL_EXTRA"

        fun show(context: Context, call: Call) {
            val intent = Intent(context, CallActivity::class.java)
            intent.putExtra(CALL_EXTRA, call)
            context.startActivity(intent)
        }
    }
}

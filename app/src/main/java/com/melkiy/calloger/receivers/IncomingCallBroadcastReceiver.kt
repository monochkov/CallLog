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

package com.melkiy.calloger.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager

import com.melkiy.calloger.activities.PopupActivity

class IncomingCallBroadcastReceiver : BroadcastReceiver() {

    private var context: Context? = null
    private var telephonyManager: TelephonyManager? = null
    private var listener: StateListener = StateListener()

    override fun onReceive(context: Context, intent: Intent) {
        this.context = context
        telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager!!.listen(listener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    private inner class StateListener : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, incomingNumber: String) {
            when (state) {
                TelephonyManager.CALL_STATE_IDLE -> {
                    PopupActivity.show(context!!)
                }
            }
            telephonyManager!!.listen(listener, PhoneStateListener.LISTEN_NONE)
        }
    }
}

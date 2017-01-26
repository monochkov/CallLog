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

package com.melkiy.calloger.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.melkiy.calloger.activities.PopupActivity;

public class IncomingCallBroadcastReceiver extends BroadcastReceiver {

    private Context context;
    private TelephonyManager telephonyManager;
    private StateListener listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        listener = new StateListener();
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private class StateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE: {
                    PopupActivity.show(context);
                    break;
                }
            }
            telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);
        }
    }
}

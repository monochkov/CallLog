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

/****************************************************************************
 **
 ** GuapTime - Android homescreen widget
 ** Copyright (C) 2014 Yaroslav (aka KciRay).
 ** Contact: Yaroslav (kciray8@gmail.com)
 **
 ** This program is free software: you can redistribute it and/or modify
 ** it under the terms of the GNU General Public License as published by
 ** the Free Software Foundation, either version 3 of the License, or
 ** (at your option) any later version.
 **
 ** This program is distributed in the hope that it will be useful,
 ** but WITHOUT ANY WARRANTY; without even the implied warranty of
 ** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 ** GNU General Public License for more details.
 **
 ** You should have received a copy of the GNU General Public License
 ** along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **
 *****************************************************************************/

package com.kciray.guaptime;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class GuapTimeService extends Service {
    BroadcastReceiver receiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("GTime","onStartCommand " + startId);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                GuapTime.updateWidgets(context);
            }
        };

        registerReceiver(receiver, new IntentFilter(Intent.ACTION_TIME_TICK));
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_TIME_CHANGED));

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("GTime","onDestroy");
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}

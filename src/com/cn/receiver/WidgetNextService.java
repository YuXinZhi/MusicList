package com.cn.receiver;

import com.cn.sava.MusicNum;
import com.example.musiclist.MusicService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class WidgetNextService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		String	a="no";
		try{
	    a=intent.getStringExtra("play");
		}catch(NullPointerException e){
			e.printStackTrace();
			a="no";
		}
if(!a.equals("no")){
		MusicNum.putplay(5);
		MusicNum.putisok(true);
		Intent intent2=new Intent(WidgetNextService.this,MusicService.class);
		startService(intent2);}
		super.onStart(intent, startId);}
//	}

}
package com.cn.receiver;

import com.example.musiclist.Index;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
public class WidgetReceive extends BroadcastReceiver {
//以下打开Index接收者
	@Override
	public void onReceive(Context context, Intent intent) {
Intent intent2=new Intent(context,Index.class);
intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
context.startActivity(intent2);
	}
	

}
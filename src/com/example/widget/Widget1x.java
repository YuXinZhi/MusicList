package com.example.widget;

import com.cn.receiver.WidgetFrontService;
import com.cn.receiver.WidgetNextService;
import com.cn.receiver.WidgetPlayService;
import com.example.musiclist.MusicActivity;
import com.example.musiclist.MusicService;
import com.example.musiclist.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class Widget1x extends AppWidgetProvider {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		//-----------------���ð��¿�ʼ��ť�����Ĺ㲥Action-----------------------
				Intent startIntent = new Intent(context,WidgetPlayService.class);	
				startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startIntent.putExtra("play", "ok");
				PendingIntent startPendingIntent = PendingIntent.getService(context, 0, startIntent,PendingIntent.FLAG_UPDATE_CURRENT);	
				
				
				
				//-----------------���ð�����һ�װ�ť�����Ĺ㲥Action-----------------------
				Intent frontIntent = new Intent(context,WidgetFrontService.class);	
				frontIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				frontIntent.putExtra("play", "ok");
				PendingIntent frontPendingIntent = PendingIntent.getService(context, 0, frontIntent,PendingIntent.FLAG_UPDATE_CURRENT);
						
				//-----------------���ð�����һ�װ�ť�����Ĺ㲥Action-----------------------
				Intent nextIntent = new Intent(context,WidgetNextService.class);	
				nextIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				nextIntent.putExtra("play", "ok");
				PendingIntent nextPendingIntent = PendingIntent.getService(context, 0, nextIntent,PendingIntent.FLAG_UPDATE_CURRENT);
				
				
				//ϲ����ť
				Intent loveIntent = new Intent(context,MusicService.class);		
				loveIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
			    Intent stopIntent = new Intent(context,MusicActivity.class);	
				stopIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				stopIntent.putExtra("ooook",true);
				
				PendingIntent stopPendingIntent = PendingIntent.getActivity(context, 0, stopIntent,PendingIntent.FLAG_UPDATE_CURRENT);
				Intent stopIntent2 = new Intent("ggsdgvrghr");	
				PendingIntent stopPendingIntent2 = PendingIntent.getBroadcast(context, 0, stopIntent2,PendingIntent.FLAG_UPDATE_CURRENT);
			
				//�õ�RemoteViews����
				RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget1x);
				remoteViews.setOnClickPendingIntent(R.id.widget1xpre, frontPendingIntent );
				remoteViews.setOnClickPendingIntent(R.id.widget1xplay, startPendingIntent);
				remoteViews.setOnClickPendingIntent(R.id.widget1xnext, nextPendingIntent);
				
				remoteViews.setOnClickPendingIntent(R.id.widget1xlogo,stopPendingIntent);
				
				remoteViews.setOnClickPendingIntent(R.id.widget1xbg,stopPendingIntent2);
				
				AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
				ComponentName componentName = new ComponentName(context,appWidgetProvider.class);
				appWidgetManager.updateAppWidget(componentName, remoteViews);
				
				
			super.onReceive(context, intent);  
	}	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		//-----------------���ð��¿�ʼ��ť�����Ĺ㲥Action-----------------------
		Intent startIntent = new Intent(context,WidgetPlayService.class);	
		startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startIntent.putExtra("play", "ok");
		PendingIntent startPendingIntent = PendingIntent.getService(context, 0, startIntent,PendingIntent.FLAG_UPDATE_CURRENT);	
		
		
		
		//-----------------���ð�����һ�װ�ť�����Ĺ㲥Action-----------------------
		Intent frontIntent = new Intent(context,WidgetFrontService.class);	
		frontIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		frontIntent.putExtra("play", "ok");
		PendingIntent frontPendingIntent = PendingIntent.getService(context, 0, frontIntent,PendingIntent.FLAG_UPDATE_CURRENT);
				
		//-----------------���ð�����һ�װ�ť�����Ĺ㲥Action-----------------------
		Intent nextIntent = new Intent(context,WidgetNextService.class);	
		nextIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		nextIntent.putExtra("play", "ok");
		PendingIntent nextPendingIntent = PendingIntent.getService(context, 0, nextIntent,PendingIntent.FLAG_UPDATE_CURRENT);
		
		
		//ϲ����ť
		Intent loveIntent = new Intent(context,MusicService.class);		
		loveIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
	    Intent stopIntent = new Intent(context,MusicActivity.class);	
		stopIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		stopIntent.putExtra("ooook",true);
		
		PendingIntent stopPendingIntent = PendingIntent.getActivity(context, 0, stopIntent,PendingIntent.FLAG_UPDATE_CURRENT);
		Intent stopIntent2 = new Intent("ggsdgvrghr");	
		PendingIntent stopPendingIntent2 = PendingIntent.getBroadcast(context, 0, stopIntent2,PendingIntent.FLAG_UPDATE_CURRENT);
	
		
		//�õ�RemoteViews����
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget1x);
		remoteViews.setOnClickPendingIntent(R.id.widget1xpre, frontPendingIntent );
		remoteViews.setOnClickPendingIntent(R.id.widget1xplay, startPendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.widget1xnext, nextPendingIntent);
		
		remoteViews.setOnClickPendingIntent(R.id.widget1xlogo,stopPendingIntent);
		
		remoteViews.setOnClickPendingIntent(R.id.widget1xbg,stopPendingIntent2);
		//����widget
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);			
	}
	
	//================��ӵ�һ��widge������ʱ���ã��ڶ������ϲ��ٵ��ã�ֻ����һ��====================
	@Override
    public void onEnabled(Context context) {
			super.onEnabled(context);
		}
  		
	//================ɾ��widgetʱ�����==========================================================
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {

		super.onDeleted(context, appWidgetIds);
	}
		
	//================��ɾ�����һ��widgetʱ�Ż����===============================================
	@Override
	public void onDisabled(Context context) {
		
		super.onDisabled(context);
	}	
}
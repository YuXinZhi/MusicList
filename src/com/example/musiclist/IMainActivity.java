package com.example.musiclist;

import java.util.List;

import com.cn.sava.Indexviewpager;
import com.cn.sava.MusicNum;
import com.cn.ui.ScreenInfo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class IMainActivity extends Activity// implements OnTouchListener
{
	private ListView listView;
	private Close close;
	private ImageButton musiclist_play, musiclist_back;
    int lastX=0;
    int  lastY=0;
    ScreenInfo s;
//    RelativeLayout.LayoutParams layoutParams;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.setContentView(R.layout.iactivity_main);
	//	getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
	//			R.layout.title_bar);
		listView = (ListView) this.findViewById(R.id.musiclistevery);
		close = new Close();
		IntentFilter filter22 = new IntentFilter("com.sleep.close");
		this.registerReceiver(close, filter22);
		s=new ScreenInfo(IMainActivity.this);
		List<Music> listMusic = MusicList.getMusicData(getApplicationContext());
		
		
  //      int top = Index.viewPager.getTop() -200;  
   //     int bottom = Index.viewPager.getBottom() -200;    
   	
        
  //     Index.viewPager.layout(0, 885, s.getWidth(), 1630);  
       
       
       
	/*	musiclist_play = (ImageButton) this.findViewById(R.id.musiclist_play);
		musiclist_back = (ImageButton) this.findViewById(R.id.musiclist_back);
		musiclist_play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(IMainActivity.this,
						MusicActivity.class);
				startActivity(intent);

			}
		});
		musiclist_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();

			}
		});*/
	//	layoutParams=(RelativeLayout.LayoutParams)Index.viewPager.getLayoutParams();
		MusicAdapter adapter = new MusicAdapter(IMainActivity.this, listMusic);
		listView.setAdapter(adapter);
//		listView.setOnTouchListener(this);
		MusicList.getMusicData(this);
		listView.setSelection(Indexviewpager.getmainlistposition());
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent1 = new Intent(IMainActivity.this,
						MusicService.class);
				// intent1.putExtra("play",8);
				MusicNum.putplay(8);
				MusicNum.putisok(true);
				intent1.putExtra("_id", arg2);
				startService(intent1);

				Intent intent = new Intent(IMainActivity.this,
						MusicActivity.class);
				// intent.putExtra("id", arg2);
				startActivity(intent);
			}
		});
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				Indexviewpager.putmainlistposition(arg1);
			}
		});
	}

	@Override
	protected void onDestroy() {
		listView.setAdapter(null);
		this.unregisterReceiver(close);
		super.onDestroy();
	}

	public class Close extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();

		}
	}
/*
	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		int action=event.getAction();  
        switch(action){
        case MotionEvent.ACTION_DOWN:  
            lastY = (int) event.getRawY();  
            break;  

        case MotionEvent.ACTION_MOVE:  
             int dy =(int)event.getRawY() - lastY; 
             int top = Index.viewPager.getTop() + dy;  
             Log.i("top", top+"");
             if(top>885)
            	 top=885;
             int bottom = Index.viewPager.getBottom() + dy;  
             if(bottom>1630)
            	 bottom=1630;
             Log.i("bottom", bottom+"");
             
            Index.viewPager.layout(0, top, s.getWidth(), bottom);  
            lastX = (int) event.getRawX();  
            lastY = (int) event.getRawY();                    
            break;  
        case MotionEvent.ACTION_UP:
        	
        	
        	
            break;                
        }
		return false;
	}*/
}

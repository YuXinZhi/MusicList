package com.cn.lyric;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cn.sava.DebugMessage;
import com.example.musiclist.Music;
import com.example.musiclist.MusicList;
import com.example.musiclist.MusicService;
import com.example.musiclist.PreferenceService;
import com.example.musiclist.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class LyricActivity extends Activity {
	public LrcView lrc_view;
	public LrcProcess mLrcProcess;
	public LrcView mLrcView;
	public LinearLayout	nolrc;
	ListView erllist;
	TextView shoudonglrc;
	private MusicPlay6er receiver9;
	private MyProgressBroadCa receiver;
	PreferenceService service;
	String word = "";
	private List<Music> lists;
	String[] lrc=new String[200];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lyricview);
		lrc_view = (LrcView) findViewById(R.id.LyricShow);
//		erllist = (ListView) findViewById(R.id.erllist);
		shoudonglrc= (TextView) findViewById(R.id.shoudonglrc);
		nolrc = (LinearLayout) findViewById(R.id.nolrc);
		lists = MusicList.getMusicData(getApplicationContext());
		service=new PreferenceService(this);
		
		receiver9=new MusicPlay6er();
		IntentFilter filter9=new IntentFilter("com.cn.musicserviceplayer");
		this.registerReceiver(receiver9, filter9);
//		erllist.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getData()));
		
		receiver=new MyProgressBroadCa();
		IntentFilter filter=new IntentFilter("cn.com.karl.progress");
		this.registerReceiver(receiver, filter);
		
		   shoudonglrc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(LyricActivity.this,LrcSearch.class);
				startActivity(intent);
			}
		});
	}
	
	public class MyProgressBroadCa extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			lrc_view.SetIndex(LrcIndex.LrcIndex());
			lrc_view.invalidate();
		}
		}
	
    @Override
	protected void onStart() {
		Intent intent=new Intent(LyricActivity.this,MusicService.class);
		startService(intent);
		Map<String,String>paramslrcurl=service.getPreferenceslrcurl();
		mLrcProcess = new LrcProcess();
		if(lists.size()>MusicService._id){
			
			if(!paramslrcurl.get("lrcurledit").equals("")){
				mLrcProcess = new LrcProcess();
				mLrcProcess.readLRC(paramslrcurl.get("lrcurledit")+lists.get(MusicService._id).getName());
			}else{
			mLrcProcess = new LrcProcess();
			mLrcProcess.readLRC(lists.get(MusicService._id).getUrl());
			}
		}
		LrcIndex.lrcList = mLrcProcess.getLrcContent();
		if(LrcIndex.lrcList.size()<=0){
		   nolrc.setVisibility(View.VISIBLE);

		}else{
		nolrc.setVisibility(View.GONE);
		lrc_view.setSentenceEntities(LrcIndex.lrcList);		
		}
		super.onStart();
	}
	//prepareÍê³Éºó
	public class MusicPlay6er extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					Looper.prepare();
				//	getWords();
					Looper.loop();
				}
			}).start();
			
			
			Map<String,String>paramslrcurl=service.getPreferenceslrcurl();
			mLrcProcess = new LrcProcess();
			
			if(!paramslrcurl.get("lrcurledit").equals("")){
				try{
				mLrcProcess.readLRC(paramslrcurl.get("lrcurledit")+lists.get(MusicService._id).getName());
				}catch(Exception e){
					e.printStackTrace();
				}
				}else{
					try{
			mLrcProcess.readLRC(lists.get(MusicService._id).getUrl());
					}catch(Exception e){
						e.printStackTrace();
					}
			}
			LrcIndex.lrcList = mLrcProcess.getLrcContent();
			if(LrcIndex.lrcList.size()<=0){
				nolrc.setVisibility(View.VISIBLE);
				lrc_view.setSentenceEntities(LrcIndex.lrcList);
			}else{
				try{
			nolrc.setVisibility(View.GONE);
			lrc_view.setSentenceEntities(LrcIndex.lrcList);
				}catch(Exception e){
				e.printStackTrace();
			   }
			 }
			
		   }
		}

@Override
protected void onDestroy() {
	mLrcProcess=null;
	mLrcView=null;
	this.unregisterReceiver(receiver);
	this.unregisterReceiver(receiver9);
	super.onDestroy();
}
/*
private List<String> getData(){
    List<String> data = new ArrayList<String>();
    data.add("");
    return data;
}
*/
/*
@SuppressLint("ShowToast")
private void getWords() {
	Map<String,String>paramslrcurl=service.getPreferenceslrcurl();
	Music m=lists.get(MusicService._id);
	Strsinger =m.getSinger();
	Strmusic = m.getTitle();
	if(Strsinger == "" || Strmusic == ""){
		Toast.makeText(getApplicationContext(), "¿Õ", 1).show();
	}else{
	SearchLRC search = new SearchLRC(Strmusic, Strsinger);
	@SuppressWarnings("rawtypes")
	ArrayList result = search.fetchLyric();
    if(result == null){
    	Log.i("ads", "null");
    }else{
	word = "";
	if (result.size() > 0) {
		for (int i = 0; i < result.size()-2; i++) {
			word += result.get(i);
			word += "\n";
		}
		DebugMessage.put(paramslrcurl.get("lrcurledit"), word, m.getName().substring(m.getName().length()-4, m.getName().length()-3).equals(".")  ? m.getName().substring(0, m.getName().length()-4) : m.getName());
	}
	Message msg = mHandler.obtainMessage(0);
	mHandler.sendMessage(msg);
    }
	}
}
*/
/*
private Handler mHandler = new Handler() {
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case 0:
			refresh();
			break;
		}
	}
};
*/
/*
private void refresh() {
	tvword.setText("");
	tvword.setText(word);
	mLrcProcess = new LrcProcess();
	Map<String,String>paramslrcurl=service.getPreferenceslrcurl();
	if(!paramslrcurl.get("lrcurledit").equals("")){
		mLrcProcess.readLRC(paramslrcurl.get("lrcurledit")+lists.get(MusicService._id).getName());
	}else{
	mLrcProcess.readLRC(lists.get(MusicService._id).getUrl());
	}
	LrcIndex.lrcList = mLrcProcess.getLrcContent();
	if(LrcIndex.lrcList.size()<=0){
		nolrc.setVisibility(View.VISIBLE);
		lrc_view.setSentenceEntities(LrcIndex.lrcList);
	}else{
		try{
	nolrc.setVisibility(View.GONE);
	lrc_view.setSentenceEntities(LrcIndex.lrcList);
		}catch(Exception e){
		e.printStackTrace();
	   }
	 }
}*/

}

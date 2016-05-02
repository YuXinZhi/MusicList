package com.cn.lyric;

import java.util.List;

import com.example.musiclist.Music;
import com.example.musiclist.MusicList;
import com.example.musiclist.MusicService;
import com.example.musiclist.PreferenceService;
import com.example.musiclist.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class LrcSearch extends Activity {
LinearLayout noshow;
EditText lrcurledit;
Button exitlrc,enterlrc;
PreferenceService service;
private List<Music> lists;
boolean isview=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.lrcsearch);
		service=new PreferenceService(this);
		lists = MusicList.getMusicData(getApplicationContext());
		lrcurledit=(EditText)this.findViewById(R.id.lrcurledit);
		exitlrc	=(Button)this.findViewById(R.id.exitlrc);
		enterlrc=(Button)this.findViewById(R.id.enterlrc);
		exitlrc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	@Override
	protected void onStart() {
		Music m=lists.get(MusicService._id);
		String url = m.getUrl();
		for(int i=url.length();i>=0;i--){
		if(!url.substring(url.length()-1, url.length()).equals("/")){
			url=url.substring(0, url.length()-1);
		}
		}
		lrcurledit.setText(url);
		enterlrc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				service.savelrcurl(lrcurledit.getText().toString());	
				finish();
			}
		});
		super.onStart();
	}
}

package com.example.musiclist;

import java.util.ArrayList;
import java.util.HashMap;
import com.example.musiclist.R;
import com.example.musiclist.MusicActivity.Close;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Online extends Activity {
	private Close close;
	String url[] = {
			"http://mp3.easou.com/index.html?esid=VQS55LWDK9dqhQDjE3zeG_OQzMoMkozQ3&wver=t&fr=#rec%3A",
			"http://mp3.easou.com/index.html?esid=VQS55LWDK9dqhQDjE3zeG_OQzMoMkozQ3&wver=t&fr=#subject%3A%5B%22index%22%5D",
			"http://fm.baidu.com/?fr=ucyy" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.online);
	//	getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
	//			R.layout.title_bar);
	//	ImageButton equze_back = (ImageButton) this
	//			.findViewById(R.id.musiclist_back);
	//	ImageButton musiclist_play = (ImageButton) this
	//			.findViewById(R.id.musiclist_play);
	//	TextView titlename = (TextView) this.findViewById(R.id.titlename);
	//	titlename.setText("在线");

		close = new Close();
		IntentFilter filter22 = new IntentFilter("com.sleep.close");
		this.registerReceiver(close, filter22);

/*		musiclist_play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Online.this, MusicActivity.class);
				startActivity(intent);
			}
		});
		equze_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});*/
		ListView list = (ListView) findViewById(R.id.musiclistevery);

		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		String dianpuname[] = { "精品推荐", "热门榜单", "随心听" };
		for (int i = 0; i <= 2; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("online_name", dianpuname[i]);
			listItem.add(map);
		}
		SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,// 数据源
				R.layout.online_items,// ListItem的XML实现
				new String[] { "online_name" }, new int[] { R.id.online_name });
		list.setAdapter(listItemAdapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(Online.this, BaiduOnline.class);
				intent.putExtra("url", url[arg2]);
				startActivity(intent);
			}
		});
	}

	public class Close extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	}
}

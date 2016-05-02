package com.example.musiclist;
import java.util.ArrayList;
import java.util.HashMap;
import com.example.musiclist.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class List extends Activity {
	private Close close;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.list);
	//	getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
	//			R.layout.title_bar);
	//	ImageButton equze_back = (ImageButton) this
	//			.findViewById(R.id.musiclist_back);
	//	ImageButton musiclist_play = (ImageButton) this
	//			.findViewById(R.id.musiclist_play);
	//	TextView titlename = (TextView) this.findViewById(R.id.titlename);
	//	titlename.setText("�б�");
		close = new Close();
		IntentFilter filter22 = new IntentFilter("com.sleep.close");
		this.registerReceiver(close, filter22);
	/*	musiclist_play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(List.this, MusicActivity.class);
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
		String dianpuname[] = {"�����б�","�����б�", "ר���б�", "�ղ��б�", "�������", "���ֲ���" };
		String miaoshu[] = {"�оٳ����и���","�оٳ����и���", "�оٳ�����ר��", "���ղع��ĸ���", "������������Ź�������", "���ұ�������" };
		for (int i = 0; i <= 5; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("em_name", dianpuname[i]);
			map.put("em_time", miaoshu[i]);
			listItem.add(map);
		}
		SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,// ����Դ
				R.layout.list_items,// ListItem��XMLʵ��
				new String[] { "em_name", "em_time" }, new int[] {
						R.id.em_name, R.id.em_time });
		list.setAdapter(listItemAdapter);

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == 0) {
					Intent intent = new Intent(List.this, ListOfIMainActivity.class);
					startActivity(intent);
				}
				if (arg2 == 1) {
					Intent intent = new Intent(List.this, ListOfArtistsActivity.class);
					startActivity(intent);
				}
				if (arg2 == 2) {
					Intent intent = new Intent(List.this, AlbumsActivity.class);
					startActivity(intent);
				}
				if (arg2 == 3) {
					Intent intent = new Intent(List.this,
							RecentlyActivity.class);
					startActivity(intent);
				}
				if (arg2 == 4) {
					Intent intent = new Intent(List.this, NowActivity.class);
					startActivity(intent);
				}
				if (arg2 == 5) {
					Intent intent = new Intent(List.this, Search.class);
					startActivity(intent);
				}
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

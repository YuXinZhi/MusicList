package com.example.musiclist;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cn.lyric.LyricActivity;
import com.cn.lyric.NoActivity;
import com.cn.sava.Indexviewpager;
import com.cn.sava.MusicNum;
import com.example.love.ToTime;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityGroup;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint({ "DefaultLocale", "NewApi", "ShowToast" })
public class MusicActivity extends ActivityGroup {
	String KEY[] = { "remember4", "remember5", "remember9", "remember10",
			"remember12", "remember13", "nowplay", "prefsname" };
	private SharedPreferences mSettings = null;
	private TextView textName, textSinger, allmusic, currentmusic, textEndTime;
	static TextView textStartTime;
	private ImageButton imageBtnRewind;
	public static ImageButton music_equze;
	private ImageButton imageBtnForward;
	public static ImageButton imageBtnRandom;
	private ImageButton play_back;
	public ImageButton imageBtnPlay;
	ToTime totime;
	String time, musicna;
	static SeekBar seekBar1;
	private List<Music> lists;
	// SaveId id;
	SharedPreferences.Editor saveput;
	SharedPreferences saveget;
	boolean exist;
	private MyProgressBroadCastReceiver receiver;
	private MusicPlay6er receiver9;
	private Close close;
	int progress;
	static ImageView imagebg;
	ImageView page_icon;
	ImageView imageb;
	public ImageView love;
	static PreferenceService service;
	private Animation myAnimation, myAnimationx;
	private int page[] = { R.drawable.page_icon_left, R.drawable.page_icon_mid,
			R.drawable.page_icon_right };
	private ViewPager viewPager;
	private ArrayList<View> pageViews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.music);
		lists = MusicList.getMusicData(this);
		service = new PreferenceService(this);
		mSettings = getSharedPreferences(KEY[7], Context.MODE_PRIVATE);
		saveget = getSharedPreferences("shoucang", 0);
		saveput = getSharedPreferences("shoucang", 0).edit();
		totime = new ToTime();
		// �ҵ��ؼ�
		findviews();
		// ע��㲥3��
		register();
		// ��ť�������
		buttonclick();
		InItView();
		viewPager.setAdapter(new myPagerView());
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				setSelector(arg0);
			}

			@Override
			public void onPageScrolled(int paramInt, float paramFloat, int arg2) {
				Indexviewpager.putmusic(paramInt);
				if (paramInt == 0 && arg2 != 0) {
					if (paramFloat < 0.3) {
						paramFloat = 0.3f;
					}
					try {
						imagebg.setAlpha(paramFloat);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				if (paramInt == 1 && arg2 != 0) {
					if (paramFloat < 0) {
						paramFloat = 0f;
					}
					if (paramFloat > 0.7) {
						paramFloat = 0.7f;
					}
					try {
						imagebg.setAlpha(1 - paramFloat);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		seekBar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
				// �����϶�
				if (lists.size() > 0) {
					if (MusicService.player != null) {
						MusicService.player.seekTo(seekBar.getProgress()
								* MusicService.player.getDuration() / 1000);
					} else {
						Music m = lists.get(MusicService._id);
						service.save3(
								textStartTime.getText().toString(),
								Integer.valueOf((int) (seekBar1.getProgress()
										* m.getTime() / 1000)));
					}

					seekBar1.setProgress(seekBar.getProgress());
				} else {
					Toast.makeText(getApplicationContext(), "�б�Ϊ�գ���λʧ�ܣ�", 1)
							.show();
				}
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// ��ʼ�϶�
			}

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// �����϶�
				if (lists.size() > 0) {
					Music m = lists.get(MusicService._id);
					seekBar1.setProgress(seekBar.getProgress());
					textStartTime.setText(totime.toTime((int) (progress
							* m.getTime() / 1000)));
				} else {
					Log.i("ds", "�б�Ϊ�գ���λʧ�ܣ�");
				}
			}
		});
		run = true;
		handler.postDelayed(task, 10);
	}

	Bitmap bitmap;

	private void setbg() {
		SharedPreferences localSharedPreferences = getSharedPreferences(
				"music", 0);
		Uri uri = Uri.parse(localSharedPreferences.getString("background", ""));
		ContentResolver contentResolver = this.getContentResolver();
		bitmap = null;
		if (String.valueOf(uri).length() > 4) {
			try {
				bitmap = BitmapFactory.decodeStream(contentResolver
						.openInputStream(uri));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
				imagebg.setBackgroundResource(R.drawable.video_stub_large);
				Toast.makeText(getApplicationContext(), "��Ǹ���ڴ治������ʧ�ܣ�", 1)
						.show();
			}
		}
		if (localSharedPreferences.getString("background", "").length() < 4) {
			imagebg.setBackgroundResource(R.drawable.video_stub_large);
			imagebg.setVisibility(View.VISIBLE);
			imageb.setVisibility(View.INVISIBLE);

		} else {
			imagebg.setImageBitmap(bitmap);
			imagebg.setVisibility(View.VISIBLE);
			imagebg.startAnimation(myAnimation);
			imageb.startAnimation(myAnimationx);
			imageb.setVisibility(View.INVISIBLE);
		}
	}

	public void setSelector(int id) {
		for (int i = 0; i < page.length; i++) {
			if (id == i) {
				// ���õײ�ͼƬ
				page_icon.setImageResource(page[i]);
				viewPager.setCurrentItem(i);
			}
		}
	}

	void InItView() {
		pageViews = new ArrayList<View>();

		View view01 = getLocalActivityManager().startActivity("activity01",
				new Intent(this, MainActivity.class)).getDecorView();

		View view02 = getLocalActivityManager().startActivity("activity02",
				new Intent(this, NoActivity.class)).getDecorView();

		View view03 = getLocalActivityManager().startActivity("activity03",
				new Intent(this, LyricActivity.class)).getDecorView();
		pageViews.add(view01);
		pageViews.add(view02);
		pageViews.add(view03);
	}

	private void buttonclick() {
		imageBtnRewind.setOnClickListener(new MyListener());
		imageBtnPlay.setOnClickListener(new MyListener());
		imageBtnForward.setOnClickListener(new MyListener());
		imageBtnRandom.setOnClickListener(new MyListener());
		play_back.setOnClickListener(new MyListener());
		music_equze.setOnClickListener(new MyListener());
	}

	private void register() {
		receiver = new MyProgressBroadCastReceiver();
		IntentFilter filter = new IntentFilter("cn.com.karl.progress");
		this.registerReceiver(receiver, filter);

		receiver9 = new MusicPlay6er();
		IntentFilter filter9 = new IntentFilter("com.cn.musicserviceplayer");
		this.registerReceiver(receiver9, filter9);

		close = new Close();
		IntentFilter filter22 = new IntentFilter("com.sleep.close");
		this.registerReceiver(close, filter22);

	}

	private void findviews() {
		viewPager = (ViewPager) findViewById(R.id.pager);
		myAnimation = AnimationUtils.loadAnimation(this, 0x7f040001);
		myAnimationx = AnimationUtils.loadAnimation(this, 0x7f040000);
		textName = (TextView) this.findViewById(R.id.music_nameq);
		textSinger = (TextView) this.findViewById(R.id.music_singerq);
		textStartTime = (TextView) this.findViewById(R.id.music_start_time);
		textEndTime = (TextView) this.findViewById(R.id.music_end_time);
		currentmusic = (TextView) this.findViewById(R.id.currentmusic);
		allmusic = (TextView) this.findViewById(R.id.allmusic);
		seekBar1 = (SeekBar) this.findViewById(R.id.music_seekBar);
		imageBtnRewind = (ImageButton) this.findViewById(R.id.music_rewind);
		imageBtnPlay = (ImageButton) this.findViewById(R.id.music_play);
		music_equze = (ImageButton) this.findViewById(R.id.music_equze);
		play_back = (ImageButton) this.findViewById(R.id.play_back);
		imageBtnForward = (ImageButton) this.findViewById(R.id.music_foward);
		imageBtnRandom = (ImageButton) this.findViewById(R.id.music_random);
		love = (ImageView) this.findViewById(R.id.love);
		imagebg = (ImageView) this.findViewById(R.id.imageb);
		imageb = (ImageView) this.findViewById(R.id.imagebg);
		page_icon = (ImageView) this.findViewById(R.id.pageicon);
		allmusic.setText(String.valueOf(lists.size()));

	}

	@Override
	protected void onStart() {
		// �õ��ϴα���ĸ���
		Map<String, String> params = service.getPreferences();
		Map<String, String> params3 = service.getPreferences3();
		if (MusicService.player == null) {
			int ida = Integer.valueOf(params.get("currentId"));
			Log.i("ida", ida + "#");
			progress = Integer.valueOf(params3.get("progress"));
			String time = params3.get("time");
			try {
				Music m = lists.get(ida);

				seekBar1.setProgress((int) (progress * 1000 / m.getTime()));
				textStartTime.setText(time);

				if (lists.size() > 0) {
					currentmusic.setText(String.valueOf(ida + 1));
				} else {
					currentmusic.setText("0");
				}
				textName.setText(m.getTitle());
				if (m.getSinger().equals("δ֪������")) {
					textSinger.setText("music");
				} else {
					textSinger.setText(m.getSinger());
				}
				textEndTime.setText(totime.toTime((int) m.getTime()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (MusicService.nowplay) {
			Music m = lists.get(MusicService._id);
			textEndTime.setText(totime.toTime((int) m.getTime()));
			textName.setText(m.getTitle());
			if (m.getSinger().equals("δ֪������")) {
				textSinger.setText("music");
			} else {
				textSinger.setText(m.getSinger());
			}
			if (lists.size() > 0)
				currentmusic.setText(String.valueOf(MusicService._id + 1));
			else
				currentmusic.setText("0");

			imageBtnPlay.setBackgroundResource(R.drawable.pause1);
		} else {
			Music m = lists.get(MusicService._id);
			seekBar1.setProgress((int) (MusicService.player
					.getCurrentPosition() * 1000 / MusicService.player
					.getDuration()));
			textStartTime.setText(totime.toTime(MusicService.player
					.getCurrentPosition()));
			if (lists.size() > 0)
				currentmusic.setText(String.valueOf(MusicService._id));
			else
				currentmusic.setText("0");
			textName.setText(m.getTitle());
			if (m.getSinger().equals("δ֪������")) {
				textSinger.setText("music");
			} else {
				textSinger.setText(m.getSinger());
			}
			textEndTime.setText(totime.toTime((int) MusicService.player
					.getDuration()));
		}
		super.onStart();
	}

	@Override
	protected void onResume() {
		if (MusicNum.getbtn(8)) {
			getWindow()
					.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
		if (MusicNum.getbtn(20)) {
			count = 0;
			run = true;
			handler.postDelayed(task, 10);

			MusicNum.putusbtn(20, false);
		}

		setSelector(Indexviewpager.getputmusic());
		if (Indexviewpager.getputmusic() == 0
				|| Indexviewpager.getputmusic() == 2) {
			imagebg.setAlpha(0.3f);
			imageb.setAlpha(0.3f);
		}
		viewPager.setCurrentItem(Indexviewpager.getputmusic());

		exist = getexist(String.valueOf(MusicService._id));
		if (exist) {
			love.setImageResource(R.drawable.menu_add_to_favorite_n);
		} else {
			love.setImageResource(R.drawable.menu_add_to_favorite_d);
		}
		love.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (lists.size() > MusicService._id) {
					if (!exist) {
						addtosave(String.valueOf(MusicService._id));
						love.setImageResource(R.drawable.menu_add_to_favorite_n); // ���
						Toast.makeText(getApplicationContext(), "����ӵ��ղ��б�",
								Toast.LENGTH_SHORT).show();
						exist = true;
					} else if (exist) {
						try {
							delfromsave(String.valueOf(MusicService._id));
							love.setImageResource(R.drawable.menu_add_to_favorite_d); // �䰵
							Toast.makeText(getApplicationContext(), "�Ѵ��ղ��б��Ƴ�",
									Toast.LENGTH_SHORT).show();
							exist = false;
						} catch (Exception e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(), "��Ǹ��ɾ��ʧ�ܣ�",
									Toast.LENGTH_SHORT).show();
						}
					}
				} else {
					Toast.makeText(getApplicationContext(), "��Ǹ������ʧ�ܣ�",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		if (MusicNum.getbtn(11) && !MusicNum.getbtn(12)) {
			imageBtnRandom.setBackgroundResource(R.drawable.play_loop_specok);
		} else if (MusicNum.getbtn(12) && !MusicNum.getbtn(11)) {
			imageBtnRandom.setBackgroundResource(R.drawable.play_random_selok);
		} else if (!MusicNum.getbtn(12) && !MusicNum.getbtn(11)) {
			imageBtnRandom.setBackgroundResource(R.drawable.play_loop_selok);
		} else {
		}
		super.onResume();
	}

	private long count = 0;
	private boolean run = false;

	private Handler handler = new Handler();

	private Runnable task = new Runnable() {

		public void run() {
			if (run) {
				handler.postDelayed(this, 10);
				count++;
			}
			if (count > 0) {
				setbg();
				run = false;
			}
		}
	};

	@Override
	protected void onDestroy() {
		this.unregisterReceiver(receiver);
		this.unregisterReceiver(close);
		this.unregisterReceiver(receiver9);
		super.onDestroy();
	}

	public class Close extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	}

	// �����������¼������޸ģ�
	public class MyProgressBroadCastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (MusicService.player != null) {

				long position = MusicService.player.getCurrentPosition();
				long total = MusicService.player.getDuration();
				textEndTime.setText(String.valueOf(totime.toTime((int) total)));
				if (seekBar1.getProgress() < 1) {
					textEndTime.setText(String.valueOf(totime
							.toTime((int) total)));
				}
				if (position > total) {
					position = total;
				} else {
					textStartTime.setText(totime.toTime((int) position));
					seekBar1.setProgress((int) (position * 1000 / total));
					seekBar1.invalidate();
				}
			}
		}
	}

	// prepare��ɺ�
	public class MusicPlay6er extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Music m = lists.get(MusicService._id);
			textEndTime.setText(totime.toTime((int) m.getTime()));
			textName.setText(m.getTitle());
			if (m.getSinger().equals("δ֪������")) {
				textSinger.setText("music");
			} else {
				textSinger.setText(m.getSinger());
			}
			if (lists.size() > 0)
				currentmusic.setText(String.valueOf(MusicService._id + 1));
			else
				currentmusic.setText("0");
			imageBtnPlay.setBackgroundResource(R.drawable.pause1);
			exist = getexist(String.valueOf(MusicService._id));
			if (exist) {
				love.setImageResource(R.drawable.menu_add_to_favorite_n);
			} else {
				love.setImageResource(R.drawable.menu_add_to_favorite_d);
			}
		}
	}

	// ���ְ��������������޸ģ�
	private class MyListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Map<String, String> params4 = service.getPreferences4();
			if (v == imageBtnRewind) { // ��һ��
				MusicNum.putint(4);
				Intent intent = new Intent(MusicActivity.this,
						MusicService.class);
				MusicNum.putplay(7);
				MusicNum.putisok(true);
				startService(intent);
			} else if (v == imageBtnPlay) {
				// ���ڲ���
				if (MusicService.player != null && MusicService.nowplay) {
					imageBtnPlay.setBackgroundResource(R.drawable.play1);
				} else {
					if (lists.size() > 0)
						imageBtnPlay.setBackgroundResource(R.drawable.pause1);
				}
				Intent intent = new Intent(MusicActivity.this,
						MusicService.class);
				MusicNum.putplay(3);
				MusicNum.putisok(true);
				startService(intent);

			} else if (v == imageBtnForward) {
				MusicNum.putint(3);
				Intent intent = new Intent(MusicActivity.this,
						MusicService.class);
				MusicNum.putplay(5);
				MusicNum.putisok(true);
				startService(intent);
				// ��һ��
			} else if (v == imageBtnRandom) {
				// �������
				if (MusicNum.getbtn(12) == true && MusicNum.getbtn(11) == false) { // ���������ѭ��
					imageBtnRandom
							.setBackgroundResource(R.drawable.play_loop_selok); // xunhuan
					MusicNum.putusbtn(11, false);
					MusicNum.putusbtn(12, false);
					saveRemember13(false);
					saveRemember12(false);
				} else if (MusicNum.getbtn(11) == true
						&& MusicNum.getbtn(12) == false) { // ���˵��������
					imageBtnRandom
							.setBackgroundResource(R.drawable.play_random_selok);// suiji
					MusicNum.putusbtn(11, false);
					MusicNum.putusbtn(12, true);

					saveRemember13(true);
					saveRemember12(false);
				} else if (!MusicNum.getbtn(11) && !MusicNum.getbtn(12)) { // ����ѭ���䵥��
					imageBtnRandom
							.setBackgroundResource(R.drawable.play_loop_specok);// danqu
					MusicNum.putusbtn(11, true);
					MusicNum.putusbtn(12, false);
					saveRemember13(false);
					saveRemember12(true);
				}

			} else if (v == music_equze) {
				Intent intent = new Intent(MusicActivity.this, null);
				startActivity(intent);
			} else if (v == play_back) {
				finish();
			}
		}
	}

	// ��ס���ѭ�������޸ģ�
	private void saveRemember12(boolean remember12) {
		Editor editor = mSettings.edit();// ��ȡ�༭��
		editor.putBoolean(KEY[4], remember12);
		editor.commit();
	} // ��ȡ������û���

	// ��סѭ��������Ƿ���
	private void saveRemember13(boolean remember13) {
		Editor editor = mSettings.edit();// ��ȡ�༭��
		editor.putBoolean(KEY[5], remember13);
		editor.commit();
	} // ��ȡ������û���

	// ����ѭ�����������ͼ����ʾ�����أ����޸ģ�
	class myPagerView extends PagerAdapter {
		// ��ʾ��Ŀ
		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(pageViews.get(arg1));
			return pageViews.get(arg1);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// menu.add(0,1,0,"�˳�"); //���ѡ��
		// menu.add(0,2,0,"����"); //���ѡ��
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@SuppressLint("ShowToast")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.exit) {
			Intent intent = new Intent("com.sleep.close");
			sendBroadcast(intent);
			finish();
		}
		if (item.getItemId() == R.id.about) {
			Intent intent1 = new Intent(MusicActivity.this, About.class);
			startActivity(intent1);
		}
		if (item.getItemId() == R.id.deletecurrent) {
			if (MusicService.nowplay) {
				File f = new File(lists.get(MusicService._id).getUrl());

				if (f.exists()) {
					try {
						f.delete();
						if (MusicService.his > 0) {
							MusicService.his -= 1;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					MusicNum.putint(3);
					Intent intent = new Intent(MusicActivity.this,
							MusicService.class);
					MusicNum.putplay(5);
					MusicNum.putisok(true);
					startService(intent);
					Toast.makeText(getApplicationContext(), "ɾ���ɹ���", 1).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "��Ǹ,��ǰû�и����ڲ��ţ�", 1)
						.show();
			}
		}
		if (item.getItemId() == R.id.entersleep) {
			Intent intent1 = new Intent(MusicActivity.this, Sleep.class);
			startActivity(intent1);
		}
		if (item.getItemId() == R.id.menusearch) {
			Intent intent1 = new Intent(MusicActivity.this, Search.class);
			startActivity(intent1);
		}
		if (item.getItemId() == R.id.setting) {
			Intent intent1 = new Intent(MusicActivity.this, Setting.class);
			startActivity(intent1);
		}
		if (item.getItemId() == R.id.settingring) {
			if (MusicService.player == null) {
				Toast.makeText(getApplicationContext(), "��ǰ����Ϊ�գ�", 1).show();
			} else {
				try {
					Music m = lists.get(MusicService._id);
					String path = m.getUrl();
					setMyRingtone(path);
					Toast.makeText(getApplicationContext(), "���������ɹ���", 1)
							.show();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "��������ʧ�ܣ�", 1)
							.show();
				}
			}
		}
		return super.onOptionsItemSelected(item);
	}

	public void setMyRingtone(String path) {
		File file = new File(path);
		ContentValues values = new ContentValues();
		values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
		values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
		values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
		values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
		values.put(MediaStore.Audio.Media.IS_ALARM, false);
		values.put(MediaStore.Audio.Media.IS_MUSIC, false);
		Uri uri = MediaStore.Audio.Media.getContentUriForPath(file
				.getAbsolutePath());
		Uri newUri = this.getContentResolver().insert(uri, values);
		RingtoneManager.setActualDefaultRingtoneUri(this,
				RingtoneManager.TYPE_RINGTONE, newUri);
	}

	public boolean getexist(String id) {
		String all = saveget.getString("shoucang", "");
		String cuid = id;
		if (cuid.length() == 1) {
			cuid = cuid + "###";
		}
		if (cuid.length() == 2) {
			cuid = cuid + "##";
		}
		if (cuid.length() == 3) {
			cuid = cuid + "#";
		}
		if (all.contains(cuid)) {
			return true;
		} else {
			return false;
		}
	}

	public void addtosave(String id) {
		String all = saveget.getString("shoucang", "");
		String cuid = id;
		if (cuid.length() == 1) {
			cuid = cuid + "###";
		}
		if (cuid.length() == 2) {
			cuid = cuid + "##";
		}
		if (cuid.length() == 3) {
			cuid = cuid + "#";
		}
		all = all + cuid;
		saveput.putString("shoucang", all);
		saveput.commit();
	}

	public void delfromsave(String id) {
		String all = saveget.getString("shoucang", "");
		Log.i("shoucang", saveget.getString("shoucang", ""));
		String cuid = id;
		if (cuid.length() == 1) {
			cuid = cuid + "###";
		}
		if (cuid.length() == 2) {
			cuid = cuid + "##";
		}
		if (cuid.length() == 3) {
			cuid = cuid + "#";
		}
		all = all.replace(cuid, "");
		saveput.putString("shoucang", all);
		saveput.commit();
	}
}

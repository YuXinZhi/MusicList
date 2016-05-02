package com.example.musiclist;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.SoundPool;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.cn.lyric.LrcIndex;
import com.cn.lyric.LrcProcess;
import com.cn.receiver.NotiReceive;
import com.cn.sava.EquzeHelper;
import com.cn.sava.MusicNum;
import com.cn.ui.RoundCorner;
import com.example.love.ToTime;
import com.example.widget.Widget1x;
import com.example.widget.appWidgetProvider;

@SuppressLint({ "NewApi", "ShowToast" })
public class MusicService extends Service implements Runnable {
	public LrcProcess mLrcProcess;
	String KEY2[] = { "remember1", "remember2", "remember3", "remember4",
			"remember5", "remember6", "remember7", "remember8", "remember9",
			"remember10", "remember11", "remember12", "remember13", "prefsname" };
	long total;
	private SharedPreferences mSettings = null;
	public static MediaPlayer player;
	private List<Music> lists;
	public static int _id = 0;
	public static boolean nowplay = false;
	NotificationManager nm;
	ToTime toTime;
	int vol;
	private SoundPool sp;
	int j = 0, num, progress, seekvalue = 0;
	static SeekBar bar0, bar1, bar2, bar3, bar4;
	private SensorManager sensorManager;
	boolean a = false, isbutton08 = false, zhongli = false;
	private Close close;
	boolean ison = true, isbutton04, isbutton07, isbutton10, isbutton06,
			isbutton11, isRandom, isLoop, isbutton01, isbutton02, isbutton03,
			isbutton0t5, isbutton09;
	short maxEQLevel;
	private SeekBarBroadcastReceiver receiverr;
	private Secren onon;
	private Secren1 offoff;
	boolean existlrc = false;
	short minEQLevel;
	static Equalizer mEqualizer;
	boolean okzhongli = true;
	PreferenceService service;
	private int currentVolume;// 当前音量
	private AudioManager audioManager;// 音量管理者
	boolean playshang = false;
	boolean isbutton[] = { isbutton01, isbutton02, isbutton03, isbutton04,
			isbutton0t5, isbutton06, isbutton07, isbutton08, isbutton09,
			isbutton10, isbutton11, isLoop, isRandom };
	static boolean isbusy = false;
	long position;
	Notification notif;
	static int his = 0;
	static boolean isnoti = false;
	boolean isnotyplay = false;
	static int[] history = new int[300];
	static SeekBar bar[] = { bar0, bar1, bar2, bar3, bar4 };
	Bitmap bitmap = null;
	Handler handler = new Handler();
	int i = 0;
	boolean volup = false;
	boolean voldown = false;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private class Jingdu extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			int change = intent.getIntExtra("change", 0);
			int isbtn = intent.getIntExtra("isbtn", 0);
			for (int i = 0; i <= 10; i++) {
				final int n = i;
				if (n == change && isbtn == 1) {
					isbutton[change] = true;
				}
				if (n == change && isbtn == 2) {
					isbutton[change] = false;
				}
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onCreate() {
		toTime = new ToTime();
		lists = MusicList.getMusicData(getApplicationContext());
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);// 获得当前音量
		register();
		service = new PreferenceService(this);
		sp = new SoundPool(13, AudioManager.STREAM_SYSTEM, 5);
		num = sp.load(this, 0x7f060002, 1);
		mSettings = getSharedPreferences("prefsname", Context.MODE_PRIVATE);

		for (int i = 0; i <= 12; i++) {
			isbutton[i] = mSettings.getBoolean(KEY2[i], false);
			MusicNum.putusbtn(i, mSettings.getBoolean(KEY2[i], false));
		}
		new Thread(this).start();
		for (int i = 0; i < 5; i++) {
			final int n = i;
			bar[n] = new SeekBar(this);
			bar[n].setMax(2000);
			bar[n].setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				@Override
				public void onStopTrackingTouch(SeekBar arg0) {
				}

				@Override
				public void onStartTrackingTouch(SeekBar arg0) {
				}

				@Override
				public void onProgressChanged(SeekBar arg0, int arg1,
						boolean arg2) {
					try {
						mEqualizer.setBandLevel((short) (5 - n),
								(short) (arg1 + minEQLevel));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		Map<String, String> params00 = service.getPreferences00();
		Map<String, String> params11 = service.getPreferences01();
		Map<String, String> params22 = service.getPreferences02();
		Map<String, String> params33 = service.getPreferences03();
		Map<String, String> params44 = service.getPreferences04();
		
		EquzeHelper.setEe(0, Integer.valueOf(params00.get("progress0")));
		EquzeHelper.setEe(1, Integer.valueOf(params11.get("progress1")));
		EquzeHelper.setEe(2, Integer.valueOf(params22.get("progress2")));
		EquzeHelper.setEe(3, Integer.valueOf(params33.get("progress3")));
		EquzeHelper.setEe(4, Integer.valueOf(params44.get("progress4")));

		
		super.onCreate();
	}

	private void register() {
		TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telManager.listen(new MobliePhoneStateListener(),
				PhoneStateListener.LISTEN_CALL_STATE);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		if (sensorManager != null) {// 注册监听器
			sensorManager.registerListener(sensorEventListener,
					sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
					SensorManager.SENSOR_DELAY_NORMAL);
		}

		receiverr = new SeekBarBroadcastReceiver();
		IntentFilter filter = new IntentFilter("cn.com.karl.seekBar");
		this.registerReceiver(receiverr, filter);

		close = new Close();
		IntentFilter filter22 = new IntentFilter("com.sleep.close");
		this.registerReceiver(close, filter22);

		onon = new Secren();
		IntentFilter filtery6 = new IntentFilter(
				"android.intent.action.SCREEN_ON");
		this.registerReceiver(onon, filtery6);

		offoff = new Secren1();
		IntentFilter filtery67 = new IntentFilter(
				"android.intent.action.SCREEN_OFF");
		this.registerReceiver(offoff, filtery67);

		Jingdu receiver43 = new Jingdu();
		IntentFilter filter43 = new IntentFilter("cn.change.seekbar");
		this.registerReceiver(receiver43, filter43);

	}

	private SensorEventListener sensorEventListener = new SensorEventListener() {
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			if (okzhongli && nowplay && isbutton[5]) {
				Map<String, String> params2 = service.getPreferences2();
				seekvalue = (Integer.valueOf(params2.get("seekvalue")));
				float f1 = event.values[0];
				float f2 = event.values[1];
				float f3 = Math.abs(f1 - 0.0f);
				float f4 = Math.abs(f2 - 0.0f);
				int i = 22 - (seekvalue / 7);
				float f5 = 1.0F * i;
				float f6 = 0.45F * i;
				if ((f3 > f5) && (f4 > f6)) {
					if (isbutton[5] == true && nowplay && !isbutton[6]) {
						player.stop();
						sp.play(num, 1, 1, 0, 0, 1);
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						shakeok();
					} else if (isbutton[6] && ison && nowplay && isbutton[5]) {
						player.stop();
						sp.play(num, 1.0F, 1.0F, 0, 0, 1.0F);
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						shakeok();
					}
				}
			}
		}
	};

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		int play = 0;
		if (lists.size() > 0) {
			if (MusicNum.getisok()) {
				play = MusicNum.getplay();
				MusicNum.putisok(false);
			}
		} else {
			Toast.makeText(getApplicationContext(), "您的播放列表为空", 1).show();
		}
		if (play > 0) {
			if (play == 3) {// 按了播放按钮
				if (nowplay) { // 执行暂停
					zhongli = false;
					nowplay = false;
					a = true;

					if (null != player) {
						if (isbutton[10]) {
							nowplay = false;

							volumstop();

							showNotification();
						} else {
							player.pause();
							widgetstop();
							showNotification();
						}
					}
					if (nm != null && !NotiReceive.getnoti()) {
						nm.cancelAll();
					}
					try {
						Music m = lists.get(_id);
						String Name = m.getName();
						service.save(Name, Integer.valueOf(_id));
						service.save3(
								toTime.toTime(player.getCurrentPosition()),
								Integer.valueOf(player.getCurrentPosition()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (isnotyplay) {
						showNotification();
					}
					widgetstop();
					String time = toTime.toTime(player.getCurrentPosition());
					service.save3(time,
							Integer.valueOf(player.getCurrentPosition()));
				} else { // 执行播放
					if (player != null) { // 继续播放
						if (isbutton[10]) {
							volumstart();
						} else {
							player.start();
						}
						isnoti = false;
						nowplay = true;
						showNotification();
					} else if (player == null && isbutton[9]) { // 记住播放进度播放
						Map<String, String> params3 = service.getPreferences3();
						progress = Integer.valueOf(params3.get("progress"));
						Map<String, String> params = service.getPreferences();
						_id = (Integer.valueOf(params.get("currentId")));
						
						if (_id >= lists.size() - 1) {
							_id = (lists.size() - 1);
						}
						Log.i("progress", "上次的进度是：" + String.valueOf(progress));
						playMusic(_id);
						player.seekTo(progress);
					//	player.start();
						Intent intent2 = new Intent(MusicService.this,VolumService.class);
						startService(intent2);
						register();
					} else {
						if (_id >= lists.size() - 1) {
							_id = (lists.size() - 1);
						}
						playMusic(_id);
						Intent intent2 = new Intent(MusicService.this,
								VolumService.class);
						startService(intent2);
					}
					nowplay = true;
					zhongli = true;
					widgetplay();
					NotiReceive.putnoti(false);
				}
			} else if (play == 5) { // 下一首
				shakeok();
			} else if (play == 7) { // 上一首
				if (his > 0 && his <= 299) {
					_id = (history[his - 1]);

					playMusic(_id);
				} else {
					_id = (_id - 1);
					if (_id < 0) {
						_id = (lists.size() - 1);
					}
					playMusic(_id);
				}
				playshang = true;
			} else if (play == 8) { // 接收了listview
				_id = (intent.getIntExtra("_id", _id));
				playMusic(_id);
				nowplay = true;
			} else if (play == 14) {
			//	for(int i=0;i<5;i++){
			//		bar[4 - i].setProgress(e.getEe(i));	
			//	}
				int progress6 = intent.getIntExtra("progress", 0);
				int n = intent.getIntExtra("number", 0);
				bar[4 - n].setProgress(progress6);
			} else if (play == 24) {
				isnoti = true;
			} else {
				Log.i("212", "没有play");
			}
		}
	}

	private void volumstop() {
		if(!voldown){
			currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);// 获得当前音量
		}
		
		i = currentVolume;
		volup = false;
		voldown = true;
		handler.post(runnable);
	}

	private void volumstart() {

		i = 0;
		if(!volup){
			currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);// 获得当前音量
		}
		volup = true;
		voldown = false;
		handler.post(runnable);
	}

	protected void shakeok() {
		Map<String, String> params3 = service.getPreferences3();
		progress = Integer.valueOf(params3.get("progress"));
		Map<String, String> params = service.getPreferences();
		if (player == null) {
			_id = (Integer.valueOf(params.get("currentId")));
		}
		if (_id >= lists.size() - 1) {
			_id = (0);
		}
		if (_id < 0)
			_id = (lists.size() - 1);
		if (MusicNum.getbtn(12) == true) {
			_id = (Integer.valueOf((int) (Math.random() * (lists.size() - 1))));
			if (_id >= lists.size() - 1) {
				_id = (lists.size() - 1);
			} else if (_id <= 0) {
				_id = (0);
			}
			playMusic(_id);
		} else if (MusicNum.getbtn(11) == true) {
			playMusic(_id);
		} else {
			_id = (_id + 1);
			if (_id >= lists.size() - 1) {
				_id = (0);
			}
			if (_id <= 0) {
				_id = (lists.size() - 1);
			}
			playMusic(_id);
		}
		Music mz = lists.get(_id);
		String musicna = mz.getName();
		service.savename(musicna, Integer.valueOf(_id));
		okzhongli = false;
	}

	private void playMusic(int _id) {

		Map<String, String> paramslrcurl = service.getPreferenceslrcurl();
		mLrcProcess = new LrcProcess();
		if (!(paramslrcurl.get("lrcurledit").length()<4)) {
			try{
			mLrcProcess.readLRC(paramslrcurl.get("lrcurledit")+lists.get(_id).getName());
			}catch(Exception e){
				e.printStackTrace();
			}
		} else {
			mLrcProcess.readLRC(lists.get(_id).getUrl());
		}
		// 传回处理后的歌词文件
		LrcIndex.lrcList = mLrcProcess.getLrcContent();
		if (LrcIndex.lrcList.size() > 2) {
			existlrc = true;
			mHandler.post(mRunnable);
		} else {
			existlrc = false;
		}


		NotiReceive.putnoti(false);
		j = 0;
		okzhongli = false;
		zhongli = true;

		if (null != player) {
			player.release();
			player = null;
		}

		Music m = lists.get(_id);
		Intent intent = new Intent("com.cn.musicserviceplayer");
		sendBroadcast(intent);
		if (m.getTime() < 1000 || m.getTime() > 900000) {
			playMusic(_id + 1);
		}

		String url = m.getUrl();
		Uri myUri = Uri.parse(url);
		player = new MediaPlayer();
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			player.setDataSource(getApplicationContext(), myUri);
			player.prepare();
			player.setOnPreparedListener(new PrepareListener());
		} catch (IllegalArgumentException e) {

			e.printStackTrace();
		} catch (SecurityException e) {

			e.printStackTrace();
		} catch (IllegalStateException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		player.setOnCompletionListener(new OnCompletionListener());

		player.setOnErrorListener(new OnErrorListener());

	}

	private class OnCompletionListener implements
			android.media.MediaPlayer.OnCompletionListener {
		@Override
		public void onCompletion(MediaPlayer arg0) {
			shakeok();
		}
	}

	private final class OnErrorListener implements
			android.media.MediaPlayer.OnErrorListener {
		@Override
		public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
			if (null != player) {
				player.release();
				player = null;
			}
			player = new MediaPlayer();
			Music m = lists.get(_id);
			String url = m.getUrl();
			Uri myUri = Uri.parse(url);
			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
			try {
				player.setDataSource(getApplicationContext(), myUri);
				player.prepare();
				player.setOnPreparedListener(new PrepareListener());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;

		}

	}

	private final class PrepareListener implements OnPreparedListener {

		@Override
		public void onPrepared(MediaPlayer mp) {
			Log.i("OnPreparedListener", "OnPreparedListener");
			if (isbutton[10]) {
				volumstart();
			} else {
				player.start();
			}

			nowplay = true;
			try {
				total = player.getDuration();
			} catch (Exception e) {
				e.printStackTrace();
			}
			mEqualizer = new Equalizer(0, player.getAudioSessionId());
			mEqualizer.setEnabled(true);
			
	//		Map<String, String> params00 = service.getPreferences00();
	//		Map<String, String> params11 = service.getPreferences01();
	//		Map<String, String> params22 = service.getPreferences02();
	//		Map<String, String> params33 = service.getPreferences03();
	//		Map<String, String> params44 = service.getPreferences04();
			
			
			try {
				mEqualizer
						.setBandLevel((short) 5,(short) (EquzeHelper.getEe(0) + minEQLevel));
				mEqualizer
						.setBandLevel((short) 4,
								(short) (EquzeHelper.getEe(1) + minEQLevel));
				mEqualizer
						.setBandLevel((short) 3,
								(short) (EquzeHelper.getEe(2) + minEQLevel));
				mEqualizer
						.setBandLevel((short) 2,
								(short) (EquzeHelper.getEe(3) + minEQLevel));
				mEqualizer
						.setBandLevel((short) 1,
								(short) (EquzeHelper.getEe(4) + minEQLevel));
			} catch (Exception e) {
				e.printStackTrace();
			}

			widgetplay();

			if (!playshang) {
				if (his < 299) {
					his += 1;
					history[his] = _id;
				}
			} else {
				if (his >= 1) {
					his = his - 1;
				}
			}

			showNotification();
			Intent intent = new Intent("com.cn.musicserviceplayer");
			sendBroadcast(intent);
			playshang = false;
		}
	}

	@Override
	public void onDestroy() {
		if (player != null) {
			try {
				String time = toTime.toTime((int) player.getCurrentPosition());
				service.save3(time,
						Integer.valueOf((int) player.getCurrentPosition()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			Music mz = lists.get(_id);
			String musicna = mz.getName();
			service.savename(musicna, Integer.valueOf(_id));
		}
		sensorManager.unregisterListener(sensorEventListener);
		if (player != null) {
			player.release();
			player = null;
		}
		if (mEqualizer != null) {
			mEqualizer.release();
			mEqualizer = null;
		}
		this.unregisterReceiver(offoff);
		this.unregisterReceiver(onon);
		this.unregisterReceiver(receiverr);
		this.unregisterReceiver(close);
		player = null;
		nowplay = false;
		super.onDestroy();
	}

	// 电话监听
	private class MobliePhoneStateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE: // 空闲
				if (player != null && nowplay) {
					if (isbusy) {
						player.start();
						isbusy = false;
					}
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK: /* 接起电话时 */

			case TelephonyManager.CALL_STATE_RINGING: /* 电话进来时 */
				if (player != null && nowplay) {
					player.pause();
					isbusy = true;
				}
				break;
			default:
				break;
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void showNotification() {
		Music m = lists.get(_id);
		CharSequence from = m.getName();
		CharSequence message = m.getSinger();

		Intent nextIntent2 = new Intent();
		nextIntent2.setAction("liu.appwidget3.Not5ificat5ionnext"); // 为Intent对象设置Action
		PendingIntent nextPendingIntent = PendingIntent.getBroadcast(
				MusicService.this, 0, nextIntent2, 0);

		Intent StartIntent2 = new Intent();
		StartIntent2.setAction("liu.appwidget3.Not5ificat5ion"); // 为Intent对象设置Action
		PendingIntent nPendingIntent = PendingIntent.getBroadcast(
				MusicService.this, 0, StartIntent2, 0);

		Intent intent = new Intent();
		intent.setClass(this, MusicActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, 0);
		RemoteViews rv = new RemoteViews(getPackageName(),
				R.layout.notification);
		rv.setOnClickPendingIntent(R.id.notificationnext, nextPendingIntent);
		rv.setOnClickPendingIntent(R.id.notificationplay, nPendingIntent);
		rv.setTextViewText(R.id.noticationname, m.getTitle());
		rv.setTextViewText(R.id.noticationsinger, m.getSinger());
		if (nowplay) {
			rv.setImageViewResource(R.id.notificationplay,
					R.drawable.nitificapause);
		} else {
			rv.setImageViewResource(R.id.notificationplay,
					R.drawable.nitificaplay);
		}
		notif = new Notification(R.drawable.stat_notify_icon, m.getName(),
				System.currentTimeMillis());
		notif.flags = Notification.FLAG_ONGOING_EVENT;
		notif.setLatestEventInfo(this, from, message, contentIntent);
		notif.contentView = rv;
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.notify(0x7f090000, notif);
		isnotyplay = false;
	}

	// 进度条被手动改变
	private class SeekBarBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				int seekBarPosition = intent.getIntExtra("seekBarPosition", 0);
				player.seekTo(seekBarPosition * player.getDuration() / 1000);
				player.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class Secren extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			ison = true;
		}
	}

	// 屏幕黑了
	private class Secren1 extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			ison = false;
		}
	}

	@Override
	public void run() {
		while (1 > 0) {
			j++;
			if (j > 2) {
				okzhongli = true;
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (player != null && nowplay) {

				try {
					RemoteViews remoteViews = new RemoteViews(
							"com.example.musiclist", R.layout.widget_main);
					if (LrcIndex.lrcList.size() > 4) {
						if (LrcIndex.LrcIndex() > 2)
							remoteViews.setTextViewText(
									R.id.lyric0,
									LrcIndex.lrcList.get(
											LrcIndex.LrcIndex() - 3).getLrc());
						else
							remoteViews.setTextViewText(R.id.lyric0, "");
						if (LrcIndex.LrcIndex() > 1)
							remoteViews.setTextViewText(
									R.id.lyric1,
									LrcIndex.lrcList.get(
											LrcIndex.LrcIndex() - 2).getLrc());
						else
							remoteViews.setTextViewText(R.id.lyric1, "");
						if (LrcIndex.LrcIndex() > 0)
							remoteViews.setTextViewText(
									R.id.lyric2,
									LrcIndex.lrcList.get(
											LrcIndex.LrcIndex() - 1).getLrc());
						else
							remoteViews.setTextViewText(R.id.lyric2, "");
						remoteViews.setTextViewText(R.id.lyric33,
								LrcIndex.lrcList.get(LrcIndex.LrcIndex())
										.getLrc());
						remoteViews.setTextViewText(R.id.lyric3,
								LrcIndex.lrcList.get(LrcIndex.LrcIndex())
										.getLrc());
						if (LrcIndex.LrcIndex() + 1 < LrcIndex.lrcList.size()) {
							remoteViews.setTextViewText(
									R.id.lyric4,
									LrcIndex.lrcList.get(
											LrcIndex.LrcIndex() + 1).getLrc());
						} else {
							remoteViews.setTextViewText(R.id.lyric4, "");
						}
						if (LrcIndex.LrcIndex() + 2 < LrcIndex.lrcList.size()) {
							remoteViews.setTextViewText(
									R.id.lyric5,
									LrcIndex.lrcList.get(
											LrcIndex.LrcIndex() + 2).getLrc());
						} else {
							remoteViews.setTextViewText(R.id.lyric5, "");
						}
						if (LrcIndex.LrcIndex() + 3 < LrcIndex.lrcList.size() - 1) {
							remoteViews.setTextViewText(
									R.id.lyric6,
									LrcIndex.lrcList.get(
											LrcIndex.LrcIndex() + 3).getLrc());
						} else {
							remoteViews.setTextViewText(R.id.lyric6, "");
						}

					} else {
						remoteViews.setTextViewText(R.id.lyric0, "");
						remoteViews.setTextViewText(R.id.lyric33, "");
						remoteViews.setTextViewText(R.id.lyric1, "");
						remoteViews.setTextViewText(R.id.lyric2, "");
						remoteViews.setTextViewText(R.id.lyric3, "");
						remoteViews.setTextViewText(R.id.lyric4, "");
						remoteViews.setTextViewText(R.id.lyric5, "");
						remoteViews.setTextViewText(R.id.lyric6, "");
					}
					AppWidgetManager appWidgetManager = AppWidgetManager
							.getInstance(getBaseContext());
					ComponentName componentName = new ComponentName(
							getBaseContext(), appWidgetProvider.class);
					appWidgetManager
							.updateAppWidget(componentName, remoteViews);
				} catch (Exception e) {
					e.printStackTrace();
				}

				Intent intent = new Intent("cn.com.karl.progress");
				sendBroadcast(intent);
			}
		}
	}

	Handler mHandler = new Handler();
	// 歌词滚动线程
	Runnable mRunnable = new Runnable() {
		@Override
		public void run() {

			if (existlrc) {

				mHandler.postDelayed(mRunnable, 300);
			}
		}
	};

	public class Close extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			try {
				if (player != null) {
					String time = toTime.toTime(player.getCurrentPosition());
					service.save3(time,
							Integer.valueOf(player.getCurrentPosition()));
					widgetstop();
					player.release();
					player = null;
				}
				Music mz = lists.get(_id);
				String musicna = mz.getName();
				service.savename(musicna, Integer.valueOf(_id));

				String Name = mz.getName();
				service.save(Name, Integer.valueOf(_id));

			} catch (Exception e) {
				e.printStackTrace();
			}
			sensorManager.unregisterListener(sensorEventListener);

			if (mEqualizer != null) {
				mEqualizer.release();
				mEqualizer = null;
			}
			nowplay = false;
			if (nm != null) {
				nm.cancelAll();
			}
			// android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

	private void widgetplay() {
		
		
		RemoteViews remoteViews = new RemoteViews("com.example.musiclist",
				R.layout.widget_main);
		Music m = lists.get(_id);
		if(his<=1){
		Map<String, String> paramsui = service.getPreferencesback();
		Uri uri = Uri.parse(paramsui.get("background"));
		ContentResolver contentResolver = this.getContentResolver();
		if(String.valueOf(uri).length()>4){
		try {
			
			bitmap = BitmapFactory.decodeStream(contentResolver
					.openInputStream(uri));
			int h = bitmap.getHeight();
			remoteViews.setImageViewBitmap(R.id.widget_logo,
					RoundCorner.toRoundCorner(bitmap, h / 34));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		}
		}
		String sbr = m.getName().substring(0, m.getName().length() - 4);
		remoteViews.setTextViewText(R.id.widgettitlemain, sbr);
		if (m.getSinger().equals("未知艺术家")) {
			remoteViews.setTextViewText(R.id.widgetsinger, "");
		} else {
			remoteViews.setTextViewText(R.id.widgetsinger, m.getSinger());
		}
		remoteViews.setImageViewResource(R.id.widget_startBtn2,
				R.drawable.widget_pause);
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(getBaseContext());
		ComponentName componentName = new ComponentName(getBaseContext(),
				appWidgetProvider.class);
		appWidgetManager.updateAppWidget(componentName, remoteViews);

		RemoteViews remoteViews1 = new RemoteViews("com.example.musiclist",
				R.layout.widget1x);
		remoteViews1.setTextViewText(R.id.widget1xname, sbr);
		remoteViews1.setImageViewResource(R.id.widget1xplay,
				R.drawable.widget1xpause);
		AppWidgetManager appWidgetManager1 = AppWidgetManager
				.getInstance(getBaseContext());
		ComponentName componentName1 = new ComponentName(getBaseContext(),
				Widget1x.class);
		appWidgetManager1.updateAppWidget(componentName1, remoteViews1);

	}

	public void widgetstop() {
		// Drawable drawable =
		// getResources().getDrawable(R.drawable.equalizer_adjust_bgbg);
		// BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
		// Bitmap bitmap = bitmapDrawable.getBitmap();
		RemoteViews remoteViews = new RemoteViews("com.example.musiclist",
				R.layout.widget_main);
		remoteViews.setImageViewResource(R.id.widget_startBtn2,
				R.drawable.widget_play);
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(getBaseContext());
		ComponentName componentName = new ComponentName(getBaseContext(),
				appWidgetProvider.class);
		appWidgetManager.updateAppWidget(componentName, remoteViews);
		// android.os.Process.killProcess(android.os.Process.myPid());
		RemoteViews remoteViews1 = new RemoteViews("com.example.musiclist",
				R.layout.widget1x);
		remoteViews1.setImageViewResource(R.id.widget1xplay,
				R.drawable.widget1xplay);
		AppWidgetManager appWidgetManager1 = AppWidgetManager
				.getInstance(getBaseContext());
		ComponentName componentName1 = new ComponentName(getBaseContext(),
				Widget1x.class);
		appWidgetManager1.updateAppWidget(componentName1, remoteViews1);
	}

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (volup || voldown) {
				
				handler.postDelayed(runnable, 90);

				if (volup) {
					++i;
					Log.i("i", i + "");
					audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i,
							AudioManager.FLAG_ALLOW_RINGER_MODES);
					player.start();

					if (i >= currentVolume) {
						volup = false;
					
					}
				}

				if (voldown) {
					--i;
					Log.i("i", i + "");
					audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i,
							AudioManager.FLAG_ALLOW_RINGER_MODES);

					if (i <= 1) {
						player.pause();
						voldown = false;
						audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
								currentVolume,
								AudioManager.FLAG_ALLOW_RINGER_MODES);
					}
				}
			}
		}
	};
}

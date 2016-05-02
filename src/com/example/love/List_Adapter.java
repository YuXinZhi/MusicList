package com.example.love;

import com.example.musiclist.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class List_Adapter extends BaseAdapter {

	private Context myCon;
	private Cursor myCur;
	public List_Adapter(Context con, Cursor cur) {
		myCon = con;
		myCur = cur;
	}

	public int getCount() {
		return myCur.getCount();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		convertView = LayoutInflater.from(myCon).inflate(R.layout.music_item,null);
		myCur.moveToPosition(position);//根据position，Cursor的指针相应的移动
		

		
		//歌曲名称
		TextView tv_music = (TextView) convertView.findViewById(R.id.music_item_name);
		if (myCur.getString(0).length()>24){//限制24的长度
			try {
				String musicTitle = bSubstring(myCur.getString(0).trim(),24);
				tv_music.setText(musicTitle);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			tv_music.setText(myCur.getString(0).trim());
		}
		//歌手
		TextView tv_singer = (TextView) convertView.findViewById(R.id.music_item_singer);
		if (myCur.getString(2).equals("<unknown>")){
			tv_singer.setText("未知艺术家");
		}else{
			tv_singer.setText(myCur.getString(2));
		}
		//歌曲时间
		TextView tv_time = (TextView) convertView.findViewById(R.id.music_item_time);
		tv_time.setText(toTime(myCur.getInt(1)));
		
	
			
		
			
		
		return convertView;
	}
	
	public void setItemIcon(int position,int flag){		
	}

	/**
	 * 时间格式转换
	 * @param time
	 * @return
	 */
	public String toTime(int time) {

		time /= 1000;		
		
		int minute = time / 60;
		minute %= 60;
		int second = time % 60;
		@SuppressWarnings("unused")
		int hour = minute / 60;		
		
		return String.format("%02d:%02d", minute, second);
	}
	
	/**
	 * 字符串裁剪
	 * @param s
	 * @param length
	 * @return
	 * @throws Exception
	 */
	@SuppressLint("DefaultLocale")
	public static String bSubstring(String s, int length) throws Exception  
	{
	    byte[] bytes = s.getBytes("Unicode");  
	    int n = 0; // 表示当前的字节数  
	    int i = 2; // 要截取的字节数，从第3个字节开始  
	    for (; i < bytes.length && n < length; i++)  
	    {  
	        // 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节  
	        if (i % 2 == 1)  
	        {  
	            n++; // 在UCS2第二个字节时n加1  
	        }  
	        else 
	        {  
	            // 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节  
	            if (bytes[i] != 0)  
	            {  
	                n++;  
	            }  
	        }  
	    }  
	    // 如果i为奇数时，处理成偶数  
	    if (i % 2 == 1)
	    {
	        // 该UCS2字符是汉字时，去掉这个截一半的汉字  
	        if (bytes[i - 1] != 0)  
	            i = i - 1;  
	        // 该UCS2字符是字母或数字，则保留该字符  
	        else 
	            i = i + 1;  
	    }
	 
	    return new String(bytes, 0, i, "Unicode");  
	}  

}

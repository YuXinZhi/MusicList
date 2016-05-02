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
		myCur.moveToPosition(position);//����position��Cursor��ָ����Ӧ���ƶ�
		

		
		//��������
		TextView tv_music = (TextView) convertView.findViewById(R.id.music_item_name);
		if (myCur.getString(0).length()>24){//����24�ĳ���
			try {
				String musicTitle = bSubstring(myCur.getString(0).trim(),24);
				tv_music.setText(musicTitle);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			tv_music.setText(myCur.getString(0).trim());
		}
		//����
		TextView tv_singer = (TextView) convertView.findViewById(R.id.music_item_singer);
		if (myCur.getString(2).equals("<unknown>")){
			tv_singer.setText("δ֪������");
		}else{
			tv_singer.setText(myCur.getString(2));
		}
		//����ʱ��
		TextView tv_time = (TextView) convertView.findViewById(R.id.music_item_time);
		tv_time.setText(toTime(myCur.getInt(1)));
		
	
			
		
			
		
		return convertView;
	}
	
	public void setItemIcon(int position,int flag){		
	}

	/**
	 * ʱ���ʽת��
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
	 * �ַ����ü�
	 * @param s
	 * @param length
	 * @return
	 * @throws Exception
	 */
	@SuppressLint("DefaultLocale")
	public static String bSubstring(String s, int length) throws Exception  
	{
	    byte[] bytes = s.getBytes("Unicode");  
	    int n = 0; // ��ʾ��ǰ���ֽ���  
	    int i = 2; // Ҫ��ȡ���ֽ������ӵ�3���ֽڿ�ʼ  
	    for (; i < bytes.length && n < length; i++)  
	    {  
	        // ����λ�ã���3��5��7�ȣ�ΪUCS2�����������ֽڵĵڶ����ֽ�  
	        if (i % 2 == 1)  
	        {  
	            n++; // ��UCS2�ڶ����ֽ�ʱn��1  
	        }  
	        else 
	        {  
	            // ��UCS2����ĵ�һ���ֽڲ�����0ʱ����UCS2�ַ�Ϊ���֣�һ�������������ֽ�  
	            if (bytes[i] != 0)  
	            {  
	                n++;  
	            }  
	        }  
	    }  
	    // ���iΪ����ʱ�������ż��  
	    if (i % 2 == 1)
	    {
	        // ��UCS2�ַ��Ǻ���ʱ��ȥ�������һ��ĺ���  
	        if (bytes[i - 1] != 0)  
	            i = i - 1;  
	        // ��UCS2�ַ�����ĸ�����֣��������ַ�  
	        else 
	            i = i + 1;  
	    }
	 
	    return new String(bytes, 0, i, "Unicode");  
	}  

}

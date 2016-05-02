package com.cn.lyric;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LrcProcess {
	private List<LrcContent> LrcList;
	private LrcContent mLrcContent;
	public LrcProcess() {mLrcContent = new LrcContent();
		LrcList = new ArrayList<LrcContent>();
	}

	public String readLRC(String song_path) {
		
		StringBuilder stringBuilder = new StringBuilder();
		String name=song_path.replace(".mp3",".lrc");
		File f = new File(name);
		if(f.exists()){
		try {
			FileInputStream fis = new FileInputStream(f);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String s = "";
			while ((s = br.readLine()) != null) {
				s = s.replace("[", "");
				s = s.replace("]", "@");
				// ����"@"�ַ�
				String splitLrc_data[] = s.split("@");
				if (splitLrc_data.length > 1) {
					mLrcContent.setLrc(splitLrc_data[1]);
					// ������ȡ�ø���ʱ��
					int LrcTime = TimeStr(splitLrc_data[0]);
					mLrcContent.setLrc_time(LrcTime);
					// ��ӽ��б�����
					LrcList.add(mLrcContent);
					// ��������
					mLrcContent = new LrcContent();
				}
			}
			br.close();
			isr.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			stringBuilder.append("");
		} catch (IOException e) {
			e.printStackTrace();
			stringBuilder.append("");
		}
		}
		else if(!f.exists()){
			return null;
		}
		return stringBuilder.toString();
	}
	/*
	 * ��������ʱ�䴦����
	 */
	public int TimeStr(String timeStr) {
		timeStr = timeStr.replace(":", ".");
		timeStr = timeStr.replace(".", "@");
		String timeData[] = timeStr.split("@");
		// ������֡��벢ת��Ϊ����
		int currentTime = 0;
		try{
		int minute = Integer.parseInt(timeData[0]);
		int second = Integer.parseInt(timeData[1]);
		int millisecond = Integer.parseInt(timeData[2]);
		//������һ������һ�е�ʱ��ת��Ϊ������
		currentTime = (minute * 60 + second) * 1000 + millisecond * 10;
		}catch(Exception e){
			e.printStackTrace();
		}
		return currentTime;
	}
	public List<LrcContent> getLrcContent() {
		return LrcList;
	}
	/**
	 * ��ø�ʺ�ʱ�䲢���ص���
	 */
	public class LrcContent {
		private String Lrc;
		private int Lrc_time;
		
		public String getLrc() {
			return Lrc;
		}
		public void setLrc(String lrc) {
			Lrc = lrc;
		}
		public int getLrc_time() {
			return Lrc_time;
		}
		public void setLrc_time(int lrc_time) {
			Lrc_time = lrc_time;
		}
	}
}

package com.cn.lyric;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import android.util.Log;
public class SearchLRC {
	private URL url;
	StringBuffer sb = new StringBuffer();
	private boolean findNumber = false;
	/*
	 * 初期化，根据参数取得lrc的地址
	 */
	public SearchLRC(String musicName, String singerName) {
		// 将空格替换成+号
		//musicName = musicName.replace(' ', '+');
		//singerName = singerName.replace(' ', '+');
		//传进来的如果是汉字，那么就要进行编码转化
		try {
			musicName = URLEncoder.encode(musicName, "utf-8");
			singerName = URLEncoder.encode(singerName, "utf-8");
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}		
		String strUrl = "http://box.zhangmen.baidu.com/x?op=12&count=1&title=" + musicName + "$$"+ singerName +"$$$$";
		try {
			url = new URL(strUrl);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		BufferedReader br = null;
		String s;
		try {
			HttpURLConnection   httpConn   =   (HttpURLConnection)url.openConnection();
			httpConn.connect();
			InputStreamReader inReader = new InputStreamReader(httpConn.getInputStream());
			br = new BufferedReader(inReader);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			while ((s = br.readLine()) != null) {
				sb.append(s + "/r/n");				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				br.close();
			} catch (IOException e) {		
				e.printStackTrace();
			}
		}
	}
	/*
	 * 根据lrc的地址，读取lrc文件流
	 * 生成歌词的ArryList
	 * 每句歌词是一个String
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList fetchLyric() {
		int begin = 0, end = 0, number = 0;// number=0表示暂无歌词
		String strid = "";
		begin = sb.indexOf("<lrcid>");
		Log.d("test", "sb = " + sb);
		if (begin != -1) {
			end = sb.indexOf("</lrcid>", begin);
			strid = sb.substring(begin + 7, end);
			number = Integer.parseInt(strid);
		}
		String geciURL = "http://box.zhangmen.baidu.com/bdlrc/" + number / 100 + "/" + number + ".lrc";
		SetFindLRC(number);
		ArrayList gcContent =new ArrayList();
		String s = new String();
		try {
			url = new URL(geciURL);
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(url.openStream(), "GB2312"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (br == null) {
			System.out.print("stream is null");
		} else {
			try {
				while ((s = br.readLine()) != null) {
			//		s = s.replace("[", "");
			//		s = s.replace("]", "@");
					gcContent.add(s);
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return gcContent;
	}
	private void SetFindLRC(int number) {
		if(number == 0)
			findNumber = false;
		else 
			findNumber = true;
	}
	public boolean GetFindLRC(){
		return findNumber;
	}
}
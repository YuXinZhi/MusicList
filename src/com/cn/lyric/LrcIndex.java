package com.cn.lyric;

import java.util.ArrayList;
import java.util.List;

import com.cn.lyric.LrcProcess.LrcContent;
import com.example.musiclist.MusicService;

public class LrcIndex {
	public static List<LrcContent> lrcList = new ArrayList<LrcContent>();
	// 初始化歌词检索值
	private static int index = 0;
	// 初始化歌曲播放时间的变量
	private static int Current = 0;
	// 初始化歌曲总时间的变量
	private static int total = 0;

	public static int LrcIndex() {
		if (MusicService.player != null) {
			// 获得歌曲播放在哪的时间
			try {
				Current = MusicService.player.getCurrentPosition();
				total = MusicService.player.getDuration();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (Current < total) {

			for (int i = 0; i < lrcList.size(); i++) {
				if (i < lrcList.size() - 1) {
					if (Current < lrcList.get(i).getLrc_time() && i == 0) {
						index = i;
					}
					if (Current > lrcList.get(i).getLrc_time() && Current < lrcList.get(i + 1).getLrc_time()) {
						index = i;
					}
				}
				if (i == lrcList.size() - 1 && Current > lrcList.get(i).getLrc_time()) {
					index = i;
				}
			}
		}
		return index;
	}
}

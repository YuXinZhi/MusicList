package com.cn.lyric;

import java.util.ArrayList;
import java.util.List;

import com.cn.lyric.LrcProcess.LrcContent;
import com.example.musiclist.MusicService;

public class LrcIndex {
	public static List<LrcContent> lrcList = new ArrayList<LrcContent>();
	// ��ʼ����ʼ���ֵ
	private static int index = 0;
	// ��ʼ����������ʱ��ı���
	private static int Current = 0;
	// ��ʼ��������ʱ��ı���
	private static int total = 0;

	public static int LrcIndex() {
		if (MusicService.player != null) {
			// ��ø����������ĵ�ʱ��
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

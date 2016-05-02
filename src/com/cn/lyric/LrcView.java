package com.cn.lyric;

import java.util.ArrayList;
import java.util.List;

import com.cn.lyric.LrcProcess.LrcContent;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 自定义绘画歌词，产生滚动效果
 */
public class LrcView extends TextView {

	private float width;
	private float high;
	private Paint CurrentPaint;
	private Paint NotCurrentPaint;
	private float TextHigh = 48;
	private int Index = 0;
	private List<LrcContent> mSentenceEntities = new ArrayList<LrcContent>();

	public void setSentenceEntities(List<LrcContent> mSentenceEntities) {
		this.mSentenceEntities = mSentenceEntities;
	}

	public LrcView(Context context) {
		super(context);
		init();
	}

	public LrcView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public LrcView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		setFocusable(true);
		// 高亮部分
		CurrentPaint = new Paint();
		CurrentPaint.setAntiAlias(true);
		CurrentPaint.setTextAlign(Paint.Align.CENTER);
		// 非高亮部分
		NotCurrentPaint = new Paint();
		NotCurrentPaint.setAntiAlias(true);
		NotCurrentPaint.setTextAlign(Paint.Align.CENTER);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (canvas == null) {
			return;
		}
		
		CurrentPaint.setColor(Color.argb(210, 244, 244, 244));
		NotCurrentPaint.setColor(Color.argb(140, 188, 188, 188));

		CurrentPaint.setTextSize(35);
		CurrentPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
		CurrentPaint.setStyle(Paint.Style.FILL_AND_STROKE);

		NotCurrentPaint.setTextSize(33);
		NotCurrentPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
		
		try {
			setText("");
			canvas.drawText(mSentenceEntities.get(Index).getLrc(), width / 2,
					high / 2, CurrentPaint);
			float tempY = high / 2;
			// 画出本句之前的句子
		
				for (int i = Index - 1; i >= 0  ; i--) {
				// 向上推移
					tempY = tempY - TextHigh;
					if(Index-i==7){
					    NotCurrentPaint.setAlpha(20);}
					if(Index-i==6){
						NotCurrentPaint.setAlpha(40);}
					if(Index-i==5){
						NotCurrentPaint.setAlpha(70);}
					if(Index-i==4){
						NotCurrentPaint.setAlpha(90);}
					if(Index-i>=8){
						NotCurrentPaint.setAlpha(0);}
					canvas.drawText(mSentenceEntities.get(i).getLrc(), width / 2,
							tempY, NotCurrentPaint);
				}
			
			tempY = high / 2;
			// 画出本句之后的句子
			
				for (int i = Index + 1; i < mSentenceEntities.size(); i++) {
					tempY = tempY + TextHigh;
					if(i-Index==7){
					    NotCurrentPaint.setAlpha(20);}
					if(i-Index==6){
						NotCurrentPaint.setAlpha(40);}
					if(i-Index==5){
						NotCurrentPaint.setAlpha(70);}
					if(i-Index==4){
						NotCurrentPaint.setAlpha(90);}
					if(i-Index==3){
						NotCurrentPaint.setColor(Color.argb(140, 188, 188, 188));}
					if(i-Index==2){
						NotCurrentPaint.setColor(Color.argb(140, 188, 188, 188));}
					if(i-Index==1){
						NotCurrentPaint.setColor(Color.argb(140, 188, 188, 188));}
					if(i-Index>=8){
						NotCurrentPaint.setAlpha(0);}
				
					canvas.drawText(mSentenceEntities.get(i).getLrc(), width / 2,tempY, NotCurrentPaint);
				}
		} catch (Exception e) {
			setText("");
		}
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		this.width = w;
		this.high = h;
	}
	public void SetIndex(int index) {
		this.Index = index;
	}
	public int getIndex() {
		return Index;
	}
	public void setSentenceEntities(String string) {
	}
}

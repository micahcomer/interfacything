package com.example.interfacything.worldgrid;

import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.Display;
import android.view.WindowManager;

public class WorldGrid {

	Point screenSize;
	Point gridSize;	
	
	PointF globalOffset;
	PointF currentPanOffset;
	PointF gridOffset;	
	PointF beginPan;
	
	Paint paint;
	Context context;
	
	public WorldGrid(Context c){
		context = c;
		paint = new Paint();
		setScreenSizeInfo();
		initOffset();
	}
	
	private void setScreenSizeInfo(){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();		
		Point size = new Point();
		display.getSize(size);
		screenSize = size;
		
		//gridSize hard codes the size of the grid in dp
		gridSize = new Point(100, 100);
		
		
	}
		
	private void initOffset(){
		globalOffset = new PointF(0,0);
		gridOffset = new PointF(0,0);
		currentPanOffset = new PointF(0,0);
		beginPan = new PointF();
	}
	
	public void draw(Canvas c){
		
		paint.setColor(Color.YELLOW);
		
		//draw vertical lines
		for (int i=0; i<screenSize.x; i+=gridSize.x){
			c.drawLine(i+gridOffset.x, 0, i+gridOffset.x, screenSize.y, paint);
		}
		
		for (int i=0; i<screenSize.y; i+=gridSize.y){
			c.drawLine(0, i+gridOffset.y, screenSize.x, i+gridOffset.y, paint);
		}
	}
	
	public void updateOffset(float x, float y){
		
		currentPanOffset.x = x-beginPan.x;
		currentPanOffset.y = y-beginPan.y;
				
		gridOffset.x = ((globalOffset.x+currentPanOffset.x)%gridSize.x);
		gridOffset.y = ((globalOffset.y+currentPanOffset.y)%gridSize.y);
		
	}
	
	public void beginPan(float x, float y){
		beginPan.x = x;
		beginPan.y = y;
	}
	
	public void pan(float x, float y){
		updateOffset(x, y);
	}
	
	public void endPan(float x, float y){
		globalOffset.x += currentPanOffset.x;
		globalOffset.y += currentPanOffset.y;
		currentPanOffset.x = 0;
		currentPanOffset.y = 0;
		beginPan.x =0;
		beginPan.y=0;
	}
	
}

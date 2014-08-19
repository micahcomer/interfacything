package com.example.interfacything;

import com.example.interfacything.engine.PaintThread;
import com.example.interfacything.worldgrid.WorldGrid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.ScaleGestureDetector;


public class EditorWindow extends SurfaceView implements Callback{

	Context context;
	PaintThread thread;
	WorldGrid grid;
	private boolean panning = false;
	
	
	public EditorWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		InitView();
		
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent ev) {
        
		int action = MotionEventCompat.getActionMasked(ev);
		
		if (action==MotionEvent.ACTION_POINTER_DOWN){
			panning = true;
			grid.beginPan(ev.getX(0), ev.getY(0));
		}
		
		if (action == MotionEvent.ACTION_POINTER_UP){
			panning = false;
			grid.endPan(ev.getX(0), ev.getY(0));
		}
		
		if ((action == MotionEvent.ACTION_MOVE) && (panning == true)){
			grid.pan(ev.getX(0), ev.getY(0));
		}
		
        return true;
	}
	
	
	//initialization code
	public void InitView(){
			
			//initialize our screen holder
			SurfaceHolder holder = getHolder();
			holder.addCallback( this);	

			//initialize our Thread class. A call will be made to start it later
			thread = new PaintThread(holder, context, new Handler(), this);
			setFocusable(true);  
			
			grid = new WorldGrid(context);
			
		}
	
	

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if(thread.state==PaintThread.PAUSED){
			//When game is opened again in the Android OS
			thread = new PaintThread(getHolder(), context, new Handler(), this);			
			thread.start();
		}else{
			//creating the game Thread for the first time
			thread.start();
		}
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		//code to end gameloop
		thread.state=PaintThread.PAUSED;
		while (retry) {
			try {
				//code to kill Thread
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
		
	}
	
	public void Update(long delta){
		
	}
	
	public void Draw(Canvas c){
		grid.draw(c);		
		
	}
	
	



}

package com.example.interfacything;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

import com.example.interfacything.engine.PaintThread;

public class EditorWindow extends SurfaceView implements Callback, OnTouchListener {
  
  Context     context;
  Point       screenSize;
  Point       gridSize;
  PaintThread thread;
  Paint       paint;
  
  public EditorWindow(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
    InitView();
  }
  
  // initialization code
  public void InitView() {
    
    // initialize our screen holder
    SurfaceHolder holder = getHolder();
    holder.addCallback(this);
    
    // initialize our Thread class. A call will be made to start it later
    thread = new PaintThread(holder, context, new Handler(), this);
    setFocusable(true);
    
    setScreenSizeInfo();
    paint = new Paint();
  }
  
  private void setScreenSizeInfo() {
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    Point size = new Point();
    setDisplaySize(display, size);
    screenSize = size;
    
    // gridSize hard codes the size of the grid in dp
    gridSize = new Point(100, 100);
  }
  
  @SuppressLint("NewApi")
  private void setDisplaySize(Display display, Point p) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
      display.getSize(p);
    }
    else {
      // TODO: what if we aren't running HoneyComb? Do we need backwards
      // compatibility?
    }
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
  
  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    if (thread.state == PaintThread.PAUSED) {
      // When game is opened again in the Android OS
      thread = new PaintThread(getHolder(), context, new Handler(), this);
      thread.start();
    }
    else {
      // creating the game Thread for the first time
      thread.start();
    }
  }
  
  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    boolean retry = true;
    // code to end gameloop
    thread.state = PaintThread.PAUSED;
    while (retry) {
      try {
        // code to kill Thread
        thread.join();
        retry = false;
      }
      catch (InterruptedException e) {}
    }
  }
  
  public void Update(long delta) {}
  
  public void Draw(Canvas c) {
    drawGrid(c);
  }
  
  private void drawGrid(Canvas c) {
    
    paint.setColor(Color.YELLOW);
    
    // draw vertical lines
    for (int i = 0; i < screenSize.x; i += gridSize.x) {
      c.drawLine(i, 0, i, screenSize.y, paint);
    }
    
    for (int i = 0; i < screenSize.y; i += gridSize.y) {
      c.drawLine(0, i, screenSize.x, i, paint);
    }
  }
  
  @Override
  public boolean onTouch(View v, MotionEvent event) {
    
    return false;
  }

}

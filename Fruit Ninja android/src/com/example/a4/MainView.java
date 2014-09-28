/**
 * CS349 Winter 2014
 */
package com.example.a4;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.TimerTask;
import java.util.Timer;
import android.os.Handler;
import java.util.*;


/*
 * View of the main game area.
 * Displays pieces of fruit, and allows players to slice them.
 */
public class MainView extends View implements Observer {
    private final Model model;
    private final Model model2;
    private final MouseDrag drag = new MouseDrag();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Timer atimer = new Timer();
    private final Handler myHandler = new Handler();
    private int counter=0;
    private int missed=0;
    final Runnable myRunnable = new Runnable() {
        synchronized public void run() {
            for (Fruit s : model.getShapes()) {
                s.move();
                float[] temp=new float[9];
                s.getTransform().getValues(temp);
                if(temp[5]>MainActivity.displaySize.y)
                {
                    model.remove(s);
                    missed++;
                }
                if(missed==5)
                {
                    atimer.cancel();
                    MainActivity.gameover.bringToFront();
                    MainActivity.gameover.setVisibility(View.VISIBLE);
                    for (Fruit a : model.getShapes()) {
                        model.remove(a);

                    }
                    for (Fruit b : model2.getShapes()) {
                        model.remove(b);

                    }
                    MainActivity.startB.bringToFront();
                }
                //System.out.println("missed value is "+missed);
            }
            for (Fruit i : model2.getShapes()) {
                i.translate((float)(i.expandNumber*0.7),(float)(i.droppingNumber*1.5));
                float[] temp=new float[9];
                i.getTransform().getValues(temp);
                if(temp[5]>MainActivity.displaySize.y)
                    model2.remove(i);
            }
            invalidate();
            if(counter%20==0)
            {
            Fruit f1 = new Fruit(new float[] {0, 20, 20, 0, 40, 0, 60, 20, 60, 40, 40, 60, 20, 60, 0, 40});
                f1.scale((float)0.7,(float)0.7);
            f1.translate((float)(MainActivity.displaySize.x*0.5), (float)MainActivity.displaySize.y);
           // System.out.println(MainActivity.displaySize.toString());
            model.add(f1);
            }
            counter++;
        }
    };

    // Constructor
    MainView(Context context, Model m, Model m2) {
        super(context);

        // register this view with the model
        model = m;
        model2= m2;
        model.addObserver(this);
        model2.addObserver(this);

        // TODO BEGIN CS349



        // test fruit, take this out before handing in
       atimer.scheduleAtFixedRate(new TimerTask() {
           @Override
           public void run() {UpdateGUI();} },0,70);

        // TODO END CS349

        // add controller
        // capture touch movement, and determine if we intersect a shape
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Log.d(getResources().getString(R.string.app_name), "Touch down");
                        drag.start(event.getX(), event.getY());
                        break;

                    case MotionEvent.ACTION_UP:
                        // Log.d(getResources().getString(R.string.app_name), "Touch release");
                        drag.stop(event.getX(), event.getY());

                        // find intersected shapes
                        Iterator<Fruit> i = model.getShapes().iterator();
                        while(i.hasNext()) {
                            Fruit s = i.next();
                            if (s.intersects(drag.getStart(), drag.getEnd())) {
                                Log.d("MainView","splited");
                            	s.setFillColor(Color.RED);
                                try {
                                    Fruit[] newFruits = s.split(drag.getStart(), drag.getEnd());

                                    // TODO BEGIN CS349
                                    // you may want to place the fruit more carefully than this
                                   // newFruits[0].translate(-30, -30);
                                    //newFruits[1].translate(+30, +30);
                                    // TODO END CS349
                                    model2.add(newFruits[0]);
                                    model2.add(newFruits[1]);

                                    // TODO BEGIN CS349
                                    // delete original fruit from model
                                    // TODO END CS349
                                    model.remove(s);
                                } catch (Exception ex) {
                                    Log.e("fruit_ninja", "Error: " + ex.getMessage());
                                }
                            } else {
                                s.setFillColor(Color.BLUE);
                            }
                            invalidate();
                        }
                        break;
                }
                return true;
            }
        });
    }
    private void UpdateGUI() {
        myHandler.post(myRunnable);
    }


    // inner class to track mouse drag
    // a better solution *might* be to dynamically track touch movement
    // in the controller above
    class MouseDrag {
        private float startx, starty;
        private float endx, endy;

        protected PointF getStart() { return new PointF(startx, starty); }
        protected PointF getEnd() { return new PointF(endx, endy); }

        protected void start(float x, float y) {
            this.startx = x;
            this.starty = y;
        }

        protected void stop(float x, float y) {
            this.endx = x;
            this.endy = y;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw background
        setBackgroundColor(Color.WHITE);

      /*  if(missed==5)
        {
            paint.setColor(Color.RED);
            paint.setTextSize(40);
            canvas.drawText("Game Over",(float)(MainActivity.displaySize.x*0.35),(float)(MainActivity.displaySize.y*0.3),paint);
        }*/

        // draw all pieces of fruit
        for (Fruit s : model.getShapes()) {
            s.move();
            s.draw(canvas);
        }
        for (Fruit i : model2.getShapes()) {
            i.translate((float)(i.expandNumber*0.7),(float)(i.droppingNumber*1.5));
            i.draw(canvas);
        }

    }

    @Override
    public void update(Observable observable, Object data) {
        invalidate();
    }
}

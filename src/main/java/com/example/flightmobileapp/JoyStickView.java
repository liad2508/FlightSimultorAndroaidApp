package com.example.flightmobileapp;



import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

public class JoyStickView extends View implements ObservableI {

    private float x;
    private float y;
    private float radius;
    private float startWidth;
    private float endWidth;
    private float startHeight;
    private float endHeight;
    private RectF oval;
    private Boolean playMoving = false;
    private List<ObserverI> obs = new LinkedList<>();




    public JoyStickView(Context context) {
        super(context);
        this.radius = 70;
    }

    /**
     * draw oval and circle according to x,y,radius
     * @param canvas
     */
    protected void onDraw(Canvas canvas) {


        super.onDraw(canvas);
        Paint myPaint = new Paint();

        myPaint.setColor(Color.rgb(0, 0, 0));
        myPaint.setStrokeWidth(1);

        Paint myPaint2 = new Paint();
        myPaint2.setColor(Color.rgb(0, 0, 255));
        myPaint2.setStrokeWidth(1);

        canvas.drawOval(this.oval, myPaint2);
        canvas.drawCircle(this.x , this.y , this.radius, myPaint);

    }

    /**
     * called when the user resize the window
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        returnDefualt();
        this.startWidth = (float)getWidth()/4;
        this.endWidth = (float)getWidth()-((float)getWidth()/4);
        this.startHeight = (float)getHeight()/12;
        this.endHeight = getHeight()-((float)getHeight()/12);
        this.oval = new RectF(this.startWidth,this.startHeight , this.endWidth, this.endHeight);
    }

    /**
     * set default values for x,y, notify observer about the change
     */
    public void returnDefualt() {
        this.x = (float)getWidth()/2;
        this.y = (float)getHeight()/2;
        notifyObservers(normelizeAilron(this.x), normelizeElevator(this.y));
    }

    /**
     * change x,y according user's input, notify observers
     * @param event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            //if the user touched the screen
            case MotionEvent.ACTION_DOWN: {
                //check if the input is inside the circle
                if(CheckIfInside(event.getX(), event.getY())) {
                    this.playMoving = true;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (!this.playMoving)
                    return true;
                //make sure user input is inside limits
                if (CheckForLimit(event.getX(), event.getY())) {
                    this.x = event.getX();
                    this.y = event.getY();
                    notifyObservers(normelizeAilron(this.x), normelizeElevator(this.y));
                    invalidate();
                }
                break;
            }
            //user input's is finished
            case MotionEvent.ACTION_UP :
                this.playMoving = false;
                returnDefualt();
                //call on draw
                invalidate();
        }
        return true;
    }

    /**
     * check if user touching inside the circle
     * @param xVal
     * @param yVal
     * @return
     */
    Boolean CheckIfInside(float xVal, float yVal) {
        double distance = Math.sqrt((this.x-xVal)*(this.x-xVal) + (this.y-yVal)*(this.y-yVal));
        return (distance <= this.radius);
    }

    /**
     * make sure give x,y inside the oval shape
     * @param xVal
     * @param yVal
     * @return
     */
    Boolean CheckForLimit(float xVal, float yVal) {
        return (this.oval.contains(xVal, yVal) &&
                this.oval.contains(xVal, yVal+radius) &&
                this.oval.contains(xVal, yVal-radius) &&
                this.oval.contains(xVal+radius, yVal) &&
                this.oval.contains(xVal-radius, yVal));
    }


    public void addToObserver(ObserverI obs) {
        this.obs.add(obs);
    }


    public void notifyObservers(float x, float y) {
        for(ObserverI obs : this.obs) {
            obs.update(x,y);
        }
    }

    public float normelizeAilron(float x) {
        return (x-((this.startWidth+this.endWidth)/2))/((this.endWidth-this.startWidth)/2);
    }

    public float normelizeElevator(float y) {
        return (y-((this.startHeight+this.endHeight)/2))/((this.startHeight-this.endHeight)/2);
    }

}
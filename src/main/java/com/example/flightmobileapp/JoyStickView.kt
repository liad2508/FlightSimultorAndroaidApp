package com.example.flightmobileapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import java.util.*

class JoyStickView(context: Context?) : View(context),
    ObservableI {
    private var x1 = 0f
    private var y1 = 0f
    private val radius = 70f
    private var startWidth = 0f
    private var endWidth = 0f
    private var startHeight = 0f
    private var endHeight = 0f
    private var oval: RectF? = null
    private var playMoving = false
    private val obs: MutableList<ObserverI> = LinkedList()

    /**
     * draw oval and circle according to x,y,radius
     * @param canvas
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val myPaint = Paint()
        myPaint.color = Color.rgb(0, 0, 0)
        myPaint.strokeWidth = 1f
        val myPaint2 = Paint()
        myPaint2.color = Color.rgb(0, 0, 255)
        myPaint2.strokeWidth = 1f
        canvas.drawOval(oval!!, myPaint2)
        canvas.drawCircle(x1, y1, radius, myPaint)
    }

    /**
     * called when the user resize the window
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    public override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        returnDefualt()
        startWidth = width.toFloat() / 4
        endWidth = width.toFloat() - width.toFloat() / 4
        startHeight = height.toFloat() / 12
        endHeight = height - height.toFloat() / 12
        oval = RectF(startWidth, startHeight, endWidth, endHeight)
    }

    /**
     * set default values for x,y, notify observer about the change
     */
    fun returnDefualt() {
        x1= width.toFloat() / 2
        y1 = height.toFloat() / 2
        notifyObservers(normelizeAilron(x1), normelizeElevator(y1))
    }

    /**
     * change x,y according user's input, notify observers
     * @param event
     * @return
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.actionMasked
        when (action) {
            MotionEvent.ACTION_DOWN -> {

                //check if the input is inside the circle
                if (CheckIfInside(event.x, event.y)) {
                    playMoving = true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (!playMoving) return true
                //make sure user input is inside limits
                if (CheckForLimit(event.x, event.y)) {
                    x1 = event.x
                    y1 = event.y
                    notifyObservers(normelizeAilron(x1), normelizeElevator(y1))
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                playMoving = false
                returnDefualt()
                //call on draw
                invalidate()
            }
        }
        return true
    }

    /**
     * check if user touching inside the circle
     * @param xVal
     * @param yVal
     * @return
     */
    fun CheckIfInside(xVal: Float, yVal: Float): Boolean {
        val distance =
            Math.sqrt((x1 - xVal) * (x1 - xVal) + (y1 - yVal) * (y1 - yVal).toDouble())
        return distance <= radius
    }

    /**
     * make sure give x,y inside the oval shape
     * @param xVal
     * @param yVal
     * @return
     */
    fun CheckForLimit(xVal: Float, yVal: Float): Boolean {
        return oval!!.contains(xVal, yVal) &&
                oval!!.contains(xVal, yVal + radius) &&
                oval!!.contains(xVal, yVal - radius) &&
                oval!!.contains(xVal + radius, yVal) &&
                oval!!.contains(xVal - radius, yVal)
    }

    override fun addToObserver(obs: ObserverI) {
        this.obs.add(obs)
    }

    override fun notifyObservers(x: Float, y: Float) {
        for (obs in obs) {
            obs.update(x, y)
        }
    }

    fun normelizeAilron(x: Float): Float {
        return (x - (startWidth + endWidth) / 2) / ((endWidth - startWidth) / 2)
    }

    fun normelizeElevator(y: Float): Float {
        return (y - (startHeight + endHeight) / 2) / ((startHeight - endHeight) / 2)
    }

}
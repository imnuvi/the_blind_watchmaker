package com.example.the_blind_watchmaker

import android.util.AttributeSet
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.widget.AppCompatImageView
import android.util.Log
import android.view.MotionEvent
import androidx.core.content.res.ResourcesCompat

class WatchBoard @JvmOverloads constructor(context: Context, attrs: AttributeSet ?= null , defStyleAttr: Int = 0) : AppCompatImageView(context, attrs, defStyleAttr) {
    private lateinit var watchCanvas: Canvas
    private lateinit var config: Config
    private var canvasWidth: Int = 0
    private var canvasHeight: Int = 0
    private var watchWidth: Int = 0
    private var watchHeight: Int = 0
    private var angle: Float = 60.toFloat()
    private var watchBoard: MutableList<MutableList<Watch>> = mutableListOf()
    private lateinit var parentWatch: Watch

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.canvasWidth = w
        this.canvasHeight = h
    }

    override fun onDraw(canvas: Canvas) {
        this.watchCanvas = canvas
        super.onDraw(this.watchCanvas)
        handleTouch(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var touchXPos : Float? = event?.x
        var touchYPos : Float? = event?.y
        when (event?.action){
            MotionEvent.ACTION_DOWN -> {}
            MotionEvent.ACTION_MOVE -> {}
            MotionEvent.ACTION_UP -> {
                Log.d("TOUCHED", touchXPos.toString() + "        " + touchYPos.toString())
            }
        }
        invalidate()
//        handleTouch()
        return super.onTouchEvent(event)
    }

    private fun setupWatchboard() {
//        val minWatchSize = 200
        val minWatchSize = this.canvasWidth / 5
        // TODO: make sure the x and y watch counts are scalable and fit the screen
        // TODO: in the future make sure the user can move around on the screen on a infinite scale
        val xWatchCount = this.canvasWidth / minWatchSize
        val yWatchCount = this.canvasHeight / minWatchSize
//        val xWatchCount = 2
//        val yWatchCount = 2
        watchWidth = minWatchSize
        watchHeight = minWatchSize
        //double loops for creating watch rows and columns
        for (i in 0..xWatchCount){
            val watchRow = mutableListOf<Watch>()
            for (j in 0..yWatchCount){
                val curWatchXPos = i * watchWidth
                val curWatchYPos = j * watchHeight
                var testWatch = Watch(Point(curWatchXPos.toFloat(),curWatchYPos.toFloat()), watchWidth, watchHeight)
//                val testWatch = Watch(Point(0.toFloat(),0.toFloat()), this.canvasWidth, this.canvasHeight)
                watchRow.add(testWatch)
            }
            watchBoard.add(watchRow)
        }
    }

    fun createWatches(){
    }

    fun showWatches(canvas: Canvas){
        for ( rowList in watchBoard ){
            for ( cell in rowList) {
                cell.watchCanvas = canvas
                cell.show()
            }
        }
    }

    fun showBlindWatches(canvas: Canvas){
        for ( rowList in watchBoard ){
            for ( cell in rowList) {
                cell.watchCanvas = canvas
                cell.show()
            }
        }
    }

    private fun handleTouch(canvas: Canvas) {
        canvas.drawColor(Color.argb(255,0,0,0))
        if (watchBoard.size == 0){
            setupWatchboard()
            Log.d("H1","INIT BRO")
        }
        showWatches(canvas)
        Log.d("TEST","running bro")
    }
}



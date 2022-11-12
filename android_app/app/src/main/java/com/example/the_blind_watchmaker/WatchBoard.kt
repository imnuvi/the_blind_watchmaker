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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.canvasWidth = w
        this.canvasHeight = h
    }

    override fun onDraw(canvas: Canvas) {
        this.watchCanvas = canvas
        handleTouch()
        super.onDraw(this.watchCanvas)
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var touchXPos : Float? = event?.x
        var touchYPos : Float? = event?.y
        when (event?.action){
            MotionEvent.ACTION_DOWN -> {}
            MotionEvent.ACTION_MOVE -> {}
            MotionEvent.ACTION_UP -> {
            }
        }
        this.invalidate()
        return super.onTouchEvent(event)
    }

    private fun setupWatchboard() {
        val minWatchSize = 300
        // TODO: make sure the x and y watch counts are scalable and fit the screen
        // TODO: in the future make sure the user can move around on the screen on a infinite scale
        val xWatchCount = this.canvasWidth / minWatchSize
        val yWatchCount = this.canvasHeight / minWatchSize
        watchWidth = minWatchSize
        watchHeight = minWatchSize
    }

    private fun handleTouch() {
        setupWatchboard()
        Log.d("TEST","running bro")
        val testWatch: Watch = Watch(Point(0.toFloat(),0.toFloat()), this.canvasWidth, this.canvasWidth, watchCanvas)
//        val testWatch: Watch = Watch(Point(0.toFloat(),0.toFloat()), watchWidth, watchHeight, watchCanvas)
        testWatch.testFun()
        testWatch.setupWatch()
        testWatch.createBranches()
        testWatch.showBranches()
    }
}



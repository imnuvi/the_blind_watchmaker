package com.example.the_blind_watchmaker

import android.util.AttributeSet
import android.content.Context
import android.graphics.Canvas
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
                Log.d("TAG", "touch UP action")
            }
        }
        this.invalidate()
        return super.onTouchEvent(event)
    }

    private fun crateConfig(): Config {
        var drawColor = ResourcesCompat.getColor(resources, R.color.black, null)
        return Config(500, 500, drawColor)
    }

    private fun handleTouch() {
        Log.d("TAG", "touch being handled")
        Log.d("TAG", this.canvasHeight.toString() + this.canvasWidth.toString())
        watchCanvas.drawARGB(0, 225, 225, 255);
        this.watchCanvas.drawText("TEST", 500.toFloat(), 500.toFloat(), Paint().apply {
            color = ResourcesCompat.getColor(resources, R.color.black, null)
            style = Paint.Style.FILL
        });
        this.config = crateConfig()
        var testWatch: Watch = Watch(500, 500, this.config, watchCanvas)
        testWatch.createBranches()
        testWatch.showBranches()
    }
}


class Config constructor(val watchWidth: Int, val watchHeight: Int, val drawColor: Int){
    var linePaint = Paint().apply {
        color = drawColor
        style = Paint.Style.FILL
    }
}

class Watch constructor(val startXPos: Int, val startYPos: Int, val config: Config, val watchCanvas: Canvas) {
    public var branchList: MutableList<Branch> = mutableListOf()

    fun createBranches(){
//        create branches and proliferate
        val firstBranch: Branch = Branch(500.toFloat(), 1000.toFloat(), 50.toFloat(), 50.toFloat())
        this.branchList.add(firstBranch)
    }

    fun showBranches(){
        Log.d("TAG", "showing watch branches")
//        create branches and proliferate
        for (branch in branchList){
            branch.showBranch(watchCanvas, config.linePaint)
        }
    }
}

class Branch constructor(val startX: Float, val startY: Float, val endX: Float, val endY: Float){
    fun showBranch(canvas: Canvas, paint: Paint){
        Log.d("TAG", "rendering branches")
//        basically draw the branch on the canvas element.
        canvas.drawLine(startX, startY, endX, endY, paint)
    }

}

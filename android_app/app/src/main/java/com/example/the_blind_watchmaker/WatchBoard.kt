package com.example.the_blind_watchmaker

import android.util.AttributeSet
import android.content.Context
import android.graphics.Canvas
import androidx.appcompat.widget.AppCompatImageView
import android.util.Log
import android.view.MotionEvent

class WatchBoard @JvmOverloads constructor(context: Context, attrs: AttributeSet ?= null , defStyleAttr: Int = 0) : AppCompatImageView(context, attrs, defStyleAttr) {
    private lateinit var watchCanvas: Canvas
    override fun onDraw(canvas: Canvas) {
        this.watchCanvas = canvas
        super.onDraw(canvas)
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
        handleTouch()
        return super.onTouchEvent(event)
    }
    private fun handleTouch() {
        Log.d("TAG", "touch being handled")
    }
}


class Config constructor(watchWidth: Int, watchHeight: Int){
    var watchWidth: Int = 300
    var watchHeight: Int = 300
}

class Watch constructor(startXPos: Int, startYPos: Int, config: Config) {
    public lateinit var watchCanvas: Canvas

    fun createBranches(){
//        create branches and proliferate

    }
}

class Branch constructor(startX: Int, startY: Int, endX: Int, endY: Int){

}
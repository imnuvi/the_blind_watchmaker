package com.example.the_blind_watchmaker

import android.util.AttributeSet
import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import android.util.Log
import android.view.MotionEvent

class WatchBoard @JvmOverloads constructor(context: Context, attrs: AttributeSet ?= null , defStyleAttr: Int = 0) : AppCompatImageView(context, attrs, defStyleAttr) {
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        handleTouch()
        return super.onTouchEvent(event)
    }
    private fun handleTouch() {
        Log.d("TAG", "touch being handled")
    }
}
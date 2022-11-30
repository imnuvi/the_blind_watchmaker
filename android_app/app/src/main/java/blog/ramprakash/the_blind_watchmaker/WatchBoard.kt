package blog.ramprakash.the_blind_watchmaker

import android.util.AttributeSet
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.widget.AppCompatImageView
import android.util.Log
import android.view.MotionEvent
import androidx.core.content.res.ResourcesCompat
import kotlin.math.floor

class WatchBoard @JvmOverloads constructor(context: Context, attrs: AttributeSet ?= null , defStyleAttr: Int = 0) : AppCompatImageView(context, attrs, defStyleAttr) {
    private lateinit var watchCanvas: Canvas
    private lateinit var config: Config
    private var canvasWidth: Int = 0
    private var canvasHeight: Int = 0
    private var watchWidth: Int = 0
    private var watchHeight: Int = 0
    private var angle: Float = 60.toFloat()
    private var watchBoard: MutableList<MutableList<Watch>> = mutableListOf()
    private var xWatchCount: Int = 0
    private var yWatchCount: Int = 0
    private var minWatchSize: Int = 0
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
                invalidate()
                var (watchPointX, watchPointY) = selectWatch(touchXPos, touchYPos)
                parentWatch = watchBoard[watchPointX][watchPointY]
            }
        }
//        handleTouch()
        return super.onTouchEvent(event)
    }

    fun selectWatch(touchX: Float?, touchY: Float?): Pair<Int, Int> {
        // this function will set the clicked watch as the parent
        var clickedWatchXPos: Float = 0.toFloat()
        var clickedWatchYPos: Float = 0.toFloat()
        touchX?.let {
            clickedWatchXPos = floor(touchX / this.watchWidth)
        }
        touchY?.let {
            clickedWatchYPos = floor(touchY / this.watchHeight)
        }
        return Pair((clickedWatchXPos + 1).toInt(), (clickedWatchYPos + 1).toInt())
    }

    private fun setupWatchboard(canvas: Canvas) {
//        val minWatchSize = 200
        this.minWatchSize = this.canvasWidth / 5
        // TODO: make sure the x and y watch counts are scalable and fit the screen
        // TODO: in the future make sure the user can move around on the screen on a infinite scale
        xWatchCount = this.canvasWidth / minWatchSize
        yWatchCount = this.canvasHeight / minWatchSize
//        val xWatchCount = 2
//        val yWatchCount = 2
        watchWidth = minWatchSize
        watchHeight = minWatchSize
        parentWatch = Watch(Point(0.toFloat(),0.toFloat()), watchWidth, watchHeight)
        parentWatch.watchCanvas = canvas
        parentWatch.setupWatch()
        //double loops for creating watch rows and columns
        for (i in 0..xWatchCount){
            val watchRow = mutableListOf<Watch>()
            for (j in 0..yWatchCount){
                val curWatchXPos = i * watchWidth
                val curWatchYPos = j * watchHeight
                val childConfig = parentWatch.config.spawnChild(canvas)
                val testWatch = Watch(Point(curWatchXPos.toFloat(),curWatchYPos.toFloat()), watchWidth, watchHeight)
                testWatch.config = childConfig
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
                // for the actual blind watchmaker, lets just update the config and keep the watch instance as is
                cell.watchCanvas = canvas
                val childConfig = parentWatch.config.spawnChild(canvas)
                cell.config = childConfig
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
            setupWatchboard(canvas)
            Log.d("H1","INIT BRO")
        }
        showWatches(canvas)
        Log.d("TEST","running bro")
    }
}



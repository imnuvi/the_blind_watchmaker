package com.example.the_blind_watchmaker

import android.util.AttributeSet
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.appcompat.widget.AppCompatImageView
import android.util.Log
import android.view.MotionEvent
import androidx.core.content.res.ResourcesCompat
import kotlin.math.cos
import kotlin.math.sin

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
        return Config(this.canvasWidth, this.canvasHeight/4, drawColor)
    }

    private fun handleTouch() {
        Log.d("TAG", "touch being handled")
        Log.d("TAG", this.canvasHeight.toString() + this.canvasWidth.toString())
        watchCanvas.drawARGB(0, 225, 225, 255);
        this.config = crateConfig()
        var testWatch: Watch = Watch(0, this.canvasHeight/2, this.config, watchCanvas)
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
    private var watchWidth: Int = config.watchWidth
    private var watchHeight: Int = config.watchWidth
    private var linePaint: Paint = config.linePaint

    fun lineHelper(): List<Point>{
        var lineStartX: Int = startXPos + watchWidth/2
        var lineStartY: Int = startYPos + watchHeight/4
        var lineEndX: Int = startXPos + watchWidth/2
        var lineEndY: Int = startYPos + watchHeight/2
        Log.d("LINE HELPER", lineStartX.toString() + "    " + lineStartY.toString() + "    " + lineEndX.toString() + "    " + lineEndY.toString())
        var returnSet: List<Point> = listOf<Point>(Point(lineStartX.toFloat(), lineStartY.toFloat()), Point(lineEndX.toFloat(), lineEndY.toFloat()))
        return returnSet
    }

    fun calcLength(startPoint: Point, endPoint: Point): Float{
        return (Math.sqrt(Math.pow(startPoint.x.toDouble() - endPoint.x.toDouble(), 2.0) + Math.pow(startPoint.y.toDouble() - endPoint.y.toDouble(), 2.0))).toFloat()
    }

    fun createBranches(){
//        create branches and proliferate
        var branchHelper = lineHelper()
        val firstBranch: Branch = Branch(
            branchHelper.elementAt(0).x,
            branchHelper.elementAt(0).y,
            branchHelper.elementAt(1).x,
            branchHelper.elementAt(1).y,
            calcLength(branchHelper.elementAt(0), branchHelper.elementAt(1)),
            1.7.toFloat()
        )
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


//actually much better would be to have a custom point class that handles all your access and stuff
class Point constructor(val x: Float, val y: Float) {
}

class Branch constructor(val startX: Float, val startY: Float, private val endX: Float, val endY: Float, val branchLen: Float, val scale: Float){

    fun extendBranch(startX: Float, startY: Float, endX: Float, endY: Float): Point{
        val newX = endX + ( ( endX - startX ) / branchLen ) * scale
        val newY = endY + ( ( endY - startY ) / branchLen ) * scale
        return Point(newX.toFloat(), newY.toFloat())
    }

    fun rotateChild(angle: Float, newX: Float, newY: Float): Point{
//        var childEndX = k
        var angle = Math.toRadians(angle.toDouble())
        var rotatedX = (newX - this.endX) * cos(angle.toDouble()) - ( newY - this.endY) * sin(angle.toDouble()) + this.endX
        var rotatedY = (newX - this.endX) * sin(angle.toDouble()) - ( newY - this.endY) * cos(angle.toDouble()) + this.endY
        Log.d("ANGLE", angle.toString())
        Log.d("DEBUG", cos(angle).toString())
        Log.d("ORIGINAL", newX.toString() + "     " + newY.toString())
        Log.d("ROTATED", rotatedX.toString() + "     " + rotatedY.toString())
        return Point(rotatedX.toFloat(), rotatedY.toFloat())

    }

    fun showBranch(canvas: Canvas, paint: Paint){
        Log.d("TAG", "rendering branches")
//        basically draw the branch on the canvas element.
        val eb = extendBranch(startX, startY, endX, endY)
        val rc = rotateChild(90.toFloat(), eb.x, eb.y)
        Log.d("OG", startX.toString() + "     " + startY.toString())
        Log.d("END", endX.toString() + "     " + endY.toString())
        Log.d("EXTENDED", eb.x.toString() + "     " + eb.y.toString())
        Log.d("ROTATED", rc.x.toString() + "     " + rc.y.toString())
        canvas.drawLine(endX, endY, rc.x, rc.y, paint)
        canvas.drawLine(startX, startY, endX, endY, paint)
        canvas.drawLine(startX, startY, 100.toFloat(), 100.toFloat(), paint)
    }

}

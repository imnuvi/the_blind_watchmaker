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


    private fun createConfig(): Config {
        var drawColor = ResourcesCompat.getColor(resources, R.color.black, null)
        return Config(this.canvasWidth, this.canvasWidth, drawColor, this.angle.toFloat(), 0.75.toFloat(), watchCanvas, 9)
    }

    fun updateAngle(angle: Float){
        this.angle = angle
        this.config = createConfig()
        this.invalidate()
    }

    private fun handleTouch() {
        watchCanvas.drawARGB(0, 225, 225, 255);
        this.config = createConfig()
        var testWatch: Watch = Watch(0, 500, this.config, watchCanvas)
        testWatch.createBranches()
        testWatch.showBranches()
    }
}

class Config constructor(val watchWidth: Int, val watchHeight: Int, val drawColor: Int, val tiltAngle: Float, val childScale: Float, val canvas: Canvas, val depth: Int){
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
        var lineStartY: Int = startYPos + watchHeight/2
        var lineEndX: Int = startXPos + watchWidth/2
        var lineEndY: Int = startYPos + watchHeight/4
        var returnSet: List<Point> = listOf<Point>(Point(lineStartX.toFloat(), lineStartY.toFloat()), Point(lineEndX.toFloat(), lineEndY.toFloat()))
        return returnSet
    }

    fun calcLength(startPoint: Point, endPoint: Point): Float{
        var length = Math.sqrt(Math.pow(startPoint.x.toDouble() - endPoint.x.toDouble(), 2.0) + Math.pow(startPoint.y.toDouble() - endPoint.y.toDouble(), 2.0))
        return length.toFloat()
    }

    fun createBranches(){
//        create branches and proliferate
        var branchHelper = lineHelper()
        val firstBranch: Branch = Branch(
            branchHelper.elementAt(0),
            branchHelper.elementAt(1),
            calcLength(branchHelper.elementAt(0), branchHelper.elementAt(1)),
            90.toFloat(),
            config,
            0
        )
        this.branchList.add(firstBranch)
    }

    fun showBranches(){
//        display branches and children
        for (branch in branchList){
            branch.spawnChildren()
            branch.showBranch()
        }
    }
}


//actually much better would be to have a custom point class that handles all your access and stuff
class Point constructor(val x: Float, val y: Float) {
}

class Branch constructor(val startPoint: Point, private val endPoint: Point, val branchLen: Float, val angle: Float, val config: Config, val depth: Int){
    private lateinit var childrenList: List<Branch>

    fun extendBranch(startX: Float, startY: Float, endX: Float, endY: Float): Point{
        val scale = config.childScale
        val newX = endX + ( ( endX - startX ) / branchLen ) * scale
        val newY = endY + ( ( endY - startY ) / branchLen ) * scale
        return Point(newX.toFloat(), newY.toFloat())
    }

    fun rotateChild_11(angle: Float, newX: Float, newY: Float): Point{
        var angle = Math.toRadians(angle.toDouble())
        var rotatedX = (newX - this.endPoint.x) * cos(angle.toDouble()) - ( newY - this.endPoint.y) * sin(angle.toDouble()) + this.endPoint.x
        var rotatedY = (newX - this.endPoint.x) * sin(angle.toDouble()) - ( newY - this.endPoint.y) * cos(angle.toDouble()) + this.endPoint.y
        return Point(rotatedX.toFloat(), rotatedY.toFloat())
    }

    fun quadrantConvertor(angle: Float): Array<Int> {
        // IMPORTANT: since the y axis is inverted on the canvas quadrants will change appropriately
        // basically the current angles mentioned are for classic quadrants but values are offsetted for canvas
        var modAngle = angle % 360
        var sinValue = 1
        var cosValue = 1
        if ( ( modAngle < 90 ) && ( modAngle > 0 ) ) {
            sinValue = 1
            cosValue = 1
        }
        else if ( ( modAngle < 180 ) && ( modAngle > 90 ) ) {
            sinValue = 1
            cosValue = -1
        }
        else if ( ( modAngle < 270 ) && ( modAngle > 180 ) ) {
            sinValue = -1
            cosValue = -1
        }
        else if ( ( modAngle < 360 ) && ( modAngle > 270 ) ) {
            sinValue = -1
            cosValue = 1
        }
        else{
            sinValue = 1
            cosValue = 1
        }
        return arrayOf(sinValue, cosValue)
    }

    fun rotateChild(length: Float, angle: Float): Point{
        var finAngle = 360 - angle
        var valueArray = quadrantConvertor(angle)
        var sinValue = valueArray.elementAt(0)
        var cosValue = valueArray.elementAt(1)
        var yPos = length * sin(Math.toRadians(finAngle.toDouble()))
        var xPos = length * cos(Math.toRadians(finAngle.toDouble()))
        return Point(xPos.toFloat(), yPos.toFloat())
    }

    fun spawnChildren(){
        var childLen = branchLen * config.childScale
        var rightPoint = moveToOrigin(rotateChild(childLen, this.angle + config.tiltAngle))
        var leftPoint = moveToOrigin(rotateChild(childLen, this.angle - config.tiltAngle))
        var rightChild = Branch(
            endPoint,
            rightPoint,
            childLen,
            (angle + config.tiltAngle).toFloat(),
            config,
            depth + 1
        )
        var leftChild = Branch(
            endPoint,
            leftPoint,
            childLen,
            (angle - config.tiltAngle).toFloat(),
            config,
            depth + 1
        )
        childrenList = listOf(leftChild, rightChild)
    }

    fun moveToOrigin(originalPoint: Point): Point{
        return Point(originalPoint.x + endPoint.x, originalPoint.y + endPoint.y)
    }

    fun drawLine(size: Int, angle: Int){
        var canvas = config.canvas
        var paint = config.linePaint
        var testChild = moveToOrigin(rotateChild(size.toFloat(), angle.toFloat()))
        canvas.drawLine(startPoint.x, startPoint.y, testChild.x, testChild.y, paint)
        canvas.drawCircle(testChild.x, testChild.y, 10.toFloat(), paint);
    }

    fun showBranch(){
        var canvas = config.canvas
        var paint = config.linePaint
        canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, paint)
        var leftChild = childrenList.elementAt(0)
        var rightChild = childrenList.elementAt(1)
        if (depth == config.depth){
            return
        }
        else{
            leftChild.spawnChildren()
            leftChild.showBranch()
            rightChild.spawnChildren()
            rightChild.showBranch()
        }

    }

}

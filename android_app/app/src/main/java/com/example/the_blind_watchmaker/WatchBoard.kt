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
        return Config(this.canvasWidth, this.canvasWidth, drawColor, 10.toFloat(), 0.7.toFloat(), watchCanvas, 2)
    }

    private fun handleTouch() {
        Log.d("TAG", "touch being handled")
        Log.d("TAG", this.canvasHeight.toString() + this.canvasWidth.toString())
        watchCanvas.drawARGB(0, 225, 225, 255);
        this.config = crateConfig()
        var testWatch: Watch = Watch(0, 300, this.config, watchCanvas)
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
        var lineStartY: Int = startYPos + watchHeight/4
        var lineEndX: Int = startXPos + watchWidth/2
        var lineEndY: Int = startYPos + watchHeight/2
        Log.d("LINE HELPER", lineStartX.toString() + "    " + lineStartY.toString() + "    " + lineEndX.toString() + "    " + lineEndY.toString())
        var returnSet: List<Point> = listOf<Point>(Point(lineStartX.toFloat(), lineStartY.toFloat()), Point(lineEndX.toFloat(), lineEndY.toFloat()))
        return returnSet
    }

    fun calcLength(startPoint: Point, endPoint: Point): Float{
        var length = Math.sqrt(Math.pow(startPoint.x.toDouble() - endPoint.x.toDouble(), 2.0) + Math.pow(startPoint.y.toDouble() - endPoint.y.toDouble(), 2.0))
        Log.d("LENGTH", length.toString())
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
        Log.d("TAG", "showing watch branches")
//        create branches and proliferate
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
        Log.d("EXTENDED", newX.toString() + "     " + newY.toString())
        return Point(newX.toFloat(), newY.toFloat())
    }

    fun rotateChild_11(angle: Float, newX: Float, newY: Float): Point{
//        var childEndX = k
        var angle = Math.toRadians(angle.toDouble())
        var rotatedX = (newX - this.endPoint.x) * cos(angle.toDouble()) - ( newY - this.endPoint.y) * sin(angle.toDouble()) + this.endPoint.x
        var rotatedY = (newX - this.endPoint.x) * sin(angle.toDouble()) - ( newY - this.endPoint.y) * cos(angle.toDouble()) + this.endPoint.y
        return Point(rotatedX.toFloat(), rotatedY.toFloat())
    }

    fun quadrantConvertor(angle: Float): Array<Int> {
        // IMPORTANT: since the y axis is inverted on the canvas quadrants will change appropriately
        // basically the current angles mentioned are for classic quadrants but values are offsetted for canvas
        var modAngle = angle % 360
        Log.d("ANGLE", modAngle.toString() + "     " + angle.toString())
        var sinValue = 1
        var cosValue = 1
        if ( ( modAngle < 90 ) && ( modAngle > 0 ) ) {
            Log.d("CASE 1", "case 1")
            sinValue = 1
            cosValue = 1
        }
        else if ( ( modAngle < 180 ) && ( modAngle > 90 ) ) {
            Log.d("CASE 2", "case 2")
            sinValue = 1
            cosValue = -1
        }
        else if ( ( modAngle < 270 ) && ( modAngle > 180 ) ) {
            Log.d("CASE 3", "case 3")
            sinValue = -1
            cosValue = -1
        }
        else if ( ( modAngle < 360 ) && ( modAngle > 270 ) ) {
            Log.d("CASE 4", "case 4")
            sinValue = -1
            cosValue = 1
        }
        else{
            sinValue = 1
            cosValue = 1
        }
        Log.d("LOGGING", sinValue.toString() + "       " + cosValue.toString())
        return arrayOf(sinValue, cosValue)
    }

    fun rotateChild(length: Float, angle: Float): Point{
        var finAngle = 360 - angle
        var valueArray = quadrantConvertor(angle)
        var sinValue = valueArray.elementAt(0)
        var cosValue = valueArray.elementAt(1)
        var yPos = length * sin(Math.toRadians(finAngle.toDouble()))
        var xPos = length * cos(Math.toRadians(finAngle.toDouble()))
        Log.d("ROTATE", Math.toRadians(finAngle.toDouble()).toString())
        Log.d("ROTATED VALUES", xPos.toString() + "            " + yPos.toString() )
        return Point(xPos.toFloat(), yPos.toFloat())
    }

    fun spawnChildren(){
        var childLen = branchLen * config.childScale
        Log.d("CHILD LEN", childLen.toString())
        var rightPoint = moveToOrigin(rotateChild(childLen, this.angle + config.tiltAngle))
//        val ec = rc)
        var leftPoint = moveToOrigin(rotateChild(childLen, this.angle - config.tiltAngle))
        Log.d("POINTS", rightPoint.x.toString() + "            " + rightPoint.y.toString() )
        Log.d("POINTS", leftPoint.x.toString() + "            " + leftPoint.y.toString() )
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
//        var leftChild = Branch(
//            branchHelper.elementAt(0),
//            branchHelper.elementAt(1),
//            calcLength(branchHelper.elementAt(0), branchHelper.elementAt(1)),
//            1.7.toFloat(),
//            180.toFloat()
//        )
    }

    fun moveToOrigin(originalPoint: Point): Point{
        return Point(originalPoint.x + startPoint.x, originalPoint.y + startPoint.y)
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
        Log.d("TAG", "rendering branches")
//        val rc = rotateChild(branchLen, this.angle.toFloat())
//        val ec = moveToOrigin(rc)
//        canvas.drawLine(startPoint.x, startPoint.y, rc.x, rc.y, paint)
        canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, paint)
//        drawLine(100, 0)
//        drawLine(100, 30)
//        drawLine(100, 60)
//        drawLine(100, 90)
//        drawLine(100, 110)
//        drawLine(100, 130)
//        drawLine(100, 310)
//        drawLine(100, 220)
//        drawLine(100, 270)
//        drawLine(100, 300)
//        drawLine(100, 320)
        canvas.drawCircle(endPoint.x, endPoint.y, 10.toFloat(), paint);
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

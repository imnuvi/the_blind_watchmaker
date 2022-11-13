package com.example.the_blind_watchmaker

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class Watch constructor(val startPos: Point, val watchWidth: Int, val watchHeight: Int, val watchCanvas: Canvas) {
    private var branchList: MutableList<Branch> = mutableListOf()
    private lateinit var config: Config


    fun testFun(){
    }

    fun setupWatch(){
        val defaultConfig: DefaultConfig = DefaultConfig()
        this.config = Config(watchCanvas, defaultConfig)
        this.config.generateMutations()
        Log.d("TEST",this.config.canvas.toString())
        var helPaint = Paint().apply {
            color = Color.argb(255, 255, 165, 0)
            style = Paint.Style.FILL
        }
        this.config.canvas.drawCircle(500.toFloat(), 500.toFloat(), 10.toFloat(), helPaint)
    }

    fun lineHelper(): List<Point>{
        val lineStartX: Int = startPos.x.toInt() + watchWidth/2
        val lineStartY: Int = startPos.y.toInt() + watchHeight - watchHeight/4
        val lineEndX: Int = startPos.x.toInt() + watchWidth/2
        val lineEndY: Int = startPos.y.toInt() + watchHeight/2
        val returnSet: List<Point> = listOf<Point>(Point(lineStartX.toFloat(), lineStartY.toFloat()), Point(lineEndX.toFloat(), lineEndY.toFloat()))
        return returnSet
    }

    fun calcLength(startPoint: Point, endPoint: Point): Float{
        val length = Math.sqrt(Math.pow(startPoint.x.toDouble() - endPoint.x.toDouble(), 2.0) + Math.pow(startPoint.y.toDouble() - endPoint.y.toDouble(), 2.0))
        return length.toFloat()
    }

    fun createBranches(){
//        create branches and proliferate
        val branchHelper = lineHelper()
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
        watchCanvas.drawRect(startPos.x, startPos.y, startPos.x + watchWidth, startPos.y + watchHeight, config.bgPaint)
        for (branch in branchList){
            branch.spawnChildren()
            branch.showBranch()
        }
    }
}

// TO-DO: decommission the angle, scale created for solid fractals
class Config constructor(val canvas: Canvas, val defaultConfig: DefaultConfig){

    var mutationHashMap: MutableMap<Int,BranchConfig> = mutableMapOf()
    //    var mutationMutableMap: MutableMap<String><Float>

//    val angle: Float, val scale: Float, val depth: Int, val parentBranchConfig: BranchConfig?
    val lengthScale = defaultConfig.lengthScale.toFloat()
    val lengthRandomDev = defaultConfig.lengthRandomDev.toFloat()
    val angleDeviation = defaultConfig.angleDeviation.toFloat()
    var angle: Float = defaultConfig.angle.toFloat()
    var drawColor = generateRandomColor()
    val bgColor = defaultConfig.bgColor.toFloat()
    var depth: Int = defaultConfig.depth

    var linePaint = Paint().apply {
        color = drawColor
        style = Paint.Style.FILL
    }
    var bgPaint = Paint().apply {
        color = bgColor.toInt()
        style = Paint.Style.FILL
    }
    var highlightPaint = Paint().apply {
        color = Color.argb(255, 255, 165, 0)
        style = Paint.Style.FILL
    }


    fun setup(){
        val minWatchSize = 300
        this.angle = 60.toFloat()
    }


    fun generateRandomColor(): Int {
        val rValue = Random.nextInt(255)
        val gValue = Random.nextInt(255)
        val bValue = Random.nextInt(255)
        return Color.argb(255, rValue, gValue, bValue)
    }

    fun generateRandomRatio(startpos: Float, endpos: Float): Float{
        // so apparently java random does not love 0. or anything rounded to 0. so we have to scale it to hundred perform calc and return
        val scaledStartpos = startpos * 100
        val scaledEndpos = endpos * 100
        val rangeval = scaledEndpos.toInt()-scaledStartpos.toInt()
        val randomValue = Random.nextInt(rangeval)
        val finalRandom = randomValue + startpos
        return (finalRandom / 100).toFloat()
    }

    fun generateMuatatedAngle(): Float{
        val mutationAngleFloat = generateRandomRatio(-this.angleDeviation , this.angleDeviation)
        return mutationAngleFloat
    }

    fun generateMutatedLengthRatio(): Float{
        val mutationLenFloat = generateRandomRatio(this.lengthScale-this.lengthRandomDev , this.lengthScale+this.lengthRandomDev)
        return mutationLenFloat
    }

    // here, we will be creating a map which will contain the mutation for all the branches and the children will use the map as reference
    fun generateMutations(){
        for (i in 0..depth+1){
            val genMutationBranch = BranchConfig(generateMutatedLengthRatio(), generateMuatatedAngle())
            mutationHashMap[i] = genMutationBranch
        }
    }

}

//class BranchConfig constructor(val angle: Float, val scale: Float, val depth: Int, val parentBranchConfig: BranchConfig?){
//    fun createMutationDict
//}
class BranchConfig constructor(val lengthMut: Float, val angleMut: Float){
}

class DefaultConfig {
    val lengthScale = 0.65.toFloat()
    val lengthRandomDev = 0.1.toFloat()
    val angleDeviation = 20.toFloat()
    var angle: Float = 60.toFloat()
    val bgColor = Color.argb(255, 0, 0, 0)
    var depth: Int = 6
}

//actually much better would be to have a custom point class that handles all your access and stuff
class Point constructor(val x: Float, val y: Float) {
}

class Branch constructor(val startPoint: Point, private val endPoint: Point, val branchLen: Float, val angle: Float, val config: Config, val depth: Int){
    private lateinit var childrenList: List<Branch>
    val mutationConfig: BranchConfig = config.mutationHashMap.getValue(key=depth)

    fun extendBranch(startX: Float, startY: Float, endX: Float, endY: Float): Point{
//        val scale = config.lengthScale
        // getting value for mutation for branchdepth from config
        val scale = mutationConfig.lengthMut
        val newX = endX + ( ( endX - startX ) / branchLen ) * scale
        val newY = endY + ( ( endY - startY ) / branchLen ) * scale
        return Point(newX.toFloat(), newY.toFloat())
    }

    fun rotateChild_11(rotAngle: Float, newX: Float, newY: Float): Point{
        val angle = Math.toRadians(rotAngle.toDouble())
        val rotatedX = (newX - this.endPoint.x) * cos(angle.toDouble()) - ( newY - this.endPoint.y) * sin(angle.toDouble()) + this.endPoint.x
        val rotatedY = (newX - this.endPoint.x) * sin(angle.toDouble()) - ( newY - this.endPoint.y) * cos(angle.toDouble()) + this.endPoint.y
        return Point(rotatedX.toFloat(), rotatedY.toFloat())
    }

    private fun rotateChild(length: Float, angle: Float): Point{
        val finAngle = 360 - angle
        val yPos = length * sin(Math.toRadians(finAngle.toDouble()))
        val xPos = length * cos(Math.toRadians(finAngle.toDouble()))
        return Point(xPos.toFloat(), yPos.toFloat())
    }

    fun spawnChildren(){
        // so each child will have the same config and each of their children will have the same config, depending on the depth
        val childLen = branchLen * config.lengthScale
        val rotAngle = mutationConfig.angleMut
        val rightPoint = moveToOrigin(rotateChild(childLen, this.angle + rotAngle))
        val leftPoint = moveToOrigin(rotateChild(childLen, this.angle - rotAngle))
        val rightChild = Branch(
            endPoint,
            rightPoint,
            childLen,
            (angle + config.angle).toFloat(),
            config,
            depth + 1
        )
        val leftChild = Branch(
            endPoint,
            leftPoint,
            childLen,
            (angle - config.angle).toFloat(),
            config,
            depth + 1
        )
        childrenList = listOf(leftChild, rightChild)
    }

    private fun moveToOrigin(originalPoint: Point): Point{
        return Point(originalPoint.x + endPoint.x, originalPoint.y + endPoint.y)
    }

    fun drawLine(size: Int, angle: Int){
        val canvas = config.canvas
        val paint = config.linePaint
        val testChild = moveToOrigin(rotateChild(size.toFloat(), angle.toFloat()))
        canvas.drawLine(startPoint.x, startPoint.y, testChild.x, testChild.y, paint)
        canvas.drawCircle(testChild.x, testChild.y, 10.toFloat(), paint);
    }

    fun showBranch(){
        val canvas = config.canvas
        val paint = config.linePaint
        canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, paint)
        val leftChild = childrenList.elementAt(0)
        val rightChild = childrenList.elementAt(1)
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

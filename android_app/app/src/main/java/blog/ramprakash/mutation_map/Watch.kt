package blog.ramprakash.mutation_map

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class Watch constructor(val startPos: Point, val watchWidth: Int, val watchHeight: Int ) {
    lateinit var watchCanvas: Canvas
    private var branchList: MutableList<Branch> = mutableListOf()
    private lateinit var config: Config
    lateinit var baseBranch: Branch


    fun testFun(){
    }

    fun setupWatch(){
        if (this::config.isInitialized and this::watchCanvas.isInitialized){
            this.config = this.config.spawnChild(this.watchCanvas)
        }
        else{
            val defaultConfig: DefaultConfig = DefaultConfig()
            this.config = Config(this.watchCanvas, defaultConfig)
        }
    }

    fun lineHelper(): List<Point>{
        val lineStartX: Int = startPos.x.toInt() + watchWidth/2
        val lineStartY: Int = startPos.y.toInt() + watchHeight - watchHeight/3
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
            this.config,
            0
        )
        this.branchList = mutableListOf(firstBranch)
        this.baseBranch = firstBranch
    }

    fun showBranches(){
//        display branches and children
//        watchCanvas.drawRect(startPos.x, startPos.y, startPos.x + watchWidth, startPos.y + watchHeight, config.bgPaint)

//        for (branch in branchList){
//            branch.spawnChildren()
//            branch.showBranch()
//        }
        baseBranch.spawnChildren()
        baseBranch.showBranch()
        watchCanvas.drawRect(startPos.x, startPos.y, startPos.x + watchWidth - this.config.defaultConfig.borderSize, startPos.y + watchHeight - this.config.defaultConfig.borderSize, this.config.borderPaint)
    }

    fun show(){
        this.testFun()
        this.setupWatch()
        this.createBranches()
        this.showBranches()
    }
}

// TO-DO: decommission the angle, scale created for solid fractals
class Config constructor(val canvas: Canvas, val defaultConfig: DefaultConfig){

    //    var mutationMutableMap: MutableMap<String><Float>

//    val angle: Float, val scale: Float, val depth: Int, val parentBranchConfig: BranchConfig?
    var lengthScale = defaultConfig.lengthScale.toFloat()
    var lengthRandomDev = defaultConfig.lengthRandomDev.toFloat()
    var angleDeviation = defaultConfig.angleDeviation.toFloat()
    var colorDeviation = defaultConfig.colorDeviation.toFloat()
    var angle: Float = defaultConfig.angle.toFloat()
    var drawColor = defaultConfig.randomColor
    var bgColor = defaultConfig.bgColor
    var depth: Int = defaultConfig.depth

    var mutationHashMap: MutableMap<Int,BranchConfig> = generateMutations()

    var linePaint = convertToPaint(drawColor)
    var borderPaint = convertToBorderPaint(drawColor)
    var bgPaint = convertToPaint(bgColor)



    fun setup(config: Config){
        // Basically set everything up if a previous config is available
        // no actually this function is useless. We will write a spawn function which will spawn a child from itself
//        this.parentConfig = config
//        this.lengthScale = parentConfig.lengthScale
//        this.lengthRandomDev = parentConfig.lengthRandomDev
//        this.angleDeviation = parentConfig.angleDeviation
//        this.angle = parentConfig.angle
//        this.drawColor = parentConfig.drawColor
//        this.bgColor = parentConfig.bgColor
//        this.depth = parentConfig.depth
//
//        var linePaint = parentConfig.linePaint
//        var bgPaint = parentConfig.bgPaint
    }

    fun convertToBorderPaint(colorPoint: ARGBPoint): Paint{
        return Paint().apply {
            color = convertColor(colorPoint)
            strokeWidth = defaultConfig.borderSize.toFloat()
            style = Paint.Style.STROKE
        }
    }

    fun convertToPaint(colorPoint: ARGBPoint): Paint{
        return Paint().apply {
            color = convertColor(colorPoint)
            style = Paint.Style.FILL
            strokeWidth = 3.toFloat()
        }
    }

    fun convertColor(colorPoint: ARGBPoint): Int{
        return Color.argb(colorPoint.a, colorPoint.r, colorPoint.g, colorPoint.b)
    }


    fun generateRandomColor(previousARGB: ARGBPoint, newARGB: ARGBPoint): ARGBPoint {
        val aValue = 255
        val rValue = generateRandomRatio((previousARGB.r-newARGB.r).toFloat(), (previousARGB.r+newARGB.r).toFloat())
        val gValue = generateRandomRatio((previousARGB.g-newARGB.g).toFloat(), (previousARGB.g+newARGB.g).toFloat())
        val bValue = generateRandomRatio((previousARGB.b-newARGB.b).toFloat(), (previousARGB.b+newARGB.b).toFloat())
        val colorPoint = ARGBPoint(aValue, rValue.toInt(), gValue.toInt(), bValue.toInt())
        return colorPoint
    }

//    fun generateRandomBetweenZeroOne(): Float{
//        //generates a random  number between zero and one
//        val randomValue = Random.nextInt(100)
//        return (randomValue * 0.01).toFloat()
//    }
    fun generateRandomBetweenZeroOne(): Float{
        //generates a random  number between zero and one
        return Math.random().toFloat()
    }

    fun generateRandomRatio(startpos: Float, endpos: Float, bounceBackStart: Float = startpos, bounceBackEnd: Float = endpos): Float{
        // so apparently java random does not love 0. or anything rounded to 0. so we have to scale it to hundred perform calc and return
        // okay. so this function is not that secure, and does nothing useful
        // so rewriting the function to give a value between 0 and 1. thats it then we scale it however we want
        val rangeval = endpos-startpos
        val randomScale = generateRandomBetweenZeroOne()
        val randomValue = rangeval * randomScale
        val finalRandom = randomValue + startpos
        val returnRandom: Float
        if (finalRandom < bounceBackStart){
            returnRandom = bounceBackStart + (bounceBackStart - finalRandom)
        }
        else if (finalRandom > bounceBackEnd){
            returnRandom = bounceBackEnd - (finalRandom - bounceBackEnd)
        }
        else{
            returnRandom = finalRandom
        }
        return (returnRandom).toFloat()
    }


    fun generateMutationsFromParent(angleMutation: Float, lengthMutation: Float): BranchConfig{
        val genMutationBranch = BranchConfig(generateMutatedLengthRatio(lengthMutation), generateMuatatedAngle(angleMutation))
        return genMutationBranch
    }

    fun generateMuatatedColor(currentColor: ARGBPoint): ARGBPoint{
        //TO-DO: maybe replace this with a better config for deviation specific spectrum only like r g or b
        val mutatedR = generateRandomRatio(currentColor.r-this.colorDeviation , currentColor.r+this.colorDeviation, defaultConfig.colorBounceBackStart, defaultConfig.colorBounceBackEnd)
        val mutatedG = generateRandomRatio(currentColor.g-this.colorDeviation , currentColor.g+this.colorDeviation, defaultConfig.colorBounceBackStart, defaultConfig.colorBounceBackEnd)
        val mutatedB = generateRandomRatio(currentColor.b-this.colorDeviation , currentColor.b+this.colorDeviation, defaultConfig.colorBounceBackStart, defaultConfig.colorBounceBackEnd)
        return ARGBPoint(currentColor.a, mutatedR.toInt(), mutatedG.toInt(), mutatedB.toInt())
    }

    fun generateMuatatedAngle(currentAngleDeviation: Float): Float{
        val mutationAngleFloat = generateRandomRatio(currentAngleDeviation-this.angleDeviation , currentAngleDeviation+this.angleDeviation)
        return mutationAngleFloat
    }

    fun generateMutatedLengthRatio(currentLengthDeviation: Float): Float{
//        val mutationLenFloat = generateRandomRatio(currentLengthDeviation-this.lengthRandomDev , currentLengthDeviation+this.lengthRandomDev)
        //discarding this logic in favour of sending just the ratio and the branch will take care of the scaling
//        val mutationLenFloat = generateRandomRatio(currentLengthDeviation-this.lengthRandomDev , currentLengthDeviation+this.lengthRandomDev)
        val mutationLenFloat = generateRandomRatio(currentLengthDeviation-this.lengthRandomDev , currentLengthDeviation+this.lengthRandomDev, this.defaultConfig.minLength, this.defaultConfig.maxLength)
        return mutationLenFloat
    }

    fun generateDeviation(parentHashMap: MutableMap<Int,BranchConfig>):  MutableMap<Int,BranchConfig>{
        for ((key, value) in parentHashMap){
            val parentAngleDeviation = value.angleMut
            val parentLengthDeviation = value.lengthMut
            mutationHashMap[key] = generateMutationsFromParent(parentAngleDeviation, parentLengthDeviation)
        }
        return mutationHashMap
    }

    fun generateMutations(): MutableMap<Int,BranchConfig> {
        val newMutationHashMap: MutableMap<Int,BranchConfig> = mutableMapOf()
        for (i in 0..depth+1){
            val genMutationBranch = BranchConfig(generateMutatedLengthRatio(this.lengthScale), generateMuatatedAngle(this.angle))
            newMutationHashMap[i] = genMutationBranch
        }
        return newMutationHashMap
    }

//    fun generateMutations(): MutableMap<Int,BranchConfig>{
//        // basically the same function. Move it to the same or think of a better solution
//        for (i in 0..depth+1){
//            val genMutationBranch = BranchConfig(generateMutatedLengthRatio(this.angle), generateMuatatedAngle(this.lengthScale))
//            mutationHashMap[i] = genMutationBranch
//        }
//    }

    fun spawnChild(spawnCanvas: Canvas): Config{
        // Basically set everything up if a previous config is available
        // no actually this function is useless. We will write a spawn function which will spawn a child from itself
        val childConfig = Config(spawnCanvas, defaultConfig)
//        childConfig.lengthScale = this.lengthScale
        childConfig.lengthRandomDev = this.lengthRandomDev
        childConfig.angleDeviation = this.angleDeviation
//        childConfig.angle = this.angle
        val mutatedDrawcolor = generateMuatatedColor(this.drawColor)
        childConfig.drawColor = mutatedDrawcolor
        childConfig.borderPaint = convertToBorderPaint(mutatedDrawcolor)
        childConfig.bgColor = this.bgColor
        childConfig.depth = this.depth

        childConfig.linePaint = convertToPaint(childConfig.drawColor)
        childConfig.bgPaint = this.bgPaint
        childConfig.mutationHashMap = generateDeviation(this.mutationHashMap)
        return childConfig

    }
    // here, we will be creating a map which will contain the mutation for all the branches and the children will use the map as reference

}

//class BranchConfig constructor(val angle: Float, val scale: Float, val depth: Int, val parentBranchConfig: BranchConfig?){
//    fun createMutationDict
//}
class BranchConfig constructor(val lengthMut: Float, val angleMut: Float){
}

class DefaultConfig {
    var mutationHashMap: MutableMap<Int,BranchConfig> = mutableMapOf()
    val lengthScale = 0.65.toFloat()
    val minLength = 0.4.toFloat()
    val maxLength = 0.9.toFloat()
    val lengthRandomDev = 0.08.toFloat()
    val angleDeviation = 20.toFloat()
    var angle: Float = 60.toFloat()
    var colorDeviation: Float = 10.toFloat()
    val colorBounceBackStart: Float = 40.toFloat()
    val colorBounceBackEnd: Float = 255.toFloat()
    val bgColPoint = ARGBPoint(255, 0, 0, 0)
    val randomColor = generateRandomColor()
    val defaultColorDev = ARGBPoint(255, 10, 10, 10)
    val bgColor = bgColPoint
    var depth: Int = 6
    val borderSize: Int = 5
    // TO-DO: create a max len to which the branch can grow

    fun generateRandomBetweenZeroOne(): Float{
        //generates a random  number between zero and one
        return Math.random().toFloat()
    }

    fun generateRandomColor(): ARGBPoint {
        val rRandom = generateRandomBetweenZeroOne()
        val gRandom = generateRandomBetweenZeroOne()
        val bRandom = generateRandomBetweenZeroOne()
        val rValue = (255 * rRandom).toInt()
        val gValue = (255 * gRandom).toInt()
        val bValue = (255 * bRandom).toInt()
        return ARGBPoint(255, rValue, gValue, bValue)
    }

}

class ARGBPoint constructor(val a: Int, val r: Int, val g: Int, val b: Int){
    //TO-DO: convert this to a sealed class or something so that both int and float can be used
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
        val childLen = branchLen * mutationConfig.lengthMut
        val rotAngle = mutationConfig.angleMut
        val rightPoint = moveToOrigin(rotateChild(childLen, this.angle + rotAngle))
        val leftPoint = moveToOrigin(rotateChild(childLen, this.angle - rotAngle))
        val rightChild = Branch(
            endPoint,
            rightPoint,
            childLen,
            (angle + rotAngle).toFloat(),
            config,
            depth + 1
        )
        val leftChild = Branch(
            endPoint,
            leftPoint,
            childLen,
            (angle - rotAngle).toFloat(),
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
        if (depth != 0){
            canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, paint)
        }
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

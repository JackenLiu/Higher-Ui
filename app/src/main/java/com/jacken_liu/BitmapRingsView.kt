package com.jacken_liu

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import kotlin.math.min

open class BitmapRingsView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    /**
     * 画中心圆的
     */
    private val centerPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 模拟的取样图
     */
    private val centerBitmap: Bitmap = BitmapFactory.decodeResource(
            getContext().resources,
            R.drawable.test1)

    /**
     * 圆的中心坐标
     */
    private var centerX: Float = 0.0f
    private var centerY: Float = 0.0f

    /**
     * 下面画环参数的默认值
     */
    private var diameterDiffDefaultValue = 12f
    private var outsideDiameterDefaultValue = 148f
    private var diameterAddSubSpeedDefaultValue = 5f
    private var diameterDiffSubSpeedDefaultValue = 0.15f
    private var ringAlphaSubSpeedDefaultValue = 3.3f

    // 相对速度,越大越快
//    private var speedScale = 1.0f
    private var speedScale = 0.3f

    /**
     * 环间距
     */
    private var ringDistance = 51.5

    /**
     * 环的动态参数
     */

    // 两圆半径相差动态改变的值
    private var diameterDiffSubSpeed = diameterDiffSubSpeedDefaultValue

    // 内外圈圆半径增加值（都在扩大），动态改变实现环半径越来越大
    private var diameterAddSubSpeed = diameterAddSubSpeedDefaultValue

    private var ringAlphaSubSpeed = ringAlphaSubSpeedDefaultValue

    /**
     * 四个圆环对象
     */
    private val ringOne = Ring()
    private val ringTwo = Ring()
    private val ringThree = Ring()
    private val ringFour = Ring()
    private val ringFive = Ring()
    private val ringList = listOf(ringOne, ringTwo, ringThree, ringFour, ringFive)

    /**
     * 是否开始画顺序的环
     */
    private var isStartTwo = false
    private var isStartThree = false
    private var isStartFour = false
    private var isStartFive = false

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = width.toFloat() / 2
        centerY = height.toFloat() / 2
    }

    open fun startAnimate() {
        for (ring in ringList) {
            ring.initData()
        }

        isStartTwo = false
        isStartThree = false
        isStartFour = false
        isStartFive = false

        invalidate()
    }

    open fun stopAnimate() {
        for (ring in ringList) {
            ring.isStop = true
        }
    }

    open fun isStopAnimate(): Boolean {
        return ringOne.isStop || ringTwo.isStop || ringThree.isStop || ringFour.isStop || ringFive.isStop
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 第一个环
        ringOne.blackPaint.color = Color.YELLOW
        drawRing(ringOne, canvas)

        /**
         * 记录要画剩下的环的标识
         */
        if (ringOne.outsideDiameter > outsideDiameterDefaultValue + ringDistance)
            isStartTwo = true

        if (ringOne.outsideDiameter > outsideDiameterDefaultValue + ringDistance * 2)
            isStartThree = true

        if (ringOne.outsideDiameter > outsideDiameterDefaultValue + ringDistance * 3)
            isStartFour = true

        if (ringOne.outsideDiameter > outsideDiameterDefaultValue + ringDistance * 4)
            isStartFive = true

        /**
         * 开始画剩下的环
         */
        if (isStartTwo) {
            ringTwo.blackPaint.color = Color.RED
            drawRing(ringTwo, canvas)
        }
        if (isStartThree) {
            ringThree.blackPaint.color = Color.GREEN
            drawRing(ringThree, canvas)
        }
        if (isStartFour) {
            ringFour.blackPaint.color = Color.WHITE
            drawRing(ringFour, canvas)
        }
        if (isStartFive) {
            ringFive.blackPaint.color = Color.BLUE
            drawRing(ringFive, canvas)
        }

        if (!ringOne.isStop || ringTwo.isStop || ringThree.isStop || ringFour.isStop || ringFive.isStop)
            invalidate()
    }

    /**
     * 画圆形 Bitmap
     */
    private fun drawCircle(bitmap: Bitmap, radius: Float): Bitmap {
        val w = bitmap.width
        val h = bitmap.height
        val r = min(w, h).toFloat()

        // 获取正方形
        val newBitmap = Bitmap.createBitmap(r.toInt(), r.toInt(), Bitmap.Config.ARGB_8888)

        val canvas = Canvas(newBitmap)
        canvas.setBitmap(newBitmap)

        val bitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        centerPaint.shader = bitmapShader

        canvas.drawCircle(r / 2, r / 2, r / 2, centerPaint)
        return scaleBitmap(newBitmap, radius)
    }

    /**
     * Bitmap 等比压缩
     */
    private fun scaleBitmap(bitmap: Bitmap, radius: Float): Bitmap {
        val w = bitmap.width
        val h = bitmap.height

        val scaleW = radius / w
        val scaleH = radius / h

        val matrix = Matrix()
        matrix.postScale(scaleW, scaleH)

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun drawRing(ring: Ring, canvas: Canvas) {

        // 数据恢复
        if (ring.outsideDiameter >= width - 2) {
            // 如果画完了一个周期需要停止则不再画当前圆
            if (ring.isStop) {
                return
            }

            ring.outsideDiameter = outsideDiameterDefaultValue
            ring.insideDiameter = ring.outsideDiameter - diameterDiffDefaultValue
            ring.diameterDiff = diameterDiffDefaultValue
            ring.alpha = 255f
        }

        ring.paint.alpha = ring.alpha.toInt()
        canvas.drawBitmap(drawCircle(centerBitmap, ring.outsideDiameter),
                centerX - ring.outsideDiameter / 2,
                centerY - ring.outsideDiameter / 2, ring.paint)

        ring.blackPaint.alpha = 100
        canvas.drawCircle(centerX, centerY, ring.insideDiameter / 2, ring.blackPaint)

        // 中心圆
        canvas.drawBitmap(drawCircle(centerBitmap, 128f),
                centerX - 64, centerY - 64, centerPaint)

        // 数据动态改变
        ring.outsideDiameter += diameterAddSubSpeed * speedScale
        ring.insideDiameter += diameterAddSubSpeed * speedScale
        ring.diameterDiff -= diameterDiffSubSpeed * speedScale
        ring.insideDiameter = ring.outsideDiameter - ring.diameterDiff
        ring.alpha -= ringAlphaSubSpeed * speedScale
    }

    /**
     * 圆环对象
     * ！！ 动态情况下的参数必须写在对象里面独立起来
     */
    class Ring {

        /**
         * 圆环宽度
         * 即组成圆环的两个圆的半径差
         */
        var diameterDiff: Float = 12f

        /**
         * 环的外圆的直接
         */
        var outsideDiameter: Float = 138f

        /**
         * 圆的半径
         * 圆环的圆心到圆环宽度一半的距离
         */
        var insideDiameter: Float = outsideDiameter - diameterDiff

        /**
         * 透明度
         */
        var alpha = 255f

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val blackPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        /**
         * 准备停止绘制当前环
         */
        var isStop = false

        fun initData() {
            diameterDiff = 12f
            outsideDiameter = 138f
            insideDiameter = outsideDiameter - diameterDiff
            alpha = 255f
            isStop = false
        }
    }
}
package com.yunbao.live.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import cn.qqtheme.framework.util.ScreenUtils
import com.yunbao.common.bean.FootballLineup
import com.yunbao.live.R
import kotlin.math.max


/**
 * Copyright：Rapoo
 * Created by Swan on 2021/6/6 10:25
 * @package: com.yunbao.live.views
 * Description：TODO
 */
class LineUpView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val NUMBER_SIZE = 24
        private const val TEXT_SIZE = 24f
    }

    private var venueBg: Drawable? = null
    private var drawableMatrix: Matrix? = null
    private var scaleSize: Float = 0F
    private var lineup: FootballLineup.Lineups? = null
    private var homeName: String? = null
    private var awayName: String? = null

    private val formationPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.WHITE
            alpha = 125
        }
    }
    private val namePaint = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
        textSize = TEXT_SIZE
    }

    private val textOffset: Float

    init {
        val ascent = namePaint.ascent();
        val descent = namePaint.descent();

        //偏移量，用于辅助文字居中
        textOffset = (ascent + descent) / 2
        val array = context.obtainStyledAttributes(attrs, R.styleable.LineUpView)
        venueBg = array.getDrawable(R.styleable.LineUpView_venueSrc)
        venueBg?.let {
            it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
        }
        array.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measureWidth(widthMeasureSpec)
        val height = measureHeight(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        configureDrawable()
        canvas.save()
        if (drawableMatrix != null) {
            canvas.concat(drawableMatrix)
        }
        venueBg?.draw(canvas)
        canvas.restore()
        lineup?.let {

            it.home?.forEach { player ->
                drawHomePosition(canvas, player)
            }
            it.away.forEach { player ->
                drawAwayPosition(canvas, player)
            }
        }
    }

    private fun configureDrawable() {
        venueBg?.let {
            if (it.intrinsicWidth != measuredWidth) {
                val postScaleSize = measuredWidth * 1F / it.intrinsicWidth
                if (postScaleSize != scaleSize || drawableMatrix == null) {
                    this.scaleSize = postScaleSize
                    if (drawableMatrix == null) {
                        drawableMatrix = Matrix().apply {
                            postScale(scaleSize, scaleSize)
                        }
                    }
                }
            }
        }
    }

    private fun measureWidth(measureSpec: Int): Int {
        val defaultWidth = venueBg?.intrinsicWidth ?: 0
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        Log.e("LineUpView", "measureWidth---speSize = $specSize")
        return when (specMode) {
            MeasureSpec.AT_MOST -> defaultWidth
            MeasureSpec.EXACTLY -> {
                if (defaultWidth != 0) {
                    scaleSize = specSize.toFloat() / defaultWidth
                }
                specSize
            }
            else -> {
                val width = max(defaultWidth, specSize)
                if (defaultWidth != 0) {
                    scaleSize = width.toFloat() / defaultWidth
                }
                width
            }
        }
    }

    private fun measureHeight(measureSpec: Int): Int {
        val defaultHeight = venueBg?.intrinsicHeight ?: 0
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        Log.e("LineUpView", "measureHeight---speSize = $specSize")
        return when (specMode) {
            MeasureSpec.AT_MOST -> if (scaleSize != 0F) (defaultHeight * scaleSize).toInt() else defaultHeight
            MeasureSpec.EXACTLY -> specSize
            else -> {
                if (scaleSize != 0F) (defaultHeight * scaleSize).toInt() else defaultHeight
            }
        }
    }

    fun playPosition(lineup: FootballLineup.Lineups, home: String?, away: String?) {
        this.lineup = lineup
        this.homeName = home
        this.awayName = away
        invalidate()
    }

    /**
     * 主队坐标原点：左上；即：x轴方向向右，y轴方向向下；
     */
    private fun drawHomePosition(canvas: Canvas, player: FootballLineup.Player) {
        if (player.x == 0 && player.y == 0) {
            return
        }
        if (player.first == 0) {
            return
        }
        if (venueBg == null) return
        val venueWidth = measuredWidth
        val venueHeight = measuredHeight
        val realX = player.x / 100F * venueWidth
        val realY = player.y / 100F * venueHeight / 2
        drawPlayerNumber(canvas, Color.BLUE, player.shirtNumber.toString(), realX, realY)
        val nameWidth = namePaint.measureText(player.name, 0, player.name.length)
        canvas.drawText(player.name, realX - nameWidth / 2, realY + TEXT_SIZE * 2 - textOffset, namePaint)
    }

    private fun drawPlayerNumber(canvas: Canvas, color: Int, number: String, x: Float, y: Float) {
        formationPaint.textSize = TEXT_SIZE
        formationPaint.alpha = 255
        formationPaint.color = Color.WHITE
        canvas.drawCircle(x, y, NUMBER_SIZE.toFloat(), formationPaint)
        formationPaint.color = color
        canvas.drawCircle(x, y, (NUMBER_SIZE - 2).toFloat(), formationPaint)
        val length = namePaint.measureText(number, 0, number.length)
        canvas.drawText(number, x - length / 2, y - textOffset, namePaint)
    }

    /**
     * 客队坐标原点：右下；即：x轴方向向左，y轴方向向上。
     */
    private fun drawAwayPosition(canvas: Canvas, player: FootballLineup.Player) {
        if (player.x == 0 && player.y == 0) {
            return
        }
        if (venueBg == null) return
        val venueWidth = measuredWidth
        val venueHeight = measuredHeight
        val realX = (1 - player.x / 100F) * venueWidth
        val realY = venueHeight - player.y / 100F * venueHeight / 2
        drawPlayerNumber(canvas, Color.RED, player.shirtNumber.toString(), realX, realY)
        val nameWidth = namePaint.measureText(player.name, 0, player.name.length)
        canvas.drawText(player.name, realX - nameWidth / 2, realY + TEXT_SIZE * 2 - textOffset, namePaint)
    }
}
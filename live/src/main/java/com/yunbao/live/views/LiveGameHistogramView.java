package com.yunbao.live.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yunbao.common.utils.DpUtil;
import com.yunbao.live.R;
import com.yunbao.live.bean.MultiGroupHistogramChildData;
import com.yunbao.live.bean.MultiGroupHistogramGroupData;

import java.util.List;

/**
 * @author Juwan
 * @date 2020/9/24
 * Description:
 */
public class LiveGameHistogramView extends View {
    private int width;
    private int height;
    // 坐标轴线宽度
    private int coordinateAxisWidth;

    // 组名称字体大小
    private int groupNameTextSize;
    // 小组之间间距
    private int groupInterval;
    // 组内子直方图间距
    private int histogramInterval;
    private int histogramValueTextSize;
    private int histogramHistogramWidth;
    private int chartPaddingTop;
    private int histogramPaddingStart;
    private int histogramPaddingEnd;
    // 各组名称到X轴的距离
    private int distanceFormGroupNameToAxis;
    // 直方图上方数值到直方图的距离
    private int distanceFromValueToHistogram;

    // 直方图最大高度
    private int maxHistogramHeight;
    // 轴线画笔
    private Paint coordinateAxisPaint;
    // 组名画笔
    private Paint groupNamePaint;
    private Paint.FontMetrics groupNameFontMetrics;
    private Paint.FontMetrics histogramValueFontMetrics;
    // 直方图数值画笔
    private Paint histogramValuePaint;
    // 直方图画笔
    private Paint histogramPaint;

    private Paint semicirclePaint;
    // 直方图绘制区域
    private Rect histogramPaintRect;
    // 直方图表视图总宽度
    private int histogramContentWidth;
    // 存储组内直方图shader color，例如，每组有3个直方图，该SparseArray就存储3个相对应的shader color
    private SparseArray<int[]> histogramShaderColorArray;

    private List<MultiGroupHistogramGroupData> dataList;
    private SparseArray<Integer> childMaxValueArray;

    private Scroller scroller;
    private int minimumVelocity;
    private int maximumVelocity;
    private VelocityTracker velocityTracker;

    public LiveGameHistogramView(Context context) {
        this(context, null);
    }

    public LiveGameHistogramView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LiveGameHistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LiveGameHistogramView);
        coordinateAxisWidth = typedArray.getDimensionPixelSize(R.styleable.LiveGameHistogramView_coordinateAxisWidth, DpUtil.dp2px(2));
        // 坐标轴线颜色
        int coordinateAxisColor = typedArray.getColor(R.styleable.LiveGameHistogramView_coordinateAxisColor, Color.parseColor("#434343"));
        // 底部小组名称字体颜色
        int groupNameTextColor = typedArray.getColor(R.styleable.LiveGameHistogramView_groupNameTextColor,Color.parseColor("#2B2626"));
        groupNameTextSize = typedArray.getDimensionPixelSize(R.styleable.LiveGameHistogramView_groupNameTextSize, DpUtil.dp2px(12));
        groupInterval = typedArray.getDimensionPixelSize(R.styleable.LiveGameHistogramView_groupInterval, DpUtil.dp2px(55));
        histogramInterval = typedArray.getDimensionPixelSize(R.styleable.LiveGameHistogramView_histogramInterval, DpUtil.dp2px(8));
        // 直方图数值文本颜色
        int histogramValueTextColor = typedArray.getColor(R.styleable.LiveGameHistogramView_histogramValueTextColor, Color.parseColor("#CC202332"));
        histogramValueTextSize = typedArray.getDimensionPixelSize(R.styleable.LiveGameHistogramView_histogramValueTextSize, DpUtil.dp2px(12));
        histogramHistogramWidth = typedArray.getDimensionPixelSize(R.styleable.LiveGameHistogramView_histogramHistogramWidth, DpUtil.dp2px(10));
        chartPaddingTop = typedArray.getDimensionPixelSize(R.styleable.LiveGameHistogramView_chartPaddingTop, DpUtil.dp2px(10));
        histogramPaddingStart = typedArray.getDimensionPixelSize(R.styleable.LiveGameHistogramView_histogramPaddingStart, DpUtil.dp2px(40));
        histogramPaddingEnd = typedArray.getDimensionPixelSize(R.styleable.LiveGameHistogramView_histogramPaddingEnd, DpUtil.dp2px(30));
        distanceFormGroupNameToAxis = typedArray.getDimensionPixelSize(R.styleable.LiveGameHistogramView_distanceFormGroupNameToAxis, DpUtil.dp2px(15));
        distanceFromValueToHistogram = typedArray.getDimensionPixelSize(R.styleable.LiveGameHistogramView_distanceFromValueToHistogram, DpUtil.dp2px(10));
        typedArray.recycle();

        coordinateAxisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        coordinateAxisPaint.setStyle(Paint.Style.FILL);
        coordinateAxisPaint.setStrokeWidth(coordinateAxisWidth);
        coordinateAxisPaint.setColor(coordinateAxisColor);

        groupNamePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        groupNamePaint.setTextSize(groupNameTextSize);
        groupNamePaint.setColor(groupNameTextColor);
        groupNameFontMetrics = groupNamePaint.getFontMetrics();

        histogramValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        histogramValuePaint.setTextSize(histogramValueTextSize);
        histogramValuePaint.setColor(histogramValueTextColor);
        histogramValueFontMetrics = histogramValuePaint.getFontMetrics();

        semicirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        semicirclePaint.setStrokeWidth(5);
        semicirclePaint.setAntiAlias(true);//取消锯齿
        semicirclePaint.setColor(histogramValueTextColor);
        semicirclePaint.setStyle(Paint.Style.FILL);

        histogramPaintRect = new Rect();
        histogramPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scroller = new Scroller(getContext(), new LinearInterpolator());
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        minimumVelocity = configuration.getScaledMinimumFlingVelocity();
        maximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        maxHistogramHeight = height - groupNameTextSize - coordinateAxisWidth - distanceFormGroupNameToAxis - distanceFromValueToHistogram - histogramValueTextSize - chartPaddingTop;
    }

    /**
     * 判断是否可以水平滑动
     * @param direction 标识滑动方向 正数：右滑(手指从右至左移动)；负数：左滑(手指由左向右移动)
     * 您可参考ScaollView或HorizontalScrollView理解滑动方向
     */
    @Override
    public boolean canScrollHorizontally(int direction) {
        if (direction > 0) {
            return histogramContentWidth - getScrollX() - width + histogramPaddingStart + histogramPaddingEnd > 0;
        } else {
            return getScrollX() > 0;
        }
    }

    /**
     * 根据滑动方向获取最大可滑动距离
     * @param direction 标识滑动方向 正数：右滑(手指从右至左移动)；负数：左滑(手指由左向右移动)
     * 您可参考ScaollView或HorizontalScrollView理解滑动方向
     */
    private int getMaxCanScrollX(int direction) {
        if (direction > 0) {
            return histogramContentWidth - getScrollX() - width + histogramPaddingStart + histogramPaddingEnd > 0 ?
                    histogramContentWidth - getScrollX() - width + histogramPaddingStart + histogramPaddingEnd : 0;
        } else if (direction < 0) {
            return getScrollX();
        }
        return 0;
    }

    private float lastX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTrackerIfNotExists();
        velocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                lastX = event.getX();
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                int deltaX = (int) (event.getX() - lastX);
                lastX = event.getX();
                // 滑动处理
                if (deltaX > 0 && canScrollHorizontally(-1)) {
                    scrollBy(-Math.min(getMaxCanScrollX(-1), deltaX), 0);
                } else if (deltaX < 0 && canScrollHorizontally(1)) {
                    scrollBy(Math.min(getMaxCanScrollX(1), -deltaX), 0);
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                velocityTracker.computeCurrentVelocity(1000, maximumVelocity);
                int velocityX = (int) velocityTracker.getXVelocity();
                fling(velocityX);
                recycleVelocityTracker();
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                recycleVelocityTracker();
                break;
            }
        }
        return super.onTouchEvent(event);
    }

    private void initVelocityTrackerIfNotExists() {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    // ACTION_UP事件触发
    private void fling(int velocityX) {
        if (Math.abs(velocityX) > minimumVelocity) {
            if (Math.abs(velocityX) > maximumVelocity) {
                velocityX = maximumVelocity * velocityX / Math.abs(velocityX);
            }
            scroller.fling(getScrollX(), getScrollY(), -velocityX, 0, 0, histogramContentWidth + histogramPaddingStart - width, 0, 0);
        }
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), 0);
        }
    }

    public void setDataList(@NonNull List<MultiGroupHistogramGroupData> dataList) {
        this.dataList = dataList;
        if (childMaxValueArray == null) {
            childMaxValueArray = new SparseArray<>();
        } else {
            childMaxValueArray.clear();
        }
        histogramContentWidth = 0;
        for (MultiGroupHistogramGroupData groupData : dataList) {
            List<MultiGroupHistogramChildData> childDataList = groupData.getChildDataList();
            if (childDataList != null && childDataList.size() > 0) {
                for (int i = 0; i < childDataList.size(); i++) {
                    histogramContentWidth += histogramHistogramWidth + histogramInterval;
                    MultiGroupHistogramChildData childData = childDataList.get(i);
                    int childMaxValue = childMaxValueArray.get(i);
                    if (childMaxValue < childData.getValue()) {
//                    if (childMaxValue == null || childMaxValue < childData.getValue()) {
                        childMaxValueArray.put(i, childData.getValue());
                    }
                }
                histogramContentWidth += groupInterval - histogramInterval;
            }
        }
        histogramContentWidth += -groupInterval;
    }

    /**
     * 设置组内直方图颜色(并不是设置所有直方图颜色，而是根据每组数据内直方图数量设置)
     */
    public void setHistogramColor(int[]... colors) {
        if (colors != null && colors.length > 0) {
            if (histogramShaderColorArray == null) {
                histogramShaderColorArray = new SparseArray<>();
            } else {
                histogramShaderColorArray.clear();
            }
            for (int i = 0; i < colors.length; i++) {
                histogramShaderColorArray.put(i, colors[i]);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (width == 0 || height == 0) {
            return;
        }
        if (dataList != null && dataList.size() > 0) {
            int xAxisOffset = histogramPaddingStart; // 每个直方图在x轴的偏移量
            for (MultiGroupHistogramGroupData groupData : dataList) {
                List<MultiGroupHistogramChildData> childDataList = groupData.getChildDataList();
                if (childDataList != null && childDataList.size() > 0) {
                    int groupWidth = 0;
                    for (int i = 0; i < childDataList.size(); i++) {
                        MultiGroupHistogramChildData childData = childDataList.get(i);
                        histogramPaintRect.left = xAxisOffset;
                        histogramPaintRect.right = histogramPaintRect.left + histogramHistogramWidth;
                        int childHistogramHeight;
                        if (childData.getValue() <= 0 || childMaxValueArray.get(i) <= 0) {
                            childHistogramHeight = 0;
                        } else {
                            childHistogramHeight = (int) (childData.getValue() / childMaxValueArray.get(i) * maxHistogramHeight);
                        }
                        histogramPaintRect.top = height - childHistogramHeight - coordinateAxisWidth - distanceFormGroupNameToAxis - groupNameTextSize;
                        histogramPaintRect.bottom = histogramPaintRect.top + childHistogramHeight;
                        int[] histogramShaderColor = histogramShaderColorArray.get(i);
//                        LinearGradient shader = null;
//                        if (histogramShaderColor != null && histogramShaderColor.length > 0) {
//                            shader = getHistogramShader(histogramPaintRect.left, chartPaddingTop + distanceFromValueToHistogram + histogramValueTextSize,
//                                    histogramPaintRect.right, histogramPaintRect.bottom, histogramShaderColor);
//                        }
                        int color = histogramShaderColor[0];

                        histogramPaint.setColor(color);
                        canvas.drawRect(histogramPaintRect, histogramPaint);

                        semicirclePaint.setColor(color);
                        @SuppressLint("DrawAllocation") RectF rect=new RectF(histogramPaintRect.left, histogramPaintRect.top - histogramHistogramWidth/2, histogramPaintRect.right, histogramPaintRect.top + histogramHistogramWidth/2);//确定外切矩形范围
                        canvas.drawArc(rect, 0, -180, false, semicirclePaint);//绘制圆弧，不含圆心

                        int childValue = (int) childData.getValue();
                        String childHistogramHeightValue = childValue+"";

                        float valueTextX = xAxisOffset + (histogramHistogramWidth - histogramValuePaint.measureText(childHistogramHeightValue)) / 2;
                        // 数值绘制Y轴位置特别处理
                        float valueTextY = histogramPaintRect.top - distanceFormGroupNameToAxis + (histogramValueFontMetrics.bottom) / 2;

                        histogramValuePaint.setColor(color);
                        canvas.drawText(childHistogramHeightValue, valueTextX, valueTextY, histogramValuePaint);
                        int deltaX = i < childDataList.size() - 1 ? histogramHistogramWidth + histogramInterval : histogramHistogramWidth;
                        groupWidth += deltaX;
                        // 注意此处偏移量累加
                        xAxisOffset += i == childDataList.size() - 1 ? deltaX + groupInterval : deltaX;
                    }
                    String groupName = groupData.getGroupName();

                    float groupNameTextWidth = groupNamePaint.measureText(groupName);
                    float groupNameTextX = xAxisOffset - groupWidth - groupInterval + (groupWidth - groupNameTextWidth) / 2;
                    // 组名绘制Y轴位置特别处理
                    float groupNameTextY = histogramPaintRect.bottom  + DpUtil.dp2px(15);
                    canvas.drawText(groupName, groupNameTextX, groupNameTextY, groupNamePaint);
                }
            }
        }
    }

    private LinearGradient getHistogramShader(float x0, float y0, float x1, float y1, int[] colors) {
        return new LinearGradient(x0, y0, x1, y1, colors, null, Shader.TileMode.CLAMP);
    }
}

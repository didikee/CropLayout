package com.github.croper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.github.croper.util.PaintUtil;


/**
 * user author: didikee
 * create time: 2019-07-30 11:15
 * description: 
 */
public class AspectRatioView extends View {
    private int mAspectRatioX = 0;
    private int mAspectRatioY = 0;
    private Paint mPaint;
    private RectF drawRectF;
    private float rectRadius;
    private int mColor;

    public AspectRatioView(Context context) {
        super(context);
        init();
    }

    public AspectRatioView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AspectRatioView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(PaintUtil.dp2px(getContext(), 2));
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        drawRectF = new RectF();
        this.rectRadius = PaintUtil.dp2px(getContext(), 2);
    }

    public void setAspectRatio(int x, int y) {
        this.mAspectRatioX = x;
        this.mAspectRatioY = y;
    }

    public void setColor(int color) {
        this.mColor = color;
        mPaint.setColor(color);
    }

    public void setRectRadius(float rectRadius) {
        this.rectRadius = rectRadius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        float size = Math.min(width, height) * 0.75f;
        boolean aspect = mAspectRatioX > 0 && mAspectRatioY > 0;

        // 如果是0，那么以1：1来显示
        int ratioX = mAspectRatioX;
        int ratioY = mAspectRatioY;
        if (!aspect) {
            ratioX = 1;
            ratioY = 1;
        }

        float base = Math.max(ratioX, ratioY);
        float x = size * (ratioX / base);
        float y = size * (ratioY / base);
        drawRectF.set(0, 0, x, y);
        drawRectF.offset((width - x) / 2, (height - y) / 2);
        //画矩形
        if (aspect) {
            //画矩形
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mColor);
            canvas.drawRoundRect(drawRectF, rectRadius, rectRadius, mPaint);
        } else {
            // 画free crop
            drawFreeCrop(canvas, drawRectF);
        }
//        if (!aspect) {
//            mPaint.setStyle(Paint.Style.FILL);
//            mPaint.setColor(Color.GRAY);
//            float centerSize = (drawRectF.width() - (drawRectF.width() * 0.618f)) / 2;
//            canvas.drawRect(drawRectF.left + centerSize, drawRectF.top, drawRectF.right - centerSize, drawRectF.bottom, mPaint);
//            canvas.drawRect(drawRectF.left, drawRectF.top + centerSize, drawRectF.right, drawRectF.bottom - centerSize, mPaint);
//        }
        // drawFreeCrop(canvas, drawRectF);
    }

    private void drawFreeCrop(Canvas canvas, RectF rectF) {
        if (canvas == null || rectF == null) {
            return;
        }
        // left-top
        float length = Math.min(rectF.width(), rectF.height()) * 0.2f;
        canvas.drawLine(rectF.left, rectF.top, rectF.left + length, rectF.top, mPaint);
        canvas.drawLine(rectF.left, rectF.top, rectF.left, rectF.top + length, mPaint);
        // right-top
        canvas.drawLine(rectF.right, rectF.top, rectF.right - length, rectF.top, mPaint);
        canvas.drawLine(rectF.right, rectF.top, rectF.right, rectF.top + length, mPaint);
        // bottom-left
        canvas.drawLine(rectF.left, rectF.bottom, rectF.left, rectF.bottom - length, mPaint);
        canvas.drawLine(rectF.left, rectF.bottom, rectF.left + length, rectF.bottom, mPaint);
        // bottom-right
        canvas.drawLine(rectF.right, rectF.bottom, rectF.right - length, rectF.bottom, mPaint);
        canvas.drawLine(rectF.right, rectF.bottom, rectF.right, rectF.bottom - length, mPaint);
    }

}

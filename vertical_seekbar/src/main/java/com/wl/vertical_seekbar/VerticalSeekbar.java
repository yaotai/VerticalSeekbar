package com.wl.vertical_seekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class VerticalSeekbar extends View {
    //控件高
    private int height = 0;
    //控件宽
    private int width = 0;
    //进度条宽度
    private int barWidth = 0;
    //游标半径
    private int radius = 15;
    //背景画笔
    private Paint bgPaint;
    //背景色
    private int bgColor = Color.LTGRAY;
    //游标画笔
    private Paint cursorPaint;
    //游标颜色
    private int cursorColor = Color.WHITE;
    //进度画笔
    private Paint progressPaint;
    //游标颜色
    private int progressColor = Color.BLUE;
    //需要通知监听
    private boolean needUpdateProgress = false;
    private OnProgressCall onProgressCall;
    private int progressY = 0;
    private int defaultProgress = 0;
    private Bitmap thumb=null;
    private int max=100;
    public VerticalSeekbar(Context context) {
        super(context);
    }

    public VerticalSeekbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Logs.print("function ---VerticalSeekbar--->");
        initParams(context, attrs);
    }

    private void initParams(Context context, AttributeSet attrs) {
        Logs.print("function ---initParams--->");
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerticalSeekbar);
        if (typedArray != null) {
            bgColor = typedArray.getColor(R.styleable.VerticalSeekbar_bgColor, bgColor);
            progressColor = typedArray.getColor(R.styleable.VerticalSeekbar_progressColor, progressColor);
            defaultProgress = typedArray.getInteger(R.styleable.VerticalSeekbar_progress, 0);
            cursorColor=typedArray.getColor(R.styleable.VerticalSeekbar_thumbColor,Color.WHITE);
            max=typedArray.getInteger(R.styleable.VerticalSeekbar_max,100);
            Drawable drawable =typedArray.getDrawable(R.styleable.VerticalSeekbar_thumb);
            thumb =  ((BitmapDrawable)drawable).getBitmap();
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Logs.print("function ---onMeasure--->");
        //根据提供的测量值(格式)提取模式(三个模式之一)
        //MeasureSpec有3种模式分别是UNSPECIFIED, EXACTLY和AT_MOST,
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);  //取出宽度的测量模式
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);//获取View的大小(宽度的确切数值)

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        Logs.print("onMeasure---widthMode--->" + widthMode);
        switch (widthMode) {
            case MeasureSpec.EXACTLY:

                break;
            case MeasureSpec.AT_MOST:

                break;
            case MeasureSpec.UNSPECIFIED:

                break;
        }
        Logs.print("onMeasure--widthSize--->" + widthSize);
        Logs.print("onMeasure--heightMode-->" + heightMode);
        Logs.print("onMeasure--heightSize-->" + heightSize);
    }

//    @Override`
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        Logs.print("function ---onSizeChanged--->");
//    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Logs.print("function ---onLayout--->");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Logs.print("function ---onDraw--->");
        width = getWidth();
        height = getHeight();
        barWidth = width * 3 / 5;
        if (progressY < radius) {
            progressY = radius;
        }
        if (progressY > (height - radius)) {
            progressY = height - radius;
        }

        if (onProgressCall != null && needUpdateProgress) {
            if (max==100){
                int progress = (progressY - radius) * 100 / (height - radius * 2);
                Logs.print("height:" + (height - radius * 2) + ", progressY:" + (progressY - radius) + ", progress:" + (100 - progress));
                onProgressCall.change(progress);
            }else{
                int flagment=(height-radius*2)/max;
                int time=(progressY-radius)/flagment;
                Logs.print("onProgressCall:" +  time);
                onProgressCall.change(max-time);
            }
        }
        //----------------画背景-----------------
        bgPaint = new Paint();
        bgPaint.setColor(bgColor);// 设置红色
        //画圆角矩形
        bgPaint.setStyle(Paint.Style.FILL);//充满
        bgPaint.setAntiAlias(true);// 设置画笔的锯齿效果
        RectF oval3 = new RectF(width / 5, radius, width * 4 / 5, height - radius);// 设置个新的长方形
        canvas.drawRoundRect(oval3, radius, radius, bgPaint);//第二个参数是x半径，第三个参数是y半径

        //----------------画进度-----------------

        progressPaint = new Paint();
        progressPaint.setColor(progressColor);// 设置红色
        // 画圆角矩形
        progressPaint.setStyle(Paint.Style.FILL);//充满
        progressPaint.setAntiAlias(true);// 设置画笔的锯齿效果
        RectF ovalprogress = new RectF(width / 5, progressY, width * 4 / 5, height - radius);// 设置个新的长方形
        canvas.drawRoundRect(ovalprogress, radius, radius, progressPaint);//第二个参数是x半径，第三个参数是y半径



        //----------------画游标drawable-----------------
        if (thumb!=null){
            int bitmapWidth = thumb.getWidth();
            int bitmapHeight = thumb.getHeight();
            float scaleWight = ((float)width)/bitmapWidth;
            float scaleHeight = ((float)width)/bitmapHeight;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWight, scaleHeight);
            Bitmap res = Bitmap.createBitmap(thumb, 0,0,bitmapWidth, bitmapHeight, matrix, true);
            Logs.print("Bitmap width:"+res.getWidth()+" width:"+width);
            canvas.drawBitmap(res, 0,progressY-radius,  cursorPaint);// 小圆
        }else {
            //----------------画游标-----------------
            cursorPaint = new Paint();
            cursorPaint.setColor(Color.RED);// 设置红色
            cursorPaint.setStyle(Paint.Style.FILL);//充满
            cursorPaint.setAntiAlias(true);// 设置画笔的锯齿效果
            canvas.drawCircle(width / 2, progressY, width / 2, cursorPaint);// 小圆
        }
        needUpdateProgress = false;
        if (defaultProgress != 0) {
            setProgress(defaultProgress);
            defaultProgress = 0;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                progressY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                progressY = (int) event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                needUpdateProgress = true;
                if (max!=100){
                    int flagment=(height-radius*2)/max;
                    progressY = (int) event.getY();
                    int extras=(progressY-radius)%flagment;
                    int time=(progressY-radius)/flagment;
                    if (extras>=(flagment/2)){
                        progressY=flagment*(time+1)+radius;
                    }else {
                        progressY=flagment*time+radius;
                    }
                }
                invalidate();

                break;
        }
        return true;
    }

    public interface OnProgressCall {
        void change(int progress);
    }

    public void setOnProgressCall(OnProgressCall onProgressCall) {
        this.onProgressCall = onProgressCall;
    }

    public void setProgress(int progress) {
        Logs.print("setProgress:" + progress);
        if (max==100){
            progressY = (height - radius * 2) * (100 - progress) / 100 + radius;
        }else{
            int flagment=(height-radius*2)/max;
            progressY=(max-progress)*flagment+radius;
        }

        Logs.print("setProgress progressY:" + progressY);
        invalidate();
    }

    public void setMax(int max) {
        this.max = max;
    }
}

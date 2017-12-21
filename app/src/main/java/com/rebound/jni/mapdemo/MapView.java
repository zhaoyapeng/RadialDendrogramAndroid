package com.rebound.jni.mapdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaoyapeng
 * @version create time:17/12/1920:18
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public class MapView extends View {
    private PointF cenPointF;
    private Paint mCirPaint, pathPaint, mRtextPaint, mLtextPaint;
    private Context mContext;

    private int firstCount;
    private int firstRadius;

    private int circleRadius = 0;

    private Path mPath;

    private int besselCardinality;

    private PointUtil pointUtil;

    protected VelocityTracker mVelocityTracker;

    protected ArrayList<RectFModel> rectfList = new ArrayList<RectFModel>();

    private float scaleX, scaleY;// 缩放时 x,y坐标

    // 数据相关变量(假数据）
    private List<PointFModel> firstList;

    private List<PointFModel> secondList;

    public MapView(Context context) {
        super(context);
        mContext = context;
        initData();
        initViews();
        initAtions();
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initData();
        initViews();
        initAtions();
    }

    public MapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initData();
        initViews();
        initAtions();
    }

    private void initData() {
        pointUtil = new PointUtil(mContext);
        firstCount = 6;
        firstRadius = MapOpinion.getSpacing(mContext);
        circleRadius = MapOpinion.getCircleRadius(mContext);
        besselCardinality = +Util.dip2px(mContext, 30);
        mPath = new Path();
        mTran = new float[]{0, 0};

        cenPointF = new PointF();

        mCirPaint = new Paint();
        mCirPaint.setColor(Color.parseColor("#FF7200"));
        mCirPaint.setStrokeWidth(2);
        mCirPaint.setStyle(Paint.Style.FILL);

        pathPaint = new Paint();
        pathPaint.setColor(Color.parseColor("#9b9b9b"));
        pathPaint.setStrokeWidth(2);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setAntiAlias(true);

        mRtextPaint = new Paint();
        mRtextPaint.setColor(Color.parseColor("#000000"));
        mRtextPaint.setTextSize(MapOpinion.getTextSize(mContext));
        mRtextPaint.setStyle(Paint.Style.STROKE);

        mLtextPaint = new Paint();
        mLtextPaint.setColor(Color.parseColor("#000000"));
        mLtextPaint.setTextSize(MapOpinion.getTextSize(mContext));
        mLtextPaint.setTextAlign(Paint.Align.RIGHT);
        mLtextPaint.setStyle(Paint.Style.STROKE);

        mVelocityTracker = VelocityTracker.obtain();

        firstList = new ArrayList<>();
        secondList = new ArrayList<>();

    }

    private void initViews() {

    }

    private void initAtions() {
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cenPointF.x = w / 2;
        cenPointF.y = h / 2;
        scaleX = cenPointF.x;
        scaleY = cenPointF.y;
        setData();
    }


    boolean isDrawFinish = false;

    private Matrix invertMatrix = new Matrix();
    ;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(mTran[0], mTran[1]);
        canvas.scale(scaleCount, scaleCount, cenPointF.x, cenPointF.y);
        canvas.getMatrix().invert(invertMatrix);

        for (int j = 0; j < secondList.size(); j++) {
            PointFModel pm = secondList.get(j);
            PointF pointF = pm.pointF;

            mPath.reset();
            mPath.moveTo(pm.relyPonitF.x, pm.relyPonitF.y);
            mPath.cubicTo(pm.firstBessel.x, pm.firstBessel.y, pm.secondBessel.x, pm.secondBessel.y, pointF.x, pointF.y);
//                    mPath.lineTo(pointF.x, pointF.y);
            canvas.drawPath(mPath, pathPaint);
            mPath.close();

            canvas.drawCircle(pointF.x, pointF.y, circleRadius, mCirPaint);

            canvas.save();
            if (pm.angle > 90 && pm.angle < 270) {
                canvas.rotate(pm.angle + 180, pm.pointF.x, pm.pointF.y);
                canvas.drawText("径向树状图" + j, pm.pointF.x - circleRadius * 2, pm.pointF.y + circleRadius / 2, mLtextPaint);
            } else {
                canvas.rotate(pm.angle, pm.pointF.x, pm.pointF.y);
                canvas.drawText("径向树状图" + j, pm.pointF.x + circleRadius, pm.pointF.y + circleRadius / 2, mRtextPaint);
            }
            canvas.restore();
        }

        for (int i = 0; i < firstList.size(); i++) {
            mPath.reset();
            //绘制第一层
            PointFModel pointFModel = firstList.get(i);
            PointF pointF = pointFModel.pointF;

            mPath.moveTo(cenPointF.x, cenPointF.y);

            PointF bPointF1 = MathHelper.getInstance().calcArcEndPointXY(cenPointF.x, cenPointF.y, MapOpinion.geFtBesselCardinalitX(mContext), pointFModel.angle - 20);
            PointF bPointF2 = MathHelper.getInstance().calcArcEndPointXY(cenPointF.x, cenPointF.y, MapOpinion.geFtBesselCardinalitX(mContext), pointFModel.angle + 20);

            mPath.cubicTo(bPointF1.x, bPointF1.y, bPointF2.x, bPointF2.y, pointF.x, pointF.y);
//            mPath.lineTo(pointF.x, pointF.y);
            canvas.drawPath(mPath, pathPaint);

            canvas.drawCircle(pointF.x, pointF.y, circleRadius, mCirPaint);
            mPath.close();


        }

        canvas.drawCircle(cenPointF.x, cenPointF.y, circleRadius, mCirPaint);

        canvas.restore();
    }


    private boolean doublePointer;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        if (!enableTouch)
//            return true;
//        if (onTouchView != null)
//            onTouchView.onTouch();

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (doublePointer)
                    doublePointer = false;
                if (isMove) {
                    isMove = false;
                    break;
                }

                float[] f = {event.getX(), event.getY()};
                invertMatrix.mapPoints(f);
                RectFModel rectFModel = isInRect(f[0], f[1], rectfList);
                if (rectFModel != null) {
                    Toast.makeText(mContext, rectFModel.tag, Toast.LENGTH_SHORT).show();
                }
        }

        switch (event.getPointerCount()) {
            case 1:
                translate(event);
                break;
            case 2:
                scale(event);
                break;
        }
        return true;
    }

    protected float mTran[];
    protected float downX, downY, initX, initY;

    protected void translate(MotionEvent event) {
        if (doublePointer) {
            return;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                initX = downX;
                initY = downY;
                break;
            case MotionEvent.ACTION_MOVE:
                if ((Math.abs(event.getX() - initX)) < 5 && (Math.abs(event.getY() - initY) < 5))
                    isMove = false;
                else
                    isMove = true;
                mTran[0] = mTran[0] + event.getX() - downX;
                mTran[1] = mTran[1] + event.getY() - downY;
                downX = event.getX();
                downY = event.getY();
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
    }

    protected boolean isMove;
    float lastScale = 1;
    protected float x3;
    protected float scaleCount = 1;
    protected float minit = 0;

    protected void scale(MotionEvent event) {
        doublePointer = true;
        mVelocityTracker.addMovement(event);
        mVelocityTracker.computeCurrentVelocity(2000);
        isMove = true;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_UP:
                lastScale = scaleCount;
                break;
            case MotionEvent.ACTION_MOVE:
                float toScal = getXYLength(event);
                x3 = toScal / minit;
                float v = mVelocityTracker.getXVelocity();
                if (Math.abs(v) < 5)
                    break;
                if (x3 < 1 && scaleCount <= 0.6)
                    break;
                scaleCount = x3 - 1 + lastScale;
                postInvalidate();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() == 2) {
                    float x = event.getX(0) - event.getX(1);
                    float y = event.getY(0) - event.getY(1);

                    scaleX = event.getX(0) + Math.abs(x / 2);
                    scaleY = event.getY(0) + Math.abs(y / 2);
                    minit = getXYLength(event);
                    scaleCount = lastScale;
                }
                break;
        }
    }


    public float getXYLength(MotionEvent event) {

        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);


        return (float) Math.sqrt(x * x + y * y);
    }

    private RectFModel isInRect(float x, float y, ArrayList<RectFModel> list) {

        int count = 0;
        for (RectFModel rectFModel : list) {
            if (rectFModel.rectF.contains(x, y)) {
                return rectFModel;
            }
            count++;
        }
        return null;
    }

    public void setData() {
        firstList = pointUtil.getPointFByCount(firstCount, cenPointF.x, cenPointF.y, firstRadius);

        for (int i = 0; i < firstList.size(); i++) {
            PointFModel pointFModel = firstList.get(i);
            secondList.addAll(pointUtil.getPointFByAngle(9, cenPointF, pointFModel.pointF, firstRadius * 2, pointFModel.angle));
        }


        for (int j = 0; j < secondList.size(); j++) {
            PointFModel pm = secondList.get(j);
            PointF pointF = pm.pointF;

            RectF rect = new RectF(pointF.x - circleRadius, pointF.y - circleRadius, pointF.x + circleRadius, pointF.y + circleRadius);
            RectFModel rectFModel = new RectFModel();
            rectFModel.rectF = rect;
            rectFModel.tag = "径向树状图" + j;
            rectfList.add(rectFModel);
        }
    }
}

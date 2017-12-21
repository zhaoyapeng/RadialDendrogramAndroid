package com.rebound.jni.mapdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author zhaoyapeng
 * @version create time:17/12/1920:06
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public class MapItemView extends View {


    public MapItemView(Context context) {
        super(context);
    }

    public MapItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MapItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}

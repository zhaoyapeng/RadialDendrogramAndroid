package com.rebound.jni.mapdemo;

import android.content.Context;

/**
 * @author zhaoyapeng
 * @version create time:17/12/1920:23
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public class Util {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}

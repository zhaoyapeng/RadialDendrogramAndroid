package com.rebound.jni.mapdemo;

import android.content.Context;

/**
 * @author zhaoyapeng
 * @version create time:17/12/2011:18
 * @Email zyp@jusfoun.com
 * @Description ${TODO}
 */
public class MapOpinion {
    public static int getBesselCardinalitY(Context mContext){
        return getSpacing(mContext)/24;
    }

    public static int geFtBesselCardinalitX(Context mContext){
        return getSpacing(mContext)/2;
    }

    public static int getSBesselCardinalitX(Context mContext){
        return getSpacing(mContext)/2 ;
    }


    /**
     *  获取层间隔
     * */
    public static int getSpacing(Context mContext){
        return Util.dip2px(mContext,80);
    }

    public static int getCircleRadius(Context mContext){
        return Util.dip2px(mContext,5);
    }


    public static int getTextSize(Context mContext){
        return Util.dip2px(mContext,8);
    }

}

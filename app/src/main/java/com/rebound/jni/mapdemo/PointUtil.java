package com.rebound.jni.mapdemo;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaoyapeng
 * @version create time:17/12/1920:27
 * @Email zyp@jusfoun.com
 * @Description ${计算坐标工具类}
 */
public class PointUtil  {
    private Context mContext;
    public PointUtil(Context mContext){
        this.mContext=mContext;
    }

    /**
     *  根据 个数 ，半径 计算 圆 坐标
     * */
    public  List<PointFModel>  getPointFByCount(int count,float centerX,float centerY,int radius){
        List<PointFModel> list = new ArrayList<>();
        for(int i=0;i<count;i++){
            PointFModel pointFModel = new PointFModel();
            PointF pointF = MathHelper.getInstance().getPointF(360/count*i,centerX,centerY,radius);
            pointFModel.pointF =pointF;
            pointFModel.angle = 360/count*i;
            list.add(pointFModel);
        }

        return list;
    }

    /**
     * 根据个数，半径，角度，计算 圆坐标
     * */
    public  List<PointFModel>  getPointFByAngle(int count, PointF cenPointF,PointF currentPointF , int radius, int angle){
        int baseAngle = Util.dip2px(mContext,2);
        int  besselCardinality =120;
        List<PointFModel> list = new ArrayList<>();
        for(int i=0;i<count;i++){
            PointFModel pointFModel = new PointFModel();
            PointF pointF;
            if(i<=count/2){
                pointF= MathHelper.getInstance().getPointF(angle+(count/2-i)*baseAngle,cenPointF.x,cenPointF.y,radius);

                pointFModel.angle = angle+(count/2-i)*baseAngle;
//                // 贝塞尔 第一个点
//                PointF firstBessel = new PointF();
//                firstBessel.x = currentX+MapOpinion.geFtBesselCardinalitX(mContext);
//                firstBessel.y = currentY-MapOpinion.getBesselCardinalitY(mContext);
//                pointFModel.firstBessel = firstBessel;
//
//                // 内塞尔 第二个点
//                PointF secondBessel = new PointF();
//                secondBessel.x = pointF.x-MapOpinion.getSBesselCardinalitX(mContext);
//                secondBessel.y = pointF.y+MapOpinion.getBesselCardinalitY(mContext);
//                pointFModel.secondBessel = secondBessel;



                PointF bPointF1= MathHelper.getInstance().calcArcEndPointXY(currentPointF.x,currentPointF.y,MapOpinion.geFtBesselCardinalitX(mContext),pointFModel.angle -10);
                pointFModel.firstBessel = bPointF1;

                PointF bPointF2= MathHelper.getInstance().calcArcEndPointXY(currentPointF.x,currentPointF.y,MapOpinion.getSBesselCardinalitX(mContext),pointFModel.angle +15);
                pointFModel.secondBessel = bPointF2;

//                pointFModel.firstBessel = getBezier(currentPointF,pointF)[0];
//                pointFModel.secondBessel = getBezier(currentPointF,pointF)[1];


            }else{
                int mAngle =angle-(i-count/2)*baseAngle;
                if(mAngle<0){
                    mAngle=mAngle+360;
                }
                pointF= MathHelper.getInstance().getPointF(mAngle,cenPointF.x,cenPointF.y,radius);

                pointFModel.angle = mAngle;

                PointF bPointF1= MathHelper.getInstance().calcArcEndPointXY(currentPointF.x,currentPointF.y,MapOpinion.geFtBesselCardinalitX(mContext),pointFModel.angle +10);
                pointFModel.firstBessel = bPointF1;
                PointF bPointF2= MathHelper.getInstance().calcArcEndPointXY(currentPointF.x,currentPointF.y,MapOpinion.getSBesselCardinalitX(mContext),pointFModel.angle -15);
                pointFModel.secondBessel = bPointF2;

//                pointFModel.firstBessel = getBezier(currentPointF,pointF)[0];
//                pointFModel.secondBessel = getBezier(currentPointF,pointF)[1];
            }


            pointFModel.pointF =pointF;
            pointFModel.relyPonitF = currentPointF;


            list.add(pointFModel);
        }

        return list;
    }

    public PointF [] getBezier (PointF startPotintF,PointF endPointF){


        PointF c1 = new PointF();
        PointF c2= new PointF();

        PointF [] pointFs = {c1,c2};
        PointF cen=new PointF(startPotintF.x+(endPointF.x-startPotintF.x)/2,startPotintF.y+(endPointF.y-startPotintF.y)/2);

        float dx1 = startPotintF.x - cen.x;
        float dy1 = startPotintF.y - cen.y;
        float dx2 = cen.x - endPointF.x;
        float dy2 = cen.y - endPointF.y;

        float m1X = (startPotintF.x + cen.x) / 2.0f;
        float m1Y = (startPotintF.y + c1.y) / 2.0f;
        float m2X = (cen.x + endPointF.x) / 2.0f;
        float m2Y = (cen.y + endPointF.y) / 2.0f;

        float l1 = (float) Math.sqrt(dx1 * dx1 + dy1 * dy1);
        float l2 = (float) Math.sqrt(dx2 * dx2 + dy2 * dy2);

        float dxm = (m1X - m2X);
        float dym = (m1Y - m2Y);
        float k = l2 / (l1 + l2);
        if (Float.isNaN(k)) k = 0.0f;
        float cmX = m2X + dxm * k;
        float cmY = m2Y + dym * k;

        float tx = cen.x - cmX;
        float ty = cen.y - cmY;

        c1.x= (int) (m1X + tx);
        c1.y= (int) (m1Y + ty);

        c2.x= (int) (m2X + tx);
        c2.y= (int) (m2Y + ty);
        return pointFs;
    }
}

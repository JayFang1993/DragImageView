package info.fangjie.dragimageview;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by FangJie on 15/9/4.
 */
public class TranslateValueAnimator extends ValueAnimator {

    public static final int LEFT=1;
    public static final int RIGHT=2;
    public static final int BOTTOM=3;
    public static final int TOP=4;

    public int relativeX,relativeY;
    public int startX,startY,endX,endY;//相对于View的坐标

    private static int viewX,viewY;//动画开始时候view的位置

    private View view;

    public TranslateValueAnimator(int relativeX, int relativeY, int startX, int startY, int endX, int endY,View view) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.view = view;
    }


    public static TranslateValueAnimator createTransAnimation(final int relativeX,final int relativeY,int startX,int startY,int x,int y,int time,final View view,
                                                              Animator.AnimatorListener listener){

        TranslateValueAnimator animator=new TranslateValueAnimator(relativeX,relativeY,startX,startY,x,y,view);

        if (listener!=null)
            animator.addListener(listener);

        animator.addListener(new CustomAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                RelativeLayout.LayoutParams parms = (RelativeLayout.LayoutParams) view.getLayoutParams();
                if (relativeX==LEFT){
                    parms.leftMargin =viewX;
                }else{
                    parms.rightMargin = viewX;
                }
                if (relativeY==TOP){
                    parms.topMargin = viewY;
                }else {
                    parms.bottomMargin =viewY;
                }
                view.setLayoutParams(parms);
            }
        });

        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float xValue = (Float) valueAnimator.getAnimatedValue("X");
                float yValue = (Float) valueAnimator.getAnimatedValue("Y");

                RelativeLayout.LayoutParams parms = (RelativeLayout.LayoutParams) view.getLayoutParams();
                if (relativeX==LEFT){
                    parms.leftMargin = (int) xValue;
                }else{
                    parms.rightMargin = (int) xValue;
                }
                if (relativeY==TOP){
                    parms.topMargin = (int) yValue;
                }else {
                    parms.bottomMargin = (int) yValue;
                }
                view.setLayoutParams(parms);
            }
        });
        animator.setDuration(time);
        return animator;
    }


    public static TranslateValueAnimator createTransAnimation(final int relativeX,final int relativeY,int x,int y,int time,final View view,
                                                              Animator.AnimatorListener listener){

        TranslateValueAnimator animator=new TranslateValueAnimator(relativeX,relativeY,0,0,x,y,view);
        if (listener!=null)
            animator.addListener(listener);

        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float xValue = (Float) valueAnimator.getAnimatedValue("X");
                float yValue = (Float) valueAnimator.getAnimatedValue("Y");

                RelativeLayout.LayoutParams parms = (RelativeLayout.LayoutParams) view.getLayoutParams();
                if (relativeX==LEFT){
                    parms.leftMargin = (int) xValue;
                }else{
                    parms.rightMargin = (int) xValue;
                }
                if (relativeY==TOP){
                    parms.topMargin = (int) yValue;
                }else {
                    parms.bottomMargin = (int) yValue;
                }
                view.setLayoutParams(parms);
            }
        });
        animator.setDuration(time);
        return animator;
    }

    @Override
    public void start() {
        updateAbsolutePos();
        super.start();
    }


    public void updateAbsolutePos(){
        RelativeLayout.LayoutParams parms = (RelativeLayout.LayoutParams) view.getLayoutParams();

        if (relativeX==LEFT){
            viewX=parms.leftMargin;
        }else {
            viewX=parms.rightMargin;
        }
        if (relativeY==TOP){
            viewY=parms.topMargin;
        }else {
            viewY=parms.bottomMargin;
        }

        int startPosX=startX+viewX;
        int startPosY=startY+viewY;

        int endPosX=endX;
        int endPosY=endY;
        PropertyValuesHolder xValue= PropertyValuesHolder.ofFloat("X", startPosX, endPosX);
        PropertyValuesHolder yValue= PropertyValuesHolder.ofFloat("Y", startPosY, endPosY);
        setValues(xValue,yValue);
    }
}

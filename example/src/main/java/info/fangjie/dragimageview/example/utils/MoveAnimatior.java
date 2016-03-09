package info.fangjie.dragimageview.example.utils;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.view.View;
import android.widget.RelativeLayout;

import info.fangjie.dragimageview.utils.CustomAnimatorListener;

/**
 * 移动 “萌 ”或 “否” 的图标动画类
 * Created by FangJie on 15/9/4.
 */
public class MoveAnimatior extends ValueAnimator {

    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 3;
    public static final int TOP = 4;

    public int relativeX, relativeY;
    public int startX, startY, endX, endY;//相对于View的坐标

    private static int viewX, viewY;//动画开始时候view的位置

    private View view;

    public MoveAnimatior(int relativeX, int relativeY, int startX, int startY, int endX, int endY, View view) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.view = view;
    }

    /**
     * 移动动画
     * @param relativeX x坐标相对于LEFT或RIGHT
     * @param relativeY y坐标相对于TOP或BOTTOM
     * @param startX
     * @param startY
     * @param x
     * @param y
     * @param time  动画时长
     * @param view  动画的View
     * @param listener
     * @return
     */
    public static MoveAnimatior createTransAnimation(final int relativeX, final int relativeY, int startX, int startY,
                                                     int x, int y, int time, final View view,
                                                     Animator.AnimatorListener listener) {

        MoveAnimatior animator = new MoveAnimatior(relativeX, relativeY, startX, startY, x, y, view);

        if (listener != null)
            animator.addListener(listener);

        animator.addListener(new CustomAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                RelativeLayout.LayoutParams parms = (RelativeLayout.LayoutParams) view.getLayoutParams();
                if (relativeX == LEFT) {
                    parms.leftMargin = viewX;
                } else {
                    parms.rightMargin = viewX;
                }
                if (relativeY == TOP) {
                    parms.topMargin = viewY;
                } else {
                    parms.bottomMargin = viewY;
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
                if (relativeX == LEFT) {
                    parms.leftMargin = (int) xValue;
                } else {
                    parms.rightMargin = (int) xValue;
                }
                if (relativeY == TOP) {
                    parms.topMargin = (int) yValue;
                } else {
                    parms.bottomMargin = (int) yValue;
                }
                view.setLayoutParams(parms);
            }
        });
        animator.setDuration(time);
        return animator;
    }


    /**
     * 缺省已当前布局坐标为起点Move动画
     * @param relativeX
     * @param relativeY
     * @param x
     * @param y
     * @param time
     * @param view
     * @param listener
     * @return
     */
    public static MoveAnimatior createTransAnimation(final int relativeX, final int relativeY, int x, int y, int time, final View view,
                                                     Animator.AnimatorListener listener) {

        MoveAnimatior animator = new MoveAnimatior(relativeX, relativeY, 0, 0, x, y, view);
        if (listener != null)
            animator.addListener(listener);

        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float xValue = (Float) valueAnimator.getAnimatedValue("X");
                float yValue = (Float) valueAnimator.getAnimatedValue("Y");

                RelativeLayout.LayoutParams parms = (RelativeLayout.LayoutParams) view.getLayoutParams();
                if (relativeX == LEFT) {
                    parms.leftMargin = (int) xValue;
                } else {
                    parms.rightMargin = (int) xValue;
                }
                if (relativeY == TOP) {
                    parms.topMargin = (int) yValue;
                } else {
                    parms.bottomMargin = (int) yValue;
                }
                view.setLayoutParams(parms);
            }
        });
        animator.setDuration(time);
        return animator;
    }

    /**
     * 更新 图标 的坐标位置达到 Move动画效果
     */
    public void updateAbsolutePos() {
        RelativeLayout.LayoutParams parms = (RelativeLayout.LayoutParams) view.getLayoutParams();

        if (relativeX == LEFT) {
            viewX = parms.leftMargin;
        } else {
            viewX = parms.rightMargin;
        }
        if (relativeY == TOP) {
            viewY = parms.topMargin;
        } else {
            viewY = parms.bottomMargin;
        }

        int startPosX = startX + viewX;
        int startPosY = startY + viewY;

        int endPosX = endX;
        int endPosY = endY;
        PropertyValuesHolder xValue = PropertyValuesHolder.ofFloat("X", startPosX, endPosX);
        PropertyValuesHolder yValue = PropertyValuesHolder.ofFloat("Y", startPosY, endPosY);
        setValues(xValue, yValue);
    }

    @Override
    public void start() {
        updateAbsolutePos();
        super.start();
    }

}

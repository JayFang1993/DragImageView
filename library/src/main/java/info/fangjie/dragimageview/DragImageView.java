package info.fangjie.dragimageview;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import info.fangjie.dragimageview.utils.CustomAnimatorListener;

/**
 * Created by FangJie on 15/8/27.
 */

public class DragImageView extends ImageView {

    //拖拽距离 是 弹簧回弹的距离 的倍数
    public final static int REBOUND = 6;
    //复位动画的 动画时间
    public final static int RESETTIME = 300;
    //弹簧动画的 动画时间
    public final static int REBOUNDTIME = 100;
    //拖拽旋转地中心点坐标
    public static final int ROTATE_CENTER_X = 400;
    public static final int ROTATE_CENTER_Y = 4000;

    //拖拽按下的点
    private float startPosX, startPosY;
    //拖拽松开的点
    private float endPosX, endPosY;
    //拖拽松开的角度
    private float endRorate;

    private Matrix matrix;
    private DragListener dragListener;
    public float resWidth, resHeight;
    public float imageViewWidth, imageViewHeight;
    //图片资源缩放比例
    public float scale;

    public DragImageView(Context context) {
        super(context);
        init(context);
    }

    public DragImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DragImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setScaleType(ScaleType.CENTER_CROP);
        matrix = new Matrix();
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setScaleType(ScaleType.MATRIX);
        resWidth = getDrawable().getIntrinsicWidth();
        resHeight = getDrawable().getIntrinsicHeight();
        imageViewWidth = getWidth();
        imageViewHeight = getHeight();
    }

    public Matrix resetMatrix() {
        matrix.reset();
        matrix.postTranslate(0, 0);
        resWidth = getDrawable().getIntrinsicWidth();
        resHeight = getDrawable().getIntrinsicHeight();
        scale = (imageViewWidth / resWidth) > (imageViewHeight / resHeight) ? (imageViewWidth / resWidth) :
                (imageViewHeight / resHeight);
        matrix.postScale(scale, scale);
        setImageMatrix(matrix);
        return matrix;
    }

    public void setDragListener(DragListener dragListener) {
        this.dragListener = dragListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            endPosX = event.getX();
            endPosY = event.getY();

            int radius = (int) (ROTATE_CENTER_Y - endPosY);
            int perimeter = (int) (radius * 2 * Math.PI);

            //拖拽的距离
            float detaX = endPosX - startPosX;
            float detaY = endPosY - startPosY;

            //1.平移
            matrix.reset();
            matrix.postTranslate(detaX, detaY);
            //2.缩放
            matrix.postScale(scale, scale);
            //3.旋转
            if (endPosX > startPosX) {
                endRorate = (endPosX - startPosX) * 360 / perimeter;
            } else {
                endRorate = (360 - (startPosX - endPosX) * 360 / perimeter);
            }
            matrix.postRotate(endRorate, ROTATE_CENTER_X, ROTATE_CENTER_Y);

            setImageMatrix(matrix);
            if (dragListener != null)
                dragListener.onDrag(matrix, DragListener.STATE_DRAGING);

        } else if (event.getAction() == MotionEvent.ACTION_UP && endPosX != 0) {
            //满足拖拽距离大于1/4，从右侧划出
            if (endPosX - startPosX > imageViewWidth / 4) {
                moveAnimation(endPosX - startPosX, resWidth, endPosY - startPosY, -200, endRorate, 0, false);
            }
            //满足拖拽距离小于-1/4，从左侧划出
            else if (startPosX - endPosX > imageViewWidth / 4) {
                moveAnimation(endPosX - startPosX, -resWidth, endPosY - startPosY, resHeight - 200, endRorate, 0, false);
            }
            //否则RESET
            else {
                moveAnimation(endPosX - startPosX, 0, endPosY - startPosY, 0, endRorate, 0, true);
            }
            endPosX = 0;
            endPosX = 0;
            endRorate = 0;
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startPosX = event.getX();
            startPosY = event.getY();
        }
        return true;
    }

    /**
     * 将DragImageview从起点移动到终点的translate动画
     *
     * @param startX
     * @param endX
     * @param startY
     * @param endY
     * @param startRorate
     * @param endRotate
     * @param isReBound   移动结束是否伴随弹簧动画
     */
    private void moveAnimation(final float startX, final float endX, final float startY, final float endY,
                               float startRorate, float endRotate, final boolean isReBound) {
        PropertyValuesHolder xValue = PropertyValuesHolder.ofFloat("X", startX, endX);
        PropertyValuesHolder yValue = PropertyValuesHolder.ofFloat("Y", startY, endY);
        if (startRorate > 180) {
            startRorate = startRorate - 360;
        }
        PropertyValuesHolder rValue = PropertyValuesHolder.ofFloat("R", startRorate, endRotate);
        ValueAnimator mCurrentAnimator = ValueAnimator.ofPropertyValuesHolder(xValue, yValue, rValue);
        mCurrentAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float xValue = (Float) animation.getAnimatedValue("X");
                float yValue = (Float) animation.getAnimatedValue("Y");
                float rValue = (Float) animation.getAnimatedValue("R");
                matrix.reset();
                matrix.postTranslate(xValue, yValue);
                matrix.postScale(scale, scale);
                matrix.postRotate(rValue);
                DragImageView.this.setImageMatrix(matrix);
                if (dragListener != null)
                    dragListener.onDrag(matrix, DragListener.STATE_NOT_DRAGING);
            }
        });

        mCurrentAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (dragListener != null) {
                    if (endX == 0) {
                        if (startX > 0)
                            dragListener.onDragReset(DragListener.RIGHT);
                        else {
                            dragListener.onDragReset(DragListener.LEFT);
                        }
                    } else if (endX < 0) {
                        dragListener.onDragOut(DragListener.LEFT);
                    } else {
                        dragListener.onDragOut(DragListener.RIGHT);
                    }
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isReBound) {
                    springAnimation(endX, endX + (startX - endX) / REBOUND, endY, endY + (startY - endY) / REBOUND,
                            new CustomAnimatorListener() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    springAnimation(endX + (startX - endX) / REBOUND, endX, endY + (startY - endY) / REBOUND, endY, null);
                                }
                            });
                } else {
                    if (dragListener != null) {
                        if (endX < 0) {
                            dragListener.onDragOutFinish(DragListener.LEFT);
                        } else {
                            dragListener.onDragOutFinish(DragListener.RIGHT);
                        }
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        mCurrentAnimator.setDuration(RESETTIME);
        mCurrentAnimator.start();
    }

    /**
     * 弹簧动画
     *
     * @param startX
     * @param endX
     * @param startY
     * @param endY
     * @param listener
     */
    private void springAnimation(final float startX, final float endX, final float startY, final float endY,
                                 Animator.AnimatorListener listener) {
        PropertyValuesHolder xValue = PropertyValuesHolder.ofFloat("X", startX, endX);
        PropertyValuesHolder yValue = PropertyValuesHolder.ofFloat("Y", startY, endY);
        ValueAnimator mCurrentAnimator = ValueAnimator.ofPropertyValuesHolder(xValue, yValue);
        mCurrentAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float xValue = (Float) animation.getAnimatedValue("X");
                float yValue = (Float) animation.getAnimatedValue("Y");
                matrix.reset();
                matrix.postTranslate(xValue, yValue);
                matrix.postScale(scale, scale);
                DragImageView.this.setImageMatrix(matrix);
                if (dragListener != null)
                    dragListener.onDrag(matrix, DragListener.STATE_NOT_DRAGING);
            }
        });
        if (listener != null)
            mCurrentAnimator.addListener(listener);
        mCurrentAnimator.setDuration(REBOUNDTIME);
        mCurrentAnimator.start();
    }
}

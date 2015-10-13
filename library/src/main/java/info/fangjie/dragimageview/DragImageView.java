package info.fangjie.dragimageview;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;


/**
 * Created by FangJie on 15/8/27.
 */
public class DragImageView extends ImageView {

    public final static int REBOUND = 6;
    public final static int RESETTIME = 300;
    public final static int REBOUNDTIME = 100;

    private float startPosX, startPosY, endPosX, endPosY;

    private float curPosX = 0, curPosY = 0, curRorate = 0;
    public float resWidth, resHeight;
    public float imageViewWidth, imageViewHeight;
    public float scale;
    private Matrix matrix;

    private DragListener dragListener;

    private Context mContext;

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


    public float getImageViewHeight() {
        return imageViewHeight;
    }

    public float getImageViewWidth() {
        return imageViewWidth;
    }

    private void init(Context context) {
        setScaleType(ScaleType.MATRIX);
        matrix = new Matrix();
        mContext = context;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        resWidth = getDrawable().getIntrinsicWidth();
        resHeight = getDrawable().getIntrinsicHeight();
        imageViewWidth = getWidth();
        imageViewHeight = getHeight();

        matrix.reset();
        matrix.postTranslate(0, 0);
        scale = (imageViewWidth / resWidth) > (imageViewHeight / resHeight) ? (imageViewWidth / resWidth) : (imageViewHeight / resHeight);
        matrix.postScale(scale, scale);
        setImageMatrix(matrix);
    }

    public Matrix resetMatrix() {
        matrix.reset();
        matrix.postTranslate(0, 0);
        resWidth = getDrawable().getIntrinsicWidth();
        resHeight = getDrawable().getIntrinsicHeight();
        scale = (imageViewWidth / resWidth) > (imageViewHeight / resHeight) ? (imageViewWidth / resWidth) : (imageViewHeight / resHeight);
        matrix.postScale(scale, scale);
        setImageMatrix(matrix);
        return matrix;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            endPosX = event.getX();
            endPosY = event.getY();

            curPosX += (endPosX - startPosX);
            curPosY += (endPosY - startPosY);
            curRorate += (endPosX - startPosX) * 360 / 48000;
            //1.平移
            matrix.reset();
            matrix.postScale(scale, scale);
            matrix.postTranslate(curPosX, curPosY);
            //2.旋转
            if (endPosX > startPosX) {
                matrix.postRotate((endPosX - startPosX) * 360 / 48000, MatrixUtils.ROTATE_CENTER_X, MatrixUtils.ROTATE_CENTER_Y);
            } else {
                matrix.postRotate(360 - (startPosX - endPosX) * 360 / 48000, MatrixUtils.ROTATE_CENTER_X, MatrixUtils.ROTATE_CENTER_Y);
            }

            setImageMatrix(matrix);
            if (dragListener != null)
                dragListener.onDrag(matrix, DragListener.STATE_DRAGING);
            startPosX = endPosX;
            startPosY = endPosY;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (curPosX > imageViewWidth / 4) {//右侧划出
                resetWithAnimation(curPosX, resWidth, curPosY, -200, curRorate, 0, false);
            } else if (-curPosX > imageViewWidth / 4) {//左侧划出
                resetWithAnimation(curPosX, -resWidth, curPosY, resHeight - 200, curRorate, 0, false);
            } else {
                resetWithAnimation(curPosX, 0, curPosY, 0, curRorate, 0, true);
            }
            curPosX = 0;
            curRorate = 0;
            curPosY = 0;
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startPosX = event.getX();
            startPosY = event.getY();
        }
        return true;
    }

    //定点动画
    private void resetWithAnimation(final float startX, final float endX, final float startY, final float endY,
                                    float startRorate, float endRotate, final boolean isReBound) {

        PropertyValuesHolder xValue = PropertyValuesHolder.ofFloat("X", startX, endX);
        PropertyValuesHolder yValue = PropertyValuesHolder.ofFloat("Y", startY, endY);
        PropertyValuesHolder rorateValue = PropertyValuesHolder.ofFloat("RORATE", startRorate, endRotate);
        ValueAnimator mCurrentAnimator = ValueAnimator.ofPropertyValuesHolder(xValue, yValue, rorateValue);
        mCurrentAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float xValue = (Float) animation.getAnimatedValue("X");
                float yValue = (Float) animation.getAnimatedValue("Y");
                float rorateValue = (Float) animation.getAnimatedValue("RORATE");
                matrix.reset();
                matrix.postTranslate(xValue, yValue);
                matrix.postScale(scale, scale);
//                matrix.postScale(imageViewWidth / resWidth, imageViewHeight / resHeight);
                DragImageView.this.setImageMatrix(matrix);
                if (dragListener != null) {
                    if (endX == 0 && startX > 0) {
                        dragListener.onDrag(matrix, DragListener.STATE_RESET_FROM_RIGHT);
                    } else if (endX == 0 && startX < 0) {
                        dragListener.onDrag(matrix, DragListener.STATE_RESET_FROM_LEFT);
                    } else if (endX > 0) {
                        dragListener.onDrag(matrix, DragListener.STATE_DRAG_TO_RIGHT);
                    } else if (endX < 0) {
                        dragListener.onDrag(matrix, DragListener.STATE_DRAG_TO_LEFT);
                    }
                }

            }
        });

        mCurrentAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
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
            public void onAnimationEnd(Animator animator) {
                if (isReBound) {
                    reboundAnimation(endX, endX + (startX - endX) / REBOUND, endY, endY + (startY - endY) / REBOUND, new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            reboundAnimation(endX + (startX - endX) / REBOUND, endX, endY + (startY - endY) / REBOUND, endY, null);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

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
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        mCurrentAnimator.setDuration(RESETTIME);
        mCurrentAnimator.start();
    }

    //回弹动画
    private void reboundAnimation(final float startX, final float endX, final float startY, final float endY,
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
                    dragListener.onDrag(matrix, DragListener.STATE_RESET_REBOUND);
            }
        });
        if (listener != null)
            mCurrentAnimator.addListener(listener);
        mCurrentAnimator.setDuration(REBOUNDTIME);
        mCurrentAnimator.start();
    }

    public void setDragListener(DragListener dragListener) {
        this.dragListener = dragListener;
    }

}

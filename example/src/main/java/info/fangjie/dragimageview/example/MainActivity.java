package info.fangjie.dragimageview.example;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import info.fangjie.dragimageview.BitmapUtils;
import info.fangjie.dragimageview.CustomAnimatorListener;
import info.fangjie.dragimageview.DragImageView;
import info.fangjie.dragimageview.DragListener;
import info.fangjie.dragimageview.MatrixUtils;
import info.fangjie.dragimageview.RotateRelativeLayout;
import info.fangjie.dragimageview.TranslateValueAnimator;

public class MainActivity extends Activity {

    private DragImageView mDargViewTop,mDragViewBottom;
    private RotateRelativeLayout rotateRelativeLayoutTop,rotateRelativeLayoutBottom;
    private TextView textViewTop,textViewBottom;

    private ImageView mImageviewLeft,mImageviewRight;
    private FrameLayout mFlTop,mFlBottom;
    private int leftViewLeftMargin,leftViewBottomMargin;//左下图标的初始margin值
    private int rightViewRightMargin,rightViewBottomMargin;//右下图标的初始margin值

    private String arr[]={"http://image17-c.poco.cn/mypoco/qing/20151005/16/3175349739049393137_1024x682_320.jpg",
        "http://image17-c.poco.cn/mypoco/qing/20150930/00/6519029902905064376_1024x682_320.jpg",
        "http://image17-c.poco.cn/mypoco/qing/20150729/21/336442309889423136_1024x682_320.jpg",
        "http://image17-c.poco.cn/mypoco/qing/20150903/20/1588628341864082833_661x1024_320.jpg",
        "http://image17-c.poco.cn/mypoco/qing/20150704/16/1514271282634186881_768x1024_320.jpg"};

    private String string[]={"第一张","第二张","第三张","第四张","第五张"};

    private int index=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);

        mDargViewTop=(DragImageView)findViewById(R.id.dragview_top);
        mFlTop=(FrameLayout)findViewById(R.id.fl_top);
        mFlBottom=(FrameLayout)findViewById(R.id.fl_bottom);
        rotateRelativeLayoutTop=(RotateRelativeLayout)findViewById(R.id.rl_top);
        rotateRelativeLayoutBottom=(RotateRelativeLayout)findViewById(R.id.rl_bottom);
        textViewTop=(TextView)findViewById(R.id.tv_mark_top);
        textViewBottom=(TextView)findViewById(R.id.tv_mark_bottom);

        mImageviewLeft=(ImageView)findViewById(R.id.iv_left);
        mImageviewRight=(ImageView)findViewById(R.id.iv_right);

        mDragViewBottom=(DragImageView)findViewById(R.id.dragview_bottom);

        ImageLoader.getInstance().displayImage(arr[0], mDargViewTop);
        textViewTop.setText("前面");

        ImageLoader.getInstance().displayImage(arr[1], mDragViewBottom);
        textViewBottom.setText("后面");

        index=0;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams parmsLeft = (RelativeLayout.LayoutParams) mImageviewLeft.getLayoutParams();
                leftViewLeftMargin = parmsLeft.leftMargin;
                leftViewBottomMargin = parmsLeft.bottomMargin;

                RelativeLayout.LayoutParams parmsRight = (RelativeLayout.LayoutParams) mImageviewRight.getLayoutParams();
                rightViewRightMargin = parmsRight.rightMargin;
                rightViewBottomMargin = parmsRight.bottomMargin;
            }
        }, 300);

        mDragViewBottom.setDragListener(new DragListener() {
            @Override
            public void onDrag(Matrix matrix, int state) {
                rotateRelativeLayoutBottom.setMatrix(matrix);
                float distanceX = MatrixUtils.getTransXY(matrix)[0];
                float distanceY = MatrixUtils.getTransXY(matrix)[1];

                if (state == DragListener.STATE_DRAGING && distanceX > 0 && distanceX > 50) {
                    updateRightLayout(distanceX, distanceY, mImageviewRight);
                } else if (state == DragListener.STATE_DRAGING && distanceX < 0 && distanceX < -50) {
                    updateLeftLayout(-distanceX, distanceY, mImageviewLeft);
                }
            }

            @Override
            public void onDragOutFinish(int direction) {

            }

            @Override
            public void onDragOut(int direction) {
                if (direction == LEFT) {
                    resetLeftLayout();
                    resetRightLayout();
                } else {
                    resetLeftLayout();
                    resetRightLayout();
                }
                setLayoutBottom(mFlBottom);
                index++;
                if ((index+1)>=5){
                    index=-1;
                }
                ImageLoader.getInstance().displayImage(arr[index + 1], mDragViewBottom,new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        Matrix matrix=BitmapUtils.centerCorpImageview(mDragViewBottom,loadedImage);
                        rotateRelativeLayoutBottom.setMatrix(matrix);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
                Matrix matrix=mDargViewTop.resetMatrix();
                rotateRelativeLayoutTop.setMatrix(matrix);
                textViewBottom.setText(string[index+1]);
            }

            @Override
            public void onDragReset(int direction) {
                if (direction == LEFT) {
                    resetLeftLayout();
                    resetRightLayout();
                } else {
                    resetLeftLayout();
                    resetRightLayout();
                }
            }

            @Override
            public void onDragResetFinish() {

            }
        });


        mDargViewTop.setDragListener(new DragListener() {

            @Override
            public void onDragOut(int direction) {
                if (direction == LEFT) {
                    resetLeftLayout();
                    resetRightLayout();

                } else {
                    resetLeftLayout();
                    resetRightLayout();
                }
                setLayoutBottom(mFlTop);
                index++;
                if ((index+1)>=5) {
                    index=-1;
                }
                ImageLoader.getInstance().displayImage(arr[index + 1], mDargViewTop, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        Matrix matrix = BitmapUtils.centerCorpImageview(mDargViewTop, loadedImage);
                        rotateRelativeLayoutTop.setMatrix(matrix);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
                Matrix matrix=mDargViewTop.resetMatrix();
                rotateRelativeLayoutTop.setMatrix(matrix);
                textViewTop.setText(string[index+1]);
            }

            @Override
            public void onDragReset(int direction) {
                if (direction == LEFT) {
                    resetLeftLayout();
                    resetRightLayout();
                } else {
                    resetLeftLayout();
                    resetRightLayout();
                }
            }

            @Override
            public void onDragResetFinish() {

            }

            @Override
            public void onDragOutFinish(int state) {
            }

            @Override
            public void onDrag(Matrix matrix, int state) {
                rotateRelativeLayoutTop.setMatrix(matrix);
                float distanceX = MatrixUtils.getTransXY(matrix)[0];
                float distanceY = MatrixUtils.getTransXY(matrix)[1];
                if (state == DragListener.STATE_DRAGING && distanceX > 0 && distanceX > 50) {
                    updateRightLayout(distanceX, distanceY, mImageviewRight);
                } else if (state == DragListener.STATE_DRAGING && distanceX < 0 && distanceX < -50) {
                    updateLeftLayout(-distanceX, distanceY, mImageviewLeft);
                }
            }
        });
    }



    private void updateLeftLayout(float x,float y ,View view){
        RelativeLayout.LayoutParams parms = (RelativeLayout.LayoutParams) view.getLayoutParams();
        int moveX=1.2*x>300?300:(int)(1.2*x);
        double moveRotate=Math.PI/3;
        if (y>0){
            moveRotate=Math.PI/6+Math.atan(y/x);
        }else{
            moveRotate=Math.PI/4-Math.atan(-y/x);
        }

        moveRotate= moveRotate < Math.PI/9 ? Math.PI: moveRotate;
        moveRotate= moveRotate > (Math.PI*3)/10 ?(Math.PI*3)/10: moveRotate;
        int moveY=(int)(Math.tan(moveRotate) *moveX);

        moveY=moveY>450?450:moveY;

        parms.leftMargin = leftViewLeftMargin+moveX;
        parms.bottomMargin = leftViewBottomMargin+moveY;
        view.setLayoutParams(parms);
    }

    private void updateRightLayout(float x,float y ,View view){
        RelativeLayout.LayoutParams parms = (RelativeLayout.LayoutParams) view.getLayoutParams();

        int moveX=1.2*x>400?400:(int)(1.2*x);

        double moveRotate=Math.PI/3;
        if (y>0){
            moveRotate=Math.PI/4-Math.atan(y/x);
        }else{
            moveRotate=Math.PI/6+Math.atan(-y/x);
        }

        moveRotate= moveRotate < Math.PI/9 ? Math.PI: moveRotate;
        moveRotate= moveRotate > (Math.PI*3)/10 ?(Math.PI*3)/10: moveRotate;
        int moveY=(int)(Math.tan(moveRotate) *moveX);

        moveY=moveY>450?450:moveY;

        parms.rightMargin = rightViewRightMargin+moveX;
        parms.bottomMargin = rightViewBottomMargin+moveY;
        view.setLayoutParams(parms);

    }


    private void resetLeftLayout(){
        ValueAnimator leftReset= TranslateValueAnimator.createTransAnimation(TranslateValueAnimator.LEFT, TranslateValueAnimator.BOTTOM,
            leftViewLeftMargin, leftViewBottomMargin, 200, mImageviewLeft, new CustomAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {

                }
            });
        leftReset.start();
    }


    private void resetRightLayout(){
        ValueAnimator rightReset=TranslateValueAnimator.createTransAnimation(TranslateValueAnimator.RIGHT, TranslateValueAnimator.BOTTOM,
                rightViewRightMargin, rightViewRightMargin, 200, mImageviewRight, new CustomAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }
                });
        rightReset.start();
    }

    private void setLayoutBottom(FrameLayout bottom){
        ViewGroup viewGroup=(ViewGroup) bottom.getParent();
        viewGroup.removeView(bottom);
        viewGroup.addView(bottom, 0);
    }

}

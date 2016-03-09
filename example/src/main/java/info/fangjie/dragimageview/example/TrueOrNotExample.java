package info.fangjie.dragimageview.example;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import info.fangjie.dragimageview.BaseLayerView;
import info.fangjie.dragimageview.example.utils.MoveAnimatior;
import info.fangjie.dragimageview.utils.ScaleTypeUtils;
import info.fangjie.dragimageview.utils.CustomAnimatorListener;
import info.fangjie.dragimageview.DragImageView;
import info.fangjie.dragimageview.DragListener;
import info.fangjie.dragimageview.utils.LayerViewUtils;
import info.fangjie.dragimageview.utils.MatrixUtils;
import info.fangjie.dragimageview.RotateRelativeLayout;

public class TrueOrNotExample extends Activity {

    private MyLayerView layerViewTop,layerViewBottom;

    private ImageView mImageviewLeft, mImageviewRight;

    private int leftViewLeftMargin, leftViewBottomMargin;//左下图标的初始margin值
    private int rightViewRightMargin, rightViewBottomMargin;//右下图标的初始margin值

    private String arr[] = {"http://ww2.sinaimg.cn/large/7a8aed7bjw1f1qed6rs61j20ss0zkgrt.jpg",
        "http://ww3.sinaimg.cn/large/7a8aed7bjw1f1p77v97xpj20k00zkgpw.jpg",
        "http://ww1.sinaimg.cn/large/7a8aed7bjw1f1o75j517xj20u018iqnf.jpg",
        "http://ww4.sinaimg.cn/large/7a8aed7bjw1f1klhuc8w5j20d30h9gn8.jpg",
        "http://ww4.sinaimg.cn/large/7a8aed7bjw1f1jionqvz6j20hs0qoq7p.jpg"};
    private String string[] = {"First", "Second", "Third", "Fourth", "Fifth"};

    private static int index = 0;

    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_true_or_not);

        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);

        options=new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.cute_or_not_default)
                .showImageOnFail(R.drawable.cute_or_not_default)
                .build();

        //top layer
        layerViewTop=new MyLayerView(BaseLayerView.LAYER_TOP);
        layerViewTop.frameLayout = (FrameLayout) findViewById(R.id.fl_top);
        layerViewTop.dragImageView = (DragImageView) findViewById(R.id.dragview_top);
        layerViewTop.relativeLayout = (RotateRelativeLayout) findViewById(R.id.rl_top);
        layerViewTop.textView = (TextView) findViewById(R.id.tv_mark_top);

        //bottom layer
        layerViewBottom=new MyLayerView(BaseLayerView.LAYER_BOTTOM);
        layerViewBottom.frameLayout=(FrameLayout) findViewById(R.id.fl_bottom);
        layerViewBottom.dragImageView = (DragImageView) findViewById(R.id.dragview_bottom);
        layerViewBottom.relativeLayout = (RotateRelativeLayout) findViewById(R.id.rl_bottom);
        layerViewBottom.textView = (TextView) findViewById(R.id.tv_mark_bottom);

        layerViewTop.dragImageView.setDragListener(new MyDragListener(BaseLayerView.LAYER_TOP));
        layerViewBottom.dragImageView.setDragListener(new MyDragListener(BaseLayerView.LAYER_BOTTOM));

        mImageviewLeft = (ImageView) findViewById(R.id.iv_left);
        mImageviewRight = (ImageView) findViewById(R.id.iv_right);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        }, 300);

    }

    private void initData(){
        RelativeLayout.LayoutParams parmsLeft = (RelativeLayout.LayoutParams) mImageviewLeft.getLayoutParams();
        leftViewLeftMargin = parmsLeft.leftMargin;
        leftViewBottomMargin = parmsLeft.bottomMargin;

        RelativeLayout.LayoutParams parmsRight = (RelativeLayout.LayoutParams) mImageviewRight.getLayoutParams();
        rightViewRightMargin = parmsRight.rightMargin;
        rightViewBottomMargin = parmsRight.bottomMargin;

        //init data
        ImageLoader.getInstance().displayImage(arr[0], layerViewTop.dragImageView, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) { }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}

            @Override
            public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
                Matrix matrix = ScaleTypeUtils.centerCorpImageview(layerViewTop.dragImageView,
                    loadedImage);
                layerViewTop.relativeLayout.setMatrix(matrix);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {}
        });

        layerViewTop.textView.setText(string[0]);
        ImageLoader.getInstance().displayImage(arr[1], layerViewBottom.dragImageView, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {}

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}

            @Override
            public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
                Matrix matrix = ScaleTypeUtils.centerCorpImageview(layerViewBottom.dragImageView,
                    loadedImage);
                layerViewBottom.relativeLayout.setMatrix(matrix);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {}
        });
        layerViewBottom.textView.setText(string[1]);
        index = 0;
    }

     class MyDragListener implements DragListener{
        private int layerOrder;

        MyDragListener(int layerOrder){
            this.layerOrder=layerOrder;
        }

        @Override
        public void onDrag(Matrix matrix, int state) {
            if (layerOrder==BaseLayerView.LAYER_BOTTOM){
                layerViewBottom.relativeLayout.setMatrix(matrix);
            }else{
                layerViewTop.relativeLayout.setMatrix(matrix);
            }
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
            if (direction == LEFT) {
                resetLeftLayout();
                resetRightLayout();

            } else {
                resetLeftLayout();
                resetRightLayout();
            }
            LayerViewUtils.setLayoutBottom(layerOrder == BaseLayerView.LAYER_BOTTOM ? layerViewBottom :
                    layerViewTop);
            index++;
            if ((index + 1) >= 5) {
                index = -1;
            }
            ImageLoader.getInstance().displayImage(arr[index + 1], layerOrder == BaseLayerView.LAYER_BOTTOM ? layerViewBottom.dragImageView :
                    layerViewTop.dragImageView,options,new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {}

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    Matrix matrix = ScaleTypeUtils.centerCorpImageview(
                        layerOrder == BaseLayerView.LAYER_BOTTOM ? layerViewBottom.dragImageView
                            : layerViewTop.dragImageView, loadedImage);
                    (layerOrder == BaseLayerView.LAYER_BOTTOM ? layerViewBottom.relativeLayout :
                            layerViewTop.relativeLayout).setMatrix(matrix);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {}
            });
            if (layerOrder==BaseLayerView.LAYER_TOP){
                Matrix matrix = layerViewTop.dragImageView.resetMatrix();
                layerViewTop.relativeLayout.setMatrix(matrix);
                layerViewTop.textView.setText(string[index + 1]);
            }else {
                Matrix matrix = layerViewBottom.dragImageView.resetMatrix();
                layerViewBottom.relativeLayout.setMatrix(matrix);
                layerViewBottom.textView.setText(string[index + 1]);
            }
        }

        @Override
        public void onDragOut(int direction) {

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
     }


    private void updateLeftLayout(float x, float y, View view) {
        RelativeLayout.LayoutParams parms = (RelativeLayout.LayoutParams) view.getLayoutParams();
        int moveX = 1.2 * x > 300 ? 300 : (int) (1.2 * x);
        double moveRotate = Math.PI / 3;
        if (y > 0) {
            moveRotate = Math.PI / 6 + Math.atan(y / x);
        } else {
            moveRotate = Math.PI / 4 - Math.atan(-y / x);
        }
        moveRotate = moveRotate < Math.PI / 9 ? Math.PI : moveRotate;
        moveRotate = moveRotate > (Math.PI * 3) / 10 ? (Math.PI * 3) / 10 : moveRotate;
        int moveY = (int) (Math.tan(moveRotate) * moveX);

        moveY = moveY > 450 ? 450 : moveY;

        parms.leftMargin = leftViewLeftMargin + moveX;
        parms.bottomMargin = leftViewBottomMargin + moveY;
        view.setLayoutParams(parms);
    }

    private void updateRightLayout(float x, float y, View view) {
        RelativeLayout.LayoutParams parms = (RelativeLayout.LayoutParams) view.getLayoutParams();

        int moveX = 1.2 * x > 400 ? 400 : (int) (1.2 * x);
        double moveRotate = Math.PI / 3;
        if (y > 0) {
            moveRotate = Math.PI / 4 - Math.atan(y / x);
        } else {
            moveRotate = Math.PI / 6 + Math.atan(-y / x);
        }
        moveRotate = moveRotate < Math.PI / 9 ? Math.PI : moveRotate;
        moveRotate = moveRotate > (Math.PI * 3) / 10 ? (Math.PI * 3) / 10 : moveRotate;
        int moveY = (int) (Math.tan(moveRotate) * moveX);
        moveY = moveY > 450 ? 450 : moveY;

        parms.rightMargin = rightViewRightMargin + moveX;
        parms.bottomMargin = rightViewBottomMargin + moveY;
        view.setLayoutParams(parms);

    }


    private void resetLeftLayout() {
        ValueAnimator leftReset = MoveAnimatior.createTransAnimation(MoveAnimatior.LEFT,
            MoveAnimatior.BOTTOM, leftViewLeftMargin, leftViewBottomMargin, 200, mImageviewLeft,
            new CustomAnimatorListener() {
                @Override public void onAnimationEnd(Animator animation) {

                }
            });
        leftReset.start();
    }


    private void resetRightLayout() {
        ValueAnimator rightReset = MoveAnimatior.createTransAnimation(MoveAnimatior.RIGHT, MoveAnimatior.BOTTOM,
                rightViewRightMargin, rightViewRightMargin, 200, mImageviewRight, new CustomAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }
                });
        rightReset.start();
    }
}

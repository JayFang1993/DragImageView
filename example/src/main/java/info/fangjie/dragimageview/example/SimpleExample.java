package info.fangjie.dragimageview.example;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import info.fangjie.dragimageview.BaseLayerView;
import info.fangjie.dragimageview.utils.ScaleTypeUtils;
import info.fangjie.dragimageview.DragImageView;
import info.fangjie.dragimageview.DragListener;
import info.fangjie.dragimageview.utils.LayerViewUtils;

/**
 * Created by FangJie on 15/10/21.
 */
public class SimpleExample extends Activity {

    private BaseLayerView layerViewTop,layerViewBottom;

    private String arr[] = {"http://ww2.sinaimg.cn/large/7a8aed7bjw1f1qed6rs61j20ss0zkgrt.jpg",
            "http://ww3.sinaimg.cn/large/7a8aed7bjw1f1p77v97xpj20k00zkgpw.jpg",
            "http://ww1.sinaimg.cn/large/7a8aed7bjw1f1o75j517xj20u018iqnf.jpg",
            "http://ww4.sinaimg.cn/large/7a8aed7bjw1f1klhuc8w5j20d30h9gn8.jpg",
            "http://ww4.sinaimg.cn/large/7a8aed7bjw1f1jionqvz6j20hs0qoq7p.jpg"};

    private static int index = 0;

    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_simple);

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

        //bottom layer
        layerViewBottom=new MyLayerView(BaseLayerView.LAYER_BOTTOM);
        layerViewBottom.frameLayout=(FrameLayout) findViewById(R.id.fl_bottom);
        layerViewBottom.dragImageView = (DragImageView) findViewById(R.id.dragview_bottom);

        layerViewTop.dragImageView.setDragListener(new MyDragListener(BaseLayerView.LAYER_TOP));
        layerViewBottom.dragImageView.setDragListener(new MyDragListener(BaseLayerView.LAYER_BOTTOM));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        },300);

    }

    private void initData(){
        //init data
        ImageLoader.getInstance().displayImage(arr[0], layerViewTop.dragImageView, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
                ScaleTypeUtils.centerCorpImageview(layerViewTop.dragImageView, loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }
        });

        ImageLoader.getInstance().displayImage(arr[1], layerViewBottom.dragImageView, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
                ScaleTypeUtils.centerCorpImageview(layerViewBottom.dragImageView, loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }
        });
        index = 0;
    }

    class MyDragListener implements DragListener {
        private int layerOrder;

        MyDragListener(int layerOrder){
            this.layerOrder=layerOrder;
        }

        @Override
        public void onDrag(Matrix matrix, int state) {

        }

        @Override
        public void onDragOutFinish(int direction) {

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
                    ScaleTypeUtils.centerCorpImageview(
                        layerOrder == BaseLayerView.LAYER_BOTTOM ? layerViewBottom.dragImageView
                            : layerViewTop.dragImageView, loadedImage);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {}
            });
            if (layerOrder==BaseLayerView.LAYER_TOP){
                layerViewTop.dragImageView.resetMatrix();
            }else {
                layerViewBottom.dragImageView.resetMatrix();
            }
        }

        @Override
        public void onDragOut(int direction) {

        }

        @Override
        public void onDragReset(int direction) {

        }
    }


}

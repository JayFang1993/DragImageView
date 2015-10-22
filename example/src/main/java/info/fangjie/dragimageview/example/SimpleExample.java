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
import info.fangjie.dragimageview.BitmapUtils;
import info.fangjie.dragimageview.DragImageView;
import info.fangjie.dragimageview.DragListener;
import info.fangjie.dragimageview.LayerViewUtils;

/**
 * Created by FangJie on 15/10/21.
 */
public class SimpleExample extends Activity {

    private BaseLayerView layerViewTop,layerViewBottom;

    private String arr[] = {"http://image17-c.poco.cn/mypoco/qing/20151005/16/3175349739049393137_1024x682_320.jpg",
            "http://image17-c.poco.cn/mypoco/qing/20150930/00/6519029902905064376_1024x682_320.jpg",
            "http://image17-c.poco.cn/mypoco/qing/20150729/21/336442309889423136_1024x682_320.jpg",
            "http://image17-c.poco.cn/mypoco/qing/20150903/20/1588628341864082833_661x1024_320.jpg",
            "http://image17-c.poco.cn/mypoco/qing/20150704/16/1514271282634186881_768x1024_320.jpg"};

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
                BitmapUtils.centerCorpImageview(layerViewTop.dragImageView, loadedImage);
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
                BitmapUtils.centerCorpImageview(layerViewBottom.dragImageView, loadedImage);
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
                    BitmapUtils.centerCorpImageview(layerOrder == BaseLayerView.LAYER_BOTTOM ? layerViewBottom.dragImageView :
                            layerViewTop.dragImageView, loadedImage);
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

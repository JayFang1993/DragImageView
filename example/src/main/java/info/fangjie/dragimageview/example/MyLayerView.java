package info.fangjie.dragimageview.example;

import android.widget.TextView;

import info.fangjie.dragimageview.BaseLayerView;
import info.fangjie.dragimageview.RotateRelativeLayout;

/**
 * Created by FangJie on 15/10/13.
 */
public class MyLayerView extends BaseLayerView {

    MyLayerView(int layerOrder){
        super(layerOrder);
    }

    public RotateRelativeLayout relativeLayout;

    public TextView textView;

}

package info.fangjie.dragimageview;

import android.widget.FrameLayout;

/**
 * Created by FangJie on 15/10/13.
 */
public class BaseLayerView {

    public final static int LAYER_TOP=1;
    public final static int LAYER_BOTTOM=2;

    public int layerOrder;

    public FrameLayout frameLayout;

    public DragImageView dragImageView;

    public BaseLayerView(int layerOrder){
        this.layerOrder=layerOrder;
    }

}

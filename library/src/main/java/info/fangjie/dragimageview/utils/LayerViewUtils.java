package info.fangjie.dragimageview.utils;

import android.view.ViewGroup;
import info.fangjie.dragimageview.BaseLayerView;

/**
 * Created by FangJie on 15/10/13.
 */
public class LayerViewUtils {

    /**
     * move layerview to the bottom
     * @param layerView
     */
    public static  void setLayoutBottom(BaseLayerView layerView) {
        ViewGroup viewGroup = (ViewGroup) layerView.frameLayout.getParent();
        viewGroup.removeView(layerView.frameLayout);
        viewGroup.addView(layerView.frameLayout, 0);
    }
}

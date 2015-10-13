package info.fangjie.dragimageview;

import android.view.ViewGroup;

/**
 * Created by FangJie on 15/10/13.
 */
public class LayerViewUtils {

    public static  void setLayoutBottom(BaseLayerView layerView) {
        ViewGroup viewGroup = (ViewGroup) layerView.frameLayout.getParent();
        viewGroup.removeView(layerView.frameLayout);
        viewGroup.addView(layerView.frameLayout, 0);
    }
}

package info.fangjie.dragimageview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by FangJie on 15/9/3.
 */
@Deprecated
public class MatrxMarkViewGroup extends RelativeLayout {

    public MatrxMarkViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MatrxMarkViewGroup(Context context) {
        super(context);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long time) {
        //MatrixMakeView - is view that I'm trying to rotate
        if(!(child instanceof MatrixMakeView)) {
            return super.drawChild(canvas, child, time);
        }

        final int width = child.getWidth();
        final int height = child.getHeight();

        final int left = child.getLeft();
        final int top = child.getTop();
        final int right = left + width;
        final int bottom = top + height;

        int restoreTo = canvas.save();

        canvas.translate(left, top);

        invalidate(left - width, top - height, right + width, bottom + height);
        child.draw(canvas);

        canvas.restoreToCount(restoreTo);

        return true;
    }
}

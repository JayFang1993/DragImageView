package info.fangjie.dragimageview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import info.fangjie.dragimageview.utils.MatrixUtils;

/**
 * Created by FangJie on 15/9/3.
 */
public class RotateRelativeLayout extends RelativeLayout {

    private Matrix matrix;

    public RotateRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RotateRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RotateRelativeLayout(Context context) {
        super(context);
        init();
    }

    private void init() {
        matrix=new Matrix();
    }


    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        Matrix newMatrix= MatrixUtils.removeScale(matrix);
        canvas.setMatrix(newMatrix);
        super.dispatchDraw(canvas);
        canvas.restore();
    }

}

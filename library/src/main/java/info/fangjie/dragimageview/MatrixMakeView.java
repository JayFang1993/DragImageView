package info.fangjie.dragimageview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by FangJie on 15/9/3.
 */
public class MatrixMakeView extends TextView {

    public MatrixMakeView(Context context) {
        super(context);
        this.mMatrix = new Matrix();
    }

    public MatrixMakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mMatrix = new Matrix();
    }

    private Matrix mMatrix;

    public void setMatrix(Matrix matrix)
    {
        mMatrix = matrix;
        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();

        canvas.concat(MatrixUtils.removeScale(mMatrix));
        super.draw(canvas);
        canvas.restore();
    }
}

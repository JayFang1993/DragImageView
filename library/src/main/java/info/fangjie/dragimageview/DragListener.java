package info.fangjie.dragimageview;

import android.graphics.Matrix;

/**
 * Created by FangJie on 15/8/27.
 */
public interface DragListener {

    public static final int LEFT = 1;
    public static final int RIGHT = 2;

    //手动拖拽使得Dragimageview矩阵变换
    public static final int STATE_DRAGING = 100;
    //MOVE动画使得Dragimageview矩阵变换
    public static final int STATE_NOT_DRAGING = 101;

    /**
     * 拖动过程中
     */
    public void onDrag(Matrix matrix, int state);

    /**
     * 移出动画结束之后
     */
    public void onDragOutFinish(int direction);

    /**
     * 切出动画开始
     */
    public void onDragOut(int direction);

    /**
     * RESET动画开始
     */
    public void onDragReset(int direction);

}

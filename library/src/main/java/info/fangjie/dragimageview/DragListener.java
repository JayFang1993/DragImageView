package info.fangjie.dragimageview;

import android.graphics.Matrix;

/**
 * Created by FangJie on 15/8/27.
 */
public interface DragListener {

    public static final int LEFT=1;
    public static final int RIGHT=2;

    public static final int STATE_DRAGING=100;
    public static final int STATE_RESET_FROM_RIGHT=101;
    public static final int STATE_RESET_FROM_LEFT=102;
    public static final int STATE_DRAG_TO_LEFT=103;
    public static final int STATE_DRAG_TO_RIGHT=104;
    public static final int STATE_RESET_REBOUND=105;

    /**
     * 拖动过程中
     * @param matrix
     */
    public void onDrag(Matrix matrix, int state);

    /**
     * 手指拖拽之后有两种结果
     *
     * 1.恢复原样  onDragReset
     * 2.移出去，切换  onDragOut （又分左边切出还是右边切出）
     *
     */

    /**
     * 切出动画结束之后
     * @param direction
     */
    public void onDragOutFinish(int direction);

    /**
     * 开始切出动画
     */
    public void onDragOut(int direction);

    /**
     * 开始reset动画
     */
    public void onDragReset(int direction);


    /**
     * reset动画结束之后
     */
    public void onDragResetFinish();
}

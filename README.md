# DragImageView

![](https://camo.githubusercontent.com/3f7996bf7bd441deb7199c498aaa835164dee8da/68747470733a2f2f696d672e736869656c64732e696f2f6475622f6c2f766962652d642e737667)

<img src="https://raw.githubusercontent.com/JayFang1993/DragImageView/master/example/src/main/res/drawable/icon.png">

一个可拖拽变换的Imageview，且可以使其他组件伴随变换

<img src="https://raw.githubusercontent.com/JayFang1993/DragImageView/master/example/demo2.gif" width="40%" height="40%">


### How To Use

* 1.每一层必须要用Fragment嵌套DragImageView，如果该层还有其他布局，也要在Fragment内，如果该布局需要随DragImageView共同变换的话，需要嵌套在RotateRelativeLayout内。

```
    <FrameLayout
        android:id="@+id/fl_bottom"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <info.fangjie.dragimageview.DragImageView
            android:layout_width="fill_parent"
            android:id="@+id/dragview_bottom"
            android:background="#00ffffff"
            android:src="@drawable/cute_or_not_default"
            android:layout_height="fill_parent" />

        <info.fangjie.dragimageview.RotateRelativeLayout
            android:layout_width="fill_parent"
            android:id="@+id/rl_bottom"
            android:layout_height="fill_parent">
            <TextView
                android:layout_width="wrap_content"
                android:gravity="left"
                android:id="@+id/tv_mark_bottom"
                android:textSize="16sp"
                android:layout_centerHorizontal="true"
                android:layout_marginTop="60dp"
                android:textColor="#ffffff"
                android:layout_height="wrap_content" />
        </info.fangjie.dragimageview.RotateRelativeLayout>
    </FrameLayout>
```

* 2.每一层布局对应一个LayerView

```

public class MyLayerView extends BaseLayerView {

    MyLayerView(int layerOrder){
        super(layerOrder);
    }

    public RotateRelativeLayout relativeLayout;

    public TextView textView;

}

```

* 3.每层的DragImagview需要实现 DragListener，实现事件响应。

```

        layerViewBottom.dragImageView.setDragListener(new DragListener() {
            @Override
            public void onDrag(Matrix matrix, int state) {
                
            }

            @Override
            public void onDragOutFinish(int direction) {

            }

            @Override
            public void onDragOut(int direction) {

            }

            @Override
            public void onDragReset(int direction) {

            }
        });
```

* More... see example

### 使用此项目的应用有：
* 如果宠物

<img src="https://raw.githubusercontent.com/JayFang1993/DragImageView/master/example/demo.gif" width="40%" height="40%">






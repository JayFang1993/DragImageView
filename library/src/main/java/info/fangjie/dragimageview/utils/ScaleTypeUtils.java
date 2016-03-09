package info.fangjie.dragimageview.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import info.fangjie.dragimageview.DragImageView;

/**
 * Created by FangJie on 15/10/13.
 */
public class ScaleTypeUtils {

    /**
     * help DragImageView to CENTER_CORP
     * @param imageView
     * @param bitmap
     * @return
     */
    public static Matrix centerCorpImageview(DragImageView imageView,Bitmap bitmap){
        float height,width;
        int x1=0,y1=0;
        float scale=1;

        if (imageView.imageViewHeight/bitmap.getHeight()>imageView.imageViewWidth/bitmap.getWidth()){
            height=bitmap.getHeight();
            scale=imageView.imageViewHeight/height;
            width=imageView.imageViewWidth/scale;
            x1=(int)((bitmap.getWidth()-width)/2);
            y1=0;
        }else{
            width=bitmap.getWidth();
            scale=imageView.imageViewWidth/width;
            height=imageView.imageViewHeight/scale;
            y1=(int)((bitmap.getHeight()-height)/2);
            x1=0;
        }


        //cut the bitmap
        Bitmap cutBitmap = Bitmap.createBitmap(bitmap, x1, y1,(int)width, (int)height);
        imageView.setImageBitmap(cutBitmap);

        //return the matrxi value
        return imageView.resetMatrix();
    }
}

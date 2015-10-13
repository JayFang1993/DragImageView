package info.fangjie.dragimageview;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by FangJie on 15/10/13.
 */
public class BitmapUtils {
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
        Bitmap cutBitmap = Bitmap.createBitmap(bitmap, x1, y1,(int)width, (int)height);
        imageView.setImageBitmap(cutBitmap);
        return imageView.resetMatrix();
    }
}

package info.fangjie.dragimageview;

import android.graphics.Matrix;

/**
 * Created by FangJie on 15/9/3.
 */
public class MatrixUtils {

    public static final int ROTATE_CENTER_X=200;
    public static final int ROTATE_CENTER_Y=10000;

    public static Matrix removeScale(Matrix matrix){
        Matrix newMatrix=new Matrix();
        float[] v = new float[9];
        matrix.getValues(v);
        // translation is simple
        float tx = v[Matrix.MTRANS_X];
        float ty = v[Matrix.MTRANS_Y];
        newMatrix.postTranslate(tx, ty);
        // calculate real scale
        float scalex = v[Matrix.MSCALE_X];
        float skewy = v[Matrix.MSKEW_Y];
        float rScale = (float) Math.sqrt(scalex * scalex + skewy * skewy);
        // calculate the degree of rotation
        float rAngle = Math.round(Math.atan2(v[Matrix.MSKEW_X], v[Matrix.MSCALE_X]) * (180 / Math.PI));
        newMatrix.postRotate(rAngle);
        return newMatrix;
    }

    public static float[] getTransXY(Matrix matrix){
        float[] v = new float[9];
        float[] result=new  float[2];
        matrix.getValues(v);
        // translation is simple
        float tx = v[Matrix.MTRANS_X];
        float ty = v[Matrix.MTRANS_Y];
        result[0]=tx;
        result[1]=ty;
        return result;
    }


    public static float getRotate(Matrix matrix){
        float[] v = new float[9];
        matrix.getValues(v);
        // translation is simple
        float tx = v[Matrix.MTRANS_X];
        float ty = v[Matrix.MTRANS_Y];
        // calculate real scale
        float scalex = v[Matrix.MSCALE_X];
        float skewy = v[Matrix.MSKEW_Y];
        float rScale = (float) Math.sqrt(scalex * scalex + skewy * skewy);
        // calculate the degree of rotation
        float rAngle = Math.round(Math.atan2(v[Matrix.MSKEW_X], v[Matrix.MSCALE_X]) * (180 / Math.PI));
        return rAngle;
    }

}

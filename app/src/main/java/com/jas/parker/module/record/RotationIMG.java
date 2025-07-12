package com.jas.parker.module.record;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.File;

/**
 * Created by bluej on 2016/3/27.
 */
public class RotationIMG {

    public static Bitmap rotationIMG(File IMGFile, Bitmap bitmap) {

        String path = IMGFile.getAbsolutePath();
        try {
            ExifInterface ei = new ExifInterface(path);

            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotateImage(bitmap, 180);
                    break;

            }
            return bitmap;
        } catch (Exception e) {

            return null;
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap result=null;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        if(source!=null) {
            result = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        }
        return result;
       // return source;
    }


}

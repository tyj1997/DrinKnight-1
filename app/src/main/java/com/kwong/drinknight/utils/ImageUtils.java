package com.kwong.drinknight.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by 锐锋 on 2017/10/14.
 */

public class ImageUtils {
    public static Bitmap scaleImg(Bitmap bitmap, float scale) {
        // 获取这个图片的宽和高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 定义预转换成的图片的宽度和高度
        int newWidth = (int)((float)width * scale);
        int newHeight = (int)((float)height * scale);
        // 计算缩放率，新尺寸除原始尺寸
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return resizedBitmap;
    }
}

package com.kwong.drinknight.share;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.kwong.drinknight.utils.ImageUtils;
import com.kwong.drinknight.utils.ScreenUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.kwong.drinknight.utils.MyApplication.getContext;

/**
 * Created by 锐锋 on 2017/10/14.
 */

public class GetBitmap {
    /**
     * 截取scrollview的屏幕
     **/
    public static Bitmap getScrollViewBitmap(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
        }
        // 创建对应大小的bitmap

        bitmap = Bitmap.createBitmap(ScreenUtils.getScreenWidth(scrollView.getContext()), h,
                Bitmap.Config.ARGB_4444);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.parseColor("#f2f7fa"));
        scrollView.draw(canvas);
        return bitmap;
    }

    /**
     * 截图listview
     **/
    public static Bitmap getListViewBitmap(ListView listView, String picpath) {
        int h = 0;
        Bitmap bitmap;
        // 获取listView实际高度
        for (int i = 0; i < listView.getChildCount(); i++) {
            h += listView.getChildAt(i).getHeight();
        }
        listView.getHeight();
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(listView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        listView.draw(canvas);
        // 测试输出
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(picpath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (null != out) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
        }
        return bitmap;
    }

    /**
     * 截图listview
     **/
    public static Bitmap getRecyclerViewBitmap(RecyclerView recyclerView, String picpath) {
        int h = 0;
        Bitmap bitmap;
        // 获取listView实际高度
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            h += recyclerView.getChildAt(i).getHeight();
        }
        recyclerView.getHeight();
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(recyclerView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        recyclerView.draw(canvas);
        // 测试输出
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(picpath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (null != out) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
        }
        return bitmap;
    }

    /**
     * 生成某个view的图片
     *
     * @author gengqiquan
     * @date 2017/3/20 上午10:34
     */
    public static Bitmap getViewDrawingCacheBitmap(View view) {
        view = view.getRootView();
        if (!view.isDrawingCacheEnabled()) {
            view.setDrawingCacheEnabled(true);
        }
        view.destroyDrawingCache();
        view.buildDrawingCache();
        Bitmap bm = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bm;
    }
    public static Bitmap getViewBitmap(View v) {
        Bitmap bitmap =null;
        try{
            Log.d("GetBitmap",String.valueOf(v.getMeasuredWidth()));
            System.out.print("GetBitmap"+String.valueOf(v.getMeasuredWidth()));
            bitmap = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.WHITE);
            v.draw(canvas);

        }catch (Exception e){
            e.printStackTrace();
        }

        return bitmap;
    }
    /**
     * 生成某个LinearLayout的图片
     *
     * @author gengqiquan
     * @date 2017/3/20 上午10:34
     */
    public static Bitmap getLinearLayoutBitmap(LinearLayout linearLayout) {
        int h = 0;
        // 获取LinearLayout实际高度
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            linearLayout.getChildAt(i).measure(0, 0);
            h += linearLayout.getChildAt(i).getMeasuredHeight();
        }
        linearLayout.measure(0, 0);
        // 创建对应大小的bitmap
        Bitmap bitmap = Bitmap.createBitmap(linearLayout.getMeasuredWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);

        linearLayout.draw(canvas);
        return bitmap;
    }

    /**
     *拼接图片
     * @param first 分享的长图
     * @param second  公司logo图
     *@author gengqiquan
     *@date 2017/3/25 下午4:56
     */
    public static Bitmap add2Bitmap(Bitmap first, Bitmap second) {
        float scale = ((float) first.getWidth()) / second.getWidth();
        second = ImageUtils.scaleImg(second, scale);
        int width = first.getWidth();
        int height = first.getHeight() + second.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, 0, 0, null);
        canvas.drawBitmap(second, 0, first.getHeight(), null);
        return result;
    }

    public static String createShareFile(Bitmap bitmap) {
        //Bitmap bitmap = GetBitmap.getViewDrawingCacheBitmap(v);
        //将生成的Bitmap插入到手机的图片库当中，获取到图片路径
        String filePath = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, null, null);
        //及时回收Bitmap对象，防止OOM
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        //转uri之前必须判空，防止保存图片失败
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        return getRealPathFromURI(getContext(), Uri.parse(filePath));
    }
    private static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor == null) {
                return "";
            }
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}

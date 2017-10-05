package com.kwong.drinknight.utils;

import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.kwong.drinknight.R;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.kwong.drinknight.utils.Global.SERVER_URL;

/**
 * Created by 锐锋 on 2017/10/5.
 */

public class UploadImage extends AsyncTask<String,Void,String> {



    @Override
    protected String doInBackground(String... params) {
        Log.d("UploadImage","start");
        String account = params[1];
        final String url = SERVER_URL+"/user/"+account+"/image/"; //此处写上自己的URL
        String path = params[0];
        final Map<String,Object> paramMap = new HashMap<String, Object>(); //文本数据全部添加到Map里
        paramMap.put("title","test");
        paramMap.put("datetime", "1890-06-08T08:08:00Z");
        paramMap.put("time_str", "my test");
        paramMap.put("place", "120");

        //String path = "/storage/emulated/0/DCIM/Camera/IMG20171003220343.jpg"; //此处写上要上传的文件在系统中的路径
        final File pictureFile = new File(path); //通过路径获取文件

        try{
            Log.d("UploadImage","to HttpConnectionUtil");
            HttpConnectionUtil.doPostPicture(url, paramMap, pictureFile);
            return "success";
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if (s == "success"){
            onCancelled();
        }
    }
}

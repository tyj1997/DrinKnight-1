package com.kwong.drinknight.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kwong.drinknight.R;
import com.kwong.drinknight.home_page.DrinkData;
import com.kwong.drinknight.home_page.MainActivity;
import com.kwong.drinknight.user_data_page.UserData;

import org.json.JSONObject;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.kwong.drinknight.utils.Global.SERVER_URL;
import static com.kwong.drinknight.utils.Global.drinkDataList;
import static com.kwong.drinknight.utils.Global.userData;
import static com.kwong.drinknight.utils.Global.volumeDose;
import static java.lang.Float.parseFloat;

/**
 * Created by 锐锋 on 2017/10/6.
 */

public  class UpdateAll {

    public static class UpdateTask extends AsyncTask<Void,Void,Boolean>{
        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                OkHttpClient client1 = new OkHttpClient();
                Request request1 = new Request.Builder()
                        .url(SERVER_URL+"/user/"+userData.getAccount()+"/drinkdatas/#")
                        .build();

                //Log.d("MainActivity","request success");
                Response response1 = client1.newCall(request1).execute();
                String responseData1 = response1.body().string();
                System.out.println(responseData1);
                drinkDataList = parseJSONWithGSONtoDrinkData(responseData1);
                //Log.d("MainActivity","GSON success"+drinkDataList.size());
                //获得用户数据

                OkHttpClient client2 = new OkHttpClient();
                Request request2 = new Request.Builder()
                        .url(SERVER_URL+"/user/"+userData.getAccount()+"/profile/#")
                        .build();
                //Log.d("MainActivity","request2 success");
                Response response2 = client2.newCall(request2).execute();
                String responseData2 = response2.body().string();
                userData = parseJSONWithGSONtoUserData(responseData2);

            }catch (Exception e){
                e.printStackTrace();
        }
        return true;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            //updateUI();
            super.onPostExecute(aBoolean);
        }
    }
    //解析json格式数据
   public static List parseJSONWithGSONtoDrinkData(String jsonData) {
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            volumeDose=parseFloat(jsonObject.getString("volume_dose"));

        }
        catch (Exception e){
            e.printStackTrace();
        }
        List<DrinkData> drinkDatas = gson.fromJson(jsonData.substring(jsonData.indexOf("["),jsonData.lastIndexOf("}")), new TypeToken<List<DrinkData>>(){}.getType());
        return drinkDatas;
    }
    public static UserData parseJSONWithGSONtoUserData(String jsonData) {
        Gson gson = new Gson();
        UserData userData = gson.fromJson(jsonData, UserData.class);
        return userData;
    }
}

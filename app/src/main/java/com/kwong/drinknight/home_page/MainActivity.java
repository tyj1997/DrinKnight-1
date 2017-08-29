package com.kwong.drinknight.home_page;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kwong.drinknight.R;
import com.kwong.drinknight.background_service.NotifyService;
import com.kwong.drinknight.drinking_data_view.TodayData;
import com.kwong.drinknight.ranking_page.RankingActivity;
import com.kwong.drinknight.user_data_page.UserData;
import com.kwong.drinknight.user_data_page.UserDataActivity;


import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.Float.parseFloat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static TextView volumeDoseText;
    private static TextView lastDoseText;
    private static TextView lastTimeText;
    private static TextView userNameText;
    private static TextView suggestedVolumeDoseText;
    private static TextView suggestedNextTimeText;
    private static UserData userData;
    private static List<DrinkData>drinkDataList;
    public static String suggestedVolumeDose;
    public static String suggestedNextTime;
    private static MySinkingView mSinkingView;
    public static ArrayList<String>DrinkingTime=new ArrayList<>();
    public static ArrayList<Integer>DrinkingDose=new ArrayList<>();
    public static double sumdrink;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        volumeDoseText = (TextView)findViewById(R.id.volume_dose);
        lastDoseText = (TextView)findViewById(R.id.last_dose);
        lastTimeText = (TextView)findViewById(R.id.last_time);
        userNameText = (TextView)findViewById(R.id.user_name);
        suggestedNextTimeText = (TextView)findViewById(R.id.suggested_next_time);
        suggestedVolumeDoseText = (TextView)findViewById(R.id.suggested_volume_dose);

        sendRequestWithOkHttp();
        mSinkingView = (MySinkingView) findViewById(R.id.sinking);
        intent = new Intent(this, NotifyService.class);
        //开启关闭Service
        startService(intent);
        //设置一个Toast来提醒使用者提醒的功能已经开始
        Toast.makeText(MainActivity.this,"提醒喝水的功能已经开启",Toast.LENGTH_SHORT).show();
        Button userDataButton = (Button)findViewById(R.id.user_data_bt);
        Button todayDataButton = (Button)findViewById(R.id.today_data_bt);
        Button rankListButton = (Button)findViewById(R.id.rank_list_bt);


        userDataButton.setOnClickListener(this);
        todayDataButton.setOnClickListener(this);
        rankListButton.setOnClickListener(this);
    }

    public static void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //Log.d("MainActivity","into try");
                    //获得饮水数据
                    OkHttpClient client1 = new OkHttpClient();
                    Request request1 = new Request.Builder()
                            .url("http://192.168.87.2/drinking_data.json")
                            //.url("http://140.255.159.226:9090/drinking_data.json")
                            .build();
                    //Log.d("MainActivity","request success");
                    Response response1 = client1.newCall(request1).execute();
                    String responseData1 = response1.body().string();
                    drinkDataList = parseJSONWithGSONtoDrinkData(responseData1);
                    //Log.d("MainActivity","GSON success"+drinkDataList.size());
                    //获得用户数据
                    OkHttpClient client2 = new OkHttpClient();
                    Request request2 = new Request.Builder()
                            .url("http://192.168.87.2/user_data.json")
                            //.url("http://140.255.159.226:9090/user_data.json")
                            //.url("http://10.8.189.234/user_data.json")
                            .build();
                    //Log.d("MainActivity","request2 success");
                    Response response2 = client2.newCall(request2).execute();
                    String responseData2 = response2.body().string();
                    userData = parseJSONWithGSONtoUserData(responseData2);
                    //Log.d("MainActivity","GSON2 success"+userData.getName());
                    Activity a = (Activity)volumeDoseText.getContext();
                    handleDatas(a,drinkDataList,userData);
                    //Log.d("MainActivity","HANDLE success");
                    //showUserData(userData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showUserData(UserData userData) {
        String userPortrait;
        String userName;
        String userId;
        String userGender;
        String userAge;
        String userHeight;
        String userWeight ;
        userPortrait = userData.getPortraitName();
        Log.d("MainActivity","userPortraitName"+userPortrait);
        userName = userData.getName();
        userId = userData.getId();
        userGender = userData.getGender();
        userAge = userData.getAge();
        userHeight = userData.getHeight();
        userWeight = userData.getWeight();
        String[] userDatas ={userPortrait,userName,userId,userGender,userAge,userHeight,userWeight};
        Intent intent = new Intent(MainActivity.this,UserDataActivity.class);
        intent.putExtra("user_datas",userDatas);
        startActivity(intent);
    }

    //解析json格式数据
    private static List parseJSONWithGSONtoDrinkData(String jsonData) {
        Gson gson = new Gson();
        List<DrinkData> drinkDatas = gson.fromJson(jsonData, new TypeToken<List<DrinkData>>(){}.getType());
        return drinkDatas;
    }
    private static UserData parseJSONWithGSONtoUserData(String jsonData) {
        Gson gson = new Gson();
        UserData userData = gson.fromJson(jsonData, UserData.class);
        return userData;
    }

    //处理数据
    public static void handleDatas(Activity activity, List<DrinkData> drinkDataList, UserData userData) {
        float volumeDose = 0;
        String lastTime = new String();
        String lastDose = new String();
        String userName = new String();
        //Log.d("MainActivity","handleDatas "+drinkDataList.size()+" "+drinkDataList.get(0).getDose());
        for (DrinkData oneData : drinkDataList) {
            //Log.d("MainActivity","handleDatas "+oneData.getDose()+" "+oneData.getName()+" ");
            volumeDose = volumeDose + parseFloat(oneData.getDose());
            lastTime = oneData.getTime();
            lastDose = oneData.getDose();
            userName = oneData.getName();
            DrinkingTime.add(lastTime);
            DrinkingDose.add(Integer.valueOf(lastDose));

        }
        //Log.d("MainActivity","handleDatas ok");
        sumdrink=volumeDose;
        suggestedVolumeDose = calculateSuggest(userData);
        suggestedNextTime = lastTime + 30;
        final float per = (volumeDose / Float.parseFloat(suggestedVolumeDose));
        if (per <= 1) {
            mSinkingView.setPercent(per);
            //Log.d("MainActivity","mSinkingView.setPercent"+per);
            //percent += 0.01f;
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            mSinkingView.setPercent(1);
        }
        //Log.d("MainActivity","mSinkingView.setPercent success");
        final String finalVolumeDose = String.valueOf(volumeDose);
        final String finalLastDose = lastDose;
        final String finalLastTime = lastTime;
        final String finalUserName = userName;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                volumeDoseText.setText(finalVolumeDose);
                lastDoseText.setText(finalLastDose);
                lastTimeText.setText(finalLastTime);
                userNameText.setText(finalUserName);
                suggestedVolumeDoseText.setText(suggestedVolumeDose);
                suggestedNextTimeText.setText(suggestedNextTime);

            }
        });
        //Log.d("MainActivity","handleDatas end");
    }
    private static String calculateSuggest(UserData userData) {
        int age = Integer.parseInt(userData.getAge());
        int height = Integer.parseInt(userData.getHeight());
        int weight = Integer.parseInt(userData.getWeight());
        if ((userData.getGender()).equals("man")){
            return String.valueOf((age-40)*5+height*10+weight*20+1000);
        }else {
            return String.valueOf((age-40)*5+height*10+weight*20+2000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在Activity被关闭后，关闭Service
        stopService(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_data_bt:
                showUserData(userData);
                break;
            case R.id.rank_list_bt:
                Intent intent = new Intent(MainActivity.this,RankingActivity.class);
                startActivity(intent);
                break;
            case R.id.today_data_bt:
                Intent intent1 = new Intent(MainActivity.this,TodayData.class);
                intent1.putExtra("drink_time",DrinkingTime);
                intent1.putExtra("drink_dose",DrinkingDose);
                intent1.putExtra("drink_sum",sumdrink);
                startActivity(intent1);
            default:
                break;
        }
    }



}

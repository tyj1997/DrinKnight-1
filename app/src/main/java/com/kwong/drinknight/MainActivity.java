package com.kwong.drinknight;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;


import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.Float.parseFloat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView volumeDoseText;
    private TextView lastDoseText;
    private TextView lastTimeText;
    private TextView userNameText;
    private TextView suggestedVolumeDoseText;
    private TextView suggestedNextTimeText;
    private UserData userData;

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
    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Log.d("MainActivity","into try");
                    //获得饮水数据
                    OkHttpClient client1 = new OkHttpClient();
                    Request request1 = new Request.Builder()
                            //.url("http://192.168.87.2/drinking_data.json")
                            .url("http://140.255.159.226:9090/drinking_data.json")
                            .build();
                    Log.d("MainActivity","request success");
                    Response response1 = client1.newCall(request1).execute();
                    String responseData1 = response1.body().string();
                    List<DrinkData>drinkDataList = parseJSONWithGSONtoDrinkData(responseData1);
                    Log.d("MainActivity","GSON success");
                    //获得用户数据
                    OkHttpClient client2 = new OkHttpClient();
                    Request request2 = new Request.Builder()
                            //.url("http://192.168.87.2/user_data.json")
                            .url("http://140.255.159.226:9090/user_data.json")
                            .build();
                    Log.d("MainActivity","request2 success");
                    Response response2 = client2.newCall(request2).execute();
                    String responseData2 = response2.body().string();
                    userData = parseJSONWithGSONtoUserData(responseData2);
                    Log.d("MainActivity","GSON2 success");
                    handleDatas(drinkDataList,userData);
                    Log.d("MainActivity","HANDLE success");
                    //showUserData(userData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showUserData(UserData userData) {
        String userName= new String();
        String userId = new String();
        String userGender= new String();
        String userAge = new String();
        String userHeight = new String();
        String userWeight = new String();
        userName = userData.getName();
        userId = userData.getId();
        userGender = userData.getGender();
        userAge = userData.getAge();
        userHeight = userData.getHeight();
        userWeight = userData.getWeight();
        String[] userDatas ={userName,userId,userGender,userAge,userHeight,userWeight};
        Intent intent = new Intent(MainActivity.this,UserDataActivity.class);
        intent.putExtra("user_datas",userDatas);
        startActivity(intent);
    }

    //解析json格式数据
    private List parseJSONWithGSONtoDrinkData(String jsonData) {
        Gson gson = new Gson();
        List<DrinkData> drinkDatas = gson.fromJson(jsonData, new TypeToken<List<DrinkData>>(){}.getType());
        return drinkDatas;
    }
    private UserData parseJSONWithGSONtoUserData(String jsonData) {
        Gson gson = new Gson();
        UserData userData = gson.fromJson(jsonData, UserData.class);
        return userData;
    }

    //处理数据
    private void handleDatas(List<DrinkData> drinkDataList,UserData userData) {
        float volumeDose = 0;
        String lastTime = new String();
        String lastDose = new String();
        String userName = new String();
        for (DrinkData oneData : drinkDataList){
            volumeDose = volumeDose+parseFloat(oneData.getDose());
            lastTime = oneData.getTime();
            lastDose = oneData.getDose();
            userName = oneData.getName();
        }
        
        final String finalVolumeDose = String.valueOf(volumeDose);
        final String finalLastDose = lastDose;
        final String finalLastTime = lastTime;
        final String finalUserName = userName;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                volumeDoseText.setText(finalVolumeDose);
                lastDoseText.setText(finalLastDose);
                lastTimeText.setText(finalLastTime);
                userNameText.setText(finalUserName);
            }
        });
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
            default:
                break;
        }
    }
}

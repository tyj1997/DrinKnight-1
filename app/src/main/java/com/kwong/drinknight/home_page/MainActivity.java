package com.kwong.drinknight.home_page;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.internal.BottomNavigationItemView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kwong.drinknight.R;
import com.kwong.drinknight.background_service.NotifyService;
import com.kwong.drinknight.drinking_data_view.TodayData;
import com.kwong.drinknight.login.LoginActivity;
import com.kwong.drinknight.login.Register;
import com.kwong.drinknight.ranking_page.RankingActivity;
import com.kwong.drinknight.user_data_page.UserData;
import com.kwong.drinknight.user_data_page.UserDataActivity;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.Float.parseFloat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static TextView volumeDoseText;
    private static TextView lastDoseText;
    private static TextView lastTimeText;
    private static TextView userNameText;
    public static int fuck1;
    private static TextView suggestedVolumeDoseText;
    private static TextView suggestedNextTimeText;
    private static UserData userData;
    private static List<DrinkData>drinkDataList;
    public static String suggestedVolumeDose;
    public static String suggestedNextTime;
    private static MySinkingView mSinkingView;
    public static ArrayList<String>DrinkingTime=new ArrayList<>();
    public static ArrayList<Integer>DrinkingDose=new ArrayList<>();
    private static LinkedHashMap<String,HashMap<String,Double>> DrinkDateTime = new LinkedHashMap<String,HashMap<String,Double>>();
    private static String id;
    public static double sumdrink;
    public static  float volumeDose = 0;
    Intent intent;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                   ;
                    break;
                case 1:

                    break;
                case 2:

                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
        Intent intent=getIntent();
        id=intent.getStringExtra("user_id");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        View v =findViewById(R.id.layouts);
        v.getBackground().setAlpha(180);
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
        Button toDrinkButton=(Button)findViewById(R.id.to_drink);
        toDrinkButton.setOnClickListener(this);
        userDataButton.setOnClickListener(this);
        todayDataButton.setOnClickListener(this);
        rankListButton.setOnClickListener(this);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
    }

    public static void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //Log.d("MainActivity","into try");
                    //获得当天饮水数据
                    OkHttpClient client1 = new OkHttpClient();
                    Request request1 = new Request.Builder()
                            //.url("http://192.168.87.2/drinking_data.json")
                            //.url("http://140.255.159.226:9090/drinking_data.json")
                            //.url("http://10.8.189.234/drinking_data.json")
                            //.url("http://10.206.13.81:8089/user/"+id+"/drinkdatas/2017/9/8/")
                            .url("http://10.8.188.98:8000/user/"+id+"/drinkdatas/2017/9/10/")

                            .build();
                    System.out.println(id);
                    //Log.d("MainActivity","request success");
                    Response response1 = client1.newCall(request1).execute();

                    String responseData1 = response1.body().string();
                    drinkDataList = parseJSONWithGSONtoDrinkData(responseData1);
                    //Log.d("MainActivity","GSON success"+drinkDataList.size());
                    //获得用户数据

                    OkHttpClient client2 = new OkHttpClient();
                    Request request2 = new Request.Builder()
                            //.url("http://192.168.87.2/user_data.json")
                            //.url("http://140.255.159.226:9090/user_data.json")
                           // .url("http://10.8.189.234/user_data.json")
                           // .url("http://10.206.13.81:8089/user/"+id+"/profile/#")
                             .url("http://10.8.188.98:8000/user/"+id+"/profile/#")
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
        String userName;
        String account;
        String potrait;
        String phoneNumber;
        String emailAddress;
        String registerTime;
        String gender;
        int userAge;
        float userHeight;
        float userWeight ;

        Log.d("MainActivity","userPortraitName");
        userName = userData.getUserName();

        account = userData.getAccount();
        gender = userData.getGender();
        potrait=userData.getPortrait();
        userAge = userData.getAge();
        userHeight = userData.getHeight();
        userWeight = userData.getWeight();
        phoneNumber=userData.getPhoneNumber();
        emailAddress=userData.getEmailAddress();
        registerTime=userData.getRegisterTime();
        String[] userDatas ={potrait,userName,account,gender,String.valueOf(userAge),String.valueOf(userHeight),String.valueOf(userWeight),phoneNumber,emailAddress,registerTime};
        Intent intent = new Intent(MainActivity.this,UserDataActivity.class);
        intent.putExtra("user_datas",userDatas);
        startActivityForResult(intent,1);
    }

    //解析json格式数据
    private static List parseJSONWithGSONtoDrinkData(String jsonData) {
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
    private static UserData parseJSONWithGSONtoUserData(String jsonData) {
        Gson gson = new Gson();
        UserData userData = gson.fromJson(jsonData, UserData.class);
        return userData;
    }

    //处理数据
    public static void handleDatas(Activity activity, List<DrinkData> drinkDataList, UserData userData) {

        String lastTime = new String();
        String lastDose = new String();
        String userName = new String();
        String lastDate = new String();
        userName=userData.getUserName();
        //Log.d("MainActivity","handleDatas "+drinkDataList.size()+" "+drinkDataList.get(0).getDose());
            for (DrinkData oneData : drinkDataList) {
                //Log.d("MainActivity","handleDatas "+oneData.getDose()+" "+oneData.getName()+" ");
                lastTime = oneData.getTime();
                lastDose = oneData.getDose();
                volumeDose+=Float.parseFloat(oneData.getDose());
                //       lastDate=oneData.getDate();
//            if(!DrinkDateTime.containsKey(lastDate)){
//                DrinkDateTime.put(lastDate,new HashMap<String, Double>());
//            }
//            if(!DrinkDateTime.get(lastDate).containsKey(lastTime)){
//                DrinkDateTime.get(lastDate).put(lastTime,Double.valueOf(lastDose));
//
//            }

//            if(!DrinkingTime.contains(lastTime)) {
//                DrinkingTime.add(lastTime);
//                DrinkingDose.add(Integer.valueOf(lastDose));
//            }
            }


            //Log.d("MainActivity","handleDatas ok");
            sumdrink = volumeDose;

        System.out.println(sumdrink+"444");
            suggestedVolumeDose = calculateSuggest(userData);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = sdf.parse(lastTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar ca = Calendar.getInstance();
            ca.setTime(date);
            ca.add(Calendar.HOUR_OF_DAY, 1);
            suggestedNextTime = sdf.format(ca.getTime());
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
            } else {
                mSinkingView.setPercent(1);
            }
            //Log.d("MainActivity","mSinkingView.setPercent success");
            final String finalVolumeDose = String.valueOf(volumeDose);
            final String finalLastDose = lastDose;
            final String finalLastTime = lastTime;
            final String finalUserName = userName;
            //  System.out.println(finalVolumeDose);
            //     System.out.println(userName);
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
//        else {
//            final  String userNames=userName;
//            activity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    volumeDoseText.setText("您今天还未喝水");
//                    lastDoseText.setText("您今天还未喝水");
//                    lastTimeText.setText("您今天还未喝水");
//                    userNameText.setText(userNames);
//                    suggestedVolumeDoseText.setText(suggestedVolumeDose);
//                    suggestedNextTimeText.setText(suggestedNextTime);
//
//                }
//            });
//        }
        //Log.d("MainActivity","handleDatas end");
        volumeDose=0;
    }
    private static String calculateSuggest(UserData userData) {
        int age = userData.getAge();
        float height = userData.getHeight();
        float weight = userData.getWeight();
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
                //intent1.putExtra("drink_time",DrinkingTime);
               // intent1.putExtra("drink_dose",DrinkingDose);
                intent1.putExtra("drink_datetime",DrinkDateTime);
                intent1.putExtra("drink_sum",sumdrink);
                startActivity(intent1);
                break;
            case R.id.to_drink:
                System.out.println("6666");
                getWater();
                System.out.println("6666");
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){

            this.finish();  //finish当前activity
            overridePendingTransition(R.anim.in_from_left,
                    R.anim.out_from_right);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    userData = parseJSONWithGSONtoUserData(data.getStringExtra("json_return"));
                    handleDatas((Activity)volumeDoseText.getContext(),drinkDataList,userData);
                }
                break;
            default:
        }
    }
    protected void  getWater(){
        final EditText et = new EditText(this);
    System.out.println("6666");
        new AlertDialog.Builder(this).setTitle("请输入饮水量(ml)")
                .setIcon(android.R.drawable.btn_star)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        SentWaterinfo(input);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
    protected void  SentWaterinfo(final String dose){

        SimpleDateFormat formatter  =  new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");
        Date curDate  =   new Date(System.currentTimeMillis());//获取当前时间
        String time  =  formatter.format(curDate);
                 //   String urlPath = "http://10.206.13.81:8089/user/"+ id+"/drinkdatas";
                 String urlPath = "http://10.8.188.98:8000/user/"+ id+"/drinkdatas/";
                 URL urls;
                 try {
                     urls=new URL(urlPath);
                     JSONObject jsonObject = new JSONObject();
                     jsonObject.put("dose",Float.parseFloat(dose));
                     OkHttpClient okHttpClient = new OkHttpClient();
                     //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
                     String content = String.valueOf(jsonObject);
                     HttpURLConnection conn = (HttpURLConnection) urls.openConnection();
                     conn.setConnectTimeout(5000);
                     conn.setDoOutput(true);
                     conn.setRequestMethod("POST");
                     conn.setRequestProperty("ser-Agent", "Fiddler");// 设置contentType
                     conn.setRequestProperty("Content-Type", "application/json");
                     OutputStream os = conn.getOutputStream();
                     os.write(content.getBytes());
                     os.flush();
                     os.close();
                     System.out.println();
                     int code = conn.getResponseCode();
                     System.out.println(code);
                     System.out.println(content);
                     System.out.println(conn.getContent().toString());
                     if (code == 200) {
                         Toast.makeText(MainActivity.this, "喝水成功", Toast.LENGTH_SHORT).show();
                        sendRequestWithOkHttp();
                     }
                     else
                     {
                         Toast.makeText(MainActivity.this, "喝水失败", Toast.LENGTH_SHORT).show();
                     }
                 }
                 catch (Exception e){
                     e.printStackTrace();

                 }
             }

}

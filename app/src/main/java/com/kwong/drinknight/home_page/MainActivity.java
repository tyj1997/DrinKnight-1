package com.kwong.drinknight.home_page;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kwong.drinknight.R;
import com.kwong.drinknight.background_service.NotifyService;
import com.kwong.drinknight.drinking_data_view.TodayData;
import com.kwong.drinknight.ranking_page.RankingActivity;
import com.kwong.drinknight.step_page.StepActivity;
import com.kwong.drinknight.user_data_page.UserData;
import com.kwong.drinknight.user_data_page.UserDataActivity;
import com.kwong.drinknight.utils.UpdateAll;


import org.json.JSONObject;

import java.io.BufferedReader;
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

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;

import static com.kwong.drinknight.utils.Global.SERVER_URL;
import static com.kwong.drinknight.utils.Global.drinkDataList;
import static com.kwong.drinknight.utils.Global.suggestedVolumeDose;
import static com.kwong.drinknight.utils.Global.suggestedNextTime;
import static com.kwong.drinknight.utils.Global.sumdrink;
import static com.kwong.drinknight.utils.Global.userData;
import static com.kwong.drinknight.utils.Global.volumeDose;
import static com.kwong.drinknight.utils.UpdateAll.parseJSONWithGSONtoUserData;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static TextView volumeDoseText;
    private static TextView lastDoseText;
    private static TextView lastTimeText;
    private static TextView userNameText;
    public static int fuck1;
    private static TextView suggestedVolumeDoseText;
    private static TextView suggestedNextTimeText;


    private static MySinkingView mSinkingView;
    public static ArrayList<String>DrinkingTime=new ArrayList<>();
    public static ArrayList<Integer>DrinkingDose=new ArrayList<>();
    private static LinkedHashMap<String,HashMap<String,Double>> DrinkDateTime = new LinkedHashMap<String,HashMap<String,Double>>();
    private static String id;


    public DrawerLayout mDrawerLayout;
    public CircleImageView userimage;
    Intent intent1;
    public SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
        Intent intent=getIntent();
        id=intent.getStringExtra("user_id");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        View v =findViewById(R.id.drawer_layout);
        v.getBackground().setAlpha(180);
        volumeDoseText = (TextView)findViewById(R.id.volume_dose);
        lastDoseText = (TextView)findViewById(R.id.last_dose);
        lastTimeText = (TextView)findViewById(R.id.last_time);
        userNameText = (TextView)findViewById(R.id.user_name);
        suggestedNextTimeText = (TextView)findViewById(R.id.suggested_next_time);
        suggestedVolumeDoseText = (TextView)findViewById(R.id.suggested_volume_dose);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSinkingView = (MySinkingView) findViewById(R.id.sinking);
        mSinkingView.setTextColor(R.color.colorPrimary);
        updateUI();//更新数据及UI


        intent1 = new Intent(this, NotifyService.class);
        //开启关闭Service
        startService(intent1);
        //设置一个Toast来提醒使用者提醒的功能已经开始
        Toast.makeText(MainActivity.this,"提醒喝水的功能已经开启",Toast.LENGTH_SHORT).show();

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);


        NavigationView navView = (NavigationView)findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        navView.setCheckedItem(R.id.nav_drink_data);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                switch (item.getItemId()){
                    case R.id.nav_drink_data:
                        Intent intent1 = new Intent(MainActivity.this,TodayData.class);
                        intent1.putExtra("user_id",id);
                        startActivity(intent1);
                        break;
                    case R.id.nav_user_data:
                        showUserData(userData);
                        break;
                    case R.id.nav_rank:
                        Intent intent = new Intent(MainActivity.this,RankingActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_step:
                        Intent intent2 = new Intent(MainActivity.this,StepActivity.class);

                        startActivity(intent2);
                        break;
                    default:
                }
                return true;
            }
        });

        FloatingActionButton toDrinkButton=(FloatingActionButton)findViewById(R.id.to_drink);
        toDrinkButton.setOnClickListener(this);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateUI();
            }
        });
    }



    private void showUserData(UserData userData) {
        String userName;
        String account;
        String imageName;
        String phoneNumber;
        String emailAddress;
        String registerTime;
        String gender;
        int userAge;
        float userHeight;
        float userWeight ;

        Log.d("MainActivity","userPortraitName");
        userName = userData.getUserName();
        imageName = userData.getImageName();
        account = userData.getAccount();
        gender = userData.getGender();

        userAge = userData.getAge();
        userHeight = userData.getHeight();
        userWeight = userData.getWeight();
        phoneNumber=userData.getPhoneNumber();
        emailAddress=userData.getEmailAddress();
        registerTime=userData.getRegisterTime();
        String[] userDatas ={userName,account,gender,String.valueOf(userAge),String.valueOf(userHeight),String.valueOf(userWeight),phoneNumber,emailAddress,registerTime,imageName};
        Intent intent = new Intent(MainActivity.this,UserDataActivity.class);
        intent.putExtra("user_datas",userDatas);
        startActivityForResult(intent,1);
    }


    public void updateUI(){
        //Activity a = (Activity)volumeDoseText.getContext();
        swipeRefreshLayout.setRefreshing(true);
        UpdateAll.UpdateTask updateTask = new UpdateAll.UpdateTask();
        updateTask.execute((Void)null);
        try{
            Thread.sleep(1000);

        }catch (InterruptedException e){
            e.printStackTrace();
        }
        if(drinkDataList != null)
        {
            handleDatas((Activity)volumeDoseText.getContext(),drinkDataList,userData);
        }
        swipeRefreshLayout.setRefreshing(false);
    }
    //处理数据
    public static void handleDatas(Activity activity, List<DrinkData> drinkDataList, UserData userData) {

        String lastTime = new String();
        String lastDose = new String();
        String userName = new String();
        String lastDate = new String();
        userName=userData.getUserName();
        volumeDose=0;
        //Log.d("MainActivity","handleDatas "+drinkDataList.size()+" "+drinkDataList.get(0).getDose());
        for (DrinkData oneData : drinkDataList) {
            //Log.d("MainActivity","handleDatas "+oneData.getDose()+" "+oneData.getName()+" ");
            lastTime = oneData.getTime();
            lastDose = String.valueOf(oneData.getDose());
            volumeDose += oneData.getDose();
        }
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
        stopService(intent1);
        super.onDestroy();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                userimage = (CircleImageView)findViewById(R.id.icon_image);
                Glide.with(MainActivity.this).load(SERVER_URL+"/user/"+userData.getAccount()+"/image/").error(R.drawable.user_0).into(userimage);
                TextView username = (TextView)findViewById(R.id.username);
                username.setText(userData.getUserName());
                TextView mail = (TextView)findViewById(R.id.mail);
                mail.setText(userData.getEmailAddress());
                break;
            default:
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
        String line;
        String s="";
        SimpleDateFormat formatter  =  new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");
        Date curDate  =   new Date(System.currentTimeMillis());//获取当前时间
        String time  =  formatter.format(curDate);
                    String urlPath = SERVER_URL+"/user/"+ id+"/drinkdatas/";
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
                     BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                     // Read response until the end
                     while ((line = rd.readLine()) != null) { s += line; }
                     System.out.println(s);
                     if (code == 200) {
                         Toast.makeText(MainActivity.this, "喝水成功", Toast.LENGTH_SHORT).show();
                         updateUI();
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

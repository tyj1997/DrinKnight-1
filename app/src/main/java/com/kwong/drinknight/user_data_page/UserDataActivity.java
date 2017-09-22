package com.kwong.drinknight.user_data_page;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kwong.drinknight.R;
import com.kwong.drinknight.login.Register;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;

import static com.kwong.drinknight.ranking_page.RankingActivity.getBitmap;

public class UserDataActivity extends AppCompatActivity {

   // private String uriStr ="http://10.8.189.234/image/";
    private String uriStr ="http://    192.168.56.1:8888/image/";
   // private String uriStr = "http://192.168.87.2/image/";
    //private String uriStr = "http://140.255.159.226:9090/image/";
    private ImageView userPortrait;
    private EditText userNameText;
    private EditText userGenderText;
    private EditText userIdText;
    private EditText userAgeText;
    private EditText userHeightText;
    private EditText userWeightText;
    private EditText userPhoneNumberText;
    private EditText userEmailText;
    private EditText userRegisterTimeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user_data);
        Intent intent = getIntent();
        String[] datas = intent.getStringArrayExtra("user_datas");
        userPortrait = (ImageView) findViewById(R.id.user_portrait);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
        showPortrait(datas[0]);
        userNameText = (EditText) findViewById(R.id.user_name);
        userNameText.setText(datas[1]);
        userIdText = (EditText) findViewById(R.id.user_id);
        userIdText.setText(datas[2]);
        userIdText.setFocusable(false);
        userIdText.setFocusableInTouchMode(false);
        userGenderText = (EditText) findViewById(R.id.user_gender);
        userGenderText.setText(datas[3]);
        userAgeText = (EditText) findViewById(R.id.user_age);
        userAgeText.setText(datas[4]);
        userHeightText = (EditText) findViewById(R.id.user_height);
        userHeightText.setText(datas[5]);
        userWeightText = (EditText) findViewById(R.id.user_weight);
        userWeightText.setText(datas[6]);
        userPhoneNumberText=(EditText) findViewById(R.id.user_phonenumber);
        userPhoneNumberText.setText(datas[7]);
        userEmailText=(EditText) findViewById(R.id.user_email);
        userEmailText.setText(datas[8]);
        userRegisterTimeText=(EditText) findViewById(R.id.user_registerTime);
        userRegisterTimeText.setText(datas[9]);
        userRegisterTimeText.setFocusable(false);
        userRegisterTimeText.setFocusableInTouchMode(false);
    }

    private void showPortrait(final String data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("UserDataActivity", "Bitmap前"+data);
                Bitmap portrait = null;
//                try {
//                    portrait = getBitmap(uriStr +data);
//                    Log.d("UserDataActivity",uriStr +data);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                Log.d("UserDataActivity", "Bitmap后");
                final Bitmap finalPortrait = portrait;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        userPortrait.setImageBitmap(finalPortrait);
                        Log.d("UserDataActivity", "Bitmap 放入");
                    }
                });

            }

        }).start();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item:
                UpdateUserData();
                finish();
                break;
            default:
                break;
        }
        return true;
    }
    public void UpdateUserData(){
        String urlPath = "http://10.206.13.81:8089/user/"+ userIdText.getText().toString()+"/profile/#";
        //String urlPath = "http://10.8.188.98:8000/user/"+ userIdText.getText().toString()+"/profile/#";
        URL urls;
        try {
            urls=new URL(urlPath);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userName", userNameText.getText().toString());
            jsonObject.put("account", userIdText.getText().toString());

                jsonObject.put("gender",userGenderText.getText());
            jsonObject.put("emailAddress", userEmailText.getText().toString());
            jsonObject.put("age",Double.parseDouble( userAgeText.getText().toString()));
            jsonObject.put("height", Double.parseDouble(userHeightText.getText().toString()));
            jsonObject.put("phoneNumber", userPhoneNumberText.getText().toString());
            jsonObject.put("weight", Double.parseDouble(userWeightText.getText().toString()));
            OkHttpClient okHttpClient = new OkHttpClient();
            //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
            System.out.println(jsonObject.toString());
            String content = String.valueOf(jsonObject);
            HttpURLConnection conn = (HttpURLConnection) urls.openConnection();
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("ser-Agent", "Fiddler");
            // 设置contentType

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
                InputStream inputStream = null;
                Toast.makeText(UserDataActivity.this, "修改信息成功", Toast.LENGTH_SHORT).show();
                inputStream = conn.getInputStream();
                Intent intent=new Intent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "gbk"));
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }
                content = builder.toString();
                intent.putExtra("json_return",content);
                setResult(RESULT_OK,intent);

            }
            else
            {
                Toast.makeText(UserDataActivity.this, "修改信息失败", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();

        }

    }
}
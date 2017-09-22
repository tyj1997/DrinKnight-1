package com.kwong.drinknight.login;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kwong.drinknight.R;
import com.kwong.drinknight.home_page.MainActivity;

import org.apache.http.client.methods.HttpPost;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/9/8.
 */

public class Register extends AppCompatActivity {

    private TextView UserName;
    private TextView UserAccount;
    private TextView UserPassword;
    private TextView UserPassword2;
    private TextView UserPhonenumber;
    private TextView UserEamil;
    private TextView UserHeight;
    private TextView UserWeight;
    private TextView UserAge;
    private Spinner UserGender;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        UserName=(TextView)findViewById(R.id.user_name);
        UserAccount=(TextView)findViewById(R.id.user_account);
        UserPassword=(TextView)findViewById(R.id.user_password);

        UserPassword2=(TextView)findViewById(R.id.user_password2);
        UserPhonenumber=(TextView)findViewById(R.id.user_phonenumber);
        UserEamil=(TextView)findViewById(R.id.user_email);
        UserHeight=(TextView)findViewById(R.id.user_height);
        UserWeight=(TextView)findViewById(R.id.user_weight);
        UserAge=(TextView)findViewById(R.id.user_age);
        UserGender=(Spinner) findViewById(R.id.user_gender);
        final Button Registers = (Button) findViewById(R.id.sign_in_button);
        Registers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(UserAccount.getText());
                if(UserName.getText().toString().equals("")||UserAccount.getText().toString().equals("")||UserPassword.getText().toString().equals("")
                        ||UserPassword2.getText().toString().equals("") ||UserPhonenumber.getText().toString().equals("")||UserEamil.getText().toString().equals("")
                        ||UserHeight.getText().toString().equals("")|| UserWeight.getText().toString().equals("")||UserAge.getText().toString().equals("")||UserGender.getSelectedItem().toString().equals(""))
                    Toast.makeText(Register.this,"请完整填写信息",Toast.LENGTH_SHORT).show();
                else if(!(UserPassword.getText().toString().equals(UserPassword2.getText().toString())))
                    Toast.makeText(Register.this,"两次输入密码请保持一致",Toast.LENGTH_SHORT).show();
                else {
                    SimpleDateFormat formatter  =  new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");
                    Date curDate  =   new Date(System.currentTimeMillis());//获取当前时间
                    String time  =  formatter.format(curDate);

                   String urlPath = "http://10.206.13.81:8089/register/";

                    //String urlPath = "http://10.8.188.98:8000/register/";
                    URL urls;
                    try {
                         urls=new URL(urlPath);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("userName", UserName.getText().toString());
                        jsonObject.put("account", UserAccount.getText().toString());
                        if (UserGender.getSelectedItem().toString().equals("男"))
                            jsonObject.put("gender","man");
                        if (UserGender.getSelectedItem().toString().equals("女"))
                            jsonObject.put("gender","woman");
                        jsonObject.put("emailAddress", UserEamil.getText().toString());
                        jsonObject.put("age",Double.parseDouble( UserAge.getText().toString()));
                        jsonObject.put("password", UserPassword.getText().toString());
                        jsonObject.put("height", Double.parseDouble(UserHeight.getText().toString()));
                        jsonObject.put("phoneNumber", UserPhonenumber.getText().toString());
                        jsonObject.put("weight", Double.parseDouble(UserWeight.getText().toString()));
                        OkHttpClient okHttpClient = new OkHttpClient();
                        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)

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
                            Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(Register.this, "注册失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();

                    }

                    finish();
                }
            }
        });

    }


//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.sign_in_button:
//                if(UserName.getText()==null||UserAccount.getText()==null||UserPassword.getText()==null||UserPassword2.getText()==null
//                        ||UserPhonenumber.getText()==null ||UserEamil.getText()==null||UserHeight.getText()==null||
//                        UserWeight.getText()==null||UserAge.getText()==null||UserGender.getSelectedItem()==null)
//                    Toast.makeText(Register.this,"请完整填写信息",Toast.LENGTH_SHORT).show();
//                else if(UserPassword.getText().toString()!=UserPassword.getText().toString())
//                    Toast.makeText(Register.this,"两次输入密码请保持一致",Toast.LENGTH_SHORT).show();
//                else {
//                    SimpleDateFormat formatter  =  new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");
//                    Date curDate  =   new Date(System.currentTimeMillis());//获取当前时间
//                    String time  =  formatter.format(curDate);
//                    String urlPath = "http://10.206.13.81:8089/register/"+ UserAccount.getText().toString();
//                    URL urls;
//                    try {
//                        urls = new URL(urlPath);
//                        JSONObject jsonObject = new JSONObject();
//                        jsonObject.put("userName", UserName.getText().toString());
//                        jsonObject.put("account", UserAccount.getText().toString());
//                        jsonObject.put("phoneNumber", UserPhonenumber.getText().toString());
//                        jsonObject.put("weight", UserWeight.getText().toString());
//                        jsonObject.put("gender",UserGender.getSelectedItem().toString());
//                        jsonObject.put("emailAddress", UserEamil.getText().toString());
//                        jsonObject.put("age", UserAge.getText().toString());
//                        jsonObject.put("password", UserPassword.getText().toString());
//                        jsonObject.put("registerTime", time);
//                        jsonObject.put("height", UserHeight.getText().toString());
//                        String content = String.valueOf(jsonObject);
//                        HttpURLConnection conn = (HttpURLConnection) urls.openConnection();
//                        conn.setConnectTimeout(5000);
//                        conn.setDoOutput(true);
//                        conn.setRequestMethod("POST");
//                        conn.setRequestProperty("ser-Agent", "Fiddler");
//                        // 设置contentType
//                        conn.setRequestProperty("Content-Type", "application/json");
//                        OutputStream os = conn.getOutputStream();
//                        os.write(content.getBytes());
//                        os.close();
//                        int code = conn.getResponseCode();
//                        if (code == 200) {
//                            Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    catch (Exception e){
//                        e.printStackTrace();
//                    }
//                    finish();
//                }
//                break;
//            default:
//                break;
//        }
//    }
}
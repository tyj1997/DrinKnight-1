package com.kwong.drinknight.cup_page;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kwong.drinknight.R;
import com.kwong.drinknight.home_page.MainActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.OkHttpClient;

import static com.kwong.drinknight.utils.Global.SERVER_URL;

public class CupActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView cupID1;
    private TextView cupID2;
    private TextView cupID3;
    private String []datas;
    private int count=0;
    private static String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cup);
        Button AddCup=(Button)findViewById(R.id.add_cup);
        AddCup.setOnClickListener(this);
        Button dltCup=(Button)findViewById(R.id.dlt_cup);
        dltCup.setOnClickListener(this);
        Intent intent=getIntent();
        id=intent.getStringExtra("user_id");
        datas = intent.getStringArrayExtra("cup_ids");

        System.out.println(datas[0]);
        cupID1=(TextView)findViewById(R.id.user_cup1);
        cupID2=(TextView)findViewById(R.id.user_cup2);
        cupID3=(TextView)findViewById(R.id.user_cup3);
        cupID1.setText(datas[0]);

        cupID2.setText(datas[1]);
        cupID3.setText(datas[2]);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.add_cup:

                add_cup();
                break;
            case R.id.dlt_cup:
                dlt_cup();
                break;
            default:
                break;
        }
    }
    protected void add_cup()
    {
        final EditText et = new EditText(this);
        new AlertDialog.Builder(this).setTitle("请输入添加水杯ID")
                .setIcon(android.R.drawable.btn_star)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        AddCupinfo(input);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
    protected void AddCupinfo(final String cup_id)
    {
        String line;
        String s="";
        SimpleDateFormat formatter  =  new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");
        Date curDate  =   new Date(System.currentTimeMillis());//获取当前时间
        String time  =  formatter.format(curDate);
        String urlPath = SERVER_URL+"/user/"+ id+"/addCup/"+cup_id+"/";
        System.out.println(urlPath);
        URL urls;
        try {
            urls=new URL(urlPath);
            JSONObject jsonObject = new JSONObject();
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
            if (s.equals("OK")) {
                Toast.makeText(this, "添加水杯成功", Toast.LENGTH_SHORT).show();
                updateUI(0,cup_id);
            }
            else
            {
                Toast.makeText(this, "添加水杯失败", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();

        }
    }
    protected void dlt_cup()
    {

        for(int k=0;k<3;k++)
        {
            if(datas[k]!=null)
                count+=1;
        }
        final String [] cup_info=new String[count];
        int i=0;
        for(int k=0;k<3;k++)
        {

            if(datas[k]!=null)
                cup_info[i++]=datas[k];
        }
        if(count!=0) {
            System.out.println(count);
            System.out.println(cup_info[0]);
            new AlertDialog.Builder(this).setTitle("选择删除杯子ID").setIcon(android.R.drawable.btn_star)
                    .setNegativeButton("取消", null)

                    .setItems(cup_info, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DltCupInfo(cup_info[which]);
                        }
                    })
                    .show();
        }
        else
        {
            Toast.makeText(this, "无法删除水杯", Toast.LENGTH_SHORT).show();
        }
        count=0;
    }
    protected  void DltCupInfo(String cup_id)
    {
        final String dlt=cup_id;
        new AlertDialog.Builder(this).setTitle("确定删除该水杯？")
                .setIcon(android.R.drawable.btn_star)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        DltCup(dlt);

                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
    protected void DltCup(String cup_id)
    {
        String line;
        String s="";

        String urlPath = SERVER_URL+"/user/"+ id+"/dltCup/"+cup_id+"/";
        System.out.println(urlPath);
        URL urls;
        try {
            urls=new URL(urlPath);
            JSONObject jsonObject = new JSONObject();
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
            if (conn.getResponseCode()==200) {
                Toast.makeText(this, "删除水杯成功", Toast.LENGTH_SHORT).show();
                updateUI(1,cup_id);
            }
            else
            {
                Toast.makeText(this, "删除水杯失败", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();

        }
    }
    protected void  updateUI(int flag,String s)
    {
        if(flag==0) {
            if(datas[0]==null)
            {
                datas[0]=s;
                cupID1.setText(s);
            }
            else if(datas[1]==null)
            {
                datas[1]=s;
                cupID2.setText(s);
            }
            else if(datas[2]==null)
            {
                datas[2]=s;
                cupID3.setText(s);
            }
        }
        else if(flag==1)
        {
            if(datas[0]!=null&&datas[0]==s)
            {
                datas[0]=null;
                cupID1.setText(null);
            }
            if(datas[1]!=null&&datas[1]==s)
            {
                datas[1]=null;
                cupID2.setText(null);
            }
            if(datas[2]!=null&&datas[2]==s)
            {
                datas[2]=null;
                cupID3.setText(null);
            }
        }
    }



}

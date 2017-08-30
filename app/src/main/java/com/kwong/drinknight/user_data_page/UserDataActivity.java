package com.kwong.drinknight.user_data_page;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.kwong.drinknight.R;

import java.io.IOException;

import static com.kwong.drinknight.ranking_page.RankingActivity.getBitmap;

public class UserDataActivity extends AppCompatActivity {

    private String uriStr ="http://10.8.189.234/image/";
   // private String uriStr = "http://192.168.87.2/image/";
    //private String uriStr = "http://140.255.159.226:9090/image/";
    private ImageView userPortrait;
    private TextView userNameText;
    private TextView userGenderText;
    private TextView userIdText;
    private TextView userAgeText;
    private TextView userHeightText;
    private TextView userWeightText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user_data);
        Intent intent = getIntent();
        String[] datas = intent.getStringArrayExtra("user_datas");
        userPortrait = (ImageView) findViewById(R.id.user_portrait);

        showPortrait(datas[0]);

        userNameText = (TextView) findViewById(R.id.user_name);
        userNameText.setText(datas[1]);
        userIdText = (TextView) findViewById(R.id.user_id);
        userIdText.setText(datas[2]);
        userGenderText = (TextView) findViewById(R.id.user_gender);
        userGenderText.setText(datas[3]);
        userAgeText = (TextView) findViewById(R.id.user_age);
        userAgeText.setText(datas[4]);
        userHeightText = (TextView) findViewById(R.id.user_height);
        userHeightText.setText(datas[5]);
        userWeightText = (TextView) findViewById(R.id.user_weight);
        userWeightText.setText(datas[6]);
    }

    private void showPortrait(final String data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("UserDataActivity", "Bitmap前"+data);
                Bitmap portrait = null;
                try {
                    portrait = getBitmap(uriStr +data);
                    Log.d("UserDataActivity",uriStr +data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
}
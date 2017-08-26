package com.kwong.drinknight;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class UserDataActivity extends AppCompatActivity {

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
        userNameText = (TextView)findViewById(R.id.user_name);
        userNameText.setText(datas[0]);
        userIdText = (TextView)findViewById(R.id.user_id);
        userIdText.setText(datas[1]);
        userGenderText = (TextView)findViewById(R.id.user_gender);
        userGenderText.setText(datas[2]);
        userAgeText = (TextView)findViewById(R.id.user_age);
        userAgeText.setText(datas[3]);
        userHeightText = (TextView)findViewById(R.id.user_height);
        userHeightText.setText(datas[4]);
        userWeightText = (TextView)findViewById(R.id.user_weight);
        userWeightText.setText(datas[5]);
    }
}

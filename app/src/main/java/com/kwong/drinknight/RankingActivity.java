package com.kwong.drinknight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RankingActivity extends AppCompatActivity {

    private List<Person>personList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ranking);
        initPersons();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rank_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        PersonAdapter adapter = new PersonAdapter(personList);
        recyclerView.setAdapter(adapter);
    }

    private void initPersons(){
       try{
        OkHttpClient client= new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://192.168.87.2/drinking_data.json")
                .build();
        Response response = client.newCall(request).execute();
        String responseData = response.body().string();
        List<Person> rankDatas = parseJSONWithGSONtoRankData(responseData);

    }catch (Exception e){
           e.printStackTrace();
       }
    }

    private List<Person> parseJSONWithGSONtoRankData(String responseData) {
        return null;
    }

}

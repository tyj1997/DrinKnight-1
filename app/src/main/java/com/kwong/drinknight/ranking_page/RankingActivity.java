package com.kwong.drinknight.ranking_page;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kwong.drinknight.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.kwong.drinknight.utils.Global.SERVER_URL;

public class RankingActivity extends AppCompatActivity {

    private ImageView headImage;
    private List<Person>personList = new ArrayList<>();
    private RecyclerView recyclerView ;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ranking);
        headImage=(ImageView)findViewById(R.id.image_rank);
        recyclerView = (RecyclerView)findViewById(R.id.rank_list);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
        progressBar = (ProgressBar)findViewById(R.id.rank_progress);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("排行榜");
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_backup);
        }
        InitPersonsTask initPersonsTask =new InitPersonsTask();
        initPersonsTask.execute((Void)null);
    }

    public class InitPersonsTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(aBoolean);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                OkHttpClient client= new OkHttpClient();
                Request request = new Request.Builder()
                        .url(SERVER_URL+"/user/krf/rankdatas/")
                        .build();
                Response response = client.newCall(request).execute();
                String responseData = response.body().string();
                personList= parseJSONWithGSONtoRankData(responseData);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String imageUrl;

                            imageUrl = SERVER_URL+"/user/"+personList.get(0).getAccount()+"/image/";
                            Glide.with(RankingActivity.this).load(imageUrl).error(R.drawable.user_0).into(headImage);


                            Log.d("RankingActivity","initPersons success"+personList.size());

                            LinearLayoutManager layoutManager = new LinearLayoutManager(RankingActivity.this);
                            recyclerView.setLayoutManager(layoutManager);
                            PersonAdapter adapter = new PersonAdapter(personList);
                            recyclerView.setAdapter(adapter);
                            Log.d("RankingActivity","recyclerView success");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
            return true;
        }
    }

    private List<Person> parseJSONWithGSONtoRankData(String jsonData) {
        Gson gson = new Gson();
        List<Person> personList = gson.fromJson(jsonData, new TypeToken<List<Person>>(){}.getType());
        return personList;
    }


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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}

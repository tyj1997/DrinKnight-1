package com.kwong.drinknight.ranking_page;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
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

    String uriStr =SERVER_URL+"/image/";

    private ImageView headImage;
    private List<Person>personList = new ArrayList<>();
    private RecyclerView recyclerView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ranking);
        headImage=(ImageView)findViewById(R.id.image_rank);
        recyclerView = (RecyclerView)findViewById(R.id.rank_list);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_from_left);
        initPersons();
    }

    private void initPersons(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client= new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(SERVER_URL+"/user/krf/rankdatas/")
                            .build();
                    Response response = client.newCall(request).execute();
                    //Log.d("RankingActivity","Response success");
                    String responseData = response.body().string();
                    personList= parseJSONWithGSONtoRankData(responseData);
                    //Log.d("RankingActivity","personList GSON success"+personList.size());
                    //showRankPersons(rankPersons);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String imageUrl;

                                imageUrl = SERVER_URL+"/media/images/"+personList.get(0).getAccount()+".jpg";
                                Glide.with(RankingActivity.this).load(imageUrl).error(R.mipmap.ic_launcher).into(headImage);


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
            }
        }).start();

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

}

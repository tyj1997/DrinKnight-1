package com.kwong.drinknight.ranking_page;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

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

public class RankingActivity extends AppCompatActivity {

    //String uriStr ="http://10.8.189.234/image/"
    //String uriStr ="http://140.255.159.226:9090/image/";
    String uriStr ="http://192.168.87.2/image/";
    private ImageView headImage;
    private List<Person>personList = new ArrayList<>();
    private RecyclerView recyclerView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ranking);
        headImage=(ImageView)findViewById(R.id.image_rank);
        recyclerView = (RecyclerView)findViewById(R.id.rank_list);
        initPersons();
    }

    private void initPersons(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client= new OkHttpClient();
                    Request request = new Request.Builder()
                           .url("http://192.168.87.2/ranking_data.json")
                            //.url("http://140.255.159.226:9090/ranking_data.json")
                            //.url("http://10.8.189.234/ranking_data.json")
                            .build();
                    Response response = client.newCall(request).execute();
                    //Log.d("RankingActivity","Response success");
                    String responseData = response.body().string();
                    personList= parseJSONWithGSONtoRankData(responseData);
                    //Log.d("RankingActivity","personList GSON success"+personList.size());
                    final Bitmap headimage = getBitmap(uriStr+ personList.get(0).getImageName());
                    //showRankPersons(rankPersons);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                headImage.setImageBitmap(headimage);
                                //Log.d("RankingActivity","initPersons success"+personList.size());

                                LinearLayoutManager layoutManager = new LinearLayoutManager(RankingActivity.this);
                                recyclerView.setLayoutManager(layoutManager);
                                PersonAdapter adapter = new PersonAdapter(personList);
                                recyclerView.setAdapter(adapter);
                                //Log.d("RankingActivity","recyclerView success");
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

/*    private void showRankPersons(List<Person> rankPersons) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
*/
    private List<Person> parseJSONWithGSONtoRankData(String jsonData) {
        Gson gson = new Gson();
        List<Person> personList = gson.fromJson(jsonData, new TypeToken<List<Person>>(){}.getType());
        return personList;
    }

    public static Bitmap getBitmap(String path) throws IOException {

        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }

}

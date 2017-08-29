package com.kwong.drinknight.ranking_page;


import android.graphics.Bitmap;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import android.os.Handler;

import com.kwong.drinknight.R;

import static com.kwong.drinknight.ranking_page.RankingActivity.getBitmap;

/**
 * Created by 锐锋 on 2017/8/25.
 */

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {

    private List<Person>mPersonList;
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView personRank;
        ImageView personImage;
        TextView personName;
        TextView personVolume;


        public ViewHolder(View view){
            super(view);
            personRank = (TextView)view.findViewById(R.id.person_rank);
            personImage = (ImageView)view.findViewById(R.id.person_portrait);
            personName = (TextView)view.findViewById(R.id.person_name);
            personVolume = (TextView)view.findViewById(R.id.person_drink_volume);
        }
    }

    public PersonAdapter(List<Person>personList){
        mPersonList = personList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.person_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
       final Handler handler = new Handler() {
            public void handleMessage(Message msg){
               holder.personImage.setImageBitmap((Bitmap) msg.obj);
            }
        };
        try {
            final Person person = mPersonList.get(position);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        Message message = new Message();
                        Bitmap bitmap = getBitmap("http://192.168.87.2/image/" + person.getImageName());
                        message.obj = bitmap;
                        handler.sendMessage(message);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();

            holder.personName.setText(person.getName());
            holder.personVolume.setText(String.valueOf(person.getDrinkVolume()));
            holder.personRank.setText(String.valueOf(person.getRank()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mPersonList.size();
    }

}

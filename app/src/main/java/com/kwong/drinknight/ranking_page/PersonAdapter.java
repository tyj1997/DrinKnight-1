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

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.kwong.drinknight.R;
import com.kwong.drinknight.utils.MyApplication;
import static com.kwong.drinknight.utils.Global.SERVER_URL;

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
        } ;
        try {
            final Person person = mPersonList.get(position);


            Glide.with(MyApplication.getContext()).load(SERVER_URL + "/user/" + person.getAccount() + "/image/").error(R.drawable.user_0).into(holder.personImage);

            holder.personName.setText(person.getAccount());
            holder.personVolume.setText(String.valueOf(person.getDose()));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mPersonList.size();
    }

}

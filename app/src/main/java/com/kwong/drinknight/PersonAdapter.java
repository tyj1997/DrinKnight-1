package com.kwong.drinknight;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

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
    public void onBindViewHolder(ViewHolder holder, int position) {
        Person person = mPersonList.get(position);
        holder.personImage.setImageResource(person.getImageId());
        holder.personName.setText(person.getName());
        holder.personVolume.setText(person.getDrinkVolume());
        holder.personRank.setText(person.getRank());
    }

    @Override
    public int getItemCount() {
        return mPersonList.size();
    }
}

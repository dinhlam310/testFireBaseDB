package com.example.testfirebasedb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.testfirebasedb.R;
import com.example.testfirebasedb.entity.Exercise;

import java.util.ArrayList;

public class MyAdapter extends PagerAdapter {
    Context context;
    ArrayList<Exercise> arrayList;
    LayoutInflater layoutInflater;

    public MyAdapter(Context context, ArrayList<Exercise> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_exercise_in_day,container,false);
        TextView txtName = view.findViewById(R.id.tv_NAME);
        TextView txtDuration = view.findViewById(R.id.tv_DURATION);
        TextView txtCaloBurn = view.findViewById(R.id.tv_CALOBURN);
        txtName.setText(arrayList.get(position).getName());
        txtDuration.setText(arrayList.get(position).getDuration()+"");
        txtCaloBurn.setText(arrayList.get(position).getCaloBurn()+"");
        container.addView(view);
        return view;
    }
}

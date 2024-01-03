package com.example.testfirebasedb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testfirebasedb.R;
import com.example.testfirebasedb.entity.Dish;

import java.util.ArrayList;
import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.MyViewHolder> {
    private List<Dish> lst = new ArrayList<>();
    public DishAdapter(List<Dish> lst, Context context) {
        this.lst = lst;
        this.context = context;
    }
    private Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Nạp layout cho View biểu diễn phần tử dish
        View dishView = (CardView) inflater.inflate(R.layout.activity_dishes_item_view, parent, false);
        return new MyViewHolder(dishView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Dish di = lst.get(position);
//        holder.imageView.setImageResource(di.getImg());
        Glide.with(context).load(lst.get(position).getImgUrl()).into(holder.imageView);
        holder.textView.setText(di.getName());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,di.getName().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return lst.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
       final private CardView cardView;
       final private ImageView imageView;
       final private TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview);
            imageView = itemView.findViewById(R.id.item_image_view);
            textView = itemView.findViewById(R.id.item_text);
        }
    }
}

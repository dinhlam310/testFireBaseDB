package com.example.testfirebasedb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testfirebasedb.R;
import com.example.testfirebasedb.entity.Dish;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.MyViewHolder> {
    private List<Dish> lst = new ArrayList<>();
    private IClickListener mClickListener;
    public interface  IClickListener {
        void onClickDeleteItem(Dish dish);
        void onClickDetails (Dish dish);
        void onClickAddItem(Dish dish);
    }
    public DishAdapter(List<Dish> lst,IClickListener listener,Context context) {
        this.lst = lst;
        this.mClickListener = listener;
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
        holder.textCalories.setText(di.getCaloriesPer100Gm()+" calores/100g");
//        holder.textGam.setText(di.parseCalories(di.getWeight())+" g");
        holder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onClickDeleteItem(di);
            }
        });

        holder.itemView.setOnClickListener(view -> {
            mClickListener.onClickDetails(lst.get(position));
        });

        holder.button_add_to_diary.setOnClickListener(v -> mClickListener.onClickAddItem(di));
    }


    @Override
    public int getItemCount() {
        return lst.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        final private CardView cardView;
        final private ImageView imageView;
        final private TextView textView;
        final private TextView textCalories;
        //       final private TextView textGam;
        private ImageButton button_delete;
        private FloatingActionButton button_add_to_diary;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview);
            imageView = itemView.findViewById(R.id.item_image_view);
            textView = itemView.findViewById(R.id.item_text);
            textCalories = itemView.findViewById(R.id.item_calories);
//            textGam = itemView.findViewById(R.id.item_g);
            button_add_to_diary = itemView.findViewById(R.id.btn_add_dish_diary);
            button_delete = itemView.findViewById(R.id.btn_delete_dish);
            button_add_to_diary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(button_add_to_diary.getTag() == null || button_add_to_diary.getTag().equals("add")){
                        button_add_to_diary.setImageResource(R.drawable.baseline_check_24);
                        button_add_to_diary.setTag("check");
                    } else {
                        button_add_to_diary.setImageResource(R.drawable.baseline_add_24);
                        button_add_to_diary.setTag("add");
                    }
                }
            });
        }
    }
}

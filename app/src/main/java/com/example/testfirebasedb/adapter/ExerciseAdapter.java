package com.example.testfirebasedb.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebasedb.R;
import com.example.testfirebasedb.entity.Exercise;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>{
    private List<Exercise> mListExercise;
    private IClickListener mClickListener;
<<<<<<< HEAD
    public void setFilterList(List<Exercise> filterList){
        this.mListExercise = filterList;
        notifyDataSetChanged();
    }
=======
>>>>>>> b6e2134c8da6faec497d4f7b0346c048be07ffd3

    public interface  IClickListener {
         void onClickDeleteItem(Exercise exercise);
         void onClickDetails (Exercise exercise);
         void onClickAddItem(Exercise exercise);
     }


    public ExerciseAdapter(List<Exercise> mListExercise, IClickListener listener) {
        this.mListExercise = mListExercise;
        this.mClickListener = listener;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = mListExercise.get(position);
        if(exercise == null)
            return;
        holder.textView_name.setText("Name: " + exercise.getName());
        holder.textView_duration.setText("Duration: " + exercise.getDuration() + " minute");
        holder.textView_caloburn.setText("CaloBurn: " + exercise.getCaloBurn()+ " kcal");
        holder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onClickDeleteItem(exercise);
            }
        });

        holder.itemView.setOnClickListener(view -> {
            mClickListener.onClickDetails(mListExercise.get(position));
        });

        holder.button_add_to_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onClickAddItem(exercise);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mListExercise != null){
            return mListExercise.size();
        }
        return 0;
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder{
    private TextView textView_duration;
    private TextView textView_caloburn;
    private TextView textView_name;
    private ImageButton button_delete;
    private FloatingActionButton button_add_to_diary;
        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_name = itemView.findViewById(R.id.tv_NAME);
            textView_duration = itemView.findViewById(R.id.tv_DURATION);
            textView_caloburn = itemView.findViewById(R.id.tv_CALOBURN);
            button_delete = itemView.findViewById(R.id.btn_delete_exercise);
            button_add_to_diary = itemView.findViewById(R.id.btn_add_exercise_diary);
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

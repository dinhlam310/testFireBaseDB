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
import java.util.List;

public class ExerciseInDayAdapter extends RecyclerView.Adapter<ExerciseInDayAdapter.ExerciseViewHolder>{
    private List<Exercise> mListExercise;
    private IClickListener mClickListener;
    public interface  IClickListener {
        void onClickDeleteItem(Exercise exercise);
        void onClickDetails (Exercise exercise);
        void onClickAddItem(Exercise exercise);
    }


    public ExerciseInDayAdapter(List<Exercise> mListExercise, IClickListener listener) {
        this.mListExercise = mListExercise;
        this.mClickListener = listener;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise_in_day, parent, false);
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
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onClickDeleteItem(exercise);
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
        private ImageButton imageButton;
        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_name = itemView.findViewById(R.id.tv_NAME);
            textView_duration = itemView.findViewById(R.id.tv_DURATION);
            textView_caloburn = itemView.findViewById(R.id.tv_CALOBURN);
            imageButton = itemView.findViewById(R.id.btn_delete_exercise);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}

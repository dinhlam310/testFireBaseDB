package com.example.testfirebasedb.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testfirebasedb.R;
<<<<<<< HEAD
import com.example.testfirebasedb.entity.Day;
=======
>>>>>>> b6e2134c8da6faec497d4f7b0346c048be07ffd3
import com.example.testfirebasedb.listeners.ToWindowOnClick;

public class MyMenuActivity extends AppCompatActivity {
    public static boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyMenuActivity.isOpen = true;
        setContentView(R.layout.activity_my_menu);
        findViewById(R.id.from_menu_to_day).setOnClickListener(new ToWindowOnClick(this, DayActivity.class));
//        findViewById(R.id.from_menu_to_diary_menu).setOnClickListener(new ToWindowOnClick(this, DiaryActivity.class));
        findViewById(R.id.from_menu_to_profile).setOnClickListener(new ToWindowOnClick(this, ProfileActivity.class));
        findViewById(R.id.from_menu_to_dishes).setOnClickListener(new ToWindowOnClick(this, DishActivity.class));
        findViewById(R.id.from_menu_to_exercises).setOnClickListener(new ToWindowOnClick(this, ExerciseActivity.class));
//        findViewById(R.id.from_menu_to_statistic).setOnClickListener(new ToWindowOnClick(this, StatisticActivity.class));
    }

    public void onExitButton(final View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isOpen = false;
    }
}

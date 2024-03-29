package com.example.testfirebasedb.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.testfirebasedb.UserManage.SignInActivity;
import com.example.testfirebasedb.adapter.ExerciseAdapter;
import com.example.testfirebasedb.adapter.MyAdapter;
import com.example.testfirebasedb.entity.Dish;
import com.example.testfirebasedb.entity.Exercise;
import com.example.testfirebasedb.entity.Profile;
import com.example.testfirebasedb.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    Profile profile;
    TextView optimalCalorieNumber;

    EditText profileHeightEditText;
    EditText profileWeightEditText;
    EditText profileAgeEditText;
    private BottomNavigationView bottomNavigationView;
    FirebaseDatabase database;
    private DatabaseReference ref;
    ArrayList<Exercise> lst = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile);
            bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(R.id.bottom_profile);
            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.bottom_home){
                        startActivity(new Intent(getApplicationContext(), DayActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    }else if(id == R.id.bottom_dish){
                        startActivity(new Intent(getApplicationContext(), DishActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    }
                    else if(id == R.id.bottom_exercise){
                        startActivity(new Intent(getApplicationContext(), ExerciseActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    }
                    else if(id == R.id.bottom_profile){
                        return true;
                    }
                    else if(id == R.id.bottom_statistics){
                        startActivity(new Intent(getApplicationContext(), StatisticActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    }
                    return false;
                }
            });
            profile = Profile.getProfile(this);

            // Lấy tham chiếu đến node "profile" trên Firebase
            SharedPreferences sharedPreferences1 = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String userEmail = sharedPreferences1.getString("userEmail","");
            database = FirebaseDatabase.getInstance();
            ref = database.getReference().child("User").child(userEmail).child("Profile");

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Kiểm tra xem có dữ liệu trong snapshot hay không
                    if (snapshot.exists()) {
                        // Lấy dữ liệu từ snapshot và gán cho profile
                        profile = snapshot.getValue(Profile.class);

                        // Kiểm tra xem profile có null hay không
                        if (profile != null) {
                            // Cập nhật giao diện người dùng với dữ liệu từ profile
                            updateUI();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý sự kiện khi có lỗi xảy ra
                }
            });
            ((EditText) findViewById(R.id.profile_age)).setText(profile.getAge() + "");
            ((EditText) findViewById(R.id.profile_height)).setText(profile.getHeight() + "");
            ((EditText) findViewById(R.id.profile_weight)).setText(profile.getWeight() + "");
            ((RadioButton) findViewById(R.id.profile_woman_radio_button)).setChecked(!profile.getGender());
            ((EditText) findViewById(R.id.profile_aim_kal_number)).setText(profile.getAimCalorie() + "");
            optimalCalorieNumber = ((TextView) findViewById(R.id.profile_optimal_kal_number));
            optimalCalorieNumber.setText(profile.calculateCalories() + " " + getString(R.string.kilocalories));
            findViewById(R.id.btn_log_out).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(ProfileActivity.this)
                            .setTitle(getString(R.string.bottom_log_out))
                            .setMessage("Do you want to log out this account")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseAuth.getInstance().signOut();
                                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                                    overridePendingTransition(0, 0);
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
            });

            profileHeightEditText = findViewById(R.id.profile_height);
            profileWeightEditText = findViewById(R.id.profile_weight);
            profileAgeEditText = findViewById(R.id.profile_age);
            ViewPager viewPager = findViewById(R.id.viewPaper);
            lst.add(new Exercise("run",20,200));
            lst.add(new Exercise("jump",30,300));
            lst.add(new Exercise("climb",30,400));
        MyAdapter myAdapter = new MyAdapter(ProfileActivity.this,lst);
        viewPager.setAdapter(myAdapter);
//        viewPager.setCurrentItem(1);
        myAdapter.notifyDataSetChanged();
    }

    private void updateUI() {
        // Cập nhật giao diện người dùng với dữ liệu từ profile
        ((EditText) findViewById(R.id.profile_age)).setText(profile.getAge() + "");
        ((EditText) findViewById(R.id.profile_height)).setText(profile.getHeight() + "");
        ((EditText) findViewById(R.id.profile_weight)).setText(profile.getWeight() + "");
        ((RadioButton) findViewById(R.id.profile_woman_radio_button)).setChecked(!profile.getGender());
        ((EditText) findViewById(R.id.profile_aim_kal_number)).setText(profile.getAimCalorie() + "");
        optimalCalorieNumber = ((TextView) findViewById(R.id.profile_optimal_kal_number));
        optimalCalorieNumber.setText(profile.calculateCalories() + " " + getString(R.string.kilocalories));

        profileHeightEditText = findViewById(R.id.profile_height);
        profileWeightEditText = findViewById(R.id.profile_weight);
        profileAgeEditText = findViewById(R.id.profile_age);
    }

    public void onChangeButtonClick(View view) {
        int myheight = Integer.parseInt(profileHeightEditText.getText().toString());
        int myweight = Integer.parseInt(profileWeightEditText.getText().toString());
        int myage = Integer.parseInt(profileAgeEditText.getText().toString());

        if (view.getId() == R.id.profile_man_radio_button) {
            profile.setGender(true);
        } else if (view.getId() == R.id.profile_woman_radio_button) {
            profile.setGender(false);
        }

        // Cập nhật calories trong profile
        profile.setHeight(myheight);
        profile.setWeight(myweight);
        profile.setAge(myage);
        // Ghi dữ liệu mới lên Firebase
        ref.setValue(profile);
        optimalCalorieNumber.setText(profile.calculateMyCalories(profile.getGender(), myweight,myheight,myage) + " " + getString(R.string.kilocalories));
        optimalCalorieNumber.requestLayout();
    }

    @Override
    public void onDestroy() {
        profile.setHeight(this, Integer.parseInt(((EditText) findViewById(R.id.profile_height)).getText().toString()));
        profile.setWeight(this, Integer.parseInt(((EditText) findViewById(R.id.profile_weight)).getText().toString()));
        profile.setAge(this, Integer.parseInt(((EditText) findViewById(R.id.profile_age)).getText().toString()));
        profile.setAimCalorie(this, Integer.parseInt(((EditText) findViewById(R.id.profile_aim_kal_number)).getText().toString()));
        profile.saveData(this);
        super.onDestroy();
    }
}
package com.example.testfirebasedb.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebasedb.R;
import com.example.testfirebasedb.adapter.DishAdapter;
import com.example.testfirebasedb.adapter.DishInDayAdapter;
import com.example.testfirebasedb.adapter.ExerciseAdapter;
import com.example.testfirebasedb.adapter.ExerciseInDayAdapter;
import com.example.testfirebasedb.entity.Day;
import com.example.testfirebasedb.entity.Dish;
import com.example.testfirebasedb.entity.Exercise;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DayActivity extends AppCompatActivity {
    TextView caloIn, caloOut, total;
    RecyclerView dish, exercise;
    Button calendarButton;
    FirebaseDatabase database;
    private DatabaseReference ref;
    private ExerciseInDayAdapter mExerciseAdapter;
    private List<Exercise> mListExercise;
    private List<Dish> mListDish;
    private DishInDayAdapter mDishAdapter;
    String selectedDate;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.bottom_home){
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
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }else if(id == R.id.bottom_statistics){
                    startActivity(new Intent(getApplicationContext(), StatisticActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });
        LocalDate currentDate = LocalDate.now();
        int day = currentDate.getDayOfMonth();
        int month = currentDate.getMonthValue();
        int year = currentDate.getYear();
        mListDish = new ArrayList<>();
        mListExercise = new ArrayList<>();
        calendarButton = findViewById(R.id.calendarButton);
        total = findViewById(R.id.textTotal);
        caloIn = findViewById(R.id.textCaloIn);
        caloOut = findViewById(R.id.textCaloOut);
        dish = findViewById(R.id.recyclerViewDish);
        exercise = findViewById(R.id.recyclerViewExercise);
        selectedDate = day + "-" + month + "-" + year;
        calendarButton.setText(selectedDate);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("Day");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Day day = snapshot.getValue(Day.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        getListExerciseFromDB();
//        getListDishFromDB();
        ImageView btn_add_dish = (ImageView) findViewById(R.id.diary_dish_add_button);
        btn_add_dish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DayActivity.this, DishActivity.class);
                startActivity(i);
            }
        });

        ImageView btn_add_exercise = (ImageView) findViewById(R.id.diary_exercise_add_button);
        btn_add_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DayActivity.this, ExerciseActivity.class);
                startActivity(i);
            }
        });


        initUiDish();
        initUiExercise();
        loadDayData(selectedDate);
    }
    // Hàm lấy danh sách bài tập từ Firebase Realtime Database
//    private void getListExerciseFromDB() {
//        DatabaseReference exerciseRef = FirebaseDatabase.getInstance().getReference().child("Day").child("Exercise");
//        exerciseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                mListExercise.clear(); // Xóa danh sách bài tập hiện tại để cập nhật lại từ Firebase
//                for (DataSnapshot exerciseSnapshot : snapshot.getChildren()) {
//                    Exercise exercise = exerciseSnapshot.getValue(Exercise.class);
//                    mListExercise.add(exercise);
//                }
//                mExerciseAdapter.notifyDataSetChanged(); // Cập nhật giao diện RecyclerView
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu từ Firebase
//            }
//        });
//    }
//
//    // Hàm lấy danh sách món ăn từ Firebase Realtime Database
//    private void getListDishFromDB() {
//        DatabaseReference dishRef = FirebaseDatabase.getInstance().getReference().child("Day").child("Dish");
//        dishRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                mListDish.clear(); // Xóa danh sách món ăn hiện tại để cập nhật lại từ Firebase
//                for (DataSnapshot dishSnapshot : snapshot.getChildren()) {
//                    Dish dish = dishSnapshot.getValue(Dish.class);
//                    mListDish.add(dish);
//                }
//                mDishAdapter.notifyDataSetChanged(); // Cập nhật giao diện RecyclerView
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu từ Firebase
//            }
//        });
//    }
    private void initUiExercise() {
        exercise = findViewById(R.id.recyclerViewExercise);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        exercise.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        exercise.addItemDecoration(dividerItemDecoration);
        mListExercise = new ArrayList<>();
        mExerciseAdapter = new ExerciseInDayAdapter(mListExercise, new ExerciseInDayAdapter.IClickListener() {
            @Override
            public void onClickDeleteItem(Exercise exercise) {
                onClickDeleteExerciseData(exercise);
            }

            @Override
            public void onClickDetails(Exercise exercise) {

            }

            @Override
            public void onClickAddItem(Exercise exercise) {

            }
        });
        exercise.setAdapter(mExerciseAdapter);
    }

    private void initUiDish() {
        dish = findViewById(R.id.recyclerViewDish);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        dish.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dish.addItemDecoration(dividerItemDecoration);
        mListDish = new ArrayList<>();
        mDishAdapter = new DishInDayAdapter(mListDish, new DishInDayAdapter.IClickListener() {
            @Override
            public void onClickDeleteItem(Dish dish) {
                onClickDeleteDishData(dish);
            }

            @Override
            public void onClickDetails(Dish dish) {

            }

            @Override
            public void onClickAddItem(Dish dish) {

            }
        }, this);
        dish.setAdapter(mDishAdapter);
    }

//    private void onClickDeleteDishData(Dish dish) {
//        new AlertDialog.Builder(this)
//                .setTitle(getString(R.string.app_name))
//                .setMessage("Ban co chac muon xoa ban ghi nay?")
//                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("Day").child(selectedDate).child("Dish");
//                        myref.child(String.valueOf(dish.getName())).removeValue(new DatabaseReference.CompletionListener() {
//                            @Override
//                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                                Toast.makeText(DayActivity.this, "Delete data success", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                })
//                .setNegativeButton("Cancel", null)
//                .show();
//    }

    private void onClickDeleteDishData(Dish dish) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Bạn có chắc chắn muốn xóa bản ghi này?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("Day").child(selectedDate).child("Dish");
                        Query query = myref.orderByChild("name").equalTo(dish.getName());

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot dishSnapshot : dataSnapshot.getChildren()) {
                                    dishSnapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(DayActivity.this, "Xóa dữ liệu thành công", Toast.LENGTH_SHORT).show();
                                                loadDayData(selectedDate);
                                            } else {
                                                Toast.makeText(DayActivity.this, "Xóa dữ liệu không thành công", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(DayActivity.this, "Lỗi khi truy cập cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
                                loadDayData(selectedDate);
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

//    private void onClickDeleteExerciseData(Exercise exercise) {
//        new AlertDialog.Builder(this)
//                .setTitle(getString(R.string.app_name))
//                .setMessage("Ban co chac muon xoa ban ghi nay?")
//                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("Day").child(selectedDate).child("Exercise");
//
//                        myref.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                for (DataSnapshot dishSnapshot : dataSnapshot.getChildren()) {
//                                    Dish dish = dishSnapshot.getValue(Dish.class);
//                                    System.out.println(dish.getName());
//                                    // Thực hiện xử lý với bản ghi Dish ở đây
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//                                // Xử lý khi có lỗi xảy ra
//                            }
//                        });
//                    }
//                })
//                .setNegativeButton("Cancel", null)
//                .show();
//    }

    private void onClickDeleteExerciseData(Exercise exercise) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Bạn có chắc chắn muốn xóa bản ghi này?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("Day").child(selectedDate).child("Exercise");
                        Query query = myref.orderByChild("name").equalTo(exercise.getName());

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot dishSnapshot : dataSnapshot.getChildren()) {
                                    dishSnapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(DayActivity.this, "Xóa dữ liệu thành công", Toast.LENGTH_SHORT).show();
                                                loadDayData(selectedDate);
                                            } else {
                                                Toast.makeText(DayActivity.this, "Xóa dữ liệu không thành công", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(DayActivity.this, "Lỗi khi truy cập cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
                                loadDayData(selectedDate);
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(DayActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                calendarButton = findViewById(R.id.calendarButton);
                calendarButton.setText(selectedDate);
                Toast.makeText(DayActivity.this, "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
                loadDayData(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

//    private void loadDayData(String selectedDate) {
//        DatabaseReference dayRef = FirebaseDatabase.getInstance().getReference().child("Day").child(selectedDate);
//        dayRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    // Xóa dữ liệu và làm sạch giao diện
//                    caloIn.setText("0");
//                    caloOut.setText("0");
//                    total.setText("0");
//                    mListDish.clear();
//                    mListExercise.clear();
//                    mDishAdapter.notifyDataSetChanged();
//                    mExerciseAdapter.notifyDataSetChanged();
//                if (snapshot.exists()) {
//                    Day day = snapshot.getValue(Day.class);
//                    if (day != null) {
//                        caloIn.setText(String.valueOf(day.getCaloIn()));
//                        caloOut.setText(String.valueOf(day.getCaloOut()));
//                        total.setText(String.valueOf(day.getCaloIn() - day.getCaloOut()));
//
//                        DataSnapshot dishSnapshot = snapshot.child("Dish");
//                        mListDish.clear();
//                        for (DataSnapshot dishDataSnapshot : dishSnapshot.getChildren()) {
//                            Dish dish = dishDataSnapshot.getValue(Dish.class);
//                            mListDish.add(dish);
//                        }
//                        mDishAdapter.notifyDataSetChanged();
//
//                        DataSnapshot exerciseSnapshot = snapshot.child("Exercise");
//                        mListExercise.clear();
//                        for (DataSnapshot exerciseDataSnapshot : exerciseSnapshot.getChildren()) {
//                            Exercise exercise = exerciseDataSnapshot.getValue(Exercise.class);
//                            mListExercise.add(exercise);
//                        }
//                        mExerciseAdapter.notifyDataSetChanged();
//                        Intent intent = new Intent(DayActivity.this, ExerciseActivity.class);
//                        intent.putExtra("THOI_GIAN", selectedDate);
//                        startActivity(intent);
//                    }
//                } else {
//                    Day emptyDay = new Day(selectedDate,null,null,0,0,0);
//                    Toast.makeText(DayActivity.this, "Không có dữ liệu cho ngày được chọn.", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu từ Firebase
//            }
//        });
//
//    }

    private void loadDayData(String selectedDate) {
        DatabaseReference dayRef = FirebaseDatabase.getInstance().getReference().child("Day").child(selectedDate);
        dayRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Day day = snapshot.getValue(Day.class);
                    if (day != null) {
                        // Hiển thị dữ liệu từ snapshot
                        float caloIn1 = 0;
                        float caloOut2 = 0;
                        // Hiển thị danh sách Dish
                        DataSnapshot dishSnapshot = snapshot.child("Dish");
                        mListDish.clear();
                        for (DataSnapshot dishDataSnapshot : dishSnapshot.getChildren()) {
                            Dish dish = dishDataSnapshot.getValue(Dish.class);
                            caloIn1+= dish.getCaloIn();
                            mListDish.add(dish);
                        }
                        mDishAdapter.notifyDataSetChanged();

                        // Hiển thị danh sách Exercise
                        DataSnapshot exerciseSnapshot = snapshot.child("Exercise");
                        mListExercise.clear();
                        for (DataSnapshot exerciseDataSnapshot : exerciseSnapshot.getChildren()) {
                            Exercise exercise = exerciseDataSnapshot.getValue(Exercise.class);
                            caloOut2+=exercise.getCaloBurn();
                            mListExercise.add(exercise);
                        }
                        mExerciseAdapter.notifyDataSetChanged();
                        caloIn.setText(String.valueOf(caloIn1));
                        caloOut.setText(String.valueOf(caloOut2));
                        total.setText(String.valueOf(caloIn1-caloOut2));
                        dayRef.child("caloIn").setValue(caloIn1);
                        dayRef.child("caloOut").setValue(caloOut2);
                        // Chuyển sang ExerciseActivity với dữ liệu selectedDate
//                        Intent intent = new Intent(DayActivity.this, ExerciseActivity.class);
//                        intent.putExtra("THOI_GIAN", selectedDate);
//                        startActivity(intent);
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("THOI_GIAN", selectedDate);
                        editor.apply();
                    }
                } else {
                    // Tạo bản ghi mới và lưu vào Firebase
                    mListDish.clear();
                    mListExercise.clear();
                    Day emptyDay = new Day(selectedDate, (ArrayList<Dish>) mListDish, (ArrayList<Exercise>) mListExercise, 0, 0, 0);
                    DatabaseReference newDayRef = FirebaseDatabase.getInstance().getReference().child("Day").child(selectedDate);
                    newDayRef.setValue(emptyDay).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Hiển thị dữ liệu mặc định
                                caloIn.setText("0");
                                caloOut.setText("0");
                                total.setText("0");
                                mListDish.clear();
                                mListExercise.clear();
                                mDishAdapter.notifyDataSetChanged();
                                mExerciseAdapter.notifyDataSetChanged();
                                Toast.makeText(DayActivity.this, "Đã tạo bản ghi mới cho ngày được chọn.", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(DayActivity.this, ExerciseActivity.class);
//                                intent.putExtra("THOI_GIAN", selectedDate);
//                                startActivity(intent);
                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("THOI_GIAN", selectedDate);
                                editor.apply();
                            } else {
                                Toast.makeText(DayActivity.this, "Không thể tạo bản ghi mới.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}


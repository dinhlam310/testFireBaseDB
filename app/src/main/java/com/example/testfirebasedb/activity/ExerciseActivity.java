package com.example.testfirebasedb.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testfirebasedb.R;
import com.example.testfirebasedb.adapter.ExerciseAdapter;
import com.example.testfirebasedb.entity.Exercise;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExerciseActivity extends AppCompatActivity {
    private RecyclerView rcvExercise;
    private ExerciseAdapter mExerciseAdapter;
    private List<Exercise> mListExercise;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_exercise);
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
        initUi();
        SearchView searchView_item = findViewById(R.id.search_item);
        searchView_item.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
        searchView_item.clearFocus();
        ImageButton btnBack = findViewById(R.id.back_main_menu);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button btnAdd = findViewById(R.id.btn_add_exercise);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddExerciseDialog();
            }
        });
        getListUserFromDB();
    }
    private void filterList(String text) {
        List<Exercise> filterList = new ArrayList<>();
        for (Exercise exercise : mListExercise){
            if(exercise.getName().toLowerCase().contains(text.toLowerCase())){
                filterList.add(exercise);
            }
        }
        if (filterList.isEmpty()){
            Toast.makeText(this, "Không có kết quả tìm thấy", Toast.LENGTH_SHORT).show();
        }else {
            mExerciseAdapter.setFilterList(filterList);
        }
    }
    private void initUi(){
        rcvExercise = findViewById(R.id.rcv_exercise);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvExercise.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvExercise.addItemDecoration(dividerItemDecoration);
        mListExercise = new ArrayList<>();
        mExerciseAdapter = new ExerciseAdapter(mListExercise, new ExerciseAdapter.IClickListener() {
            @Override
            public void onClickDeleteItem(Exercise exercise) {
                onClickDeleteData(exercise);
            }

            @Override
            public void onClickDetails(Exercise exercise) {
                EditExerciseDialog(exercise);
            }
            @Override
            public void onClickAddItem(Exercise exercise){
                addExerciseToDay(exercise);
            }
        });
        rcvExercise.setAdapter(mExerciseAdapter);
    }
    private void EditExerciseDialog(Exercise exercise){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.exercise_editor);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        dialog.setCancelable(true);

        TextView textView_name = dialog.findViewById(R.id.edit_exercise_name);
        TextView textView_duration = dialog.findViewById(R.id.edit_exercise_duration);
        TextView textView_caloBurn = dialog.findViewById(R.id.edit_exercise_caloBurn);
        FloatingActionButton button_add_minutes = dialog.findViewById(R.id.btn_add_minutes);
        FloatingActionButton button_minus_minutes = dialog.findViewById(R.id.btn_minus_minutes);
        ImageButton button_exit = dialog.findViewById(R.id.btn_exit_exercise_editor);
        EditText editText_minutes = dialog.findViewById(R.id.edit_exercise_minutes);
        editText_minutes.setText(String.valueOf(exercise.getDuration()));
        editText_minutes.setInputType(InputType.TYPE_CLASS_NUMBER);
        textView_name.setText("Name: "+exercise.getName());
        textView_duration.setText("Duration: " + exercise.getDuration());
        textView_caloBurn.setText("CaloBurn: " + exercise.getCaloBurn());
        button_add_minutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentValue = Integer.parseInt(editText_minutes.getText().toString());
                int newVal = currentValue + 1;
                editText_minutes.setText(String.valueOf(newVal));
                textView_duration.setText("Duration: " + editText_minutes.getText());
                textView_caloBurn.setText("CaloBurn: " + exercise.CarloriesFormula(Float.parseFloat(editText_minutes.getText().toString().trim())));
            }
        });
        button_minus_minutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentValue = Integer.parseInt(editText_minutes.getText().toString());
                int newVal = currentValue - 1;
                editText_minutes.setText(String.valueOf(newVal));
                textView_duration.setText("Duration: " + editText_minutes.getText());
                textView_caloBurn.setText("CaloBurn: " + exercise.CarloriesFormula(Float.parseFloat(editText_minutes.getText().toString().trim())));
            }
        });
        Button button_add_exercise_editor = dialog.findViewById(R.id.btn_add_exercise_editor);
        button_add_exercise_editor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = textView_name.getText().toString().trim();
                int duration = Integer.parseInt(editText_minutes.getText().toString());
                float caloBurn = exercise.CarloriesFormula(Float.parseFloat(editText_minutes.getText().toString().trim()));
                Exercise exercise1 = new Exercise(name, duration, caloBurn);
                addExerciseToDay(exercise1);
            }
        });
        button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void AddExerciseDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_exercise_to_db);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        EditText editText_name = dialog.findViewById(R.id.edit_name_data);
        EditText editText_duration = dialog.findViewById(R.id.edit_duration_data);
        EditText editText_caloburn = dialog.findViewById(R.id.edit_calorburn_data);
        Button button_confirm = dialog.findViewById(R.id.btn_confirm);
        Button button_cancel = dialog.findViewById(R.id.btn_cancel);

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText_name.getText().toString().trim();
                int duration = Integer.parseInt(editText_duration.getText().toString().trim());
                int caloBurn = Integer.parseInt(editText_caloburn.getText().toString().trim());
                Exercise exercise = new Exercise(name, duration, caloBurn);
                onClickAddData(exercise);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void onClickAddData(Exercise exercise) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Exercise");
        String pathObj = String.valueOf(exercise.getName());
        myRef.child(pathObj).setValue(exercise, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(ExerciseActivity.this, "Add exercise success", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void onClickDeleteData(Exercise exercise) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Ban co chac muon xoa ban ghi nay?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference myref = firebaseDatabase.getReference("Exercise");
                        myref.child(String.valueOf(exercise.getName())).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(ExerciseActivity.this, "Delete data success", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    private void getListUserFromDB(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Exercise");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Exercise exercise = snapshot.getValue(Exercise.class);
                if(exercise != null){
                    mListExercise.add(exercise);
                    mExerciseAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Exercise exercise = snapshot.getValue(Exercise.class);
                if(exercise == null || mListExercise == null || mListExercise.isEmpty()){
                    return;
                }
                for(int i = 0; i < mListExercise.size(); i++){
                    if(exercise.getName() == mListExercise.get(i).getName()){
                        mListExercise.remove(mListExercise.get(i));
                        break;
                    }
                }
                mExerciseAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void addExerciseToDay(Exercise exercise) {
//        String selectedDate = getIntent().getStringExtra("THOI_GIAN");
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String selectedDate = sharedPreferences.getString("THOI_GIAN","");
        DatabaseReference exerciseRef = FirebaseDatabase.getInstance().getReference().child("Day").child(selectedDate).child("Exercise").push();
        exerciseRef.setValue(exercise);
        Toast.makeText(ExerciseActivity.this, "Thêm bài tập thành công", Toast.LENGTH_SHORT).show();
        DatabaseReference dayRef = FirebaseDatabase.getInstance().getReference().child("Day").child(selectedDate);
        dayRef.child("caloOut").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int currentCaloOut = snapshot.getValue(Integer.class);
                    float newCaloOut = currentCaloOut + exercise.getCaloBurn();
                    dayRef.child("caloOut").setValue(newCaloOut);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

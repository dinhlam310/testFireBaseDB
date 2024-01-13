package com.example.testfirebasedb.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebasedb.adapter.DishAdapter;
import com.example.testfirebasedb.R;
import com.example.testfirebasedb.entity.Dish;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DishActivity extends AppCompatActivity {
    Map<String, Object> map;
    ArrayList<Map<String, Object>> data; // main data
    SimpleAdapter sAdapter;
    long selectedElementId=-1;
    ListView listView;
    List<Dish> dishesList = new ArrayList<>();
    Dish tempDish;
    DishAdapter myDishesViewAdapter;
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Dish");
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);
        recyclerView = findViewById(R.id.recycle_dishes_view);
        ImageButton btnAddDish = findViewById(R.id.db_dish_add);
        //Render cac mon an tu Firebase
        //O day su dung arrayList vi chua co database sample data
//        dishesList.add(new Dish("Hamburger",120,100,70,10, Dish.enumFood.Meat,R.drawable.hamburger));
//        dishesList.add(new Dish("Carrot",50,5,10,50, Dish.enumFood.FruitAndVegetable,R.drawable.carrot));
//        dishesList.add(new Dish("Salad",50,5,10,50,Dish.enumFood.fishAndSeaFood,R.drawable.salad));
//        dishesList.add(new Dish("Hamburger",120,100,70,10, Dish.enumFood.Meat,R.drawable.hamburger));
//        dishesList.add(new Dish("Carrot",50,5,10,50, Dish.enumFood.FruitAndVegetable,R.drawable.carrot));
//        dishesList.add(new Dish("Salad",50,5,10,50,Dish.enumFood.fishAndSeaFood,R.drawable.salad));
//        dishesList.add(new Dish("Hamburger",120,100,70,10, Dish.enumFood.Meat,R.drawable.hamburger));
//        dishesList.add(new Dish("Carrot",50,5,10,50, Dish.enumFood.FruitAndVegetable,R.drawable.carrot));
//        dishesList.add(new Dish("Salad",50,5,10,50,Dish.enumFood.fishAndSeaFood,R.drawable.salad));
//        dishesList.add(new Dish("Hamburger",120,100,70,10, Dish.enumFood.Meat,R.drawable.hamburger));
//        dishesList.add(new Dish("Carrot",50,5,10,50, Dish.enumFood.FruitAndVegetable,R.drawable.carrot));
//        dishesList.add(new Dish("Salad",50,5,10,50,Dish.enumFood.fishAndSeaFood,R.drawable.salad));
//        dishesList.add(new Dish("Hamburger",120,100,70,10, Dish.enumFood.Meat,R.drawable.hamburger));
//        dishesList.add(new Dish("Carrot",50,5,10,50, Dish.enumFood.FruitAndVegetable,R.drawable.carrot));
//        dishesList.add(new Dish("Salad",50,5,10,50,Dish.enumFood.fishAndSeaFood,R.drawable.salad));
        initUI();
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("message");
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Toast.makeText(DishActivity.this, value, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Toast.makeText(DishActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
        Button btnBack = (Button)findViewById(R.id.from_dishes_to_menu);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initUI() {
        myDishesViewAdapter = new DishAdapter(dishesList, new DishAdapter.IClickListener() {
            @Override
            public void onClickDeleteItem(Dish dish) {
                onClickDeleteData(dish);
            }

            @Override
            public void onClickDetails(Dish dish) {
                EditDishDialog(dish);
            }
            @Override
            public void onClickAddItem(Dish dish){
                addDishToDay(dish);
            }
        }, DishActivity.this);
        recyclerView.setAdapter(myDishesViewAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        ImageButton btnDish_Add = (ImageButton) findViewById(R.id.db_dish_add);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dishesList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Dish dish = dataSnapshot.getValue(Dish.class);
                    dishesList.add(dish);
                }
                myDishesViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addDishToDay(Dish dish) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String selectedDate = sharedPreferences.getString("THOI_GIAN","");
        DatabaseReference dishRef = FirebaseDatabase.getInstance().getReference().child("Day").child(selectedDate).child("Dish").push();

        dishRef.setValue(dish);
        Toast.makeText(DishActivity.this, "Thêm món ăn thành công", Toast.LENGTH_SHORT).show();
        DatabaseReference dayRef = FirebaseDatabase.getInstance().getReference().child("Day").child(selectedDate);
        dayRef.child("caloIn").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int currentCaloIn = snapshot.getValue(Integer.class);
                    int newCaloIn = currentCaloIn + dish.getCaloriesPer100Gm();
                    dayRef.child("caloIn").setValue(newCaloIn);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void EditDishDialog(Dish dish) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dish_editor);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        dialog.setCancelable(true);

        TextView textView_name = dialog.findViewById(R.id.edit_dish_name);
        TextView textView_gam = dialog.findViewById(R.id.edit_dish_weight_gam);
        TextView textView_calories = dialog.findViewById(R.id.edit_dish_calories);
        FloatingActionButton button_add_weight = dialog.findViewById(R.id.btn_add_weight);
        FloatingActionButton button_minus_weight = dialog.findViewById(R.id.btn_minus_weight);
        ImageButton button_exit = dialog.findViewById(R.id.btn_exit_dish_editor);
        EditText editText_weight = dialog.findViewById(R.id.edit_dish_weight);
        editText_weight.setText(dish.getWeight()+"");
//        editText_weight.setInputType(InputType.TYPE_CLASS_NUMBER);
        textView_gam.setText("Weight: "+dish.getWeight());
        textView_name.setText("Name: "+dish.getName());
        textView_calories.setText("Calories: "+dish.getCaloriesPer100Gm());
        button_add_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentValue = Integer.parseInt(editText_weight.getText().toString());
                int newVal = currentValue + 1;
//                textView_gam.setText(String.valueOf(newVal));
                dish.setWeight(Integer.parseInt(String.valueOf(newVal)));
                editText_weight.setText(dish.getWeight()+"");
                textView_gam.setText("Weight: " + Integer.parseInt(editText_weight.getText().toString()));
                textView_calories.setText("Calories: " + dish.parseCalories(Integer.parseInt(editText_weight.getText().toString())));
            }
        });
        button_minus_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentValue = Integer.parseInt(editText_weight.getText().toString());
                int newVal = currentValue - 1;
                dish.setWeight(Integer.parseInt(String.valueOf(newVal)));
                editText_weight.setText(dish.getWeight()+"");
                textView_gam.setText("Weight: " + Integer.parseInt(editText_weight.getText().toString()));
                textView_calories.setText("Calories: " + dish.parseCalories(Integer.parseInt(editText_weight.getText().toString())));
            }
        });
        Button button_add_dish_editor = dialog.findViewById(R.id.btn_add_dish_editor);
        button_add_dish_editor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dish dish1 = new Dish();
                int weightInt = dish.getWeight();
                String name = dish.getName().toString();
                int calories = dish.getCaloriesPer100Gm();
                dish1.setWeight(weightInt);
                dish1.setName(name);
                dish1.setCalories(calories);
                Toast.makeText(DishActivity.this, dish1.getWeight()+"", Toast.LENGTH_SHORT).show();
                addDishToDay(dish);
                finish();
            }
        });
        editText_weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editText_weight.getText().toString().trim()!=""){
                    try {
                        dish.setWeight(Integer.parseInt(editText_weight.getText().toString()));
                        textView_gam.setText("Weight: " + Integer.parseInt(editText_weight.getText().toString()));
                        textView_calories.setText("Calories: " + dish.parseCalories(Integer.parseInt(editText_weight.getText().toString())));
                    } catch (NumberFormatException e) {
                        Toast.makeText(DishActivity.this, "Khong dung dinh dang weight!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void onClickDeleteData(Dish dish) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Ban co chac muon xoa ban ghi nay?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference myref = firebaseDatabase.getReference("Dish");
                        myref.child(String.valueOf(dish.getName())).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(DishActivity.this, "Delete data success", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public void onCreateDish(View view){
        Intent intent = new Intent(this, DishesEditorActivity.class);
        startActivity(intent); // transfer control to editor
//        tempDish = (Dish)getIntent().getSerializableExtra("DISH");
//        dishesList.add(tempDish);
//        recyclerView.setAdapter(myDishesViewAdapter);
        selectedElementId=-1;
    }
}

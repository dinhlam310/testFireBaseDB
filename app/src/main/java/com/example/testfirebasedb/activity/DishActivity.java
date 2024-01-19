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
import android.view.MenuItem;
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
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebasedb.adapter.DishAdapter;
import com.example.testfirebasedb.R;
import com.example.testfirebasedb.entity.Dish;
import com.example.testfirebasedb.entity.Exercise;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
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
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_dish);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.bottom_home){
                    startActivity(new Intent(getApplicationContext(), DayActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }else if(id == R.id.bottom_dish){
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
    }
    private void filterList(String text) {
        List<Dish> filterList = new ArrayList<>();
        for (Dish dish : dishesList){
            if(dish.getName().toLowerCase().contains(text.toLowerCase())){
                filterList.add(dish);
            }
        }
        if (filterList.isEmpty()){
            Toast.makeText(this, "Không có kết quả tìm thấy", Toast.LENGTH_SHORT).show();
        }else {
            myDishesViewAdapter.setFilterList(filterList);
        }
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
//        ImageButton btnDish_Add = (ImageButton) findViewById(R.id.db_dish_add);
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
        TextView textView_protein = dialog.findViewById(R.id.edit_dish_protein);
        TextView textView_fat = dialog.findViewById(R.id.edit_dish_fat);
        TextView textView_fiber = dialog.findViewById(R.id.edit_dish_fiber);
        FloatingActionButton button_add_weight = dialog.findViewById(R.id.btn_add_weight);
        FloatingActionButton button_minus_weight = dialog.findViewById(R.id.btn_minus_weight);
        ImageButton button_exit = dialog.findViewById(R.id.btn_exit_dish_editor);
        EditText editText_weight = dialog.findViewById(R.id.edit_dish_weight);
        editText_weight.setText("100");
//        editText_weight.setInputType(InputType.TYPE_CLASS_NUMBER);
        textView_gam.setText("Weight: "+"100(G)");
        textView_name.setText("Name: "+dish.getName());
        textView_calories.setText("Calories: "+dish.getCaloriesPer100Gm());
        textView_protein.setText("Protein: "+dish.getProteinPer100Gm());
        textView_fat.setText("Fat: "+dish.getFatPer100Gm());
        textView_fiber.setText("Fiber: "+dish.getFiberPer100Gm());
        button_add_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentValue = Integer.parseInt(editText_weight.getText().toString());
                int newVal = currentValue + 1;
                if(newVal >= 0){
                    textView_gam.setText(String.valueOf(newVal));
                    dish.setWeight(Integer.parseInt(String.valueOf(newVal)));
                    editText_weight.setText(dish.getWeight()+"");
                    textView_gam.setText("Weight: " + Integer.parseInt(editText_weight.getText().toString()));
                    textView_calories.setText("Calories: " + dish.parseCalories(Integer.parseInt(editText_weight.getText().toString())));
                    textView_protein.setText("Protein: "+dish.parseProtein(Integer.parseInt(editText_weight.getText().toString())));
                    textView_fat.setText("Fat: "+dish.parseFat(Integer.parseInt(editText_weight.getText().toString())));
                    textView_fiber.setText("Fiber: "+dish.parseFiber(Integer.parseInt(editText_weight.getText().toString())));
                }else{
                    Toast.makeText(DishActivity.this, "Value incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
        button_minus_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentValue = Integer.parseInt(editText_weight.getText().toString());
                int newVal = currentValue - 1;
                if(newVal >= 0){
                    textView_gam.setText(String.valueOf(newVal));
                    dish.setWeight(Integer.parseInt(String.valueOf(newVal)));
                    editText_weight.setText(dish.getWeight()+"");
                    textView_gam.setText("Weight: " + Integer.parseInt(editText_weight.getText().toString()));
                    textView_calories.setText("Calories: " + dish.parseCalories(Integer.parseInt(editText_weight.getText().toString())));
                    textView_protein.setText("Protein: "+dish.parseProtein(Integer.parseInt(editText_weight.getText().toString())));
                    textView_fat.setText("Fat: "+dish.parseFat(Integer.parseInt(editText_weight.getText().toString())));
                    textView_fiber.setText("Fiber: "+dish.parseFiber(Integer.parseInt(editText_weight.getText().toString())));
                }else{
                    Toast.makeText(DishActivity.this, "Value incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button button_add_dish_editor = dialog.findViewById(R.id.btn_add_dish_editor);
        button_add_dish_editor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(editText_weight.getText().toString()) > 0){
                    Dish dish1 = new Dish();
                    int weightInt = dish.getWeight();
                    String name = dish.getName().toString();
                    int calories = dish.getCaloriesPer100Gm();
                    dish1.setWeight(weightInt);
                    dish1.setName(name);
                    dish1.setCalories(calories);
                    addDishToDay(dish);
                    dialog.dismiss();
                }else {
                    editText_weight.setText("");
                    Toast.makeText(DishActivity.this, "Value incorrect", Toast.LENGTH_SHORT).show();
                }
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

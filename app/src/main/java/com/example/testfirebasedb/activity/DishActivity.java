package com.example.testfirebasedb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testfirebasedb.adapter.DishAdapter;
import com.example.testfirebasedb.R;
import com.example.testfirebasedb.entity.Dish;
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
    List<Dish> dishesList = new ArrayList<Dish>();
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
        myDishesViewAdapter = new DishAdapter(dishesList,DishActivity.this);
        recyclerView.setAdapter(myDishesViewAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        selectedElementId = -1;
        Button btnBack = (Button)findViewById(R.id.from_dishes_to_menu);
        ImageButton btnDish_Add = (ImageButton) findViewById(R.id.db_dish_add);
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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void onCreateDish(View view){
        Intent intent = new Intent(this, com.example.testfirebasedb.activity.DishesEditorActivity.class);
        startActivity(intent); // transfer control to editor
//        tempDish = (Dish)getIntent().getSerializableExtra("DISH");
//        dishesList.add(tempDish);
//        recyclerView.setAdapter(myDishesViewAdapter);
        selectedElementId=-1;
    }
    public void onEditDish(View view) {
        if (selectedElementId<0) {
            Toast.makeText(this, getString(R.string.pick_dish), Toast.LENGTH_SHORT).show();
            return;
        }
        //fill with new data
        Dish dish = new Dish();
        map=data.get((int)selectedElementId);
    }

    public void onDeleteDish(View view) {
    }
}

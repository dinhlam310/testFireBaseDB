package com.example.testfirebasedb.activity;

import static com.example.testfirebasedb.entity.Dish.names;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testfirebasedb.R;
import com.example.testfirebasedb.entity.Dish;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class DishesEditorActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 22;
    private Dish dish;
    private Uri imageUri;
    private ImageView dishPic;
    private String[] items = names(); //create a list of type food for drop down list
    private AutoCompleteTextView autocompletetxt;
    private ArrayAdapter<String> adapter;
    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private StorageReference storageReference;
        private ActivityResultLauncher<Intent> activityResultLauncher;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes_editor);
        dishPic = findViewById(R.id.dishPic);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("/Dish");
        autocompletetxt = findViewById(R.id.auto_complete_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            autocompletetxt.setInputMethodMode(ListPopupWindow.INPUT_METHOD_NOT_NEEDED);
        }
        adapter = new ArrayAdapter<String>(this,R.layout.list_item,items);
        autocompletetxt.setAdapter(adapter);
        autocompletetxt.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }
        });
        registerResult();
        dishPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });
        findViewById(R.id.dish_editor_OK_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upLoadToFireBase();
                finish();
            }
        });
        findViewById(R.id.dish_editor_cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dish = null;
                finish();
            }
        });

    }

    private void registerResult() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try{
                            imageUri = result.getData().getData();
                            dishPic.setImageURI(imageUri);
                        }catch (Exception e){
                            Toast.makeText(DishesEditorActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void choosePicture() {
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        activityResultLauncher.launch(intent);
//        startActivityForResult(intent,1);
//        registerForActivityResult(ActivityRes)
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData() != null){
            imageUri = data.getData();
            dishPic.setImageURI(imageUri);
        }
    }

    private void upLoadToFireBase() {
//        final String randomkey = UUID.randomUUID().toString();
//        StorageReference riversRef = storageReference.child("images/"+randomkey);
        StorageReference imgReference = storageReference.child("images/"+System.currentTimeMillis() + "." + getFileExtension(imageUri));
        imgReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        dish = new Dish();
//                        storage = FirebaseStorage.getInstance();
//                        StorageReference storageReference = storage.getReference();
                        dish = new Dish();
                        dish.setName(((EditText) findViewById(R.id.dish_editor_dish_name)).getText().toString());
                        dish.setCalories(Integer.parseInt(((EditText) findViewById(R.id.dish_editor_calories)).getText().toString()));
                        dish.setFatPer100Gm(Integer.parseInt(((EditText)findViewById(R.id.dish_editor_fat)).getText().toString()));
                        dish.setFiberPer100Gm(Integer.parseInt(((EditText)findViewById(R.id.dish_editor_fiber)).getText().toString()));
                        dish.setProteinPer100Gm(Integer.parseInt(((EditText)findViewById(R.id.dish_editor_protetin)).getText().toString()));
                        dish.setTypeOfFood(((AutoCompleteTextView)findViewById(R.id.auto_complete_text)).getText().toString());
                        dish.setImgUrl(uri.toString());
                        //Doi tuong dish duoc day len firebasedatabase
                        String dishId = myRef.push().getKey();
                        myRef.child(dishId).setValue(dish);
                        Toast.makeText(DishesEditorActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private String getFileExtension(Uri fileUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }
}
package com.example.testfirebasedb.UserManage;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testfirebasedb.BroadcastReceiver.Internet;
import com.example.testfirebasedb.R;
import com.example.testfirebasedb.activity.DayActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    private EditText editEmail, editPassword;
    private Button buttonSignIn;
    private LinearLayout layoutSignUp;
    private LinearLayout layoutForgotPassword;
    private Internet internetBroadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        internetBroadcastReceiver = new Internet();
        initUi();
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(internetBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(internetBroadcastReceiver);
    }
    private void initListener() {
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSignIn();
            }

        });
        layoutForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickForgotPassword();
            }
        });
        layoutSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onClickSignIn() {
        String strEmail = editEmail.getText().toString().trim();
        String strPassword = editPassword.getText().toString().trim();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(SignInActivity.this, DayActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignInActivity.this, "Vui long kiem tra lai email va password.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onClickForgotPassword() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = editEmail.getText().toString().trim();
        if(emailAddress == null || emailAddress.toString().trim().isEmpty())
            Toast.makeText(SignInActivity.this, "Vui long nhap email!", Toast.LENGTH_SHORT).show();
        else{
            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignInActivity.this, "Email sent!", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(SignInActivity.this, "Email sent fail!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    private void initUi() {
        layoutSignUp = findViewById(R.id.layout_sign_up);
        editEmail = findViewById(R.id.edit_gmail);
        editPassword = findViewById(R.id.edit_password);
        buttonSignIn = findViewById(R.id.btn_sign_in);
        layoutForgotPassword = findViewById(R.id.layout_forgot_password);
    }
}

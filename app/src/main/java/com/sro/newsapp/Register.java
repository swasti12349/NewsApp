package com.sro.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    FirebaseAuth fAuth;
    Button signUp;
    EditText Email,Password,mob,fname;
    TextView reg;
    ProgressBar progressBar;
    FirebaseDatabase database;
    DatabaseReference reference;
    registerDetail detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fAuth = FirebaseAuth.getInstance();
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        signUp = findViewById(R.id.register);
        reg = findViewById(R.id.already);
        mob = findViewById(R.id.mobile);
        progressBar = findViewById(R.id.pb);
        fname = findViewById(R.id.fullName);


        reg.setOnClickListener(v -> startActivity(new Intent(Register.this,Login.class)));

        signUp.setOnClickListener(v -> {
            String email = Email.getText().toString();
            String password = Password.getText().toString();
            String phno = mob.getText().toString();
            String names = fname.getText().toString();

            database = FirebaseDatabase.getInstance();
            reference = database.getReference("user");
            detail = new registerDetail(names,email,phno,password);
            reference.child(phno).setValue(detail);


            if(email.isEmpty()){
                Email.setError("Email Required");
                Email.requestFocus();
            }
            else if(password.isEmpty()){
                Password.setError("Password Required");
                Password.requestFocus();
            }

            else if (email.isEmpty() && password.isEmpty()){
                Toast.makeText(Register.this,"Fields are required",Toast.LENGTH_SHORT).show();
            }
            else if(!(email.isEmpty() && password.isEmpty())){
                progressBar.setVisibility(View.VISIBLE);
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(Register.this,"SignUp Unsuccessful",Toast.LENGTH_SHORT).show();
                        }else {
                            String mobiles = mob.getText().toString();
                            Toast.makeText(Register.this,"Registered",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Register.this,otp.class);
                            intent.putExtra("otps",mobiles);
                            startActivity(intent);
                            finish();


                        }
                    }
                });
            }
            else {
                Toast.makeText(Register.this,"Error Occurred",Toast.LENGTH_SHORT).show();
            }
        });


    }
    public class registerDetail {

        String name,email,mob,password;
        public registerDetail(){

        }

        public registerDetail(String name, String email, String mob, String password) {
            this.name = name;
            this.email = email;
            this.mob = mob;
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMob() {
            return mob;
        }

        public void setMob(String mob) {
            this.mob = mob;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
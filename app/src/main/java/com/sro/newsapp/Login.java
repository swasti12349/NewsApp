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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    TextView textView;
    Button button;
    EditText email, password;
    String userid, passwords;
    FirebaseDatabase database;
    DatabaseReference reference;

    private FirebaseAuth mAuth;
    public FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textView = findViewById(R.id.notRegister);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        button = findViewById(R.id.login);
        userid = "";
        passwords = "";

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("user");
        textView.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userid = email.getText().toString();
                passwords = password.getText().toString();
                if ( userid.isEmpty()){
                    email.setError("Field is empty");
                    email.requestFocus();}
                if (passwords.isEmpty()){
                    password.setError("Field is empty");
                    password.requestFocus();}

                if (!userid.isEmpty() && !passwords.isEmpty()){
                Query checkUser = reference.orderByChild("mob").equalTo(userid);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            email.setError(null);

                            String passwordDB = snapshot.child(userid).child("password").getValue(String.class);
                            if (passwordDB.equals(passwords)) {
                                password.setError(null);
                                String nameDB = snapshot.child(userid).child("name").getValue(String.class);
                                String emailDB = snapshot.child(userid).child("email").getValue(String.class);
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                intent.putExtra("name", nameDB);
                                intent.putExtra("emailid", emailDB);
                                startActivity(intent);
                            } else {
                                password.setError("Wrong Password");
                                password.requestFocus();

                            }
                        } else {
                            email.setError("Not Registered");
                            email.requestFocus();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });}


            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
}
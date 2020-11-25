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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class otp extends AppCompatActivity {
    EditText verifyOtp;
    Button otpButton;
    ProgressBar pbo;
    String verificationCodeBySystem;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        verifyOtp = findViewById(R.id.otp);
        otpButton = findViewById(R.id.verify);
        pbo = findViewById(R.id.pbo);
        pbo.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();

//        relativeLayout = findViewById(R.id.otpxml);
//        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
//        animationDrawable.setEnterFadeDuration(2000);
//        animationDrawable.setExitFadeDuration(4000);
//        animationDrawable.start();

        String phNo = getIntent().getStringExtra("otps");

        sendVerificationCodeToUser(phNo);
        otpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = verifyOtp.getText().toString();
                if (code.isEmpty() || code.length() < 6) {
                    verifyOtp.setError("Wrong OTP");
                    verifyOtp.requestFocus();
                    return;
                }
                pbo.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        });

    }

    private void sendVerificationCodeToUser(String phNo) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phNo,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                pbo.setVisibility(View.VISIBLE);
                verifyCode(code);
                verifyOtp.setText(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(otp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String codeByUser) {
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
        signIn(phoneAuthCredential);
    }

    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(otp.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                } else {
                    Toast.makeText(otp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}


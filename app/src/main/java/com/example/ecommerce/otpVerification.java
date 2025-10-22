package com.example.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class otpVerification extends AppCompatActivity {

    private EditText etDigit1, etDigit2, etDigit3, etDigit4, etDigit5, etDigit6 ;
    private TextView tvConfirm, tvBack;


    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        etDigit1 = findViewById(R.id.etDigit1);
        etDigit2 = findViewById(R.id.etDigit2);
        etDigit3 = findViewById(R.id.etDigit3);
        etDigit4 = findViewById(R.id.etDigit4);
        etDigit5 = findViewById(R.id.etDigit5);  // <-- Add this line
        etDigit6 = findViewById(R.id.etDigit6);  // <-- And this line

        tvConfirm = findViewById(R.id.tvConfirm);
        TextView tvBack = findViewById(R.id.tvBack);

        verificationId = getIntent().getStringExtra("verificationId");

        tvConfirm.setOnClickListener(v -> {
            String otp = etDigit1.getText().toString().trim() +
                    etDigit2.getText().toString().trim() +
                    etDigit3.getText().toString().trim() +
                    etDigit4.getText().toString().trim() +
                    etDigit5.getText().toString().trim() +
                    etDigit6.getText().toString().trim();

            if (otp.length() != 6 || TextUtils.isEmpty(verificationId)) {
                Toast.makeText(this, "Invalid OTP or verification ID", Toast.LENGTH_SHORT).show();
                return;
            }

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
            FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "OTP Verified!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, brgHomepage.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // tvBack listener remains unchanged
    }

}

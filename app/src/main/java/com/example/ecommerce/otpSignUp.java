package com.example.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class otpSignUp extends AppCompatActivity {

    private EditText editTextEmail, editTextPhone;
    private TextView buttonContinue;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_sign_up);

        // Initialize views
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonContinue = findViewById(R.id.buttonContinue);

        mAuth = FirebaseAuth.getInstance();

        // Set click listener
        buttonContinue.setOnClickListener(v -> validateAndSendOtp());
    }

    private void validateAndSendOtp() {
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(phone) || phone.length() < 8) {
            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Format phone number if needed (e.g., add country code)
        if (!phone.startsWith("+")) {
            phone = "+63" + phone.replaceFirst("^0+", "");
        }

        // Start phone number verification
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                                // ✅ Auto-verification success
                                mAuth.signInWithCredential(credential)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(otpSignUp.this, "Auto-verified!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(otpSignUp.this, brgHomepage.class));
                                                finish();
                                            } else {
                                                Toast.makeText(otpSignUp.this, "Auto-verification failed", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(otpSignUp.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                // ✅ Show popup and move to verification activity
                                new AlertDialog.Builder(otpSignUp.this)
                                        .setTitle("OTP Sent")
                                        .setMessage("A verification code has been sent to your phone.\n\nPlease enter the code manually on the next screen.")
                                        .setPositiveButton("OK", (dialog, which) -> {
                                            Intent intent = new Intent(otpSignUp.this, otpVerification.class);
                                            intent.putExtra("verificationId", verificationId);
                                            startActivity(intent);
                                            finish();
                                        })
                                        .setCancelable(false)
                                        .show();
                            }
                        })
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}

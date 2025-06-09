package com.example.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class createAccount extends AppCompatActivity {

    private TextInputEditText etName, etEmail, etPhone, etPassword;
    private CheckBox cbTerms;
    private Button btnSubmit;
    private TextView tvSignIn;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        cbTerms = findViewById(R.id.cbTerms);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvSignIn = findViewById(R.id.tvSignIn);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        btnSubmit.setOnClickListener(v -> attemptSignUp());
        tvSignIn.setOnClickListener(v -> startActivity(new Intent(this, loginForm.class)));
    }

    private void attemptSignUp() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email is required");
            etEmail.requestFocus();
            return;
        }

        if (!validatePhoneNumber(phone)) return;

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Please accept the terms and conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        registerUser(name, email, phone, password);
    }

    private boolean validatePhoneNumber(String phone) {
        String digitsOnly = phone.replaceAll("[^0-9]", "");
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Phone number is required");
            etPhone.requestFocus();
            return false;
        }
        if (digitsOnly.length() < 10 || digitsOnly.length() > 11) {
            etPhone.setError("Phone number must be 10–11 digits");
            etPhone.requestFocus();
            return false;
        }
        return true;
    }

    private void registerUser(String name, String email, String phone, String password) {
        Log.d("RegisterDebug", "Starting registerUser()");

        try {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                // Update display name in Firebase Authentication
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build();

                                firebaseUser.updateProfile(profileUpdates)
                                        .addOnCompleteListener(profileTask -> {
                                            if (profileTask.isSuccessful()) {
                                                Log.d("ProfileUpdate", "User profile updated");

                                                // Now, save user info in Realtime Database
                                                String userId = firebaseUser.getUid();
                                                User user = new User(userId, name, email, phone);
                                                usersRef.child(userId).setValue(user)
                                                        .addOnCompleteListener(dbTask -> {
                                                            if (dbTask.isSuccessful()) {
                                                                Toast.makeText(createAccount.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(createAccount.this, loginForm.class));
                                                                finish();
                                                            } else {
                                                                Log.e("DatabaseError", "Failed to save user", dbTask.getException());
                                                                Toast.makeText(createAccount.this, "Failed to save user: " + dbTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                            } else {
                                                Log.e("ProfileUpdate", "Error updating profile", profileTask.getException());
                                            }
                                        });
                            } else {
                                Log.e("RegisterDebug", "mAuth.getCurrentUser() returned null");
                                Toast.makeText(createAccount.this, "User creation failed, please try again", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Log.e("AuthError", "Registration failed", task.getException());
                            Toast.makeText(createAccount.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } catch (Exception e) {
            Log.e("FirebaseError", "Exception during registration", e);
            Toast.makeText(this, "Unexpected error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}

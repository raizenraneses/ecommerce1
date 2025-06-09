package com.example.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class brgHomepage extends AppCompatActivity {

    private Button logoutButton, supreme, baconCheese, classic;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brg_homepage);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Check if user is logged in
        if (mAuth.getCurrentUser() == null) {
            // User not logged in, redirect to login form
            startActivity(new Intent(brgHomepage.this, loginForm.class));
            finish();
            return;
        }

        // Initialize buttons
        logoutButton = findViewById(R.id.logoutButton);
        supreme = findViewById(R.id.supreme);
        baconCheese = findViewById(R.id.baconCheese);
        classic = findViewById(R.id.classic);

        // Burger button listeners
        supreme.setOnClickListener(v -> startActivity(new Intent(this, brgSupreme.class)));
        baconCheese.setOnClickListener(v -> startActivity(new Intent(this, brgBaconCheese.class)));
        classic.setOnClickListener(v -> startActivity(new Intent(this, brgClassic.class)));

        // Logout button listener
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(this, "Logged out.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, loginForm.class));
            finish();
        });
    }
}

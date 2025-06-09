package com.example.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class brgLastPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brg_last_page);

        // Initialize the "Continue to Shopping" button
        Button continueShoppingButton = findViewById(R.id.continueShoppingButton);

        // Set click listener for the button
        continueShoppingButton.setOnClickListener(v -> {
            // Navigate back to the homepage or shopping activity
            Intent intent = new Intent(brgLastPage.this, brgHomepage.class);
            startActivity(intent);
            finish();
        });
    }
}

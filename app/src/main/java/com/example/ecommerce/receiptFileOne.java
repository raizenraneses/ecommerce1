package com.example.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class receiptFileOne extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipt_file_one);

        // Initialize views
        TextView orderNumberTextView = findViewById(R.id.orderNumberTextView);
        TextView orderDateTextView = findViewById(R.id.orderDateTextView);
        TextView customerNameTextView = findViewById(R.id.customerNameTextView);
        TextView customerPhoneTextView = findViewById(R.id.customerPhoneTextView);
        TextView customerAddressTextView = findViewById(R.id.customerAddressTextView);
        TextView productNameTextView = findViewById(R.id.productNameTextView);
        TextView productDescriptionTextView = findViewById(R.id.productDescriptionTextView);
        TextView productPriceTextView = findViewById(R.id.productPriceTextView);
        TextView productQuantityTextView = findViewById(R.id.productQuantityTextView);
        TextView subtotalTextView = findViewById(R.id.subtotalTextView);
        TextView deliveryFeeTextView = findViewById(R.id.deliveryFeeTextView);
        TextView totalPriceTextView = findViewById(R.id.totalPriceTextView);
        TextView deliveryTimeTextView = findViewById(R.id.deliveryTimeTextView);
        TextView trackOrderButton = findViewById(R.id.trackOrderButton);
        TextView backToHomeButton = findViewById(R.id.backToHomeButton);

        // Get intent and check for required data
        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra("productName")) {
            Toast.makeText(this, "Error: No order data received", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Extract intent data
        String productName = intent.getStringExtra("productName");
        String productDescription = intent.getStringExtra("productDescription");
        int quantity = intent.getIntExtra("quantity", 1);
        int productPrice = intent.getIntExtra("productPrice", 500);
        int deliveryFee = intent.getIntExtra("deliveryFee", 59);
        String customerName = intent.getStringExtra("customerName");
        String customerPhone = intent.getStringExtra("customerPhone");
        String customerAddress = intent.getStringExtra("customerAddress");

        // Optional: add logging or extra validation here

        // Calculate totals
        int subtotal = productPrice * quantity;
        int total = subtotal + deliveryFee;

        // Generate order number and date
        String orderNumber = "ORD-" + (100000 + new Random().nextInt(900000));
        String currentDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());

        // Set text in views
        orderNumberTextView.setText(orderNumber);
        orderDateTextView.setText(String.format("%s, %s", currentDate, currentTime));
        customerNameTextView.setText(customerName);
        customerPhoneTextView.setText(customerPhone);
        customerAddressTextView.setText(customerAddress);
        productNameTextView.setText(productName);
        productDescriptionTextView.setText(productDescription);
        productPriceTextView.setText(String.format("P%d", productPrice));
        productQuantityTextView.setText(String.format("x%d", quantity));
        subtotalTextView.setText(String.format("P%d", subtotal));
        deliveryFeeTextView.setText(String.format("P%d", deliveryFee));
        totalPriceTextView.setText(String.format("P%d", total));
        deliveryTimeTextView.setText("STANDARD (40-45 mins)");

        // Button actions
        trackOrderButton.setOnClickListener(v ->
                Toast.makeText(this, "Tracking your order...", Toast.LENGTH_SHORT).show()
        );

        backToHomeButton.setOnClickListener(v -> navigateToHome());
    }

    private void navigateToHome() {
        Intent homeIntent = new Intent(this, brgHomepage.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);
        finish();
    }
}

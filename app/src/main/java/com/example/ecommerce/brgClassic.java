package com.example.ecommerce;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class brgClassic extends AppCompatActivity {

    // Constants
    private static final int MIN_QUANTITY = 1;
    private static final int MAX_QUANTITY = 20;
    private static final int PRODUCT_PRICE = 200;
    private static final int DELIVERY_FEE = 59;

    // Views
    private TextView quantityTextView;
    private TextView productPriceTextView;
    private TextView deliveryFeeTextView;
    private TextView totalPriceTextView;
    private Button decreaseButton;
    private Button increaseButton;
    private TextView cancelButton;
    private TextView placeOrderButton;

    // Variables
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brg_classic);

        // Initialize views
        quantityTextView = findViewById(R.id.quantityTextView);
        productPriceTextView = findViewById(R.id.productPriceTextView); // Added this line
        deliveryFeeTextView = findViewById(R.id.deliveryFeeTextView);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        decreaseButton = findViewById(R.id.decreaseButton);
        increaseButton = findViewById(R.id.increaseButton);
        cancelButton = findViewById(R.id.cancelButton);
        placeOrderButton = findViewById(R.id.placeOrderButton);

        // Set initial values
        updateQuantityDisplay();
        updatePriceDisplay();

        // Set click listeners using lambda for cleaner code
        decreaseButton.setOnClickListener(v -> decreaseQuantity());

        increaseButton.setOnClickListener(v -> increaseQuantity());

        cancelButton.setOnClickListener(v -> {
            Toast.makeText(brgClassic.this, "Order cancelled", Toast.LENGTH_SHORT).show();
            finish();
        });

        placeOrderButton.setOnClickListener(v -> {
            String orderMessage = String.format("Order placed for %d items! Total: P%d",
                    quantity, calculateTotalPrice());
            Toast.makeText(brgClassic.this, orderMessage, Toast.LENGTH_SHORT).show();
        });
    }

    private void decreaseQuantity() {
        if (quantity > MIN_QUANTITY) {
            quantity--;
            updateQuantityDisplay();
            updatePriceDisplay();
        } else {
            Toast.makeText(this, "Minimum quantity reached", Toast.LENGTH_SHORT).show();
        }
    }

    private void increaseQuantity() {
        if (quantity < MAX_QUANTITY) {
            quantity++;
            updateQuantityDisplay();
            updatePriceDisplay();
        } else {
            Toast.makeText(this, "Maximum quantity reached", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateQuantityDisplay() {
        quantityTextView.setText(String.valueOf(quantity));
    }

    private void updatePriceDisplay() {
        int productTotal = PRODUCT_PRICE * quantity;
        int total = productTotal + DELIVERY_FEE;

        // Remove this line since we don't have this TextView in layout
        // productPriceTextView.setText(String.format("P%d", productTotal));

        deliveryFeeTextView.setText(String.format("P%d", DELIVERY_FEE));
        totalPriceTextView.setText(String.format("P%d", total));
    }

    private int calculateTotalPrice() {
        return (PRODUCT_PRICE * quantity) + DELIVERY_FEE;
    }
}
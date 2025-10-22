package com.example.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class brgSupreme extends AppCompatActivity {

    // Constants
    private static final int MIN_QUANTITY = 1;
    private static final int MAX_QUANTITY = 20;
    private static final int PRODUCT_PRICE = 500;
    private static final int DELIVERY_FEE = 59;

    // Views
    private TextView quantityTextView;
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
        setContentView(R.layout.activity_brg_supreme);

        // Initialize views
        quantityTextView = findViewById(R.id.quantityTextView);
        deliveryFeeTextView = findViewById(R.id.deliveryFeeTextView);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        decreaseButton = findViewById(R.id.decreaseButton);
        increaseButton = findViewById(R.id.increaseButton);
        cancelButton = findViewById(R.id.cancelButton);
        placeOrderButton = findViewById(R.id.placeOrderButton);

        // Set initial UI state
        updateQuantityDisplay();
        updatePriceDisplay();

        // Set button click listeners
        decreaseButton.setOnClickListener(v -> decreaseQuantity());
        increaseButton.setOnClickListener(v -> increaseQuantity());

        cancelButton.setOnClickListener(v -> {
            Toast.makeText(this, "Order cancelled", Toast.LENGTH_SHORT).show();
            finish();
        });

        placeOrderButton.setOnClickListener(v -> placeOrder());
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
        int total = calculateTotalPrice();
        deliveryFeeTextView.setText(String.format("P%d", DELIVERY_FEE));
        totalPriceTextView.setText(String.format("P%d", total));
    }

    private void placeOrder() {
        Intent receiptIntent = new Intent(this, receiptFileOne.class);
        receiptIntent.putExtra("productName", "Supreme");
        receiptIntent.putExtra("productDescription", "Smoky BBQ sauce, Crispy Bacon");
        receiptIntent.putExtra("quantity", quantity);
        receiptIntent.putExtra("productPrice", PRODUCT_PRICE);
        receiptIntent.putExtra("deliveryFee", DELIVERY_FEE);
        receiptIntent.putExtra("customerName", "Nina Hana Chang");
        receiptIntent.putExtra("customerPhone", "09334678901");
        receiptIntent.putExtra("customerAddress", "Upper 3, Sta Monica, Penacincio, Calumpang, Antipolo City, Bayani");

        startActivity(receiptIntent);

        Toast.makeText(this,
                String.format("Order placed for %d item%s! Total: P%d",
                        quantity, quantity > 1 ? "s" : "", calculateTotalPrice()),
                Toast.LENGTH_SHORT).show();
    }

    private int calculateTotalPrice() {
        return (PRODUCT_PRICE * quantity) + DELIVERY_FEE;
    }
}

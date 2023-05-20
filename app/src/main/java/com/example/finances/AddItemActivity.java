package com.example.finances;

import static com.example.finances.Database.helpers.ShopHelper.GetShopId;
import static com.example.finances.Database.helpers.ShopItemHelper.CreateItem;
import static com.example.finances.MainActivity.log;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.finances.Database.helpers.DatabaseHelper;

public class AddItemActivity extends AppCompatActivity {

    private DatabaseHelper db;

    public AddItemActivity() {
        this.db = new DatabaseHelper(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Initialize();
    }

    private void Initialize() {
        SetButtons();
    }

    private void SetButtons() {
        Button submit = findViewById(R.id.AddItemOkButton);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView name = findViewById(R.id.ItemName);
                TextView buyPrice = findViewById(R.id.BuyPrice);
                TextView sellPrice = findViewById(R.id.SellPrice);

                try {
                    boolean changed = CreateItem(db, name.getText().toString(), GetShopId(db, "FullPriceShop"), Double.parseDouble(buyPrice.getText().toString()), Double.parseDouble(sellPrice.getText().toString()));

                    if(!changed)
                        log(AddItemActivity.this, "Failed to change Variable, returned false");
                    else
                        log(AddItemActivity.this, "Saved successfully!");
                }
                catch (Exception e) {
                    log(AddItemActivity.this, "Failed to insert into database");
                }
            }
        });
    }
}
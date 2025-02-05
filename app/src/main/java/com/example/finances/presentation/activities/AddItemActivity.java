package com.example.finances.presentation.activities;

import static com.example.finances.frameworks_and_drivers.database.shop.ShopHelper.GetShopId;
import static com.example.finances.frameworks_and_drivers.database.shop.ShopItemHelper.CreateItem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.finances.frameworks_and_drivers.database.common.DatabaseHelper;
import com.example.finances.R;

public class AddItemActivity extends BaseActivity {

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
                    boolean changed = CreateItem(db, name.getText().toString(), GetShopId(db, "FullPriceShop"), Float.parseFloat(buyPrice.getText().toString()), Float.parseFloat(sellPrice.getText().toString()));

                    if(!changed)
                        Log("Failed to create item, returned false");
                    else
                        Log("Saved successfully!");
                }
                catch (Exception e) {
                    Log("Failed to insert into database");
                }
            }
        });
    }
}
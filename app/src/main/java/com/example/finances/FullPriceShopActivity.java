package com.example.finances;

import static com.example.finances.Database.helpers.AccountHelper.GetMoney;
import static com.example.finances.Database.helpers.ShopHelper.GetShopAccountNumber;
import static com.example.finances.Database.helpers.ShopHelper.GetShopId;
import static com.example.finances.Database.helpers.ShopItemHelper.GetShopItems;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.finances.Database.helpers.DatabaseHelper;
import com.example.finances.Database.models.ShopItemDao;

import java.util.ArrayList;

public class FullPriceShopActivity extends AppCompatActivity {
    private static final String Name = "FullPriceShop";

    private DatabaseHelper db;

    public FullPriceShopActivity() {
        this.db = new DatabaseHelper(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_price_shop);

        Initialize();
    }

    private void Initialize() {
        SetData();
        SetButtons();
    }

    private void SetData() {
        int accountNumber = GetShopAccountNumber(db, Name);
        int money = GetMoney(db, accountNumber);

        TextView shopMoney = findViewById(R.id.ShopMoney);
        shopMoney.setText(money + "€");
        SetItems();
    }

    private void SetItems() {
        int shopId = GetShopId(db, Name);
        ArrayList<ShopItemDao> items = GetShopItems(db, shopId);

        LinearLayout itemsList = findViewById(R.id.Items);

        for (ShopItemDao item : items) {
            TextView itemText = new TextView(this);
            itemText.setText(item.toString());
            itemsList.addView(itemText);
        }
    }

    private void SetButtons() {
        Button buyOrSell = findViewById(R.id.BuySellButton);
        Button addItem = findViewById(R.id.AddItemButton);
        Button shopSettings = findViewById(R.id.ShopSettingsButton);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FullPriceShopActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });
    }
}
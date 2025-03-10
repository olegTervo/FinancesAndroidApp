package com.example.finances.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.finances.R;
import com.example.finances.databinding.ActivityMenuBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MenuActivity extends BaseActivity {

    private ActivityMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setButtons();
    }

    private void setButtons() {
        Button investments = findViewById(R.id.investments);
        Button bank = findViewById(R.id.Bank);
        Button events = findViewById(R.id.Events);
        Button shop = findViewById(R.id.Shop);
        Button test = findViewById(R.id.TestActivityMenuButton);
        Button log = findViewById(R.id.logButton);

        investments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, InvestmentActivity.class);
                startActivity(intent);
            }
        });

        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, BankActivity.class);
                startActivity(intent);
            }
        });

        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, EventsActivity.class);
                startActivity(intent);
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, LogActivity.class);
                startActivity(intent);
            }
        });

        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, FullPriceShopActivity.class);
                startActivity(intent);
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, TestActivity.class);
                startActivity(intent);
            }
        });
    }
}
package com.example.finances;

import static com.example.finances.Database.helpers.AccountHelper.GetMoney;
import static com.example.finances.Database.helpers.AccountHelper.PutMoney;
import static com.example.finances.Database.helpers.ShopHelper.GetShopAccountNumber;
import static com.example.finances.Database.helpers.ShopHelper.GetShopId;
import static com.example.finances.Database.helpers.ShopItemHelper.AddItems;
import static com.example.finances.Database.helpers.ShopItemHelper.DeleteItem;
import static com.example.finances.Database.helpers.ShopItemHelper.GetShopItems;
import static com.example.finances.MainActivity.log;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.finances.Database.helpers.DailyGrowthHelper;
import com.example.finances.Database.helpers.DatabaseHelper;
import com.example.finances.Database.models.ShopItemDao;

import java.util.ArrayList;

public class FullPriceShopActivity extends AppCompatActivity {
    private static final String Name = "FullPriceShop";

    private DatabaseHelper db;
    private boolean sellMode;

    public FullPriceShopActivity() {
        this.db = new DatabaseHelper(this);
        this.sellMode = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_price_shop);

        Initialize();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SetData();
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
        itemsList.removeAllViews();

        for (ShopItemDao item : items) {
            LinearLayout row = new LinearLayout(this);
            row.setPadding(0, 0, 0, 5);
            row.setVerticalGravity(Gravity.CENTER_VERTICAL);

            row.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    row.removeAllViews();
                    row.addView(CreateTextItem(FullPriceShopActivity.this, item));
                    row.addView(CreateDeleteButton(FullPriceShopActivity.this, item));

                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SetItems();
                        }
                    });

                    return true;
                }
            });

            row.addView(CreateTextItem(this, item));
            row.addView(CreateAddInput(this, item, sellMode));

            itemsList.addView(row);
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

        buyOrSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullPriceShopActivity.this.sellMode = !FullPriceShopActivity.this.sellMode;
                SetItems();
            }
        });
    }

    private static Button CreateDeleteButton(FullPriceShopActivity context, ShopItemDao item) {
        Button delete = new Button(context);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    boolean changed = DeleteItem(context.db, item.id);

                    if(!changed)
                        log(context, "Failed to delete item, returned false");
                    else
                        log(context, "Saved successfully!");
                }
                catch (Exception e) {
                    log(context, "Failed to delete from database");
                }
                finally {
                    context.SetItems();
                }
            }
        });
        delete.setBackgroundColor(Color.RED);
        String buttonText = "delete"; //+ (item.name.length() > 7 ? item.name.substring(0, 7) : item.name);
        delete.setText(buttonText);
        delete.setPadding(10, 0, 10, 0);

        return delete;
    }

    private static TextView CreateTextItem(FullPriceShopActivity context, ShopItemDao item) {
        TextView itemText = new TextView(context);
        itemText.setText(item.toString());
        itemText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        itemText.measure(0, 0);
        itemText.setPadding(0, 0, 450 - itemText.getMeasuredWidth(), 0);

        return itemText;
    }

    private static LinearLayout CreateAddInput(FullPriceShopActivity context, ShopItemDao item, boolean sellMode) {
        LinearLayout result = new LinearLayout(context);
        result.setVerticalGravity(Gravity.BOTTOM);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 30, 0);
        result.setLayoutParams(params);

        EditText input = new EditText(context);
        input.setWidth(100);
        input.setLayoutParams(params);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        Button add = new Button(context);
        add.setBackgroundColor(Color.GREEN);
        String text = sellMode ? "sell" : "buy";
        add.setText(text);
        add.setPadding(10, 0, 10, 0);
        add.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = input.getText().toString();
                int val = text.length() > 0 ? Integer.parseInt(text) : 0;

                try {
                    boolean changed = false;

                    if(sellMode)
                        changed = AddItems(context.db, item.id, val);
                    else
                        changed = AddItems(context.db, item.id, -val);

                    if(changed) {
                        if (sellMode) {
                            int price = (int) Math.round(item.buyPrice);
                            changed = PutMoney(context.db, GetShopAccountNumber(context.db, Name), -val*price, "selling " + item.name) != -1
                                && DailyGrowthHelper.increaseTopValue(context.db, val*price);
                        }
                        else {
                            int price = (int) Math.round(item.sellPrice);
                            changed = PutMoney(context.db, GetShopAccountNumber(context.db, Name), val*price, "buying " + item.name) != -1
                                && DailyGrowthHelper.increaseTopValue(context.db, -val*price);
                        }
                    }

                    if(!changed)
                        log(context, "Failed to change item amount, returned false");
                    else
                        log(context, "Changed successfully!");
                }
                catch (Exception e) {
                    log(context, "Failed to change amount");
                }
                finally {
                    context.SetData();
                }
            }
        });

        result.addView(input);
        result.addView(add);

        return result;
    }

}
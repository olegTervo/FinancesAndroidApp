package com.example.finances.presentation.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.finances.R;

public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setTitle("Debugging zone");
        setButtons();
    }

    private void setButtons() {
        Button notify = findViewById(R.id.NotifyButton);
        Button error = findViewById(R.id.error);
        Button submit = findViewById(R.id.submit);
        Button writeLogButton = findViewById(R.id.writeLogButton);

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowInfo(view, "Loaded! Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt.", null, null);
            }
        });

        error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowError(view, "Some error happened! :(");
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowConfirmation(view, "Confirmed, sir!", 1000);
            }
        });

        writeLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText text = findViewById(R.id.logMessage);
                LogToFile(view, text.getText().toString());
                text.setText("");
            }
        });
    }
}
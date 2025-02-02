package com.example.finances.presentation.activities;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;

import com.example.finances.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public abstract class BaseActivity extends AppCompatActivity {
    public static final int MAX_LOG_ROWS = 1000;

    protected void LogToFile(View view, String message) {
        try {
            File log = getLogFile();

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(log, true));
            outputStreamWriter.write(message + "\n");
            outputStreamWriter.close();

            ShowConfirmation(view, "Log row saved", 2000);
        }
        catch (Exception e) {
            ShowError(view, "Error on logging: " + e.getMessage());
        }
    }

    protected void LogToFile(String message) {
        try {
            File log = getLogFile();

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(log, true));
            outputStreamWriter.write(message + "\n");
            outputStreamWriter.close();
        }
        catch (Exception e) {
            Log("File write failed: " + e.toString());
        }
    }

    protected void Log(String message) {
        Log(BaseActivity.this, message);
    }

    protected static void Log(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    protected void ShowInfo(View view, String message, String actionText, View.OnClickListener action) {
        showSnackbar(
                view,
                message,
                ResourcesCompat.getColor(getResources(), R.color.gray_200, null),
                null,
                actionText,
                action
        );
    }

    protected void ShowConfirmation(View view, String message, Integer duration) {
        showSnackbar(
                view,
                message,
                ResourcesCompat.getColor(getResources(), R.color.green, null),
                duration,
                null,
                null
        );
    }

    protected void ShowError(View view, String message) {
        showSnackbar(
                view,
                message,
                ResourcesCompat.getColor(getResources(), R.color.pink, null),
                null,
                getResources().getString(R.string.open),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log("clicked!");
                    }
                }
        );
    }

    private static void showSnackbar(View view, String message, Integer backgroundColour, Integer duration, String actionText, View.OnClickListener action) {
        if(view == null)
            return;
        if(message == null)
            message = "empty message";
        if(backgroundColour == null)
            backgroundColour = Color.parseColor(String.valueOf(R.color.gray_200));
        if(duration == null)
            duration = Snackbar.LENGTH_INDEFINITE;

        Snackbar snackbar = Snackbar.make(view, message, duration);
        View snackView = snackbar.getView();

        if (snackView.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) snackView.getLayoutParams();
            params.gravity = Gravity.TOP;
            snackView.setLayoutParams(params);
        }
        else {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackView.getLayoutParams();
            params.gravity = Gravity.TOP;
            snackView.setLayoutParams(params);
        }

        snackView.setBackgroundColor(backgroundColour);
        snackbar.setTextColor(Color.WHITE);

        if(action != null) {
            if (actionText == null)
                actionText = "ACT";
            snackbar.setAction(actionText, action);
            snackbar.setActionTextColor(Color.WHITE);
        }

        snackbar.show();
    }

    protected File getLogFile() throws IOException {
        File appDir = getExternalFilesDir(null);
        File logDir = new File(appDir, "log");

        if (!logDir.exists())
            logDir.mkdir();

        File[] logs = logDir.listFiles();
        File ret = new File(logDir, "log0.txt");

        if(logs.length > 0) {
            Arrays.sort(logs, (a, b) -> b.getName().compareTo(a.getName()));

            if(countRows(logs[0]) < MAX_LOG_ROWS)
                return logs[0];

            if(logs[0].getName().contains("log")) {
                String n = logs[0].getName().substring(3, logs[0].getName().indexOf("."));
                int num = Integer.parseInt(n);
                ret = new File(logDir, "log" + (num+1) + ".txt");
            }
        }

        if (!ret.exists())
            ret.createNewFile();
        return ret;
    }

    private int countRows(File file) {
        int count = 0;

        try {
            InputStreamReader streamReader = new InputStreamReader(new FileInputStream(file));
            BufferedReader reader = new BufferedReader(streamReader);
            while(reader.readLine() != null) {
                count++;
            }
        }
        catch (Exception e) {
            Log("File read failed: " + e.toString());
        }
        finally {
            return count;
        }
    }
}

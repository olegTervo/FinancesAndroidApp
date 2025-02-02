package com.example.finances.presentation.helpers;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.TypedValue;

import androidx.annotation.ColorInt;

import com.example.finances.R;

public class PaintHelper {
    public static Paint getWhitePaint() {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);

        return paint;
    }

    public static Paint getWhiteThinPaint() {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);

        return paint;
    }

    public static Paint getNeonPaint(Context context) {
        // Oh fuck, here how you get a theme color?
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(com.google.android.material.R.attr.colorSecondary, typedValue, true);
        @ColorInt int color = typedValue.data;

        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);

        return paint;
    }

    public static Paint getRedThinPaint(Context context) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        return paint;
    }

    public static Paint getPinkThinPaint(Context context) {
        Paint paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.pink));
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);

        return paint;
    }
}

package com.example.finances.views;

import static java.time.temporal.ChronoUnit.DAYS;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import com.example.finances.Database.models.DailyGrowthDao;
import com.example.finances.R;
import com.example.finances.models.GraphPoint;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LinearGraph extends View {
    private List<GraphPoint> points;
    private List<DailyGrowthDao> values;

    private int target;

    private int width;
    private int height = 400;

    public float currentX;
    public float currentY;

    private Canvas canvas;

    public LinearGraph(Context context, ArrayList<DailyGrowthDao> values, int id, int target) {
        super(context);
        this.setId(id);
        values = new ArrayList<>(values.subList(0, 25));
        Collections.reverse(values);
        this.values = values;
        this.target = target;
        this.currentX = 150;
        this.currentY = 0;
    }

    public LinearGraph(Context context, ArrayList<DailyGrowthDao> values, int id, int target, float x, float y) {
        super(context);
        this.setId(id);

        Collections.reverse(values);
        this.values = values;
        this.target = target;
        this.currentX = x;
        this.currentY = y;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.canvas = canvas;
        this.width = this.getWidth();
        this.points = new ArrayList<GraphPoint>();
        Paint monthPaint = getMonthPaint(this.getContext());

        setPoints(this.points, this.values, this.height, this.width);
        drawBackground(this.canvas, this.width, this.height, this.currentX, this.currentY);
        drawHorizontalLine(this.canvas, getTargetPaint(this.getContext()), this.width, this.height-this.target-Math.round(this.currentY));

        if(this.values.size() > 0)
            drawEndOfMonth(this.canvas, monthPaint, this.values.get(0).date, this.width, this.height);

        this.canvas.drawPath(getGraphLine(this.points), getGraphPaint(this.getContext()));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;

            case MotionEvent.ACTION_UP:
                performClick();
                return true;
        }
        return false;
    }

    // Because we call this from onTouchEvent, this code will be executed for both
    // normal touch events and for when the system calls this using Accessibility
    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    public void setDragPoints(float x, float y) {
        this.currentX += x;
        this.currentY += y;
    }

    private static void drawEndOfMonth(Canvas canvas, Paint monthPaint, LocalDate firstDate, int width, int height) {
        LocalDate nextMonthFirst = LocalDate.of(firstDate.getYear(), firstDate.getMonth().plus(1), 1);
        long days = DAYS.between(firstDate, nextMonthFirst)+1;
        drawVerticalLine(canvas, monthPaint, height, Math.round(width/30*days));
    }

    private static void drawBackground(Canvas canvas, int width, int height, float currentX, float currentY) {
        Paint defaultPaint = getDefaultPaint();
        Paint thinPaint = getThinPaint();

        int y = Math.round(currentY);

        drawHorizontalLine(canvas, thinPaint, width, 0 - y%100);
        drawHorizontalLine(canvas, thinPaint, width, 100 - y%100);
        drawHorizontalLine(canvas, thinPaint, width, 200 - y%100);
        drawHorizontalLine(canvas, thinPaint, width, 300 - y%100);
        drawHorizontalLine(canvas, thinPaint, width, 400 - y%100);
        drawHorizontalLine(canvas, thinPaint, width, 500 - y%100);
        drawHorizontalLine(canvas, thinPaint, width, 600 - y%100);
        drawHorizontalLine(canvas, thinPaint, width, 700 - y%100);
        drawHorizontalLine(canvas, thinPaint, width, 800 - y%100);
        drawHorizontalLine(canvas, defaultPaint, width, 400 - y);

        for(int i = 0; i <= 35; i+=5) {
            drawVerticalLine(canvas, thinPaint, height+400, Math.round(width/30*(i - Math.round(currentX/30)%5)));
        }
    }

    private static void drawVerticalLine(Canvas canvas, Paint paint, int height, int x) {
        Path line = getVerticalLine(height, x);
        canvas.drawPath(line, paint);
    }

    private static void drawHorizontalLine(Canvas canvas, Paint paint, int width, int y) {
        Path line = getHorizontalLine(width, y);
        canvas.drawPath(line, paint);
    }

    private static Paint getDefaultPaint() {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);

        return paint;
    }

    private static Paint getThinPaint() {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);

        return paint;
    }

    private static Paint getGraphPaint(Context context) {
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

    private static Paint getTargetPaint(Context context) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        return paint;
    }

    private static Paint getMonthPaint(Context context) {
        Paint paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.pink));
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);

        return paint;
    }

    private static Path getGraphLine(List<GraphPoint> points) {
        Path path = new Path();

        if(points.size() != 0) {
            path.moveTo(points.get(0).x, points.get(0).y);

            for (int i = 1; i < points.size(); i++){
                path.lineTo(points.get(i).x, points.get(i).y);
            }
        }

        return path;
    }

    private static Path getHorizontalLine(int width, int y) {
        Path path = new Path();
        path.moveTo(0, y);
        path.lineTo(width, y);

        return path;
    }

    private static Path getVerticalLine(int height, int x) {
        Path path = new Path();
        int margin = 0;
        int length = (height - margin) * 2;

        path.moveTo(x, margin);
        path.lineTo(x, margin+length);

        return path;
    }

    private static void setPoints(List<GraphPoint> points, List<DailyGrowthDao> values, int height, int width) {
        int max = height;
        int min = -height;
        float stepX = width/28;

        int i = 0;

        for(DailyGrowthDao growth : values) {
            if(growth.value > max)
                points.add(new GraphPoint((i*stepX), 0)); // y = 0 means on top
            else if(growth.value < min)
                points.add(new GraphPoint((i*stepX), height*2)); // lowest point
            else
                points.add(new GraphPoint((i*stepX), (height - values.get(i).value))); // normal case

            i++;
        }
    }
}

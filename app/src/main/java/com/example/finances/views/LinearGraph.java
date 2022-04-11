package com.example.finances.views;

import static java.time.temporal.ChronoUnit.DAYS;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.TypedValue;
import android.view.View;

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

    public LinearGraph(Context context, ArrayList<DailyGrowthDao> values, int id) {
        super(context);
        Collections.reverse(values);
        this.values = values;
        this.setId(id);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.points = new ArrayList<GraphPoint>();
        setPoints(this.points, this.values, 400, this.getWidth());

        Paint defaultPaint = getDefaultPaint();
        Paint thinPaint = getThinPaint();

        drawHorizontalLine(canvas, thinPaint, this.getWidth(), 200);
        drawHorizontalLine(canvas, thinPaint, this.getWidth(), 300);
        drawHorizontalLine(canvas, defaultPaint, this.getWidth(), 400);
        drawHorizontalLine(canvas, thinPaint, this.getWidth(), 500);
        drawHorizontalLine(canvas, thinPaint, this.getWidth(), 600);

        drawVerticalLine(canvas, defaultPaint, 400, 0);
        for(int i = 0; i <= 30; i+=5) {
            drawVerticalLine(canvas, thinPaint, 400, Math.round(this.getWidth()/30*i));
        }

        LocalDate today = LocalDate.now();
        LocalDate nextMonthFirst = LocalDate.of(today.getYear(), today.getMonth().plus(1), 1);
        long days = DAYS.between(today, nextMonthFirst)+1;
        drawVerticalLine(canvas, getMonthPaint(this.getContext()), 400, Math.round(this.getWidth()/30*days));

        canvas.drawPath(getGraphLine(this.points), getGraphPaint(this.getContext()));
    }

    private void drawVerticalLine(Canvas canvas, Paint paint, int height, int x) {
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
        int margin = 180;
        int length = (height - margin) * 2;

        path.moveTo(x, margin);
        path.lineTo(x, margin+length);

        return path;
    }

    private static void setPoints(List<GraphPoint> points, List<DailyGrowthDao> values, int height, int width) {
        int max = height;
        int min = -height;
        float stepX = width/30;

        int i = 0;

        for(DailyGrowthDao grouth : values) {
            if(grouth.value > max)
                points.add(new GraphPoint((i*stepX), 0)); // y = 0 means on top
            else if(grouth.value < min)
                points.add(new GraphPoint((i*stepX), height*2)); // lowest point
            else
                points.add(new GraphPoint((i*stepX), (height - values.get(i).value))); // normal case

            i++;
        }
    }
}

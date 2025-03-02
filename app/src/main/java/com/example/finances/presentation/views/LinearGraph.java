package com.example.finances.presentation.views;

import static java.time.temporal.ChronoUnit.DAYS;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.finances.presentation.helpers.PaintHelper;
import com.example.finances.domain.models.GraphPoint;
import com.example.finances.domain.models.ValueDate;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class LinearGraph extends View {
    private List<GraphPoint> points;
    private List<ValueDate> values;
    private List<ValueDate> graphValues;
    private float target;
    private int width;
    private int height;
    public float currentX;
    public float currentY;
    private Canvas canvas;

    public LinearGraph(Context context) {
        super(context);
        setDefaultValues();
    }
    public LinearGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDefaultValues();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        this.refresh();
        this.setListeners();
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

    public void setValues(ArrayList<ValueDate> values, float target, float x, float y) {
        this.values = values;

        this.target = target;
        this.currentX = x;
        this.currentY = y;

        this.graphValues = getGraphValues(
                values,
                Math.max(values.size()-27, 0),
                Math.max(values.size()-1, 0),
                0);

        redraw();
    }

    private void setDefaultValues() {
        this.points = new ArrayList<>();
        this.values = new ArrayList<>();
        this.graphValues = new ArrayList<>();

        this.height = 500;
        this.target = 0;
        this.currentX = 0;
        this.currentY = 0;
    }

    private void setDragPoints(float x, float y) {
        this.currentX += x;
        this.currentY += y;
    }

    private void redraw() {
        int daysMove = -Math.round(this.currentX / 30);
        //if (daysMove > 30) daysMove = 30;
        //if (daysMove < -1000) daysMove = -1000;

        int valueChange = Math.round(this.currentY);
        //if (valueChange > 1000) valueChange = 1000;
        //if (valueChange < -1000) valueChange = -1000;

        int startIndex = Math.min(daysMove, this.values.size() - 1);
        startIndex = Math.max(startIndex, 1);

        int endIndex = Math.min(daysMove + 32, this.values.size());
        endIndex = Math.max(endIndex, 1);

        this.graphValues = getGraphValues(
                values,
                values.size()-endIndex,
                values.size()-startIndex,
                valueChange);

        invalidate();
    }

    private void setListeners() {
        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // no clicks
            }
        });

        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        setDragPoints(x, y);
                        return true;

                    case MotionEvent.ACTION_UP:
                        setDragPoints(-x, -y);
                        redraw();
                        return true;
                }

                return true;
            }
        });
    }

    private void refresh() {
        this.width = this.getWidth();
        //this.height = this.getHeight();
        this.points = new ArrayList<GraphPoint>();

        setPoints(this.points, this.graphValues, this.height, (2*this.height)-this.getHeight(), this.width);
        drawBackground(this.canvas, this.width, this.height, this.currentX, this.currentY);
        drawHorizontalLine(this.canvas, getTargetPaint(this.getContext()), this.width, this.height-this.target-Math.round(this.currentY));

        if(this.graphValues.size() > 0)
            drawEndOfMonth(this.canvas, getMonthPaint(this.getContext()), this.graphValues.get(0).date, this.width, this.height);

        this.canvas.drawPath(getGraphLine(this.points), getGraphPaint(this.getContext()));
    }

    private static List<ValueDate> getGraphValues(List<ValueDate> values, int start, int end, int valueChange) {
        List<ValueDate> part = new ArrayList<>();

        if(values.isEmpty())
            return part;

        for (int i = start; i <= end; i++) {
            try {
                ValueDate clone = (ValueDate) values.get(i).clone();
                clone.value += valueChange;
                part.add(clone);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        return part;
    }

    private static void setPoints(List<GraphPoint> points, List<ValueDate> values, int height, int hdef, int width) {
        int max = height;
        int min = -height+hdef;
        float stepX = width/30;

        int i = 0;

        for(ValueDate growth : values) {
            if(growth.value > max)
                points.add(new GraphPoint((i*stepX), 0)); // y = 0 means on top
            else if(growth.value < min)
                points.add(new GraphPoint((i*stepX), height*2-hdef)); // lowest point
            else
                points.add(new GraphPoint((i*stepX), (height - growth.value))); // normal case

            i++;
        }
    }

    private static void drawEndOfMonth(Canvas canvas, Paint monthPaint, LocalDate firstDate, int width, int height) {
        LocalDate nextMonthFirst;

        if (firstDate.getMonth().equals(Month.DECEMBER))
            nextMonthFirst = LocalDate.of(firstDate.getYear()+1, Month.JANUARY, 1);
        else
            nextMonthFirst = LocalDate.of(firstDate.getYear(), firstDate.getMonth().plus(1), 1);

        long days = DAYS.between(firstDate, nextMonthFirst);
        drawVerticalLine(canvas, monthPaint, height+400, Math.round(width/30*days));
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
        drawHorizontalLine(canvas, thinPaint, width, 900 - y%100);
        drawHorizontalLine(canvas, thinPaint, width, 1000 - y%100);
        drawHorizontalLine(canvas, thinPaint, width, 1100 - y%100);
        drawHorizontalLine(canvas, thinPaint, width, 1200 - y%100);
        drawHorizontalLine(canvas, defaultPaint, width, 400 - y);

        for(int i = 0; i <= 35; i+=5) {
            drawVerticalLine(canvas, thinPaint, height+400, Math.round(width/30*(i - Math.round(currentX/30)%5)));
        }
    }

    private static void drawVerticalLine(Canvas canvas, Paint paint, int height, int x) {
        Path line = getVerticalLine(height, x);
        canvas.drawPath(line, paint);
    }

    private static void drawHorizontalLine(Canvas canvas, Paint paint, int width, float y) {
        Path line = getHorizontalLine(width, y);
        canvas.drawPath(line, paint);
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

    private static Path getHorizontalLine(int width, float y) {
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

    private static Paint getDefaultPaint() {
        return PaintHelper.getWhitePaint();
    }

    private static Paint getThinPaint() {
        return PaintHelper.getWhiteThinPaint();
    }

    private static Paint getGraphPaint(Context context) {
        return PaintHelper.getNeonPaint(context);
    }

    private static Paint getTargetPaint(Context context) {
        return PaintHelper.getRedThinPaint(context);
    }

    private static Paint getMonthPaint(Context context) {
        return PaintHelper.getPinkThinPaint(context);
    }

    private void writeDebug(String text, int line) {
        Paint defaultPaint = getDefaultPaint();
        defaultPaint.setTextSize(48f);
        this.canvas.drawText(text, 100, 100*line, defaultPaint);
    }
}

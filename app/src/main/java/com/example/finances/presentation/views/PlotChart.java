package com.example.finances.presentation.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.finances.domain.models.GraphPoint;
import com.example.finances.domain.models.ValueDate;

import java.util.ArrayList;
import java.util.List;

public class PlotChart extends View {
    private List<GraphPoint> points;
    private List<ValueDate> values;
    private List<ValueDate> graphValues;
    private float target;

    private int width;
    private int height;
    public float currentX;
    public float currentY;
    private Canvas canvas;

    public PlotChart(Context context) {
        super(context);
        setDefaultValues();
    }
    public PlotChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDefaultValues();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        //this.refresh();
        //this.setListeners();
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

    private void setDefaultValues() {
        this.points = new ArrayList<>();
        this.values = new ArrayList<>();
        this.graphValues = new ArrayList<>();

        this.height = 500;
        this.target = 0;
        this.currentX = 0;
        this.currentY = 0;
    }
}

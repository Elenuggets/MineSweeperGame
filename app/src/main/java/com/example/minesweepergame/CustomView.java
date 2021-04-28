package com.example.minesweepergame;

// import for the view custom
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomView extends View
{
    // variable for my rect
    private TextPaint mTextPaint;
    private Paint rectPaint;
    Rect square;
    int sideLength;

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    // init
    private void init(AttributeSet attrs, int defStyle) {
        //Set the background color.
        setBackgroundColor(Color.rgb(255,255,255));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // -- Dimensions --
        // size of my padding
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        // size of my width-height
        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        //Bounds of squares to be drawn.
        int rectBounds = contentWidth/10;

        //Side length of the square.
        sideLength = rectBounds-2 ;

        //Paint instance for drawing the squares.
        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        //Create a rect which is actually a square.
        square = new Rect(10,10, sideLength, sideLength);


        // -- Draw the squares --
        for (int i = 0;i<=9;i++) // for each lines
        {
            for(int j=0; j<=9; j++) // for each colums
            {
                //Preliminary save of the drawing origin to the stack.
                canvas.save();

                //Translate to draw a row of squares. Firs will be at (0,0).
                canvas.translate(j * rectBounds, i * rectBounds);

                //Draw it.
                rectPaint.setColor(Color.BLACK);
                canvas.drawRect(square, rectPaint);

                //Restore. Back to the origin.
                canvas.restore();
            }
        }
    }
}

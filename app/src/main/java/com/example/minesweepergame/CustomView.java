package com.example.minesweepergame;

// import for the view custom
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomView extends View
{
    // variable for my rect
    private TextPaint mTextPaint;
    private Paint rectPaint;
    Rect square;
    int sideLength;

    // variable for my cell matrix
    Cell[][] matrixCover = new Cell[10][10];

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

        //init the matrix of cell
        for (int i = 0; i<10;i++)
        {
            for (int j=0; j<10;j++)
            {
                Cell cell = new Cell();
                cell.cellInit();
                matrixCover[i][j] = cell;
            }
        }

        mineRandom(); // create the mine randomly

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        int x = (int)event.getX(); // get the pixel x
        int y = (int)event.getY(); // get the pixel y

        int action = event.getActionMasked(); // get the action

        if (action == MotionEvent.ACTION_DOWN)
        {
            if(x < sideLength * 10 && y < sideLength * 10) // check if pixel is on the window
            {
                int i = (int)event.getX()/sideLength; // get the index i
                int j = (int)event.getY()/sideLength; // get the index j

                matrixCover[j][i].unCovered = true; // set to true
            }
        }
        postInvalidate();
        return true;
    }

    // create the 20 mines randomly
    public void mineRandom(){
        int Max = 9; // the max index in the square
        int Min = 0; // the min index in the square
        int counter = 0; // counter of mines
        while (counter != 20) // for have 20 mines
        {
            int x = (int) (Math.random() * ( Max - Min ));
            int y = (int) (Math.random() * ( Max - Min ));
            if (!matrixCover[x][y].Mine)
            {
                matrixCover[x][y].Mine = true;
                counter++;
            }
        }
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
                if (matrixCover[i][j].unCovered && matrixCover[i][j].Mine) // there is a mine
                {
                    rectPaint.setColor(Color.RED);
                }

                else // no mine
                {
                    if (matrixCover[i][j].unCovered)
                        rectPaint.setColor(Color.GRAY); // the cell is covered
                    else
                        rectPaint.setColor(Color.BLACK); // the cell is not covered
                }

                canvas.drawRect(square, rectPaint); // draw each cell

                // for display the 'M'
                if (matrixCover[i][j].unCovered && matrixCover[i][j].Mine)
                {
                    // draw the 'M'
                    Paint paint = new Paint();
                    paint.setColor(Color.BLACK); // set the color to black
                    paint.setTextSize(70); // set the size
                    paint.setStyle(Paint.Style.FILL); // set text to fill
                    canvas.drawText("M", j+(sideLength/4), i+(sideLength*3/4), paint);
                }

                //Restore. Back to the origin.
                canvas.restore();
            }
        }
    }
}

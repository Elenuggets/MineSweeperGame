package com.example.minesweepergame;

// import for the view custom
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

    //variable for the state of the game
    public static boolean gameOver = false;

    // variable for the state of the flag mode
    static boolean onFlagB = false;
    static int counterFlag = 0;

    // view of my textFlag
    TextView flagNumberView;

    // view of my textWin
    TextView winView;

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

        if (action == MotionEvent.ACTION_DOWN && !gameOver)
        {
            if(x < sideLength * 10 && y < sideLength * 10) // check if pixel is on the window
            {

                int i = (int)event.getX()/sideLength; // get the index i
                int j = (int)event.getY()/sideLength; // get the index j

                if (onFlagB) // in flag mode
                {
                    if (matrixCover[j][i].marked) // if already marked
                    {
                        matrixCover[j][i].marked = false; // set marked to false
                        counterFlag--;
                        String str = counterFlag+" ⚐";
                        flagNumberView.setText(str);
                    }
                    else // if not already marked
                    {
                        if (!matrixCover[j][i].unCovered && counterFlag!=20) // if not uncovered
                        {
                            matrixCover[j][i].marked = true; // set marked to true
                            counterFlag++;
                            String str = counterFlag+" ⚐";
                            flagNumberView.setText(str);
                        }
                    }
                }

                else // not in flag mode
                {
                    if (!matrixCover[j][i].marked) // if not marked
                        matrixCover[j][i].unCovered = true; // set uncovered to true
                    if (numberDisplay(j,i)==0 && !matrixCover[j][i].Mine)
                    {
                        dispatch(j,i);
                        Log.d("Debug","APPEL DISPATCH");
                    }
                }


                if (matrixCover[j][i].Mine && !onFlagB && !matrixCover[j][i].marked) // touch a mine, so its the end of the game
                {
                    gameOver = true;
                    winView.setText("Game over ! You lost..");
                }
            }
            if (isWin())
                winView.setText("Good game ! You win :)");
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

    public static void onFlag(){ // set to flag mode
        onFlagB = true;
    }

    public static void notOnFlag(){ // set to not flag mode
        onFlagB = false;
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

                if (matrixCover[i][j].marked) // flag mode activated
                {
                    rectPaint.setColor(Color.YELLOW);
                }

                else // not flag mode
                {
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
                }

                if (gameOver)
                {
                    if (matrixCover[i][j].Mine)
                    {
                        rectPaint.setColor(Color.RED);
                    }
                }

                canvas.drawRect(square, rectPaint); // draw each cell

                // for display the 'M'
                if ((matrixCover[i][j].unCovered && matrixCover[i][j].Mine) ||(matrixCover[i][j].Mine && gameOver))
                {
                    // draw the 'M'
                    Paint paint = new Paint();
                    paint.setColor(Color.BLACK); // set the color to black
                    paint.setTextSize(70); // set the size
                    paint.setStyle(Paint.Style.FILL); // set text to fill
                    canvas.drawText("M", j+(sideLength/4), i+(sideLength*3/4), paint);
                }

                //get the number and display for the mine aroud the case
                if (matrixCover[i][j].unCovered && !matrixCover[i][j].Mine)
                {
                    int value = numberDisplay(i,j);
                    if (value!=0)
                    {
                        Paint paint = new Paint();
                        paint.setColor(Color.BLACK); // set the color to black
                        paint.setTextSize(70); // set the size
                        paint.setStyle(Paint.Style.FILL); // set text to fill
                        canvas.drawText(""+value, j+(sideLength/4), i+(sideLength*3/4), paint);
                    }
                }

                //Restore. Back to the origin.
                canvas.restore();
            }
        }
    }


    public void dispatch(int i, int j){
        if (i<9 && !matrixCover[i+1][j].unCovered)
        {
            matrixCover[i+1][j].unCovered = true;
            if (numberDisplay(i+1,j)==0)
                dispatch(i+1,j);
        }
        if (i>0 && !matrixCover[i-1][j].unCovered)
        {
            matrixCover[i-1][j].unCovered = true;
            if (numberDisplay(i-1,j)==0)
                dispatch(i-1,j);
        }
        if (j<9 && !matrixCover[i][j+1].unCovered)
        {
            matrixCover[i][j+1].unCovered = true;
            if (numberDisplay(i,j+1)==0)
                dispatch(i,j+1);
        }
        if (j>0 && !matrixCover[i][j-1].unCovered)
        {
            matrixCover[i][j-1].unCovered = true;
            if (numberDisplay(i,j-1)==0)
                dispatch(i,j-1);
        }
        if (i<9 && j<9 && !matrixCover[i+1][j+1].unCovered)
        {
            matrixCover[i+1][j+1].unCovered = true;
            if (numberDisplay(i+1,j+1)==0)
                dispatch(i+1,j+1);
        }
        if (i>0 && j<9  && !matrixCover[i-1][j+1].unCovered)
        {
            matrixCover[i-1][j+1].unCovered = true;
            if (numberDisplay(i-1,j+1)==0)
                dispatch(i-1,j+1);
        }
        if (i<9 && j>0 && !matrixCover[i+1][j-1].unCovered)
        {
            matrixCover[i+1][j-1].unCovered = true;
            if (numberDisplay(i+1,j-1)==0)
                dispatch(i+1,j-1);
        }
        if (i>0 && j>0 && !matrixCover[i-1][j-1].unCovered)
        {
            matrixCover[i-1][j-1].unCovered = true;
            if (numberDisplay(i-1,j-1)==0)
                dispatch(i-1,j-1);
        }
    }


    // return a booleen if win
    public boolean isWin (){
        int counter = 0;
        for(int i=0; i<9;i++){
            for (int j=0;j<9;j++){
                if (matrixCover[i][j].marked && matrixCover[i][j].Mine)
                    counter++;
            }
        }
        Log.d("DEBUG",counter+" BOMBEs");
        if (counter==20)
            Log.d("DEBUG","YOU WIN");
        return counter==20;
    }

    // display the number
    public int numberDisplay(int i,int j){
        int counter = 0;
        if (i<9 && matrixCover[i+1][j].Mine)
        {
            counter++;
        }
        if (i>0 && matrixCover[i-1][j].Mine)
        {
            counter++;
        }
        if (j<9 && matrixCover[i][j+1].Mine)
        {
            counter++;
        }
        if (j>0 && matrixCover[i][j-1].Mine)
        {
            counter++;
        }
        if (i<9 && j<9 && matrixCover[i+1][j+1].Mine)
        {
            counter++;
        }
        if (i>0 && j<9 && matrixCover[i-1][j+1].Mine)
        {
            counter++;
        }
        if (i<9 && j>0 && matrixCover[i+1][j-1].Mine)
        {
            counter++;
        }
        if (i>0 && j>0 && matrixCover[i-1][j-1].Mine)
        {
            counter++;
        }
        return counter;
    }
}

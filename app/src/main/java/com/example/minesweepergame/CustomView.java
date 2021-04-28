package com.example.minesweepergame;

// import for the view custom
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomView extends View
{

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
}

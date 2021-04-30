package com.example.minesweepergame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.BreakIterator;

public class MainActivity extends AppCompatActivity {

    // custom view
    public CustomView customView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // id of custom view
        customView = findViewById(R.id.customview);
        
        //id of textview
        customView.flagNumberView = findViewById(R.id.numberOfFlag);

        //if of textwin
        customView.winView = findViewById(R.id.winText);

        customView.onFlagB = false;
        customView.counterFlag = 0;
        customView.gameOver = false;

        // button restart
        Button btnReset = (Button) findViewById(R.id.ButtomReset);
        btnReset.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        final boolean[] markingMode = {false}; // mode of the flag

        // button switch flag
        Button btnFlag = (Button) findViewById(R.id.SwitchFlag);
        btnFlag.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (markingMode[0])
                {
                    //btnFlag.setText("Marking");
                    customView.notOnFlag();
                }
                else
                {
                    //btnFlag.setText("Uncover");
                    customView.onFlag();
                }
                markingMode[0] = !markingMode[0];
            }
        });


    }
}
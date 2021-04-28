package com.example.minesweepergame;

public class Cell
{
    boolean unCovered;
    boolean Mine;

    // constructor
    public void cellInit() {
        unCovered = false;
        Mine = false;
    }
}
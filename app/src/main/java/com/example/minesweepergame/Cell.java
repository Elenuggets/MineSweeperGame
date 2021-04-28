package com.example.minesweepergame;

public class Cell
{
    boolean unCovered;
    boolean Mine;
    boolean marked;

    // constructor
    public void cellInit() {
        unCovered = false;
        Mine = false;
        marked = false;
    }
}
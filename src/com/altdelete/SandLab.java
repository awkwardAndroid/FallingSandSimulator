package com.altdelete;

import java.awt.*;
import java.util.Random;
import java.util.Scanner;
import java.io.*;


public class SandLab
{

    //particle types
    public static final int EMPTY = 0;
    public static final int METAL = 1;
    public static final int SAND = 2;
    public static final int WATER = 3;

    //do NOT add any more instance variables
    private int[][] grid;
    private SandDisplay display;

    private static int numRows = 240;
    private static int numCols = 240;

    public static void main(String[] args)
    {
        SandLab lab = new SandLab(numRows, numCols);
        lab.run();
    }

    public SandLab(int numRows, int numCols)
    {
        String[] names;
        names = new String[4]; // Bad Bad

        grid = new int[numRows][numCols];

        names[EMPTY] = "Empty";
        names[METAL] = "Metal";
        names[SAND] = "Sand";
        names[WATER] = "Water";

        display = new SandDisplay("Falling Sand Simulator", numRows, numCols, names);
    }

    //called when the user clicks on a location using the given tool
    private void locationClicked(int row, int col, int tool)
    {
        int boxSize = 5;
        for(int i = 0; i < boxSize; i++){
            for(int x = 0; x < boxSize; x++){
                if(row+x < numRows && col+i < numCols) grid[row+x][col+i] = tool;
            }
        }

    }

    //copies each element of grid into the display
    public void updateDisplay() // Slos ?
    {
        for(int column = 0; column <= display.getNumCols()-1; column++){
            for(int row = 0; row <= display.getNumRows()-1; row++){
                int type = grid[row][column];

                if(type == EMPTY){
                    display.setColor(row,column, Color.BLACK);
                } else if (type == METAL){
                    display.setColor(row,column, Color.GRAY);
                } else if(type == SAND){
                    display.setColor(row,column, Color.YELLOW);
                } else if(type == WATER){
                    display.setColor(row,column, Color.BLUE);
                }
            }
        }
    }

    //called repeatedly.
    //causes one random particle to maybe do something.
    public void step()
    {
        Random r = new Random();
        //Random r2 = new Random();

        int row = r.nextInt(numRows);
        int col = r.nextInt(numCols);
        int typeAtLocation = grid[row][col];

        physics(typeAtLocation,row,col);

    }

    //do not modify
    public void run()
    {
        while (true)
        {
            for (int i = 0; i < display.getSpeed(); i++)
                step();
            updateDisplay();
            display.repaint();
            display.pause(1);  //wait for redrawing and for mouse
            int[] mouseLoc = display.getMouseLocation();
            if (mouseLoc != null)  //test if mouse clicked
                locationClicked(mouseLoc[0], mouseLoc[1], display.getTool());
        }
    }

    public void physics(int typeAtLocation, int row, int col){
        if(typeAtLocation == SAND){
            if(row < numRows-1 && col < numCols){
                if(grid[row+1][col] == EMPTY){ // if below is empty
                    grid[row][col] = EMPTY;
                    grid[row+1][col] = SAND;
                } else if(col < numCols-1  && grid[row+1][col+1] == EMPTY){ // if below is empty and right side is empty
                    grid[row][col] = EMPTY;
                    grid[row+1][col+1] = SAND;
                } else if(col > 0 && grid[row+1][col-1] == EMPTY){ // if below is empty and left side is empty
                    grid[row][col] = EMPTY;
                    grid[row+1][col-1] = SAND;
                } else if(grid[row+1][col] == WATER){ // if below is water
                    grid[row][col] = WATER;
                    grid[row+1][col] = SAND;
                } else if(col < numCols-1  && grid[row+1][col+1] == WATER){ // if below is water and right side is water
                    grid[row][col] = WATER;
                    grid[row+1][col+1] = SAND;
                } else if(col > 0 && grid[row+1][col-1] == WATER){ // if below is water and left side is water
                    grid[row][col] = WATER;
                    grid[row+1][col-1] = SAND;
                }
            }
        } else if(typeAtLocation == WATER){
            if(row < numRows-1 && col < numCols){
                if(grid[row+1][col] == EMPTY){ // if below is empty
                    grid[row][col] = EMPTY;
                    grid[row+1][col] = WATER;
                }
                else if(col < numCols-1  && grid[row+1][col+1] == EMPTY){ // if below is empty and right side is empty
                    grid[row][col] = EMPTY;
                    grid[row+1][col+1] = WATER;
                } else if(col > 0 && grid[row+1][col-1] == EMPTY){ // if below is empty and left side is empty
                    grid[row][col] = EMPTY;
                    grid[row+1][col-1] = WATER;
                } else if(col > 0 && grid[row][col-1] == EMPTY){ // if below is empty and left side is empty
                    grid[row][col] = EMPTY;
                    grid[row][col-1] = WATER;
                } else if(col > 0 && col < numCols -1 && grid[row][col+1] == EMPTY){ // if below is empty and left side is empty
                    grid[row][col] = EMPTY;
                    grid[row][col+1] = WATER;
                }
            }
        }
    }
}
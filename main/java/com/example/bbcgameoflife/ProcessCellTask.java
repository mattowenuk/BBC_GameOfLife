package com.example.bbcgameoflife;

import android.os.AsyncTask;

import java.util.List;

public class ProcessCellTask extends AsyncTask<Void, Void, List<Cell>> {    // class used to create an asynchronous task

    public interface AsyncCellResponse {            // interface to allow Activity to become a delegate
        void processCellFinish(List<Cell> cells);   // Activity will implement this method on task completion
    }

    private AsyncCellResponse delegate = null;      // delegate is stored for when task is complete
    private boolean setToDefault, setToRandom;      // parameters needed for the 'createCellList()' method
    private int gridWidth;

    public ProcessCellTask(AsyncCellResponse delegate, boolean setToDefault, boolean setToRandom, int gridWidth) {  // constructor
        this.delegate = delegate;
        this.setToDefault = setToDefault;
        this.setToRandom = setToRandom;
        this.gridWidth = gridWidth;
    }

    @Override
    protected List<Cell> doInBackground(Void... params) {       // performed on a background thread
        return Algorithm.createCellList(setToDefault, setToRandom, gridWidth);  // cell list created with static method in Algorithm class
    }

    @Override
    protected void onPostExecute(List<Cell> cells) {    // on task completion this method executes on the UI thread
        delegate.processCellFinish(cells);      // interface method is called in the Activity
        super.onPostExecute(cells);
    }
}


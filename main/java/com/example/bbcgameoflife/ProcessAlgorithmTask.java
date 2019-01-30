package com.example.bbcgameoflife;

import android.os.AsyncTask;

import java.util.List;

public class ProcessAlgorithmTask extends AsyncTask<Void, Void, Void> { // class used to create an asynchronous task

    public interface AsyncAlgorithmResponse {       // interface to allow Activity to become a delegate
        void processAlgorithmFinish();              // Activity will implement this method on task completion
    }

    private AsyncAlgorithmResponse delegate = null; // delegate is stored for when task is complete
    private List<Cell> cells;                       // parameters needed for the 'findNumber...()' method
    private int gridWidth;

    public ProcessAlgorithmTask(AsyncAlgorithmResponse delegate, List<Cell> cells, int gridWidth) { // constructor
        this.delegate = delegate;
        this.cells = cells;
        this.gridWidth = gridWidth;
    }

    @Override
    protected Void doInBackground(Void... params) {     // performed on a background thread
        Algorithm.findNumberOfAliveNeighboursAndSetNextCellState(cells, gridWidth);  // cell list updated by 'evolution' with static method in Algorithm class
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {      // on task completion this method executes on the UI thread
        delegate.processAlgorithmFinish();      // interface method is called in the Activity
        super.onPostExecute(aVoid);
    }
}


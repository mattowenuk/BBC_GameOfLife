package com.example.bbcgameoflife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements ProcessAlgorithmTask.AsyncAlgorithmResponse, ProcessCellTask.AsyncCellResponse {     // two interfaces to use the background thread task classes

    private final int DEFAULT_GRID_WIDTH = 3;       // default to 3 x 3 grid
    private final int THREAD_THRESHOLD = 7;         // threshold where processing switches from main UI thread to background threads

    private GridView gridView;                      // displays cells
    private SeekBar seekBar;                        // allows dynamic changing of grid size
    private TextView widthTextView;                 // shows current grid width
    private Button randomButton, evolveButton;      // buttoms to randomise cell states and 'evolve' the cells

    private ArrayAdapter<Cell> adapter;             // adapter used to attach the cells to the gridView
    private List<Cell> cells;                       // list of cells that supplies the gridView

    private int gridWidth;                          // property to track the grid size

    // ---- INITIALISE ----

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);         // set the resource layout

        // get references to the UI elements
        gridView = findViewById(R.id.gridView);
        seekBar = findViewById(R.id.seekBar);
        widthTextView = findViewById(R.id.widthTextView);
        randomButton = findViewById(R.id.randomButton);
        evolveButton = findViewById(R.id.evolveButton);

        gridWidth = DEFAULT_GRID_WIDTH;  // default to a 3 x 3 grid

        seekBar.setProgress(gridWidth - DEFAULT_GRID_WIDTH);    // seekBar set from 0-9 so an offset of 3 is needed to set minimum to 3 x 3 grid
        widthTextView.setText(String.valueOf(gridWidth));       // initialise the grid view textView
        setSeekBarListener();                                   // set method for seekBar event handling

        createCellList(true, false);    // create the list of cells
        gridView.setNumColumns(gridWidth);                      // initialise gridView number of columns
        adapter = new GridItemAdapter(getApplicationContext(), cells);  // create and set adapter for gridView
        gridView.setAdapter(adapter);
        setGridViewListener();                                  // set method for gridView event handling
    }

    // ---- UI SETUP ----

    private void toggleEnableUIElements(boolean isEnabled) {    // used to disable/enable UI elements during background thread tasks
        gridView.setEnabled(isEnabled);
        seekBar.setEnabled(isEnabled);
        randomButton.setEnabled(isEnabled);
        evolveButton.setEnabled(isEnabled);
    }

    private void createCellList(boolean setToDefault, boolean setToRandom) {    // uses threshold to create cells on main or background thread
        if(gridWidth < THREAD_THRESHOLD) {
            cells = Algorithm.createCellList(setToDefault, setToRandom, gridWidth); // processes data in Algorithm class
        } else {
            toggleEnableUIElements(false);  // disable UI elements
            new ProcessCellTask(this, setToDefault, setToRandom, gridWidth).execute();  // execute on background thread
        }
    }

    private void updateAdapter() {          // used to update the gridView when the grid width and the amount of cells has been changed
        gridView.setNumColumns(gridWidth);
        adapter.clear();
        adapter.addAll(cells);
        adapter.notifyDataSetChanged();
    }

    // ---- EVENT HANDLERS ----

    private void setGridViewListener() {        // the last scenario is displayed when a cell is clicked in the gridView
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String lastScenario = cells.get(position).getScenario();
                // a 'toast' message temporarily displays the scenario
                Toast.makeText(getApplicationContext(), "Last Scenario:\n" + lastScenario, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setSeekBarListener() {     // the seekBar listens for any interaction
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {    // as it changes the grid width is updated
                gridWidth = progress + DEFAULT_GRID_WIDTH;
                widthTextView.setText(String.valueOf(gridWidth));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {      // when the bar is released the new cell list is created
                createCellList(true, false);
                if(gridWidth < THREAD_THRESHOLD) {  // the adapter is updated if the cell list created on main UI thread
                    updateAdapter();
                }
            }
        });
    }

    public void onRandomButton(View view) {     // the cell list is recreated with random states
        createCellList(false, true);
        if(gridWidth < THREAD_THRESHOLD) {      // the adapter is updated if the cell list created on main ui thread
            updateAdapter();
        }
    }

    public void onEvolveButton(View view) {     // the algorithm to set the next states of the cells is called in Algorithm class
        if(gridWidth < THREAD_THRESHOLD) {      // on main thread
            Algorithm.findNumberOfAliveNeighboursAndSetNextCellState(cells, gridWidth);
            adapter.notifyDataSetChanged();
        } else {
            toggleEnableUIElements(false);  // disable UI elements
            new ProcessAlgorithmTask(this, cells, gridWidth).execute(); // task executes on background thread
        }
    }

    // ---- INTERFACE RETURN ----

    @Override
    public void processAlgorithmFinish() {  // when the algorithm completes on the background thread
        adapter.notifyDataSetChanged();     // gridView is updated
        toggleEnableUIElements(true);   // enable UI elements
    }

    @Override
    public void processCellFinish(List<Cell> cells) {   // when the cell creation completes on the background thread
        this.cells = cells;
        updateAdapter();    // gridView is updated
        toggleEnableUIElements(true);   // enable UI elements
    }
}

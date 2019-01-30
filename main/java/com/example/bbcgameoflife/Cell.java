package com.example.bbcgameoflife;

public class Cell {

    private int position;           // used as an identifier and relates to position in list of cells in gridView
    private boolean isAlive;        // determines if the cell is 'Alive' or 'Dead'
    private String scenario;        // stores the last scenario as the cell 'evolves'

    Cell(int position) {            // constructor
        this.position = position;
        isAlive = false;            // defaults to 'Dead'
        scenario = "";
    }

    // get and set methods

    public int getPosition() {
        return position;
    }

    public boolean getAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }
}

package com.example.bbcgameoflife;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Algorithm {        // class contains static methods that use algorithms to process data

    static class Coordinate {    // inner class to combine coordinate axis data
        int xPos;
        int yPos;

        Coordinate(int xPos, int yPos) {
            this.xPos = xPos;
            this.yPos = yPos;
        }
    }

    public static List<Cell> createCellList(boolean setToDefault, boolean setToRandom, int gridWidth) { // create cell list
        List<Cell> cells = new ArrayList<>();
        for(int i = 0; i < gridWidth * gridWidth; i++) {    // grid width sets the list length
            Cell cell = new Cell(i);

            if(setToDefault) {                      // default to 3 x 3 grid
                if(i == 3 || i == 4 || i == 5) {    // middle row set to all 'Alive'
                    cell.setAlive(true);
                }
            } else if(setToRandom) {                // randomly set the state of each cell
                if(new Random().nextInt(2) == 0) {
                    cell.setAlive(true);
                }
            }

            cells.add(cell);
        }
        return cells;
    }

    public static void findNumberOfAliveNeighboursAndSetNextCellState(List<Cell> cells, int gridWidth) {    // determine next cell states

        int liveNeighbourCount = 0;                             // count of 'Alive' neighbours
        List<Boolean> nextIsAliveStates = new ArrayList<>();    // list of the next state of all cells after the 'evolution'

        for(Cell cell : cells) {
            Coordinate coordinate = getCoordinateWithPosition(cell.getPosition(), gridWidth);   // each cell's position is converted into a coordinate

            // the coordinate is used to check on all 8 possible neighbours
            if(coordinate.yPos != 0) {    // NOT top row
                // create neighbour coordinate
                Coordinate neighbourCoordinate = new Coordinate(coordinate.xPos, coordinate.yPos - 1);
                // convert coordinate to position and use to get cell from list
                Cell neighbourCell = cells.get(getPositionWithCoordinate(neighbourCoordinate, gridWidth));
                // evaluate cell's 'isAlive' boolean to determine state and increment count if 'Alive'
                if(neighbourCell.getAlive()) liveNeighbourCount++;
            }
            if(coordinate.yPos != gridWidth - 1) {    // NOT bottom row
                Coordinate neighbourCoordinate = new Coordinate(coordinate.xPos, coordinate.yPos + 1);
                Cell neighbourCell = cells.get(getPositionWithCoordinate(neighbourCoordinate, gridWidth));
                if(neighbourCell.getAlive()) liveNeighbourCount++;
            }
            if(coordinate.xPos != 0) {    // NOT left column
                Coordinate neighbourCoordinate = new Coordinate(coordinate.xPos - 1, coordinate.yPos);
                Cell neighbourCell = cells.get(getPositionWithCoordinate(neighbourCoordinate, gridWidth));
                if(neighbourCell.getAlive()) liveNeighbourCount++;
            }
            if(coordinate.xPos != gridWidth - 1) {    // NOT right column
                Coordinate neighbourCoordinate = new Coordinate(coordinate.xPos + 1, coordinate.yPos);
                Cell neighbourCell = cells.get(getPositionWithCoordinate(neighbourCoordinate, gridWidth));
                if(neighbourCell.getAlive()) liveNeighbourCount++;
            }
            if(coordinate.yPos != 0 && coordinate.xPos != 0) {    // NOT top row  AND NOT left column
                Coordinate neighbourCoordinate = new Coordinate(coordinate.xPos - 1, coordinate.yPos - 1);
                Cell neighbourCell = cells.get(getPositionWithCoordinate(neighbourCoordinate, gridWidth));
                if(neighbourCell.getAlive()) liveNeighbourCount++;
            }
            if(coordinate.yPos != 0 && coordinate.xPos != gridWidth - 1) {    // NOT top row AND NOT right column
                Coordinate neighbourCoordinate = new Coordinate(coordinate.xPos + 1, coordinate.yPos - 1);
                Cell neighbourCell = cells.get(getPositionWithCoordinate(neighbourCoordinate, gridWidth));
                if(neighbourCell.getAlive()) liveNeighbourCount++;
            }
            if(coordinate.yPos != gridWidth - 1 && coordinate.xPos != 0) {    // NOT bottom row AND NOT left column
                Coordinate neighbourCoordinate = new Coordinate(coordinate.xPos - 1, coordinate.yPos + 1);
                Cell neighbourCell = cells.get(getPositionWithCoordinate(neighbourCoordinate, gridWidth));
                if(neighbourCell.getAlive()) liveNeighbourCount++;
            }
            if(coordinate.yPos != gridWidth - 1 && coordinate.xPos != gridWidth - 1) {    // NOT bottom row AND NOT right column
                Coordinate neighbourCoordinate = new Coordinate(coordinate.xPos + 1, coordinate.yPos + 1);
                Cell neighbourCell = cells.get(getPositionWithCoordinate(neighbourCoordinate, gridWidth));
                if(neighbourCell.getAlive()) liveNeighbourCount++;
            }

            // the live neighbour count and current state of the cell used to retrieve the scenario string and next cell state
            cell.setScenario(getScenarioWith(liveNeighbourCount, cell.getAlive()));
            // the next cell state is stored in the list, to apply the changes after the evolution has completed
            nextIsAliveStates.add(isCellGoingToBeAlive(liveNeighbourCount, cell.getAlive()));

            liveNeighbourCount = 0;     // reset the count
        }

        for(int i = 0; i < cells.size(); i++) {         // the next states of the cells are applied to the cells 'isAlive' boolean
            cells.get(i).setAlive(nextIsAliveStates.get(i));
        }
    }

    private static Coordinate getCoordinateWithPosition(int position, int gridWidth) {      // convert position to coordinate
        // the variable type is important to not lose information as each number is stored
        double difference = (double) position / (double) gridWidth;
        double remainder = difference - (int) difference;
        int xPos = (int) Math.round(remainder * gridWidth);
        int yPos = (int) difference;
        return new Coordinate(xPos, yPos);
    }

    private static int getPositionWithCoordinate(Coordinate coordinate, int gridWidth) {    // convert coordinate to position
        return (coordinate.yPos * gridWidth) + coordinate.xPos;
    }

    private static String getScenarioWith(int numberOfLiveNeighbours, boolean isCurrentlyAlive) {
        // the switch statement selects the scenario depending on number of live neighbours and current state
        switch(numberOfLiveNeighbours) {
            case 0: return "No interactions\nNo live cells";
            case 1: return "Under\npopulation";
            case 2:
                if(isCurrentlyAlive) {
                    return "Cell\nSurvival";
                } else {
                    return "Under\npopulation";
                }
            case 3:
                if(isCurrentlyAlive) {
                    return "Cell\nSurvival";
                } else {
                    return "Creation\nof life";
                }
            default: return "Over\npopulation";
        }
    }

    private static boolean isCellGoingToBeAlive(int numberOfLiveNeighbours, boolean isCurrentlyAlive) {
        // the next state of the cell is evaluated using the number of live neighbours and current state
        // the default is set to 'false' with only 2 specific scenarios causing a 'true' result
        return (numberOfLiveNeighbours == 2 && isCurrentlyAlive) || numberOfLiveNeighbours == 3;
    }
}



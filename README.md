# BBC_GameOfLife
BBC Technical Test - Game of Life Game

Matt Owen

The ‘Game of Life’ game was coded in Java and XML within android studio, creating an application that provides an easy and clear display of the grid, and allows user interaction to adjust the grid size, the state of the cells and perform the ‘evolution’. The files added to the provided template are:

Algorithm.java,   
Cell.java, 
MainActivity.java,  
GridItemAdapter.java, 
ProcessCellTask.java,  
ProcessAlgorithmTask.java,  
activity_main.xml, 
item_cell.xml.

The Algorithm.java contains the data processing, the other files contain the code for the look of the UI elements, their interaction and the handling of background threads. The file notes for each are detailed below along with the design considerations.

Design Notes:

A 3x3 grid was assumed to be the default, with the UI allowing it to be increased to 12x12. The code is designed to allow the possibility of a grid to grow towards ‘infinity’ with the delays from the data processing being performed independently to the UI thread. To aid this the minimal amount of data was stored within the ‘Cell’ objects to reduce the memory usage. The data processing is performed using the lowest amount of iterations through the cell list.

The brief described a theoretical ‘infinite’ grid, however, within the software the grid designed has definite edge rows and columns, so the main algorithm processing is to determine the existence of the neighbouring cells as well as their state. This is because attempting to access data that is not valid will cause errors and crashes.


File Notes:

Algorithm.java – 
All data processing is performed in this class in re-usable static methods. It creates the list of ‘Cell’ objects with Boolean parameters for the initial state of the cells. An inner ‘Coordinate’ class is used to store axis information of the cell position. This allows the neighbour cells to be identified easily using the x and y positions and their state is evaluated to count the ‘live’ neighbours. The subsequent scenario and next state of the cell is evaluated and stored within the ‘Cell’ objects.

Cell.java – 
A class to hold the information of the cell objects. The ‘isAlive’ Boolean stores the state of the cell and the last ‘scenario’ is a string of characters describing which scenario occurred during the last ‘evolution’.

MainActivity.java – 
Within this file is the method called when the application starts, ‘onCreate’. This file firstly initialises all the UI elements and stores references to them within the properties. The events generated by user interactions are handled in listener methods, to change the grid size, randomise the cell states and ‘evolve’ the cells. The grid cells can be clicked, and the last scenario string is displayed to the user.  The file uses a threshold to choose to perform the data processing on a background thread using the ‘Task’ classes if the grid size is large.

GridItemAdapter.java – 
This class contains code to recycle the views as the grid is scrolled by the user, to re-use previously created ones instead of recreating them every time. It simply displays an ”O” if the cell is ‘Alive’ and nothing if it is ‘dead’.

ProcessCellTask.java and ProcessAlgorithmTask.java – 
These classes are used to call the data processing methods in the ‘Algorithm’ class on a background thread. An Interface is used to call a method within the Activity upon task completion. This is done so these classes can potentially be re-used by other Activities and avoid memory leakage.



import java.util.Random;
public class Board {
    public Cell[][] coordinates;
    public Boat[] boats;
    // hits and misses are only used for the labels in BattleboatsGUI
    public int totalShots, turns, remainingShips, hits, misses, numOfMissileUses, numOfDroneUses;
    // message is only used to update the boardPanel message in BattleboatsGUI
    public String message;

    public Board(int x, int y) {
        if(x < 11 && x > 2 && y < 11 & y > 2) {
            coordinates = new Cell[x][y];
            for(int i=0; i<x; i++) {
                for(int j=0; j<y; j++) {
                    coordinates[i][j] = new Cell(i, j, '-');
                }
            }
        }
        hits = 0;
        misses = 0;
        totalShots = 0;
        turns = 0;
        numOfMissileUses = 0;
        numOfDroneUses = 0;
        message = "";
    }

    

    public void placeBoats() {
        int height = coordinates.length;
        int width = coordinates[0].length;
        int[] boatsNeeded;
        // initialize the length of the boats needed in order to have correct boats depending on the board size
        if(width == 3 || height == 3) {
            boatsNeeded = new int[]{2};
        }
        else if(width == 4 || height == 4) {
            boatsNeeded = new int[]{2,3};
        }
        else if(width <= 6 || height <= 6) {
            boatsNeeded = new int[]{2,3,3};
        }
        else if(width <= 8 || height <= 8) {
            boatsNeeded = new int[]{2,3,3,4};
        }
        else {
            boatsNeeded = new int[]{2,3,3,4,5};
        }
        // assign class variables properly
        boats = new Boat[boatsNeeded.length];
        remainingShips = boatsNeeded.length;
        // loop through and place each boat
        for(int i=0; i<boatsNeeded.length;) {
            Random random = new Random();
            // size of the boat
            int size = boatsNeeded[i];
            boolean thereIsRoom = true;
            // will set the direction of the boat
            boolean horizontal = random.nextBoolean();
            // initializes the array of cell objects that will be assigned to this boat
            Cell[] cells = new Cell[size];
            // these are the coordinates that the boat will be centered around
            int x = random.nextInt(height);
            int y = random.nextInt(width);
            Cell c = coordinates[x][y];
            // check to see if this specific cell is empty, if not, do another iteration
            if(c.getStatus() != '-') continue;
            // this variable keeps track of how many cells have been added in order to not go over the correct amount of cells necessary for this boat
            int cellNumber = 0;
            // this variable allows looking past the adjacent tiles if the size of the boat is greater than 3
            int move = 0;
            // loop through to check the areas around the cell that has been checked
            while(thereIsRoom==true) {
                if(horizontal) {
                    // check to see if there is room for a boat
                    if(c.getColumn()-move>0 && c.getColumn()<width-1-move) {
                        // initialize the cells just to the left and just to the right of the center coordinate, and check to see if they are both empty
                        Cell left = coordinates[x][y-1-move];
                        Cell right = coordinates[x][y+1+move];
                        boolean leftEmpty = (left.getStatus()=='-');
                        boolean rightEmpty = (right.getStatus()=='-');
                        if(!leftEmpty|| !rightEmpty) break;
                        // add one cell to the left
                        cellNumber++;
                        if(cellNumber>=size) break;
                        cells[cellNumber] = left;
                        // add one cell to the right if there is room on the board
                        cellNumber++;
                        if(cellNumber>=size) break;
                        cells[cellNumber] = right;
                        if(size>3) move++;
                    }
                    else thereIsRoom = false;
                }
                else {
                    if(c.getRow()-move>0 && c.getRow()<height-1-move) {
                        // initialize the cells just above and just below the center coordinate, and check to see if they are both empty
                        Cell above = coordinates[x-1-move][y];
                        Cell below = coordinates[x+1+move][y];
                        boolean aboveEmpty = (above.getStatus()=='-');
                        boolean belowEmpty = (below.getStatus()=='-');
                        if(!aboveEmpty || !belowEmpty) break;
                        // add one cell above
                        cellNumber++;
                        if(cellNumber>=size) break;
                        cells[cellNumber] = above;
                        // add another cell below if there is room in the boat
                        cellNumber++;
                        if(cellNumber>=size) break;
                        cells[cellNumber] = below;
                        if(size>3) move++;
                    }
                    else thereIsRoom = false;
                }
            }
            if(!thereIsRoom || cellNumber<size) {
                // continues another iteration if there was not room for a boat
                continue;
            }
            cells[0] = c;
            // set all of the cell's statuses to 'B'
            for(int k=0; k<cells.length; k++) {
                if(cells[k]!=null) cells[k].setStatus('B');
            }
            // create a new boat object
            boats[i] = new Boat(boatsNeeded[i], horizontal, cells);
            i++;
        } 
    }

    public void fire(int x, int y) {
        // check to see if coordinates are valid
        if(x >= coordinates.length || x < 0 || y >= coordinates[0].length || y < 0) {
            System.out.println("\nPenalty! Area is out of bounds.");
            turns++;
            System.out.println("\nTurn " + turns + ": skipped");
            turns++;
            return;
        }
        Cell c = coordinates[x][y];
        switch(c.getStatus()) {
            case '-':
                message = "Miss";
                System.out.println("\nMiss");
                c.setStatus('M');
                misses++;
                break;
            case 'B':
                checkIfSunk(c);
                break;
            case 'H':
                message = "Penalty! Area has already been hit.";
             // they already guessed this position and there was a boat there
                System.out.println("\nPenalty! Area has already been hit.");
                turns++;
                System.out.println("\nTurn " + turns + ": skipped");
                break;
            case 'M':
                message = "Penalty! Area has already been guessed.";
            // they already guessed this position and there was no boat there
                System.out.println("\nPenalty! Area has already been guessed.");
                turns++;
                System.out.println("\nTurn " + turns + ": skipped");
                break;
        }
        turns++;
        totalShots++;
        coordinates[x][y] = c;
    }

    public void display() {
        // print out values to show the board, but hide it when there is a boat there
        System.out.println();
        for(int i=0; i<coordinates.length; i++) {
            for(int j=0; j<coordinates[0].length; j++) {
                if(coordinates[i][j].getStatus()!='B') {
                    System.out.print(coordinates[i][j].getStatus() + " ");
                }
                else System.out.print("- ");
            }
            System.out.println();
        }
    }

    public void print() {
        System.out.println();
        for(int i=0; i<boats.length; i++) {
            System.out.println("Boat: " + boats[i]);
            for(int j=0; j<boats[i].getLocation().length; j++) {
                Cell c = boats[i].getLocation()[j];
                System.out.println("(" + c.getRow() + "," + c.getColumn() + ") status: " + c.getStatus());
            }
            System.out.println();
        }
    }

    public void missile(int x, int y) {
        turns++;
        if(x >= coordinates.length || x < 0 || y >= coordinates[0].length || y < 0) {
            message = "Please input valid coordinates";
            System.out.println("\nPlease input valid coordinates");
            return;
        }
        int lowerX = x-1, upperX = x+1, lowerY = y-1, upperY = y+1;
        // check to see if the missile will hit somewhere off the edge of the board
        if(lowerX<0) lowerX=0;
        if(upperX>=coordinates.length) upperX=coordinates.length-1;
        if(lowerY<0) lowerY=0;
        if(upperY>=coordinates.length) upperY=coordinates.length-1;
        // hit each coordinate within the lower and upper bound for x and y
        for (int i=lowerX; i<=upperX; i++) {
            for (int j=lowerY; j<=upperY; j++) {
                // create a local cell variable to save typing
                Cell c = coordinates[i][j];
                // if there is a boat there, hit it
                if(c.getStatus() == 'B') {
                    checkIfSunk(c);
                }
                // if there is nothing there, show that it was a miss
                else if(c.getStatus() == '-') {
                    c.setStatus('M');
                    misses++;
                }
                totalShots++;
            }
        }
        numOfMissileUses++;
    }

    public void drone(int direction, int index) {
        // scanning a row
        int numOfBoats = 0;
        if(direction==1) {
            for (int i=0; i<coordinates[0].length; i++) {
                if(coordinates[index][i].getStatus() == 'B') numOfBoats++;
            }
            // send the proper message based on how many boats were scanned in the row
            if(numOfBoats==1) message = "Drone has scanned " + String.valueOf(numOfBoats) + " boat in row " + String.valueOf(index);
            else message = "Drone has scanned " + String.valueOf(numOfBoats) + " boats in row " + String.valueOf(index);
            System.out.println("\nDrone has scanned " + numOfBoats + " boats in row " + index);
        }
        // scanning a column
        else {
            for (int i=0; i<coordinates.length; i++) {
                if(coordinates[i][index].getStatus() == 'B') numOfBoats++;
            }
            // send the proper message based on how many boats were scanned in the column
            if(numOfBoats==1) message = "Drone has scanned " + String.valueOf(numOfBoats) + " boat in column " + String.valueOf(index);
            else message = "Drone has scanned " + String.valueOf(numOfBoats) + " boats in column " + String.valueOf(index);
            System.out.println("\nDrone has scanned " + numOfBoats + " boats in column " + index);
        }
        numOfDroneUses++;
        turns++;
    }

    public void checkIfSunk(Cell c) {
        c.setStatus('H');
        hits++;
        int i=0;
        // use this label in order to break out of the nested for loop once the proper boat is found
        outerloop:
        for (i=0; i<boats.length; i++) {
            for (int j=0; j<boats[i].getLocation().length; j++) {
                if(c.getRow()==boats[i].getLocation()[j].getRow() && c.getColumn()==boats[i].getLocation()[j].getColumn()) {
                    break outerloop;
                }
            }
        }
        Boat b = boats[i];
        // check to see if boat is sunk
        boolean sunk = true;
        for (int k=0; k<b.getLocation().length; k++) {
            if(b.getLocation()[k].getStatus() == 'B') sunk = false;
        }
        if(sunk) {
            // the boat has been sunk
            message = "Sunk!";
            System.out.println("\nSunk!");
        }
        else {
            // the boat has not yet been sunk
            message = "Hit!";
            System.out.println("\nHit!");
        }
    }

    public boolean boatsLeft() {
        // checks to see if there are any boats that have not been sunk, if false, the game is over
        for(int i=0; i<boats.length; i++) {
            for(int j=0; j<boats[i].getLocation().length; j++) {
                if(boats[i].getLocation()[j].getStatus() == 'B') return true;
            }
        }
        message = "GAME OVER THANK YOU FOR PLAYING!";
        return false;
    }
}
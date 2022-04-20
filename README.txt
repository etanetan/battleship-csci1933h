Ethan Myos (myos0004)

How to compile: javac Game.java
                java Game

                javac BattleboatsGUI.java
                java BattleboatsGUI

Assumptions:
    The user inputs proper bounds for the battleship game (greater than 2 and less than 11) and user is also inputting an integer

Additional Features:
    Added int variables in Board.java in order to keep track of how many times the user has used the missile and drone powers: numOfMissileUses, numOfDroneUses
    A helper method to check if the player wants to play in debug mode: debugModeDecision(Scanner s)
    Don't let the user use the missile if the board size is 3 by 3 or 4 by 4, otherwise they can use each power once
    A helper method to check if a boat is sunk when it is hit: checkIfSunk(Cell c)

Outside Sources:
    How to break out of a nested for loop: https://stackoverflow.com/questions/886955/how-do-i-break-out-of-nested-loops-in-java
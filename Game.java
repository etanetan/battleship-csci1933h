import java.util.Scanner;
public class Game {
    
    
    public static void main(String[] args) {
        Scanner user = new Scanner(System.in);
        System.out.print("Input your dimensions for the battleship board (x y): ");
        // correctly size the 
        int xDimension = user.nextInt();
        int yDimension = user.nextInt();
        Board board = new Board(xDimension, yDimension);
        board.placeBoats();
        // check to see if the user wants to play in debug mode
        boolean debug = debugModeDecision(user);
        if(debug) board.print();
        else board.display();
        board.turns++;
        while(board.boatsLeft()) {
            System.out.print("\nTurn " + board.turns + ", input a command (fire, missile, drone, quit): ");
            String choice = user.next();
            switch(choice) {
                case "fire":
                    System.out.print("\nInput coordinates: ");
                    int x = user.nextInt();
                    int y = user.nextInt();
                    board.fire(x,y);
                    break;
                case "missile":
                    // if they have used a missile, don't let them use it again
                    if(board.numOfMissileUses>0) {
                        System.out.println("\nMissile has already been used.\n");
                        break;
                    }
                    // if the board size is 3 by 3 or 4 by 4, don't allow missile to be used
                    if(board.coordinates.length <= 4 && board.coordinates[0].length <= 4) {
                        System.out.println("\nMissile cannot be used on this small of a board.\n");
                        break;
                    }
                    System.out.print("\nInput coordinates: ");
                    int x2 = user.nextInt();
                    int y2 = user.nextInt();
                    board.missile(x2, y2);
                    break;
                case "drone":
                    if(board.numOfDroneUses>0) {
                        System.out.println("\nDrone has already been used.\n");
                        break;
                    }
                    System.out.print("\nWould you like to scan a row or column? type in r for row and c for column: ");
                    String rOrC = user.next();
                    // check to see if user input correct letters
                    while(!(rOrC.equals("r") || rOrC.equals("c"))) {
                        System.out.println("\nInvalid input. please type in r for row and c for column.: ");
                        rOrC = user.next();
                    }
                    System.out.print("\nWhich row or column would you like to scan?: ");
                    int scan = user.nextInt();
                    // check to see if user input a row or column within the boundaries
                    while((rOrC.equals("r") && (scan<0 || scan>board.coordinates.length)) || (rOrC.equals("c") && (scan<0 || scan<board.coordinates[0].length))) {
                        scan = user.nextInt();
                        System.out.println("\nInvalid input, please type in a number within the boundaries of the board.");
                    }
                    // call the drone function, a value of 1 means it is scanning a row, a value of 0 means it is scanning a column
                    if(rOrC.equals("r")) board.drone(1, scan);
                    else board.drone(0, scan);
                    break;
                case "quit":
                    return;
                default:
                    System.out.println();
                    break;
            }
            if(debug) board.print();
            else board.display();
        }
        System.out.println("\nGAME OVER THANK YOU FOR PLAYING!\nIt took you " + (board.turns-1) + " turns to finish the game.");
        user.close();
    }
    
    

    

    public static boolean debugModeDecision(Scanner s) {
        boolean debug = true;
        System.out.print("\nWould you like to play in debug mode? (yes or no): ");
        String str = s.next();
        switch(str) {
            case "yes":
                break;
            case "no":
                debug = false;
                break;
            default:
                debugModeDecision(s);
                break;
        }
        return debug;
    }
}

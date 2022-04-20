import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BattleboatsGUI implements ActionListener, MouseListener {

    private BattleboatsGUI game;

    private final BoardPanel boardPanel;
    private final Label message;
    private final JFrame frame;
    private final Rectangle[][] rectangles = new Rectangle[10][10];

    private final Board board;
    private boolean revealed;

    private boolean missileUsed;
    private boolean droneUsed;

    private Button missile;
    private Button drone;

    private Button rowSelect;
    private Button columnSelect;

    private Label turns;
    private Label hits;
    private Label misses;

    private int direction;

    public BattleboatsGUI() {
		// create a 10x10 board object and place the boats randomly
        board = new Board(10,10);
        board.placeBoats();

        frame = new JFrame();
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(null);

        boardPanel = new BoardPanel(this);
        boardPanel.setBounds(50, 50, 400, 400);
        boardPanel.addMouseListener(this);

		// Initialize the message displayed over the board
        message = new Label("Welcome to Battleboats!");
        message.setBounds(100,10,300,30);
		
        // button reveals the positions of the boats
        Button reveal = new Button("Reveal Board");
        reveal.addActionListener(this);
        reveal.setActionCommand("Reveal");
        reveal.setBounds(475, 475, 100, 50);

        // add a turn label
        turns = new Label("Turns: " + String.valueOf(board.turns));
        turns.setBounds(475, 10, 75, 25);
        // add a hits label
        hits = new Label("Hits: " + String.valueOf(board.hits));
        hits.setBounds(475, 40, 75, 25);
        // add a misses label
        misses = new Label("Misses: " + String.valueOf(board.misses));
        misses.setBounds(475, 70, 75, 25);

		// create a drone button
        drone = new Button("Drone " + "(" + String.valueOf(board.numOfDroneUses) + ")");
        drone.addActionListener(this);
        drone.setActionCommand("Drone");
        drone.setBounds(475, 200, 100, 50);
        // create a row button that the user will be able to see after they click on the drone button
        rowSelect = new Button("Row");
        rowSelect.addActionListener(this);
        rowSelect.setActionCommand("Row");
        rowSelect.setBounds(475, 200, 50, 50);
        rowSelect.setVisible(false);
        // create a column button that the user will be able to see after they click on the drone button
        columnSelect = new Button("Column");
        columnSelect.addActionListener(this);
        columnSelect.setActionCommand("Column");
        columnSelect.setBounds(525, 200, 50, 50);
        columnSelect.setVisible(false);

        direction = 1;
        // create a missile button
        missile = new Button("Missile " + "(" + String.valueOf(board.numOfMissileUses) + ")");
        missile.addActionListener(this);
        missile.setActionCommand("Missile");
        missile.setBounds(475, 300, 100, 50);
        // add all of the components to the frame
        frame.add(message);
        frame.add(boardPanel);
        frame.add(reveal);
        frame.add(missile);
        frame.add(drone);
        frame.add(turns);
        frame.add(hits);
        frame.add(misses);
        frame.add(rowSelect);
        frame.add(columnSelect);
        // display the frame
        frame.setVisible(true);

        int x = 0, y = 0;
        for(int row = 0; row < rectangles.length; row++) {
            for (int col = 0; col < rectangles[0].length; col++) {
                // create a new rectangle object and assign it to its proper position in the 2d array
                rectangles[row][col] = new Rectangle(x, y, 40, 40);
                x += 40;
            }
            y += 40;
            x -= 400;
        }
		// Call the repaint method to draw the rectangles for the first time
		// The board panel repaint method calls the repaint method in BattleboatsGUI
        boardPanel.repaint();
    }

    public void repaint(Graphics g) {
        Color color = Color.black;
        for (int row = 0; row < rectangles.length; row++) {
            for (int col = 0; col < rectangles[0].length; col++) {
                Rectangle r = rectangles[row][col];
                switch(board.coordinates[row][col].getStatus()) {
                    case '-':
                        color = Color.white;
                        break;
                    case 'B':
                        if(revealed==true) color = Color.yellow;
                        else color = Color.white;
                        break;
                    case 'M':
                        color = Color.blue;
                        break;
                    case 'H':
                        color = Color.red;
                        break;
                }
                g.setColor(color);
                // fill the rectangle with the color assigned to it
                g.fillRect(r.x, r.y, r.width, r.height);
                g.setColor(Color.black);
                // outline the rectangle in black
                g.drawRect(r.x, r.y, r.width, r.height);
            }
        }
    }

    public static void main(String[] args) {
        BattleboatsGUI bb = new BattleboatsGUI();
    }
    @Override
    public void actionPerformed(ActionEvent action) {
        String actionName = action.getActionCommand();
        switch (actionName) {
            case "Reveal":
                // if the board was already revealed, hide the boats again if they click reveal board again
                if(revealed==true) {
                    revealed = false;
                    board.message = "Boats hidden";
                }
                // otherwise, show the boats
                else {
                    revealed = true;
                    board.message = "Boats revealed";
                }
                boardPanel.repaint();
                break;
            case "Missile":
                // don't let them use the missile if they have already
                if(board.numOfMissileUses != 0) break;
                missileUsed = true;
                break;
            case "Drone":
                // don't let them use the drone if they have already
                if(board.numOfDroneUses != 0) break;
                droneUsed = true;
                drone.setVisible(false);
                rowSelect.setVisible(true);
                columnSelect.setVisible(true);
                break;
            case "Row":
                break;
            case "Column":
                // make sure the drone will scan a column
                direction = 0;
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        int xCoord = mouseEvent.getX();
        int yCoord = mouseEvent.getY();

        // makes sure the click was inside the board
        if (!boardPanel.contains(xCoord, yCoord)) {
            return;
        }

        // calculate the row and column position of the click
        int row = yCoord / 40;
        int col = xCoord / 40;

        if(!missileUsed && !droneUsed) {
            // user just wants to fire
            board.fire(row, col);
        }
        else if(board.numOfMissileUses<1 && missileUsed) {
            board.missile(row, col);
            missileUsed = false;
        }
        else if(board.numOfDroneUses<1 && droneUsed) {
            // call the drone function correctly based on if they want to scan a row or column
            if(direction==1) board.drone(direction, row);
            else board.drone(direction, col);
            droneUsed = false;
            // set the "row" and "column" labels to not be visible, and make the "drone" label visible
            rowSelect.setVisible(false);
            columnSelect.setVisible(false);
            drone.setVisible(true);
        }
        // update all of the labels and button labels
        this.turns.setText("Turns " + String.valueOf(board.turns));
        this.hits.setText("Hits " + String.valueOf(board.hits));
        this.misses.setText("Misses " + String.valueOf(board.misses));
        this.missile.setLabel("Missile " + "(" + String.valueOf(board.numOfMissileUses) + ")");
        this.drone.setLabel("Drone " + "(" + String.valueOf(board.numOfDroneUses) + ")");
        if(!board.boatsLeft()) this.message.setText(board.message);
        this.message.setText(board.message);
        this.boardPanel.repaint();
    }


    // TODO: NOTHING!
    //  The remaining methods don't need to filled in, they are just required to be present in the class
    //  because we are implementing MouseListener
    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

// TODO: Nothing!
class BoardPanel extends JPanel {
    BattleboatsGUI game;

    public BoardPanel(BattleboatsGUI game) {
        this.game = game;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.repaint(g);
    }
}

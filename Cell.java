public class Cell {
    private int row, col;
    // status can be '-', 'B', 'H', 'M'
    private char status;

    public Cell(int row, int col, char status) {
        this.row = row;
        this.col = col;
        this.status = status;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return col;
    }

    public char getStatus() {
        return status;
    }

    public void setRow(int r) {
        row = r;
    }

    public void setColumn(int c) {
        col = c;
    }
    
    public void setStatus(char c) {
        status = c;
    }
}
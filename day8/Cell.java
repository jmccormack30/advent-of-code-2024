import java.util.Objects;

public class Cell {
    private int row;
    private int col;
    private Character c;

    public Cell(int row, int col, Character c) {
        this.row = row;
        this.col = col;
        this.c = c;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Character getChar() {
        return c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell other = (Cell) o;
        return this.row == other.getRow() && this.col == other.getCol() && this.c == other.getChar();
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, c);
    }

    @Override
    public String toString() {
        return "ROW: " + row + ", COL: " + col + ", CHAR: " + c;
    }
}

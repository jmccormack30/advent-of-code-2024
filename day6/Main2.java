import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

class Main2 {

    public enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST
    }

    public static class Position {
        Character c;
        int row;
        int col;
        Direction direction;

        public Position(int row, int col, Direction direction) {
            this(null, row, col, direction);
        }
    
        public Position(Character c, int row, int col, Direction direction) {
            this.c = c;
            this.row = row;
            this.col = col;
            this.direction = direction;
        }
    
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Position)) return false;
            Position other = (Position) o;
            boolean isEqual = (this.row == other.row && this.col == other.col && this.direction == other.direction);
            return isEqual;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col, direction);
        }

        public String toString() {
            return "X: " + row + ", Y: " + col + ", D: " + direction;
        }
    }

	public static void main(String[] args) throws Exception {
        String fileName = "input.txt";

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        int value = decodeFile(br);
        System.out.println("Answer = " + value);
    }

    private static int decodeFile(BufferedReader br) throws Exception {
        String line;
        char[][] arr = new char[130][130];
        Set<Position> visited = new LinkedHashSet<>();
        Position player = null;

        for (int i = 0; (line = br.readLine()) != null; i++) {
            if (line.indexOf("^") != -1) {
                player = new Position(i, line.indexOf("^"), Direction.NORTH);
                visited.add(player);
            }

            char[] rowArr = line.toCharArray();
            arr[i] = rowArr;
        }

        char[][] displayArr = new char[130][130];

        for (int i = 0; i < arr.length; i++) {
            displayArr[i] = arr[i].clone();
        }

        Set<Position> values = new HashSet<>();
        Set<Position> cycles = new HashSet<>();

        Position playerCopy = new Position(player.row, player.col, player.direction);

        while (playerCopy != null) {
            Position nextPos = getNextPosition(arr, playerCopy, visited);

            if (isOutOfBounds(arr, nextPos)) {
                playerCopy = null;
                continue;
            }

            if ('^' == nextPos.c) {
                playerCopy = nextPos;
                continue;
            }

            visited.add(nextPos);
            values.add(new Position(nextPos.row, nextPos.col, null));
            playerCopy = nextPos;
        }

        for (Position p : values) {
            visited = new LinkedHashSet<>();
            playerCopy = new Position(player.row, player.col, player.direction);

            char[][] arrCopy = new char[130][130];

            for (int i = 0; i < arr.length; i++) {
                arrCopy[i] = arr[i].clone();
            }

            int row = p.row;
            int col = p.col;

            arrCopy[row][col] = '#';

            boolean isCycle = isCycle(arrCopy, playerCopy, visited);

            if (isCycle) {
                displayArr[row][col] = 'O';
                cycles.add(new Position(row, col, null));
            }

            arr[row][col] = '.';
        }
        System.out.println();

        for (char[] arrr : displayArr) {
            System.out.println(arrr);
        }
        System.out.println();

        return cycles.size();
    }

    private static boolean isCycle(char[][] arr, Position player, Set<Position> visited) {
        while (player != null) {
            visited.add(player);
            Position nextPos = getNextPosition(arr, player, visited);

            if (nextPos == null) {
                System.out.println("All 4 directions are # which makes a CYCLE!");;
                return false;
            }

            if (isOutOfBounds(arr, nextPos)) {
                player = null;
                continue;
            }

            if (visited.contains(nextPos)) {
                return true;
            }

            player = nextPos;
        }

        return false;
    }

    private static Position getNextPosition(char[][] arr, Position player, Set<Position> visited) {
        int i = 0;
        Position newPos;

        while (true && i < 4) {
            newPos = getNewPosition(arr, player);

            if (isOutOfBounds(arr, newPos)) {
                return newPos;
            }

            char c = arr[newPos.row][newPos.col];
            newPos.c = c;

            if ('.' == c || '^' == c) return newPos;

            if ('#' == c) {
                player = new Position(player.row, player.col, turnRight(player.direction));
                visited.add(player);
                newPos = getNewPosition(arr, player);
                i += 1;
            }
            else {
                throw new RuntimeException("Invalid character: " + c);
            }
        }

        return null;
    }

    private static Position getNewPosition(char[][] arr, Position player) {
        int row = player.row;
        int col = player.col;

        switch(player.direction) {
            case NORTH:
                row -= 1;
                break;
            case EAST:
                col += 1;
                break;
            case SOUTH:
                row += 1;
                break;
            case WEST:
                col -= 1;
                break;
            default:
                throw new IllegalArgumentException("Invalid direction: " + player.direction);
        }

        return new Position(row, col, player.direction);
    }

    // 90 degree turn right
    private static Direction turnRight(Direction current) {
        switch (current) {
            case NORTH:
                return Direction.EAST;
            case EAST:
                return Direction.SOUTH;
            case SOUTH:
                return Direction.WEST;
            case WEST:
                return Direction.NORTH;
            default:
                throw new IllegalArgumentException("Unexpected value: " + current);
        }
    }

    private static boolean isOutOfBounds(char[][] arr, Position pos) {
        return (pos.row < 0 || pos.row >= arr.length || pos.col < 0 || pos.col >= arr[0].length);
    }
}
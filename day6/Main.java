import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class Main {

    public enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST
    }

    public static class Position {
        int row;
        int col;
        Direction direction;
    
        public Position(int row, int col, Direction direction) {
            this.row = row;
            this.col = col;
            this.direction = direction;
        }
    
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Position)) return false;
            Position other = (Position) o;
            boolean isEqual = (this.row == other.row && this.col == other.col);
            return isEqual;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
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
        Set<Position> visited = new HashSet<>();
        Position player = null;

        for (int i = 0; (line = br.readLine()) != null; i++) {
            if (line.indexOf("^") != -1) {
                player = new Position(i, line.indexOf("^"), Direction.NORTH);
                visited.add(player);
            }

            char[] rowArr = line.toCharArray();
            arr[i] = rowArr;
        }

        while (player != null) {
            player = getNextPosition(arr, player, visited);
            if (player != null) visited.add(player);
        }

        return visited.size();
    }

    private static Position getNextPosition(char[][] arr, Position player, Set<Position> visited) {
        Position newPos;

        while (true) {
            newPos = getNewPosition(arr, player);

            if (isOutOfBounds(arr, newPos)) {
                return null;
            }

            char c = arr[newPos.row][newPos.col];

            if ('.' == c || '^' == c) return newPos;

            if ('#' == c) {
                player.direction = turnRight(player.direction);
                newPos = getNewPosition(arr, player);
            }
            else {
                throw new RuntimeException("Invalid character: " + c);
            }
        }
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
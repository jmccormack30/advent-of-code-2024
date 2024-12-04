import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Main {
	public static void main(String[] args) {
        String fileName = "input.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            int value = decodeFilePart2(br);
            System.out.println("Answer = " + value);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static int decodeFilePart1(BufferedReader br) throws Exception {
        String line;
        int total = 0;
        char[][] arr = new char[140][140];

        for (int i = 0; (line = br.readLine()) != null; i++) {
            char[] innerArr = line.toCharArray();
            arr[i] = innerArr;
        }

        for (int r = 0; r < arr.length; r++) {
            char[] row = arr[r];

            for (int c = 0; c < row.length; c++) {
                char cur = arr[r][c];

                if ('X' == cur) {
                    total += getTotalPart1(arr, r, c);
                }
            }
        }

        return total;
    }

    private static int decodeFilePart2(BufferedReader br) throws Exception {
        String line;
        char[][] arr = new char[140][140];

        for (int i = 0; (line = br.readLine()) != null; i++) {
            char[] innerArr = line.toCharArray();
            arr[i] = innerArr;
        }

        Map<Cell, Integer> cellCountMap = new HashMap<>();

        for (int r = 0; r < arr.length; r++) {
            char[] row = arr[r];

            for (int c = 0; c < row.length; c++) {
                char cur = arr[r][c];

                if ('M' == cur) {
                    getTotalPart2(arr, r, c, cellCountMap);
                }
            }
        }

        return (int) cellCountMap.values().stream().filter(num -> num == 2).count();
    }

    private static void getTotalPart2(char[][] arr, int r, int c, Map<Cell, Integer> cellCountMap) {
        boolean isSpaceRight = (c + 2 <= 139);
        boolean isSpaceLeft = (c - 2 >= 0);
        boolean isSpaceUp = (r - 2 >= 0);
        boolean isSpaceDown = (r + 2 <= 139);

        if (isSpaceRight) {
            if (isSpaceUp && isValidPart2(arr, r-1, r-2, c+1, c+2)) addToMap(cellCountMap, r-1, c+1);
            if (isSpaceDown && isValidPart2(arr, r+1, r+2, c+1, c+2)) addToMap(cellCountMap, r+1, c+1);
        }

        if (isSpaceLeft) {
            if (isSpaceUp && isValidPart2(arr, r-1, r-2, c-1, c-2)) addToMap(cellCountMap, r-1, c-1);
            if (isSpaceDown && isValidPart2(arr, r+1, r+2, c-1, c-2)) addToMap(cellCountMap, r+1, c-1);
        }
    }

    private static int getTotalPart1(char[][] arr, int r, int c) {
        int total = 0;

        boolean isSpaceRight = (c + 3 <= 139);
        boolean isSpaceLeft = (c - 3 >= 0);
        boolean isSpaceUp = (r - 3 >= 0);
        boolean isSpaceDown = (r + 3 <= 139);

        if (isSpaceRight) {
            total += (isValidPart1(arr, r, r, r, c+1, c+2, c+3)) ? 1 : 0;

            if (isSpaceUp) total += (isValidPart1(arr, r-1, r-2, r-3, c+1, c+2, c+3)) ? 1 : 0;
            if (isSpaceDown) total += (isValidPart1(arr, r+1, r+2, r+3, c+1, c+2, c+3)) ? 1 : 0;
        }

        // left-straight, left-diagonal-up, left-diagonal-down
        if (isSpaceLeft) {
            total += (isValidPart1(arr, r, r, r, c-1, c-2, c-3)) ? 1 : 0;

            if (isSpaceUp) total += (isValidPart1(arr, r-1, r-2, r-3, c-1, c-2, c-3)) ? 1 : 0;
            if (isSpaceDown) total += (isValidPart1(arr, r+1, r+2, r+3, c-1, c-2, c-3)) ? 1 : 0;
        }

        if (isSpaceDown) total += (isValidPart1(arr, r+1, r+2, r+3, c, c, c)) ? 1 : 0;
        if (isSpaceUp) total += (isValidPart1(arr, r-1, r-2, r-3, c, c, c)) ? 1 : 0;

        return total;
    }

    private static boolean isValidPart1(char[][] arr, int r1, int r2, int r3, int c1, int c2, int c3) {
        return (arr[r1][c1] == 'M' && arr[r2][c2] == 'A' && arr[r3][c3] == 'S');
    }

    private static boolean isValidPart2(char[][] arr, int r1, int r2, int c1, int c2) {
        return (arr[r1][c1] == 'A' && arr[r2][c2] == 'S');
    }

    public record Cell(Integer row, Integer col) {}

    private static void addToMap(Map<Cell, Integer> cellCountMap, int r, int c) {
        cellCountMap.compute(new Cell(r, c), (k, v) -> (v == null) ? 1 : v + 1);
    }
}
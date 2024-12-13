import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

class Main {
	public static void main(String[] args) throws Exception {
        String fileName = "input.txt";

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        long value = decodeFile(br);
        System.out.println("\nAnswer = " + value);
    }

    private static long decodeFile(BufferedReader br) throws Exception {
        Stream<String> lines = br.lines();
        char[][] charArr = lines.map(s -> s.toCharArray()).toArray(char[][]::new);

        Map<Character, Set<Cell>> charCellMap = new HashMap<>();

        for (int row = 0; row < charArr.length; row++) {
            char[] arr = charArr[row];

            for (int col = 0; col < arr.length; col++) {
                char c = arr[col];
                if (c == '.') continue;
                
                Cell cell = new Cell(row, col, c);
                charCellMap.computeIfAbsent(c, k -> new HashSet<>()).add(cell);
            }
        }

        int rows = charArr.length;
        int cols = charArr[0].length;

        Set<Cell> antinodes = new HashSet<>();

        for (Set<Cell> cells : charCellMap.values()) {
            List<Cell> list = new ArrayList<>(cells);

            for (int i = 0; i < list.size(); i++) {
                Cell c1 = list.get(i);
                for (int j = i + 1; j < list.size(); j++) {
                    Cell c2 = list.get(j);
                    
                    getAntiNodes(c1, c2, rows, cols, antinodes);
                    getAntiNodes(c2, c1, rows, cols, antinodes);
                }
            }
        }

        return antinodes.size();
    }

    private static void getAntiNodes(Cell c1, Cell c2, int rows, int cols, Set<Cell> antinodes) {
        int deltaX = c1.getCol() - c2.getCol();
        int deltaY = c1.getRow() - c2.getRow();

        int newX = c1.getCol() + deltaX;
        int newY = c1.getRow() + deltaY;

        if (newX < 0 || newX >= cols || newY < 0 || newY >= rows) {
            return;
        }
        
        Cell cell = new Cell(newY, newX, null);
        antinodes.add(cell);
    }
}
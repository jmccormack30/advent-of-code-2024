import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Main {
	public static void main(String[] args) throws Exception {
        String fileName = "input.txt";
        BufferedReader br = new BufferedReader(new FileReader(fileName));

        int[][] arr = br.lines().map(String::toCharArray).map(Main::toIntArray).toArray(int[][]::new);

        int rows = arr.length;
        int cols = arr[0].length;
        
        Set<Node> nodes = getNodes(arr);
        Map<Position, Node> nodeMap = nodes.stream().collect(Collectors.toMap(Node::getPosition, node -> node));
        addNeighbors(nodes, nodeMap, rows, cols);

        List<Node> trailHeads = nodes.stream().filter(n -> 0 == n.getNumber()).toList();

        int score = trailHeads.stream().mapToInt(Main::getScore).sum();
        int rating = trailHeads.stream().mapToInt(Main::getRating).sum();

        System.out.println("Score = " + score);
        System.out.println("Rating = " + rating);
    }

    private static int[] toIntArray(char[] charArr) {
        return IntStream.range(0, charArr.length).map(i -> Character.getNumericValue(charArr[i])).toArray();
    }

    private static Set<Node> getNodes(int[][] arr) {
        Set<Node> nodes = new HashSet<>();

        for (int r = 0; r < arr.length; r++) {
            int[] row = arr[r];

            for (int c = 0; c < row.length; c++) {
                int num = row[c];
                nodes.add(new Node(num, r, c));
            }
        }

        return nodes;
    }

    private static void addNeighbors(Set<Node> nodes, Map<Position, Node> nodeMap, int rows, int cols) {
        for (Node node : nodes) {
            Position position = node.getPosition();

            int row = position.getRow();
            int col = position.getCol();

            if (row - 1 >= 0) {
                node.addNode(nodeMap.get(new Position(row - 1, col)));
            }

            if (row + 1 < rows) {
                node.addNode(nodeMap.get(new Position(row + 1, col)));
            }

            if (col - 1 >= 0) {
                node.addNode(nodeMap.get(new Position(row, col - 1)));
            }

            if (col + 1 < cols) {
                node.addNode(nodeMap.get(new Position(row, col + 1)));
            }
        }
    }

    private static int getScore(Node node) {
        return getTrailEnds(node).size();
    }

    private static Set<Node> getTrailEnds(Node node) {
        int number = node.getNumber();

        if (9 == number) return Set.of(node);

        Set<Node> trailEnds = new HashSet<>();

        for (Node neighbor : node.getNodes()) {
            if (number + 1 == neighbor.getNumber()) {
                trailEnds.addAll(getTrailEnds(neighbor));
            }
        }
        
        return trailEnds;
    }

    private static int getRating(Node node) {
        int number = node.getNumber();

        if (9 == number) return 1;

        int rating = 0;

        for (Node neighbor : node.getNodes()) {
            if (number + 1 == neighbor.getNumber()) {
                rating += getRating(neighbor);
            }
        }

        return rating;
    }
}
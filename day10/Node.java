import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Node {
    private int number;
    private Position pos;
    private List<Node> nodes;

    public Node(int number, int row, int col) {
        this.number = number;
        this.pos = new Position(row, col);
        this.nodes = new ArrayList<>();
    }

    public int getNumber() {
        return number;
    }

    public Position getPosition() {
        return pos;
    }
    
    public int getRow() {
        return pos == null ? -1 : pos.getRow();
    }

    public int getCol() {
        return pos == null ? -1 : pos.getCol();
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void addNode(Node node) {
        this.nodes.add(node);
    }

    @Override
    public String toString() {
        return number + " : " + pos.toString();
    }

     @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return this.pos.equals(node.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos);
    }
}

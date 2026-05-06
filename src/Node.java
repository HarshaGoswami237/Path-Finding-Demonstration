public class Node implements Comparable<Node> {
    public Node parent;
    public int row, col;
    public int gCost, hCost, fCost;
    public boolean walkable;

    public Node(int r, int c) {
        this.row = r;
        this.col = c;
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.fCost, other.fCost);
    }
}
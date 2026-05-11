import java.util.*;

public class Pathfinder {
    GamePanel gp;
    Node[][] nodes;
    PriorityQueue<Node> openList;
    HashSet<Node> closedSet; // This prevents the infinite loop
    ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalReached = false;

    public Pathfinder(GamePanel gp) {
        this.gp = gp;
        nodes = new Node[gp.ROWS][gp.COLS];
        for (int r = 0; r < gp.ROWS; r++) {
            for (int c = 0; c < gp.COLS; c++) {
                nodes[r][c] = new Node(r, c);
            }
        }
    }

    public void setNodes(int startRow, int startCol, int goalRow, int goalCol) {
        openList = new PriorityQueue<>();
        closedSet = new HashSet<>();
        pathList.clear();
        goalReached = false;

        startNode = nodes[startRow][startCol];
        goalNode = nodes[goalRow][goalCol];

        for (int r = 0; r < gp.ROWS; r++) {
            for (int c = 0; c < gp.COLS; c++) {
                nodes[r][c].walkable = (gp.grid[r][c] != 1);

                nodes[r][c].gCost = 99999;  // Reset to "infinity"
                nodes[r][c].parent = null;
            }
        }

        startNode.gCost = 0;
        startNode.fCost = calculateH(startNode);
        openList.add(startNode);
    }

    public ArrayList<Node> getPath() {
        int maxChecks = 500; // Safety break
        int checks = 0;

        while (!openList.isEmpty() && !goalReached && checks < maxChecks) {
            checks++;
            currentNode = openList.poll();
            closedSet.add(currentNode);

            if (currentNode.row == goalNode.row && currentNode.col == goalNode.col) {
                goalReached = true;
                trackPath();
                break;
            }

            checkNeighbors();
        }
        return pathList;
    }

    private void checkNeighbors() {
        // 8 directions: Up, Down, Left, Right + the 4 diagonals
        int[][] dirs = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},   // Straight
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}  // Diagonals
        };

        for (int[] d : dirs) {
            int nr = currentNode.row + d[0];
            int nc = currentNode.col + d[1];

            if (nr >= 0 && nr < gp.ROWS && nc >= 0 && nc < gp.COLS) {
                Node neighbor = nodes[nr][nc];
                if (!neighbor.walkable || closedSet.contains(neighbor)) continue;

                // Straight moves cost 10, Diagonal moves cost 14 (approx 1.41)
                int moveCost = (d[0] == 0 || d[1] == 0) ? 10 : 14;
                int gScore = currentNode.gCost + moveCost;

                if (gScore < neighbor.gCost) {
                    neighbor.parent = currentNode;
                    neighbor.gCost = gScore;
                    neighbor.hCost = calculateH(neighbor);
                    neighbor.fCost = neighbor.gCost + neighbor.hCost;

                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                    }
                }
            }
        }
    }

    private int calculateH(Node n) {
        int dx = Math.abs(n.col - goalNode.col);
        int dy = Math.abs(n.row - goalNode.row);

        // D = 10 (straight), D2 = 14 (diagonal)
        // Formula: D * (dx + dy) + (D2 - 2 * D) * min(dx, dy)
        return 10 * (dx + dy) + (14 - 20) * Math.min(dx, dy);
    }

    private void trackPath() {
        Node current = goalNode;
        while (current != startNode && current != null) {
            pathList.add(0, current);
            current = current.parent;
        }
    }
}
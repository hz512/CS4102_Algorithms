import java.util.*;

public class Node {
    public String color;
    public Node partner;
    public ArrayList<Node> neighbors;
    public int column;
    public int row;
    public boolean visited;

    public Node(String label, int c, int r){
        this.column = c;
        this.row = r;
        this.color = label;
        this.neighbors = new ArrayList<>();
        this.partner = null;
        this.visited = false;
    }
}

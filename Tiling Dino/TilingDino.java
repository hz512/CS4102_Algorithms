import java.util.*;

public class TilingDino {
    public List<String> tileImage(List<String> fileLines) {
        List<String> result = new ArrayList<>();
        if (fileLines.isEmpty()) {
            result.add("impossible");
            return result;
        }

        /* fill out the graph of nodes */
        int r = fileLines.size();
        int c = fileLines.get(0).length();
        Node[][] graph = new Node[r][c];
        for (int i = 0; i < r; i++)
            for (int j = 0; j < c; j++)
                graph[i][j] = new Node("unknown", j, i);

        /* Use BFS to label (color) all nodes in graph*/
        ArrayList<Node> b_list = new ArrayList<>();
        ArrayList<Node> g_list = new ArrayList<>();
        Queue<Node> queue = new LinkedList<>();
        Node s = graph[0][0];
        s.color = "b"; s.visited = true;
        queue.add(s);
        if (fileLines.get(0).charAt(0) == '#')
            b_list.add(s);
        while (!queue.isEmpty()){
            Node curr = queue.poll();
            ArrayList<Node> nbhood = getNeighbors(graph, curr.column, curr.row);
            for (Node node : nbhood){
                curr.neighbors.add(node);
                if (!node.visited){
                    node.visited = true;
                    if (curr.color.equals("b")){
                        node.color = "g";
                        if (fileLines.get(node.row).charAt(node.column) == '#')
                            g_list.add(node);
                    }
                    else if (curr.color.equals("g")){
                        node.color = "b";
                        if (fileLines.get(node.row).charAt(node.column) == '#')
                            b_list.add(node);
                    }
                    queue.add(node);
                }
                if (node.visited && node.color.equals(curr.color))
                    System.out.println("error while BFS");
            }
        }

        /* if not equal number of nodes w/ different color */
        if (b_list.size() != g_list.size()){
            result.add("impossible");
            return result;
        }

        /* calculate the max flow */
        int n = b_list.size();
        boolean[][] matrix = new boolean[n][n];
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                Node b_node = b_list.get(i);
                Node g_node = g_list.get(j);
                if (b_node.neighbors.contains(g_node) && g_node.neighbors.contains(b_node))
                    matrix[i][j] = true;
                else
                    matrix[i][j] = false;
            }
        }
        MaxFlow mf = new MaxFlow();
        int maxFlow = mf.maxFlow(b_list, g_list, matrix);
        if (maxFlow != b_list.size()){
            result.add("impossible");
            return result;
        }

        for (Node node : b_list){
            if (node.partner == null){
                ArrayList<String> ret = new ArrayList<>();
                ret.add("impossible");
                return ret;
            }
            String str = "";
            str += node.column + " " + node.row + " " + node.partner.column + " " + node.partner.row;
            result.add(str);
        }

        return result;
    }

    public ArrayList<Node> getNeighbors(Node[][] graph, int c, int r){
        Node curr = graph[r][c];
        int row = graph.length;
        int column = graph[0].length;
        ArrayList<Node> ret = new ArrayList<>();

        for (int i = Math.max(0, r-1); i <= Math.min(row-1, r+1); i++){
            for (int j = Math.max(0, c-1); j <= Math.min(column-1, c+1); j++){
                if ( (graph[i][j] != null) && ( (i==r) || (j==c) ) && !( (i==r) && (j==c) ) ){
                    ret.add(graph[i][j]);
                }
            }
        }
        return ret;
    }
}

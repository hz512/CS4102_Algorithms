import java.util.*;

public class MaxFlow {

    public MaxFlow(){}

    // b_list as row; g_list as column
    public boolean canMatch(ArrayList<Node> b_list, ArrayList<Node> g_list, boolean matrix[][],
                            int u, boolean seen[], int match[]){
        int n = matrix.length;
        for (int v = 0; v < n; v++){
            if (matrix[u][v] && !seen[v]){
                seen[v] = true;
                if (match[v] < 0 || canMatch(b_list, g_list, matrix, match[v], seen, match)){
                    b_list.get(u).partner = g_list.get(v);
                    g_list.get(v).partner = b_list.get(u);
                    match[v] = u;
                    return true;
                }
            }
        }
        return false;
    }

    public int maxFlow(ArrayList<Node> b_list, ArrayList<Node> g_list, boolean matrix[][]){
        int n = b_list.size();
        int match[] = new int[n];
        for (int i = 0; i < n; i++){
            match[i] = -1;
        }

        int result = 0;
        for (int u = 0; u < n; u++){
            boolean[] seen = new boolean[n];
            for (int i = 0; i < n; i++)
                seen[i] = false;
            if (canMatch(b_list, g_list, matrix, u, seen, match))
                result ++;
        }
        return result;
    }
}

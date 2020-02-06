import java.util.*;

public class SeamCarving {
    private ArrayList<Integer> finalBT;
    public SeamCarving() {
        finalBT = new ArrayList<>();
    }
    
    /**
     * This method is the one you should implement.  It will be called to perform
     * the seam carving.  You may create any additional data structures as fields
     * in this class or write any additional methods you need.
     * 
     * @return the seam's weight
     */
    public double run(int[][][] image) {
        int m = image.length; // column numbers
        int n = image[0].length; // row numbers
        double[][] dp = new double[n][m];

        ArrayList<ArrayList<Integer>> backList = new ArrayList<>();
        ArrayList<ArrayList<Integer>> prevList = new ArrayList<>();
        for (int j = 0; j < n; j++){
            for (int i = 0; i < m; i++){
                double energy = getEnergy(image, i, j);
                if (j == 0) {
                    dp[j][i] = energy;
                    ArrayList<Integer> bl = new ArrayList<>();
                    bl.add(i);
                    backList.add(bl);
                    prevList.add(bl);
                }
                else{
                    ArrayList<Integer> uppers = getUpperNeighbors(image, i, j);
                    double min = Double.MAX_VALUE;
                    int index = 0;
                    double sumSoFar;
                    for (int k = 0; k < uppers.size(); k+=2){
                        sumSoFar = dp[uppers.get(k+1)][uppers.get(k)];
                        if (sumSoFar < min) {
                            min = sumSoFar;
                            index = uppers.get(k);
                        }
                    }
                    dp[j][i] = min + energy;
                    ArrayList<Integer> updatedBL = new ArrayList<>(backList.get(index));
                    if (backList.get(index).size() > j)
                        updatedBL = new ArrayList<>(prevList.get(index));
                    updatedBL.add(i);
                    backList.remove(i);
                    backList.add(i, updatedBL);
                }
            }
            prevList = new ArrayList<>(backList);
        }
        double fMin = Double.MAX_VALUE;
        int index = 0;
        for (int k = 0; k < m; k++){
            if (dp[n-1][k] < fMin) {
                fMin = dp[n - 1][k];
                index = k;
            }
        }
        finalBT = backList.get(index);
        return fMin;
    }
    
    /**
     * Get the seam, in order from top to bottom, where the top-left corner of the
     * image is denoted (0,0).
     * 
     * Since the y-coordinate (row) is determined by the order, only return the x-coordinate
     * 
     * @return the ordered list of x-coordinates (column number) of each pixel in the seam
     */
    public int[] getSeam() {
        int[] ret = new int[finalBT.size()];
        for (int i = 0; i < finalBT.size(); i++){
            ret[i] = finalBT.get(i);
        }
        return ret;
    }

    public double getDistance(int[][][] image, int i1, int j1, int i2, int j2){
        int red1 = image[i1][j1][0]; int red2 = image[i2][j2][0];
        int green1 = image[i1][j1][1]; int green2 = image[i2][j2][1];
        int blue1 = image[i1][j1][2]; int blue2 = image[i2][j2][2];
        double redDist = Math.pow(red1 - red2, 2);
        double greenDist = Math.pow(green1 - green2, 2);
        double blueDist = Math.pow(blue1 - blue2, 2);

        return Math.sqrt(redDist + greenDist + blueDist);
    }

    public double getEnergy(int[][][] image, int i, int j){
        int m = image.length; // column numbers
        int n = image[0].length; // row numbers
        if (i < 0 || j < 0 || i > m || j > n)
            return 0xBADBAD;

        int cnt = 0;
        // i+1, j
        double d1 = 0;
        if (i + 1 < m){
            cnt ++;
            d1 = getDistance(image, i, j, i+1, j);
        }
        // i, j+1
        double d2 = 0;
        if (j + 1 < n){
            cnt ++;
            d2 = getDistance(image, i, j, i, j+1);
        }
        // i-1, j
        double d3 = 0;
        if (i > 0){
            cnt ++;
            d3 = getDistance(image, i, j, i-1, j);
        }
        // i, j-1
        double d4 = 0;
        if (j > 0){
            cnt ++;
            d4 = getDistance(image, i, j, i, j-1);
        }

        return (d1+d2+d3+d4)/cnt;
    }

    public ArrayList<Integer> getUpperNeighbors(int[][][] image, int i, int j){
        int m = image.length; // column numbers
        int n = image[0].length; // row numbers
        ArrayList<Integer> list = new ArrayList<>();
        if (i > 0 && j > 0){
            list.add(i-1); list.add(j-1);
        }
        if (j > 0){
            list.add(i); list.add(j-1);
        }
        if (i < m-1 && j > 0){
            list.add(i+1); list.add(j-1);
        }
        return list;
    }
}

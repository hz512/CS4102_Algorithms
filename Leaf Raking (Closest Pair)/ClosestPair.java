/*
Name: Haoran Zhu
Email ID: hz3fr
Collaborators: None
Sources: slides from class
 */

import javafx.util.Pair;
import java.util.*;

public class ClosestPair {
    public double closestPairDistance(List<String> fileData) {
        // Sort points by x
        Pair[] pts = new Pair[fileData.size() - 1];
        for (int i = 1; i < fileData.size(); i++){
            String str = fileData.get(i); int index = str.indexOf(" ");
            double x = Double.parseDouble(str.substring(0, index));
            double y = Double.parseDouble(str.substring(index + 1));
            Pair<Double, Double> pt = new Pair<>(x,y);
            pts[i - 1] = pt;
        }
        sortX(pts);
        return cpd(pts);
    }

    public double cpd(Pair<Double, Double>[] pts){
        int n = pts.length;
        // Base case (n = 2 && n = 3)
        if (n == 2){
            Pair<Double, Double> p0 = pts[0]; Pair<Double, Double> p1 = pts[1];
            pts[0] = compareYMin(p0,p1); pts[1] = compareYMax(p0,p1);
            return getDistance(pts[0], pts[1]);
        }
        if (n == 3){
            Pair<Double, Double> p0 = pts[0]; Pair<Double, Double> p1 = pts[1]; Pair<Double, Double> p2 = pts[2];
            pts[0] = compareYMin(p0,compareYMin(p1,p2)); pts[2] = compareYMax(p0,compareYMax(p1,p2));
            pts[1] = new Pair<>(p0.getKey()+p1.getKey()+p2.getKey()-pts[0].getKey()-pts[2].getKey(),
                    p0.getValue()+p1.getValue()+p2.getValue()-pts[0].getValue()-pts[2].getValue());
            return Math.min(getDistance(pts[0], pts[1]), Math.min(getDistance(pts[1], pts[2]),
                    getDistance(pts[0], pts[2])));
        }

        // Find min distane in left and right
        Pair[] left = new Pair[n/2]; Pair[] right = new Pair[n - left.length];
        for (int i = 0; i < n/2; i++) left[i] = pts[i];
        for (int j = n/2; j < n; j++) right[j-n/2] = pts[j];
        double innerMin = Math.min(cpd(left), cpd(right));
        // Find the median
        double median = (n % 2 == 1) ? pts[n/2].getKey() : (pts[n/2].getKey() + pts[(n/2)-1].getKey())/2;
        // Find runaway
        List<Pair<Double, Double>> runawayL = new ArrayList<>();
        // Merge sorted points by y
        mergeY(left, right, pts);
        for (int i = 0; i < pts.length; i++){
            if (Math.abs((pts[i].getKey() - median)) <= innerMin) runawayL.add(pts[i]);
        }

        // Find minDistance in runaway
        double runawayMin = Double.MAX_VALUE;
        for (int i = 0; i < runawayL.size(); i++){
            ArrayList<Pair<Double, Double>> closePtsL = new ArrayList<>();
            for (int j = 1; j <= 15; j++){
                if ((i + j) <= runawayL.size() - 1){
                    closePtsL.add(runawayL.get(i + j));
                } else break;
            }
            double tempMin = bruteForceMin(closePtsL);
            if (tempMin < runawayMin)
                runawayMin = tempMin;
        }

        return Math.min(innerMin, runawayMin);
    }

    // Sorting (by x) part of mergesort
    public void sortX(Pair[] pts){
        if (pts.length <= 1) return;
        Pair[] first = new Pair[pts.length/2];
        Pair[] second = new Pair[pts.length - first.length];
        for (int i = 0; i < first.length; i++) first[i] = pts[i];
        for (int i = 0; i < second.length; i++) second[i] = pts[first.length + i];
        sortX(first); sortX(second);
        mergeX(first, second, pts);
    }

    // Merging (by x) part of mergesort
    public void mergeX(Pair[] first, Pair[] second, Pair[] pts){
        int iFirst = 0; int iSecond = 0; int j = 0;
        while (iFirst < first.length && iSecond < second.length){
            if ((double) first[iFirst].getKey() < (double) second[iSecond].getKey()){
                pts[j] = first[iFirst];
                iFirst ++;
            }
            else {
                pts[j] = second[iSecond];
                iSecond ++;
            }
            j ++;
        }
        while (iFirst < first.length){
            pts[j] = first[iFirst];
            iFirst ++;
            j ++;
        }
        while (iSecond < second.length){
            pts[j] = second[iSecond];
            iSecond ++;
            j ++;
        }
    }

    // Merge two sublists that are sorted by y
    public void mergeY(Pair[] first, Pair[] second, Pair[] pts){
        int iFirst = 0; int iSecond = 0; int j = 0;
        while (iFirst < first.length && iSecond < second.length){
            if ((double) first[iFirst].getValue() < (double) second[iSecond].getValue()){
                pts[j] = first[iFirst];
                iFirst ++;
            }
            else {
                pts[j] = second[iSecond];
                iSecond ++;
            }
            j ++;
        }
        while (iFirst < first.length){
            pts[j] = first[iFirst];
            iFirst ++;
            j ++;
        }
        while (iSecond < second.length){
            pts[j] = second[iSecond];
            iSecond ++;
            j ++;
        }
    }

    public Pair<Double, Double> compareYMin(Pair<Double, Double> a, Pair<Double, Double> b){
        return (a.getValue() < b.getValue()) ? a : b;
    }
    public Pair<Double, Double> compareYMax(Pair<Double, Double> a, Pair<Double, Double> b) {
        return (a.getValue() < b.getValue()) ? b : a;
    }
    public double getDistance(Pair<Double, Double> a, Pair<Double, Double> b){
        double xa = a.getKey(); double ya = a.getValue();
        double xb = b.getKey(); double yb = b.getValue();
        return Math.sqrt(Math.pow(xa-xb, 2) + Math.pow(ya-yb, 2));
    }

    // Only used for closePts array which contains at most 15 elements
    public double bruteForceMin(ArrayList<Pair<Double, Double>> list){
        double minD = Double.MAX_VALUE;
        for (int i = 0; i < list.size(); i++){
            for (int j = i + 1; j < list.size(); j ++){
                if (getDistance(list.get(i), list.get(j)) < minD)
                    minD = getDistance(list.get(i), list.get(j));
            }
        }
        return minD;
    }
}

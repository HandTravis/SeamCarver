package seamcarving;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dynamic programming implementation of the {@link SeamFinder} interface.
 *
 * @see SeamFinder
 * @see SeamCarver
 */
public class DynamicProgrammingSeamFinder implements SeamFinder {
    @Override
    public List<Integer> findHorizontalSeam(double[][] energies) {
        double[][] dpTable = new double[energies.length][energies[0].length];
        dpTable[0] = energies[0];
        for (int j = 1; j < energies.length; j++) {
            for (int k = 0; k < energies[0].length; k++) {
                double minEnergy = energies[j - 1][k];
                double leftUp = Double.POSITIVE_INFINITY;
                double leftDown = Double.POSITIVE_INFINITY;
                if (k > 0) {
                    leftUp = energies[j - 1][k - 1];
                }
                if (k < energies[0].length - 1) {
                    leftDown = energies[j - 1][k + 1];
                }
                minEnergy = Math.min(Math.min(leftUp, leftDown), minEnergy);
                dpTable[j][k] = minEnergy + energies[j][k];
            }
        }
        List<Integer> path = new ArrayList<>();
        int index = 0;
        double lowestEnergy = Double.POSITIVE_INFINITY;
        for (int i = 0; i < energies[0].length; i++) {
            if (dpTable[energies.length - 1][i] < lowestEnergy) {
                lowestEnergy = dpTable[energies.length - 1][i];
                index = i;
            }
        }
        path.add(index);
        for (int c = energies.length - 2; c >= 0; c--) {
            double sameRow = dpTable[c][index];
            double rowDown = Double.POSITIVE_INFINITY;
            double rowUp = Double.POSITIVE_INFINITY;
            double finalVal;
            if (index < energies[0].length - 1) {
                rowDown = dpTable[c][index + 1];
            }
            if (index > 0) {
                rowUp = dpTable[c][index - 1];
            }
            finalVal = Math.min(Math.min(rowDown, rowUp), sameRow);
            if (finalVal == rowDown) {
                index += 1;
            } else if (finalVal == rowUp) {
                index -= 1;
            }
            path.add(index);
        }
        Collections.reverse(path);
        return path;

        // for (int c = energies.length - 1; c >= 0; c--) {
        //     int currentMindex = 0;
        //     double lowestEnergy = Double.POSITIVE_INFINITY;
        //     for (int r = 0; r < energies[0].length; r++) {
        //         if (dpTable[c][r] < lowestEnergy) {
        //             currentMindex = r;
        //             lowestEnergy = dpTable[c][r];
        //         }
        //     }
        //     path.add(currentMindex);
        // }
        // Collections.reverse(path);
        // return path;
    }

    @Override
    public List<Integer> findVerticalSeam(double[][] energies) {
        double[][] dpTable = new double[energies.length][energies[0].length];
        for (int i = 0; i < dpTable[0].length; i++) {
            dpTable[0][i] = energies[0][i];
        }

        for (int j = 1; j < energies[0].length; j++) {
            for (int k = 0; k < energies.length; k++) {
                double minEnergy = energies[k][j - 1];
                double leftMin = Double.POSITIVE_INFINITY;
                double rightMin = Double.POSITIVE_INFINITY;
                if (k > 0) {
                    leftMin = energies[k - 1][j - 1];
                }
                if (k < energies.length - 1) {
                    rightMin = energies[k + 1][j - 1];
                }
                minEnergy = Math.min(Math.min(leftMin, rightMin), minEnergy);
                dpTable[k][j] = minEnergy + energies[k][j];
            }
        }
        List<Integer> path = new ArrayList<>();
        for (int r = dpTable[0].length - 1; r >= 0; r--) {
            int currentMindex = 0;
            double lowestEnergy = Double.POSITIVE_INFINITY;
            for (int c = 0; c < dpTable.length; c++) {
                if (dpTable[c][r] < lowestEnergy) {
                    currentMindex = c;
                    lowestEnergy = dpTable[c][r];
                }
            }
            path.add(currentMindex);
        }
        Collections.reverse(path);
        return path;
    }
}

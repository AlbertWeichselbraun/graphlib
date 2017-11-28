package net.semanticlab.lib.graph.network;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 * Helper class for handling Network objects.
 * 
 * @author Albert Weichselbraun
 *
 */
public class ArraysHelper {

    private ArraysHelper() {}

    public static double calcSum(double[] value) {
        return DoubleStream.of(value).sum();
    }

    public static double calcSum(double[] value, int beginIndex, int endIndex) {
        double sum = 0.0d;
        for (int i = beginIndex; i < endIndex; i++)
            sum += value[i];
        return sum;
    }

    public static double calcAverage(double[] value) {
        return calcSum(value) / value.length;
    }

    public static double calcMedian(double[] value) {
        double median;
        double[] sortedValue = Arrays.copyOf(value, value.length);
        Arrays.sort(sortedValue);
        if (sortedValue.length % 2 == 1)
            median = sortedValue[(sortedValue.length - 1) / 2];
        else
            median = (sortedValue[sortedValue.length / 2 - 1] + sortedValue[sortedValue.length / 2])
                    / 2;
        return median;
    }

    public static double calcMinimum(double[] value) {
        return DoubleStream.of(value).min().getAsDouble();
    }

    public static double calcMaximum(double[] value) {
        return DoubleStream.of(value).max().getAsDouble();
    }

    public static int calcMaximum(int[] value) {
        return IntStream.of(value).max().getAsInt();
    }

    public static int[] generateRandomPermutation(int nElements) {
        return generateRandomPermutation(nElements, new Random());
    }

    public static int[] generateRandomPermutation(int nElements, Random random) {
        int j;
        int k;
        int[] permutation;

        permutation = new int[nElements];
        for (int i = 0; i < nElements; i++)
            permutation[i] = i;
        for (int i = 0; i < nElements; i++) {
            j = random.nextInt(nElements);
            k = permutation[i];
            permutation[i] = permutation[j];
            permutation[j] = k;
        }
        return permutation;
    }
}

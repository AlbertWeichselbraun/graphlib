package net.semanticlab.lib.graph.pagerank;


import static java.lang.Math.abs;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;
import net.semanticlab.lib.graph.network.Network;

/**
 * Computes the PageRank of every node in a {@link Network}.
 * 
 * @author Albert Weichselbraun
 *
 */
@Slf4j
public class PageRank {

    public static final double DAMPING_FACTOR = 0.85;

    private PageRank() {}

    /**
     * Compute the PageRank for the given {@link Network}
     * 
     * @return the array with the corresponding pageRank values
     */
    public static double[] computePageRank(Network n, PageRankInitValue initValue,
            final int maxIterations, final double errorRate) {
        double[] pageRank = initializePageRankVector(n.getNumNodes(), initValue);
        double[] updatedPageRank;
        int[][] edges = n.getOutgoingEdges();
        int iterations = 0;

        // contribution of the fixed pagerank values
        final double randomContribution = (1 - DAMPING_FACTOR) / n.getNumNodes();

        do {
            // create the array for the updated page rank which is
            // initialized with 0.0d per java language specification (§15.9, §15.10)
            updatedPageRank = new double[n.getNumNodes()];

            // compute the updated PageRank values
            int src, dst;
            for (int i = 0; i < edges[0].length; i++) {
                src = edges[0][i];
                dst = edges[1][i];
                updatedPageRank[dst] += pageRank[src] / n.getNumOutgoingEdges(src);
            }

            // apply damping factor
            for (int i = 0; i < n.getNumNodes(); i++) {
                updatedPageRank[i] = DAMPING_FACTOR * updatedPageRank[i] + randomContribution;
            }

            iterations++;

            // exit if the PageRank has converged
            if (isConverged(updatedPageRank, pageRank, errorRate)) {
                pageRank = updatedPageRank;
                break;
            }
            pageRank = updatedPageRank;
        } while (maxIterations == 0 || iterations < maxIterations);
        log.info("PageRank converged after {} iterations.", iterations);
        return pageRank;
    }

    /**
     * Create the initial page rank vector
     * 
     * @param initValueType
     */
    private static double[] initializePageRankVector(int numNodes,
            PageRankInitValue initValueType) {
        double[] pageRank = new double[numNodes];
        Arrays.fill(pageRank, initValueType.getValue(numNodes));
        return pageRank;
    }

    /**
     * @param updatedPageRank
     * @param pageRank
     * @return whether the correction for any pageRank value is larger than the errorRate.
     */
    private static boolean isConverged(double[] updatedPageRank, double[] pageRank,
            double errorRate) {
        for (int i = 0; i < pageRank.length; i++) {
            if (abs(updatedPageRank[i] - pageRank[i]) > errorRate)
                return false;
        }
        return true;
    }

}

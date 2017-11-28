package net.semanticlab.lib.graph.pagerank;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;
import net.semanticlab.lib.graph.network.Network;
import net.semanticlab.lib.graph.pagerank.PageRank;
import net.semanticlab.lib.graph.pagerank.PageRankInitValue;

@Slf4j
public class PageRankTest {

    @Test
    public void testPageRank() {
        int[] src = {0, 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4, 4};
        int[] dst = {1, 2, 3, 4, 1, 3, 4, 1, 2, 4, 1, 2, 3, 5};

        Network n = Network.getNetwork(6, src, dst, null);
        double[] pageRank = PageRank.computePageRank(n, PageRankInitValue.FN, 0, 0.00001);

        log.info("Computed PageRanks:");
        for (int i = 0; i < pageRank.length; i++) {
            log.info("Node {}: {}", i, pageRank[i]);
        }
    }

    /**
     * Compare the computed PageRank with reference values;
     */
    @Test
    public void testPageRankExample() {
        int[] src = {0, 0, 0, 1, 1, 2, 3, 3};
        int[] dst = {1, 2, 3, 2, 3, 0, 0, 2};

        Network n = Network.getNetwork(4, src, dst, null);

        // compare values
        double[] reference = {18., 7., 14., 10.};
        double[] pageRank = PageRank.computePageRank(n, PageRankInitValue.FN, 0, 0.00001);

        for (int i = 0; i < reference.length; i++) {
            assertEquals(getRelativePageRank(reference, reference[i]),
                    getRelativePageRank(pageRank, pageRank[i]), 0.02);
        }

    }

    private static double getRelativePageRank(double[] pageRankVector, double value) {
        return value / Arrays.stream(pageRankVector).sum();
    }

}

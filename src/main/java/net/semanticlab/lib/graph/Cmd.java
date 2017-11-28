package net.semanticlab.lib.graph;

import java.io.File;

import net.semanticlab.lib.graph.network.Network;
import net.semanticlab.lib.graph.network.NetworkReader;
import net.semanticlab.lib.graph.pagerank.PageRank;
import net.semanticlab.lib.graph.pagerank.PageRankInitValue;

public class Cmd {

    public static void main(String[] args) {
        Network n = NetworkReader.readNetwork(new File(args[1]));
        double[] pageRank = PageRank.computePageRank(n, PageRankInitValue.FN, 100, 0.0001);
        for (int i = 0; i < pageRank.length; i++) {
            System.out.println(String.format("Node %d: %.8f", i, pageRank[i]));
        }
    }

}

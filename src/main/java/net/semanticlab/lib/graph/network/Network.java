package net.semanticlab.lib.graph.network;

/**
 * A memory efficient Network implementation
 * @author Albert Weichselbraun
 */

import java.util.Arrays;
import lombok.Getter;

/**
 *  Network
 * 
 *  Example:
 *   0->1, 0->2, 0->3
 *   3->4
 *   4->1, 4->2, 4->3
 * 
 *  The network is defined as follows:
 *  ----------------------------------
 *  sourceNode (based on index): | 0 | 1 | 2 | 3 | 4 | ...   |
 *  firstDestinationNodeIndex:   | 0 | 3 | 3 | 3 | 4 | ...   | 
 *                                 ↓           ↓   ↓
 *  destinationNodeId            | 1 | 2 | 3 | 4 | 1 | 2 | 3 | ...
 *  edgeWeight                   |0.5|0.7|0.1|0.2|0.3|0.4|0.1| ...
 *  
 *  Notes:
 *  ------
 *  (lastDestinationNodeIndex[n] = firstDestinationNodeIndex[n+1])
 */
public class Network {
    /**
     * Total number of nodes and the corresponding weights
     */
    protected @Getter int numNodes;
    protected double[] nodeWeight;


    /**
     * Contains the firstDestinationNodeIndex of the node. The lastDestinationNodeIndex is determined by obtaining
     * the firstDestinationNodeIndex of the subsequent node.
     */
    protected int[] firstDestinationNodeIndex;
    protected int[] destinationNodes;
    protected double[] edgeWeight;

    protected Network() {}

    /**
     * @param nNodes number of nodes in the network
     * @param nodeWeight an array of nodeWeights for every node
     * @param firstDestinationNodeIndex an array of indices indicating a node's first destination node
     * @param destinationNodes an array of destination node ids
     * @param edgeWeight an array of nodeWeights
     */
    private Network(int nNodes, double[] nodeWeight, int[] firstDestinationNodeIndex, int[] destinationNodeId,
            double[] edgeWeight) {
        this.numNodes = nNodes;

        this.firstDestinationNodeIndex = firstDestinationNodeIndex;
        this.destinationNodes = destinationNodeId;
        if (edgeWeight != null)
            this.edgeWeight = edgeWeight;
        else {
            this.edgeWeight = new double[destinationNodeId.length];
            Arrays.fill(this.edgeWeight, 1);
        }
        this.nodeWeight = (nodeWeight != null) ? nodeWeight : getTotalEdgeWeightPerNode();
    }

    /**
     * @param numNodes
     * @param edgeSrcNodes
     * @param edgeDstNodes
     * @param edgeWeight
     * @param modularityFunction
     * @return
     */
    public static Network getNetwork(int numNodes, int[] edgeSrcNodes, int[] edgeDstNodes,
            double[] edgeWeight) {
        int j;
        int[] destinationNodeIndex = new int[numNodes];

        for (int i = 0; i < edgeSrcNodes.length; i++) {
            destinationNodeIndex[edgeSrcNodes[i]]++;
        }

        int[] firstDestinationNodeIndex = new int[numNodes + 1];
        int nEdges = 0;
        for (int i = 0; i < numNodes; i++) {
            firstDestinationNodeIndex[i] = nEdges;
            nEdges += destinationNodeIndex[i];
        }

        int[] destinationNodes = new int[nEdges];
        Arrays.fill(destinationNodeIndex, 0);
        for (int i = 0; i < edgeSrcNodes.length; i++) {
            j = firstDestinationNodeIndex[edgeSrcNodes[i]] + destinationNodeIndex[edgeSrcNodes[i]];
            destinationNodes[j] = edgeDstNodes[i];
            destinationNodeIndex[edgeSrcNodes[i]]++;
        }

        // ensure that all edges (even those without an outgoing edge) have correct indices
        for (int i = 0; i < numNodes; i++) {
            if (firstDestinationNodeIndex[i + 1] < firstDestinationNodeIndex[i]) {
                firstDestinationNodeIndex[i + 1] = firstDestinationNodeIndex[i];
            }
        }

        if (edgeWeight == null) {
            edgeWeight = new double[nEdges];
            Arrays.fill(edgeWeight, 1.0);
        }

        return new Network(numNodes, null, firstDestinationNodeIndex, destinationNodes, edgeWeight);
    }


    public double getTotalNodeWeight() {
        return ArraysHelper.calcSum(nodeWeight);
    }

    public double[] getNodeWeights() {
        return Arrays.copyOf(nodeWeight, nodeWeight.length);
    }

    public double getNodeWeight(int node) {
        return nodeWeight[node];
    }
    
    /**
     * @return
     * 	the number of edges within the network
     */
    public int getNumEdges() {
    	return destinationNodes.length;
    }

    /**
     * @param node
     * @return the number of outgoing edges for the given node
     */
    public int getNumOutgoingEdges(int node) {
        return firstDestinationNodeIndex[node + 1] - firstDestinationNodeIndex[node];
    }

    /**
     * @return the number of edges for all nodes
     */
    public int[] getNumOutgoingEdgesPerNode() {
        int[] nEdgesPerNode = new int[numNodes];
        for (int i = 0; i < numNodes; i++)
            nEdgesPerNode[i] = firstDestinationNodeIndex[i + 1] - firstDestinationNodeIndex[i];
        return nEdgesPerNode;
    }

    public int[][] getOutgoingEdges() {
        int[][] edge = new int[2][];
        edge[0] = new int[getNumEdges()];
        for (int i = 0; i < numNodes; i++)
            Arrays.fill(edge[0], firstDestinationNodeIndex[i], firstDestinationNodeIndex[i + 1], i);
        edge[1] = Arrays.copyOf(destinationNodes, destinationNodes.length);
        return edge;
    }

    public int[] getOutgoingEdges(int node) {
        return Arrays.copyOfRange(destinationNodes, firstDestinationNodeIndex[node], firstDestinationNodeIndex[node + 1]);
    }

    public int[][] getEdgesPerNode() {
        int[][] edgePerNode = new int[numNodes][];
        for (int i = 0; i < numNodes; i++)
            edgePerNode[i] =
                    Arrays.copyOfRange(destinationNodes, firstDestinationNodeIndex[i], firstDestinationNodeIndex[i + 1]);
        return edgePerNode;
    }

    public double getTotalEdgeWeight() {
        return ArraysHelper.calcSum(edgeWeight);
    }

    public double getTotalEdgeWeight(int node) {
        return ArraysHelper.calcSum(edgeWeight, firstDestinationNodeIndex[node],
                firstDestinationNodeIndex[node + 1]);
    }

    public double[] getTotalEdgeWeightPerNode() {
        double[] totalEdgeWeightPerNode = new double[numNodes];
        for (int i = 0; i < numNodes; i++)
            totalEdgeWeightPerNode[i] = ArraysHelper.calcSum(edgeWeight, firstDestinationNodeIndex[i],
                    firstDestinationNodeIndex[i + 1]);
        return totalEdgeWeightPerNode;
    }

    public double[] getEdgeWeights() {
        return Arrays.copyOf(edgeWeight, edgeWeight.length);
    }

    public double[] getEdgeWeights(int node) {
        return Arrays.copyOfRange(edgeWeight, firstDestinationNodeIndex[node],
                firstDestinationNodeIndex[node + 1]);
    }

    public double[][] getEdgeWeightsPerNode() {
        double[][] edgeWeightPerNode = new double[numNodes][];
        for (int i = 0; i < numNodes; i++)
            edgeWeightPerNode[i] = Arrays.copyOfRange(edgeWeight, firstDestinationNodeIndex[i],
                    firstDestinationNodeIndex[i + 1]);
        return edgeWeightPerNode;
    }

}

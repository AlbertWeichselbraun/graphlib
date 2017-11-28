package net.semanticlab.lib.graph.network;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import net.semanticlab.lib.graph.network.Network;

public class NetworkTest {

    @Test
    public void networkTest() {
        int[] src = {0, 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4, 4};
        int[] dst = {1, 2, 3, 4, 1, 3, 4, 1, 2, 4, 1, 2, 3, 5};

        Network n = Network.getNetwork(6, src, dst, null);
        assertEquals(6, n.getNumNodes());

        // computation of edges
        assertEquals(1, n.getNumOutgoingEdges(0));
        assertEquals(3, n.getNumOutgoingEdges(1));
        assertEquals(3, n.getNumOutgoingEdges(2));
        assertEquals(3, n.getNumOutgoingEdges(3));
        assertEquals(4, n.getNumOutgoingEdges(4));
        assertEquals(0, n.getNumOutgoingEdges(5));

        // total number of edges
        assertEquals(src.length, n.getNumEdges());

        // per node edges
        assertArrayEquals(new int[] {1}, n.getOutgoingEdges(0));
        assertArrayEquals(new int[] {2, 3, 4}, n.getOutgoingEdges(1));
        assertArrayEquals(new int[] {1, 3, 4}, n.getOutgoingEdges(2));
        assertArrayEquals(new int[] {1, 2, 4}, n.getOutgoingEdges(3));
        assertArrayEquals(new int[] {1, 2, 3, 5}, n.getOutgoingEdges(4));
        assertArrayEquals(new int[] {}, n.getOutgoingEdges(5));
    }

}

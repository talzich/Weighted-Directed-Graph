package api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_AlgoTest {

    DWGraph_DS graph = new DWGraph_DS();
    dw_graph_algorithms gAlgo = new DWGraph_Algo();


    @BeforeEach
    void setTest() {

        for (int i = 0; i < 5; i++) {
            graph.addNode(i);
        }
        graph.connect(0, 1, 3);
        graph.connect(0, 3, 8);
        graph.connect(0, 4, 5);
        graph.connect(1, 3, 4);
        graph.connect(1, 2, 1);
        graph.connect(3, 2, 2);
        graph.connect(4, 3, 1);

        gAlgo.init(graph);
    }

    @Test
    void copy() {

        DWGraph_DS copy = (DWGraph_DS) gAlgo.copy();
        assertEquals(graph, copy);

        graph.addNode(5);
        graph.addNode(6);
        graph.connect(5,6,1);
        assertNotEquals(graph,copy);

        copy.addNode(5);
        copy.addNode(6);
        assertNotEquals(graph,copy);

        copy.connect(5,6,2);
        assertNotEquals(graph,copy);

        copy.connect(5,6,1);
        assertEquals(graph,copy);

        graph.removeEdge(5,6);
        assertNotEquals(graph,copy);

        copy.removeEdge(5,6);
        assertEquals(graph,copy);

    }

    @Test
    void isConnected() {

        assertFalse(gAlgo.isConnected());

        directed_weighted_graph connectedGraph = new DWGraph_DS();

        node_data n0 = new NodeData();
        node_data n1 = new NodeData();
        node_data n2 = new NodeData();
        node_data n3 = new NodeData();
        node_data n4 = new NodeData();
        node_data n5 = new NodeData();

        connectedGraph.addNode(n0);
        connectedGraph.addNode(n1);
        connectedGraph.addNode(n2);
        connectedGraph.addNode(n3);
        connectedGraph.addNode(n4);

        gAlgo.init(connectedGraph);
        assertFalse(gAlgo.isConnected());

        connectedGraph.connect(0,1,1);
        connectedGraph.connect(1,2,1);
        connectedGraph.connect(2,3,1);
        connectedGraph.connect(2,4,1);
        connectedGraph.connect(4,2,1);
        connectedGraph.connect(3,0,1);
        assertTrue(gAlgo.isConnected());

        connectedGraph.addNode(n5);
        assertFalse(gAlgo.isConnected());

        connectedGraph.connect(4,5,1);
        assertFalse(gAlgo.isConnected());

        connectedGraph.connect(5,4,1);
        assertTrue(gAlgo.isConnected());

        connectedGraph.removeNode(5);
        assertTrue(gAlgo.isConnected());

        connectedGraph.removeNode(0);
        assertFalse(gAlgo.isConnected());

        connectedGraph.connect(3,1,1);
        assertTrue(gAlgo.isConnected());

    }

    @Test
    void shortestPathDist() {

        assertEquals(0, gAlgo.shortestPathDist(0, 0));
        assertEquals(3, gAlgo.shortestPathDist(0, 1));
        assertEquals(4, gAlgo.shortestPathDist(0, 2));
        assertEquals(6, gAlgo.shortestPathDist(0, 3));
        assertEquals(5, gAlgo.shortestPathDist(0, 4));

        assertEquals(-1, gAlgo.shortestPathDist(1, 0));
        assertEquals(0, gAlgo.shortestPathDist(1, 1));
        assertEquals(1, gAlgo.shortestPathDist(1, 2));
        assertEquals(4, gAlgo.shortestPathDist(1, 3));
        assertEquals(-1, gAlgo.shortestPathDist(1, 4));

        assertEquals(-1, gAlgo.shortestPathDist(2, 0));
        assertEquals(-1, gAlgo.shortestPathDist(2, 1));
        assertEquals(0, gAlgo.shortestPathDist(2, 2));
        assertEquals(-1, gAlgo.shortestPathDist(2, 3));
        assertEquals(-1, gAlgo.shortestPathDist(2, 4));

        assertEquals(-1, gAlgo.shortestPathDist(3, 0));
        assertEquals(-1, gAlgo.shortestPathDist(3, 1));
        assertEquals(2, gAlgo.shortestPathDist(3, 2));
        assertEquals(0, gAlgo.shortestPathDist(3, 3));
        assertEquals(-1, gAlgo.shortestPathDist(3, 4));

        assertEquals(-1, gAlgo.shortestPathDist(4, 0));
        assertEquals(-1, gAlgo.shortestPathDist(4, 1));
        assertEquals(3, gAlgo.shortestPathDist(4, 2));
        assertEquals(1, gAlgo.shortestPathDist(4, 3));
        assertEquals(0, gAlgo.shortestPathDist(4, 4));

        graph.removeNode(4);
        assertEquals(gAlgo.shortestPathDist(0, 4), -1);
        assertEquals(gAlgo.shortestPathDist(1, 4), -1);
        assertEquals(gAlgo.shortestPathDist(2, 4), -1);
        assertEquals(gAlgo.shortestPathDist(3, 4), -1);
        assertEquals(gAlgo.shortestPathDist(4, 4), -1);

        assertEquals(-1, gAlgo.shortestPathDist(4, 0));
        assertEquals(-1, gAlgo.shortestPathDist(4, 1));
        assertEquals(-1, gAlgo.shortestPathDist(4, 2));
        assertEquals(-1, gAlgo.shortestPathDist(4, 3));

        DWGraph_DS g1 = new DWGraph_DS();

        for (int i = 0; i < 9; i++) {
            g1.addNode(i);
        }

        g1.connect(0,1,4);
        g1.connect(1,2,8);
        g1.connect(1,7,11);
        g1.connect(2,5,4);
        g1.connect(3,2,7);
        g1.connect(3,4,9);
        g1.connect(4,3,3);
        g1.connect(4,5,10);
        g1.connect(5,3,14);
        g1.connect(6,5,2);
        g1.connect(7,0,8);
        g1.connect(7,6,1);
        g1.connect(7,8,7);
        g1.connect(8,7,1);
        g1.connect(8,6,6);
        g1.connect(8,2,2);

        gAlgo.init(g1);
        assertEquals(0, gAlgo.shortestPathDist(0, 0));
        assertEquals(4, gAlgo.shortestPathDist(0, 1));
        assertEquals(12, gAlgo.shortestPathDist(0, 2));
        assertEquals(30, gAlgo.shortestPathDist(0, 3));
        assertEquals(39, gAlgo.shortestPathDist(0, 4));
        assertEquals(16, gAlgo.shortestPathDist(0, 5));
        assertEquals(16, gAlgo.shortestPathDist(0, 6));
        assertEquals(15, gAlgo.shortestPathDist(0, 7));
        assertEquals(22, gAlgo.shortestPathDist(0, 8));





    }

    @Test
    void shortestPath() {
    }

    @Test
    void saveLoad() {
    }
}
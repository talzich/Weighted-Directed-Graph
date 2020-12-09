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
        graph.connect(0, 3, 7);
        graph.connect(0, 4, 8);
        graph.connect(1, 3, 4);
        graph.connect(1, 2, 1);
        graph.connect(3, 2, 2);
        graph.connect(4, 3, 3);

        gAlgo.init(graph);
    }

    @Test
    void copy() {

        directed_weighted_graph copy = gAlgo.copy();
        assertEquals(graph, copy);

        graph.addNode(5);
        graph.addNode(6);
        graph.connect(5,6,1);
        assertNotEquals(graph,copy);

        
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

    }

    @Test
    void shortestPath() {
    }

    @Test
    void saveLoad() {
    }
}
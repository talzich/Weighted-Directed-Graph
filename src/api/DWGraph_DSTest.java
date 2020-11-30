package api;

import org.junit.jupiter.api.*;

class DWGraph_DSTest {

    directed_weighted_graph g = new DWGraph_DS();

    @Test
    void getEdge() {
        node_data n0 = new NodeData();
        node_data n1 = new NodeData();
        node_data n2 = new NodeData();
        node_data n3 = new NodeData();
        g.addNode(n0);
        g.addNode(n1);
        g.addNode(n2);
        g.addNode(n3);
        g.connect(0,1,3);
        g.connect(0,2,5);
        g.connect(2,1,7);
        edge_data edge = g.getEdge(1,2);
    }
}
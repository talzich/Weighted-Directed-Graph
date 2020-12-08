package api;

import org.junit.jupiter.api.*;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {

    DWGraph_DS g = new DWGraph_DS();
    DWGraph_DS bigG = new DWGraph_DS();

    @BeforeEach
    void buildGraph(){
        for (int i = 0; i < 5; i++) {
            g.addNode(i);
        }
        g.connect(0,1,3);
        g.connect(0,3,7);
        g.connect(0,4,8);
        g.connect(1,3,4);
        g.connect(1,2,1);
        g.connect(3,2,2);
        g.connect(4,3,3);
    }

    void buildBigGraph(){
        int i,j;
        double w;

        //Add 1000 nodes to the graph with keys 0-999
        for (i = 0; i < 10000; i++)
        {
            bigG.addNode(i);
        }

        i = 0;
        j = 1;
        w = 0.5;

        while(j <= 999)
        {
            bigG.connect(i,j,w);
            i++; j++; w += 0.5;
        }
    }

    @Test
    void getNode(){
        node_data n0 = g.getNode(0);
        node_data n1 = g.getNode(1);
        node_data n2 = g.getNode(2);
        node_data n3 = g.getNode(3);
        node_data n4 = g.getNode(4);
        node_data n5 = g.getNode(5);
        node_data n6 = g.getNode(0);
        node_data n7 = new NodeData(0);

        assertSame(n0,n6);
        assertNotSame(n0,n7);
        assertNotSame(n0,n1);
        assertNotSame(n0,n2);
        assertNotSame(n0,n3);
        assertNotSame(n0,n4);
        assertNull(n5);

    }

    @Test
    void connect(){
        assertTrue(g.hasEdge(0,1));
        assertTrue(g.hasEdge(0,3));
        assertTrue(g.hasEdge(0,4));
        assertFalse(g.hasEdge(1,0));
        assertFalse(g.hasEdge(3,0));
        assertFalse(g.hasEdge(4,0));
        assertFalse(g.hasEdge(0,0));
        assertFalse(g.hasEdge(0,5));
        assertFalse(g.hasEdge(5,6));
        assertFalse(g.hasEdge(0,2));
    }

    @Test
    void getE(){

        Collection edges = g.getE(0);
        edge_data edge01 = g.getEdge(0,1);
        edge_data edge03 = g.getEdge(0,3);
        edge_data edge04 = g.getEdge(0,4);
        edge_data edge43 = g.getEdge(4,3);

        assertTrue(edges.contains(edge01));
        assertTrue(edges.contains(edge03));
        assertTrue(edges.contains(edge04));
        assertFalse(edges.contains(edge43));


    }

    @Test
    void removeNode(){
        g.connect(3,1,1);

        node_data n3 = g.getNode(3);//The node we will remove
        int mc = g.getMC();//Current mode counter
        int edgeSize = g.edgeSize();//Current edge size

        assertTrue(g.hasEdge(3,2));
        assertTrue(g.hasEdge(3,1));
        assertTrue(g.hasEdge(1,3));
        assertTrue(g.hasEdge(0,3));
        assertTrue(g.hasEdge(4,3));

        assertSame(n3, g.removeNode(3));
        assertNull(g.getNode(3));

        assertFalse(g.hasEdge(3,2));
        assertFalse(g.hasEdge(3,1));
        assertFalse(g.hasEdge(1,3));
        assertFalse(g.hasEdge(0,3));
        assertFalse(g.hasEdge(4,3));

        assertEquals(edgeSize-5,g.edgeSize());
        assertEquals(mc+6, g.getMC());
    }

    @Test
    void removeEdge(){

        int edgeSize = g.edgeSize();//Current number of edges
        int mc = g.getMC();// Current mode counter

        edge_data edge01 = g.getEdge(0,1);//The edge we will remove

        assertTrue(g.hasEdge(0,1));
        assertSame(edge01, g.removeEdge(0,1));
        assertFalse(g.hasEdge(0,1));

        assertEquals(edgeSize-1, g.edgeSize());
        assertEquals(mc+1, g.getMC());

        assertNull(g.removeEdge(1,0));








    }




}
package api;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {

    DWGraph_DS g = new DWGraph_DS();

    @BeforeEach
    void clearAndBuild(){
        for (int i = 0; i < 5; i++) {
            g.addNode(i);
        }
    }

    @Test
    void getEdge() {

    }

    @Test
    void connect(){
        g.connect(0,0,1);
        g.connect(10,11,12);
        g.connect(0,20,2);
        g.connect(2,3,-3);
        g.connect(100,50,-4);
        assertFalse(g.hasEdge(0,0));
        assertFalse(g.hasEdge(10,11));
        assertFalse(g.hasEdge(0,20));
        assertFalse(g.hasEdge(2,3));
        assertFalse(g.hasEdge(100,50));
        g.connect(0,1,3);
        g.connect(0,2,5);
        g.connect(2,1,7);
        assertTrue(g.hasEdge(0,1));
        assertTrue(g.hasEdge(0,2));
        assertTrue(g.hasEdge(2,1));
    }

    @Test
    void removeNode(){

        g.connect(0,1,3);
        g.connect(0,3,7);
        g.connect(0,4,8);
        g.connect(1,2,1);
        g.connect(1,3,4);
        g.connect(3,2,2);
        g.connect(4,3,3);

        assertTrue(g.hasEdge(0,1));
        assertTrue(g.hasEdge(0,3));
        assertTrue(g.hasEdge(0,4));

        g.removeNode(0);

        assertFalse(g.hasEdge(0,1));
        assertFalse(g.hasEdge(0,3));
        assertFalse(g.hasEdge(0,4));

        node_data n = g.removeNode(6);
        assertNull(n);

        node_data n1 = g.getNode(1);
        assertEquals(n1, g.removeNode(1));
    }

    @Test
    void getE(){
        g.connect(0,1,3);
        g.connect(0,2,5);
        g.connect(2,1,7);

    }

}
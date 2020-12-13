package api;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlgoTest {

    @Test
    public void TestA()
    {
        DWGraph_DS G=new DWGraph_DS();
        DWGraph_Algo A=new DWGraph_Algo();
        A.init(G);
        boolean flag=A.isConnected();
        assertTrue(flag);
    }
    @Test
    public void TestB()
    {
        DWGraph_DS G=new DWGraph_DS();
        G.addNode(0);
        DWGraph_Algo A=new DWGraph_Algo();
        A.init(G);
        boolean flag=A.isConnected();
        assertTrue(flag);
    }
    @Test
    public void TestC1()
    {
        DWGraph_DS G=new DWGraph_DS();
        DWGraph_Algo A=new DWGraph_Algo();
        A.init(G);
        G.addNode(0);
        G.addNode(1);
        boolean flag=A.isConnected();
        assertFalse(flag);
    }
    @Test
    public void TestC2()
    {
        DWGraph_DS G=new DWGraph_DS();
        DWGraph_Algo A=new DWGraph_Algo();
        A.init(G);
        G.addNode(0);
        G.addNode(1);
        G.connect(0,1,3);
        G.connect(1,0,2);
        boolean flag=A.isConnected();
        assertTrue(flag);
    }
    public DWGraph_DS GraphBulidTestD()
    {
        DWGraph_DS G=new DWGraph_DS();
        for(int i=0;i<5;i++)G.addNode(i);
        G.connect(0,1,5);
        G.connect(1,0,8);
        G.connect(0,2,2);
        G.connect(2,0,1);
        G.connect(2,3,1);
        G.connect(3,2,0.5);
        G.connect(3,4,12);
        G.connect(4,3,1);
        G.connect(0,4,20);
        G.connect(4,0,20);
        G.connect(4,1,5);
        G.connect(1,4,5);
        return G;
    }
    public void PrintList(List<node_data> L)
    {
        Iterator<node_data> ite=L.iterator();
        while(ite.hasNext())
        {
            node_data N=ite.next();
            System.out.print(N.toString());
            if(ite.hasNext())System.out.print("-->");
        }
        System.out.println();
    }
    @Test
    public void TestD1()
    {
        DWGraph_DS G=GraphBulidTestD();
        DWGraph_Algo A=new DWGraph_Algo();
        A.init(G);
        double L=A.shortestPathDist(0,4);
        boolean flag=(L==10);
        assertTrue(flag);
    }
    @Test
    public void TestD2()
    {
        DWGraph_DS G=GraphBulidTestD();
        DWGraph_Algo A=new DWGraph_Algo();
        A.init(G);
//		List<node_data> L=A.shortestPath(0,4);
        //System.out.println("D2 : "+L.size());
        //PrintList(L);
        assertEquals(A.shortestPath(0,4).size(),3);
    }
    @Test
    public void TestD3()
    {
        DWGraph_DS G=GraphBulidTestD();
        DWGraph_Algo A=new DWGraph_Algo();
        A.init(G);
        double L=A.shortestPathDist(4,0);
        //System.out.println("D3 :"+L);
        boolean flag=(L==2.5);
        assertTrue(flag);

    }
    @Test
    public void TestD4()
    {
        DWGraph_DS G=GraphBulidTestD();
        DWGraph_Algo A=new DWGraph_Algo();
        A.init(G);
        List<node_data> L=A.shortestPath(4,0);
        //System.out.println("D4 : "+L.size());
        //PrintList(L);
        assertEquals(L.size(),4);

    }
    @Test
    public void TestD5()
    {
        DWGraph_DS G=GraphBulidTestD();
        DWGraph_Algo A=new DWGraph_Algo();
        A.init(G);
        G.removeNode(2);
        List<node_data> L=A.shortestPath(4,0);
        //System.out.println("D5 : "+L.size());
        //PrintList(L);
        assertEquals(L.size(),3);

    }
    @Test
    public void TestD6()
    {
        DWGraph_DS G=GraphBulidTestD();
        DWGraph_Algo A=new DWGraph_Algo();
        A.init(G);
        G.removeNode(2);
        double L=A.shortestPathDist(4,0);
        boolean flag=(L==13);
        //System.out.println("D6 : "+ L );
        assertTrue(flag);
    }
    //This graph was not connected!!!
    public DWGraph_DS ConnectedGraph(int Num)
    {
        DWGraph_DS G=new DWGraph_DS();
        for(int i=0;i<Num;i++)G.addNode(i);
        for(int i=2;i<Num;i++)
        {
            double W=Math.random()*10;
            G.connect(0,i,W);
            G.connect(i,0,W);
            G.connect(1,i,W);
        }
        G.connect(1,0,1);
        G.connect(0,1,1);
        return G;
    }
    //This graph was not connected
    public DWGraph_DS ConnectedGraph2(int Num)
    {
        DWGraph_DS G=new DWGraph_DS();
        for(int i=0;i<Num;i++)G.addNode(i);
        for(int i=0;i<Num;i++)
        {
            double W=Math.random()*10;
            if(i+1<Num)G.connect(i,i+1, W);
        }
        G.connect(Num-1,0,1);
        return G;
    }
    @Test
    public void TestE1()
    {
        DWGraph_DS G=ConnectedGraph(100);
        DWGraph_Algo A=new DWGraph_Algo();
        A.init(G);
        boolean flag=A.isConnected();
        assertTrue(flag);
    }
    @Test
    public void TestE2()
    {
        DWGraph_DS G=ConnectedGraph(10000);
        DWGraph_Algo A=new DWGraph_Algo();
        A.init(G);
        boolean flag=A.isConnected();
        assertTrue(flag);
    }
    @Test
    public void TestE3()
    {
        DWGraph_DS G=ConnectedGraph(1000000);
        DWGraph_Algo A=new DWGraph_Algo();
        A.init(G);
        boolean flag=A.isConnected();
        assertTrue(flag);
    }
    @Test
    public void TestE4()
    {
        DWGraph_DS G=ConnectedGraph2(4000);
        DWGraph_Algo A=new DWGraph_Algo();
        A.init(G);
        boolean flag=A.isConnected();
        assertTrue(flag);
    }
    @Test
    public void TestF1()
    {
        DWGraph_DS G=GraphBulidTestD();
        DWGraph_Algo A=new DWGraph_Algo();
        A.init(G);
        boolean flag=A.save("TestF1.json");
        assertTrue(flag);
    }
    @Test
    public void TestF2()
    {
        DWGraph_Algo A = new DWGraph_Algo();
        boolean flag=A.load("TestF1.json");
        assertTrue(flag);
    }
    @Test
    public void TestF3()
    {
        DWGraph_DS G=GraphBulidTestD();
        DWGraph_Algo A=new DWGraph_Algo();
        A.init(G);
        A.save("TestF1");
        DWGraph_Algo A1=new DWGraph_Algo();
        A1.load("TestF1");
        assertEquals(A.getGraph(),A1.getGraph());

    }
    @Test
    public void TestG1()
    {
        DWGraph_DS G=GraphBulidTestD();
        DWGraph_Algo A=new DWGraph_Algo();
        A.init(G);
        DWGraph_DS G1=(DWGraph_DS)A.copy();
        assertEquals(5,G1.nodeSize());
    }
    @Test
    public void TestG2()
    {
        DWGraph_DS G=GraphBulidTestD();
        DWGraph_Algo A=new DWGraph_Algo();
        A.init(G);
        DWGraph_DS G1=(DWGraph_DS)A.copy();
        assertEquals(12,G1.edgeSize());
    }
    @Test
    public void TestG3()
    {
        DWGraph_DS G=GraphBulidTestD();
        DWGraph_Algo A=new DWGraph_Algo();
        A.init(G);
        DWGraph_DS G1=(DWGraph_DS)A.copy();
        G1.removeNode(1);
        assertEquals(8,G1.edgeSize());
    }
    @Test
    public void TestG4()
    {
        DWGraph_DS G=GraphBulidTestD();
        DWGraph_Algo A=new DWGraph_Algo();
        A.init(G);
        DWGraph_DS G1=(DWGraph_DS)A.copy();
        G.removeNode(1);
        assertEquals(5,G1.nodeSize());
    }
    @Test
    public void TestG5()
    {
        DWGraph_DS G=GraphBulidTestD();
        DWGraph_Algo A=new DWGraph_Algo();
        A.init(G);
        DWGraph_DS G1=(DWGraph_DS)A.copy();
        G.removeNode(1);
        //System.out.println(G.toString());
        G1.removeNode(1);
        assertEquals(A.getGraph(),G1);
    }



}
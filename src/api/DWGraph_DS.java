package api;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class DWGraph_DS implements directed_weighted_graph{

    /*
     * This Hashmap gets int as key and returns the object node with the specified key.
     */
    private HashMap<Integer, node_data> nodes;

    /*
     * This Hashmap gets an Integer as a key(key of src node) and returns another Hashmap.
     * The nested Hashmap gets an Integer as a key(key of dest node) and returns edge_data as a value.
     */
    private HashMap<Integer, HashMap<Integer, edge_data>> edges;

    private int modeCounter = 0;
    private int nodeSize = 0;
    private int edgeSize = 0;

    public DWGraph_DS(){
        this.nodes = new HashMap<>();
        this.edges = new HashMap<>();
    }


    /**
     * Get the node with the specified key
     * @param key - the key of the requested node
     * @return - node_data - an object of type node with the specified key, if present in this graph.
     */
    @Override
    public node_data getNode(int key) {
        if(nodes.containsKey(key))
        {
            node_data node = this.nodes.get(key);// The requested node we will be returning
            return node;
        }
        return null;
    }

    /**
     * Return a requested edge, if present in this graph.
     * @param src - the key of the node from which this edge is going out, if present.
     * @param dest - the key of the node this edge is pointing at, if present.
     * @return edge_data - the edge coming out of node src and pointing at node dest
     */
    @Override
    public edge_data getEdge(int src, int dest) {
            if(nodes.containsKey(src) && nodes.containsKey(dest))//Both source and destination nodes are in the graph
            {
                if(nodeHasEdges(src))// If there are edges coming out of src
                {
                    HashMap nei = edges.get(src);//Address the neighbors of src

                    if(nei.containsKey(dest))//If dest is one of src's neighbors
                    {
                        return (edge_data) nei.get(dest);
                    }
                }
            }
            return null;
    }

    /**
     * Adds a node to this graph, not connecting it to any other node.
     * @param n - object of type node_data, the node to be added to this graph
     */
    @Override
    public void addNode(node_data n) {
        if(n != null && !nodes.containsKey(n.getKey())) //If node is already in the graph or node is null, return
        {
            int key = n.getKey();
            nodes.put(key,n);
            modeCounter++;
            nodeSize++;
        }
    }

    /**
     * Connecting src to edge by adding an edge pointing from src to dest
     * @param src - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {

        // Enter only if both nodes are in the graph, are different and the provided weight is non-negative
        if(nodes.containsKey(src) && nodes.containsKey(dest) && w >= 0 && dest != src)
        {
            // The hashmap representing the edges coming out of src
            HashMap<Integer, edge_data> nei = edges.get(src);

            // If dest is src's first neighbor
            if(nei == null)
            {
                nei = new HashMap();
                edges.put(src, nei);
            }

            //If the nodes were already connected, don't count the edge as a new one.
            if(hasEdge(src, dest))
            {
                edge_data origEdge = getEdge(src, dest);
                double origWeight =origEdge.getWeight();

                if(w != origWeight)
                {

                    // The edge that will connect src to dest
                    edge_data newEdge = new EdgeData(src,dest,w);

                    //The actual connection
                    nei.put(dest, newEdge);

                    modeCounter++;
                }
                else return;
            }
            else
            {
                // The edge that will connect src to dest
                edge_data newEdge = new EdgeData(src,dest,w);

                //The actual connection
                nei.put(dest, newEdge);
                modeCounter++;
                edgeSize++;

            }



        }
    }

    /**
     * @return Collection - A shallow copy of a collection of the nodes in this graph.
     */
    @Override
    public Collection<node_data> getV() {
        return nodes.values();
    }

    /**
     * This function will return a shallow copy of a collection representing all the edges going out of
     * the node with the specified key.
     * @param node_id - the node who's edges we will return
     * @return collection - a collection of the edges going out node with specified key
     */
    @Override
    public Collection<edge_data> getE(int node_id) {

        //Return null if node is not in the graph or if node has no edges
        if(!graphContainsNode(node_id) || !nodeHasEdges(node_id)) return null;

        //A Hashmap representation of the edges going out of node with specified id
        HashMap<Integer, edge_data> edgeMap = edges.get(node_id);

        return edgeMap.values();

    }

    /**
     * Removes node with specified key from the graph, alongside all edges going out of it and all edges
     * pointing at it.
     * @param key - The key of the node to remove.
     * @return node_data - The node to remove.
     */
    @Override
    public node_data removeNode(int key) {
        if(graphContainsNode(key))
        {
            //Remove all edges going out of node with specified key.
            if(nodeHasEdges(key))
            {

                int srcEdgeSize = getE(key).size();//Number of edges going out of the node we want to remove
                edgeSize -= srcEdgeSize;
                modeCounter += srcEdgeSize;
                edges.remove(key);
            }

            //Remove all edges pointing at node with specified key.
            for(HashMap<Integer, edge_data> map : edges.values())
            {
                if (map != null && map.containsKey(key))
                {
                    map.remove(key);
                    edgeSize--;
                    modeCounter++;
                }
            }

            nodeSize--;
            modeCounter++;
            return nodes.remove(key);
        }
        return null;
    }

    /**
     * Removes edge that is going out of node with key "src" and pointing at node with key "dest"
     * @param src - int, The key of the node from which the edge to remove is going out.
     * @param dest - int, The key of the node to which the edge to remove is pointing.
     * @return edgeToRemove - edge_data, The edge to remove.
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        if(hasEdge(src, dest))
        {
            //A Hashmap representation of src's neighbors
            HashMap<Integer, edge_data> srcNeis = edges.get(src);
            edgeSize--;
            modeCounter++;
            edge_data edgeToRemove = srcNeis.remove(dest);
            return edgeToRemove;
        }
        return null;
    }

    @Override
    public int nodeSize() {
        return nodeSize;
    }

    @Override
    public int edgeSize() {
        return edgeSize;
    }

    @Override
    public int getMC() {
        return modeCounter;
    }



    //********* Util Methods *********//

    /**
     * This method was written to help with tests.
     * Adds node to the graph with specific key.
     * @param key
     */
    void addNode(int key){
        if(!nodes.containsKey(key))
        {
            node_data node = new NodeData(key);
            nodes.put(key,node);
            edges.put(key,null);
            modeCounter++;
            nodeSize++;
        }
    }

    /**
     * This method was written to help with tests.
     * @param src
     * @param dest
     * @return boolean - True iff there is an edge going out of src to dest
     */
    boolean hasEdge(int src, int dest){
        //If one of the nodes is not in the graph or the nodes are the same nodes
        if(!graphContainsNode(src) || !graphContainsNode(dest) || src == dest) return false;

        HashMap nei = edges.get(src);//Hashmap of edges coming out of src

        if(nei == null) return false;
        return nei.containsKey(dest);

    }

    //********* Util Methods *********//


    //********* Private Methods *********//

    private boolean nodeHasEdges(int key){
        return (edges.get(key) != null);
    }

    private boolean graphContainsNode(int key){
        return nodes.containsKey(key);
    }

    //********* Private Methods *********//


}

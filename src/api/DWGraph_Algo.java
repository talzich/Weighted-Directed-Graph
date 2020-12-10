package api;

import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms{

    private directed_weighted_graph graph;
    private HashMap<node_data, node_data> parents = new HashMap<>();
    private String visited = "black", unvisited = "white";

    /**
     * shallow copies specified graph g to this graph.
     * @param g
     */
    @Override
    public void init(directed_weighted_graph g) {
        if (g != null) this.graph = g;
    }


    @Override
    public directed_weighted_graph getGraph() {
        return this.graph;
    }

    /**
     * This method produces and returns a deep copy of the underlying graph of this class
     * @return directed_weighted_graph - a deep copy of the underlying graph of this class
     */
    @Override
    public directed_weighted_graph copy() {
        if(graph == null) return null;

        //The graph we will be returning
        directed_weighted_graph copy = new DWGraph_DS();

        //If original graph is empty, a new empty graph will bu sufficient to return.
        if(graph.edgeSize() == 0 && graph.nodeSize() == 0) return copy;

        //We know those are not null because we passed the former if statement
        Collection<node_data> thisNodes = graph.getV();
        Collection<edge_data> currNeighbors;

        //run on all the nodes of this graph
        for(node_data currNode: thisNodes)
        {
            int currKey = currNode.getKey();

            //If copy does not contain a node with currKey
            if(copy.getNode(currKey) == null)
            {
                //copy the current node and add him to the copy graph
                node_data copiedNode = new NodeData(currNode);
                copy.addNode(copiedNode);
            }
            //run on all the edges going out from the current node if exist
            currNeighbors = graph.getE(currKey);
            if(currNeighbors != null) {

                for(edge_data currEdge : currNeighbors)
                {
                    int destKey = currEdge.getDest();
                    double currEdgeWeight = currEdge.getWeight();
                    if(copy.getEdge(currKey,destKey) == null)
                    {
                        if (copy.getNode(destKey) == null)
                        {
                            node_data origDest = graph.getNode(destKey);
                            node_data copiedDest = new NodeData(origDest);
                            copy.addNode(copiedDest);

                        }
                        copy.connect(currKey,destKey,currEdgeWeight);

                    }
                }
            }
        }


        return copy;
    }

    /**
     * A method to check whether a graph is strongly connected.
     * This method uses a dfs algorithm.
     * @return True iff  this graph is strongly connected, meaning, every node in this graph can be reached
     * from every other node in this graph.
     */
    @Override
    public boolean isConnected() {

        //If the graph is empty or has only one vertex it is vacuously connected
        if(graph.nodeSize() == 1 || graph.nodeSize() == 0) return true;

        //This map will keep track of all the nodes that can be reached from a specific node
        HashMap<Integer, Boolean> visited = new HashMap<>();

        //Setting the map for the first time
        for(node_data node : graph.getV())
        {
            visited.put(node.getKey(),false);
        }

        //Iterate through all nodes in this graph
        for(Integer src : visited.keySet())
        {
            dfs(src,visited);
            if(visited.containsValue(false))
                return false;
            setFalse(visited);
        }
        return true;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        double pathLength;

        //Return null if the graph was not initialized
        if (graph == null) return -1;

        //Pointers
        node_data source = graph.getNode(src);
        node_data destination = graph.getNode(dest);

        //If one of the nodes does not exist in the graph there is no path between them
        if (source == null || destination == null) return -1;

        //Distance from a node to itself is 0
        if (source == destination) return 0;

        dijkstra(source);

        pathLength = destination.getWeight();

        if(pathLength == Integer.MAX_VALUE*2) pathLength = -1;

        return pathLength;

    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        return null;
    }

    @Override
    public boolean save(String file) {
        return false;
    }

    @Override
    public boolean load(String file) {
        return false;
    }

    //********* Private Methods *********//

    /**
     * Depth First Search algorithm to mark True for every node that can be reached from src
     *
     * @param src     - Node to check paths from
     * @param visited - A int->boolean map to mark which nodes can be reached from src
     */
    private void dfs(int src, HashMap<Integer, Boolean> visited) {
        if (visited == null) return;
        //This stack will determine which node's neighbors will be explored next.
        Stack<Integer> stack = new Stack();
        visited.put(src, true);
        stack.push(src);

        //While there are more nodes to explore
        while (!stack.isEmpty()) {
            //next node to explore
            int nextStep = stack.pop();

            //All the edges going out of nextStep
            Collection neighbors = graph.getE(nextStep);
            if (neighbors == null) {
                visited.put(nextStep, false);
                return;
            }
            Iterator<edge_data> neiIter = neighbors.iterator();

            //While nextStep has more edges
            while (neiIter.hasNext()) {
                edge_data currEdge = neiIter.next();

                //The node currEdge is pointing to
                int dest = currEdge.getDest();

                //Checks if dest was already visited during this dfs run
                boolean keyVisited = visited.get(dest);

                //If dest was not visited we need to remember to explore it next and mark it as visited
                if (!keyVisited) {
                    visited.put(dest, true);
                    stack.push(dest);
                }
            }
        }

    }

    /**
     * Sets all values in a int -> boolean map to false
     * @param visited
     */
    private void setFalse(HashMap<Integer, Boolean> visited) {
        if(visited == null) return;
        for(Integer key: visited.keySet())
        {
            visited.put(key,false);
        }
    }

    private void dijkstra(node_data src){
        PriorityQueue<node_data> pq = new PriorityQueue<>();
        src.setTag(0);
        src.setInfo(visited); // set the info of the first node to black,
        pq.add(src); // add the first node to the list
        while (!pq.isEmpty()) { // while the list is not empty
            src = pq.poll(); // node = the first node in the list, and poll him from the list
            src.setInfo(visited); // set this node to black
            for (edge_data pointer : graph.getE(src.getKey())){

                node_data destNode = graph.getNode(pointer.getDest());
                if (destNode.getWeight() > src.getWeight() + pointer.getWeight()) { // if the tag of the node is bigger then the previews node + the weight of the edge - update the tag of the node
                    destNode.setWeight(src.getWeight() + pointer.getWeight());
                    parents.put(destNode,src);
                }
                if (destNode.getInfo().equals(unvisited)) { // if the neighbor isn't visit yet set him to grey
                    pq.add(destNode); // add him to the list
                }
            }
    }

    //********* Private Methods *********//
}

    /**
     * Sets all tags of nodes to positive infinity and all infos to "white"
     */
    private void infTags(){
        node_data node;
        Iterator<node_data> iter = graph.getV().iterator(); // run on the HashMap gr
        while (iter.hasNext()) {
            node = iter.next();
            node.setTag(Integer.MAX_VALUE*2);
            node.setInfo(unvisited);
        }
    }
}

package api;

import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms{

    private directed_weighted_graph graph;
    private HashMap<node_data, node_data> parents = new HashMap<>();
    private String visited = "visited", unvisited = "unvisited", flipped = "flipped";

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
     * This method uses  Kosaraju's Algorithm and a DFS algorithm to count the number of SCC in the graph,
     * if it's more than one, the graph is not strongly connected.
     * @return True iff  this graph is strongly connected, meaning, every node in this graph can be reached
     * from every other node in this graph.
     */
    @Override
    public boolean isConnected() {

        setUnvisited();

        //Will count the number of SCC in this graph, if it's more than one, the graph is not connected.
        int SCCSize = 0;

        return Kosaraju() == 1;
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

        infWeights();

        Dijkstra(source);

        pathLength = destination.getWeight();

        if(pathLength == Double.POSITIVE_INFINITY) pathLength = -1;

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

    private void Dijkstra(node_data src){


        PriorityQueue<node_data> pq = new PriorityQueue<>();
        src.setWeight(0);
        src.setInfo(visited); // set the info of the first node to black,
        pq.add(src); // add the first node to the list
        while (!pq.isEmpty()) { // while the list is not empty
            src = pq.poll(); // node = the first node in the list, and poll him from the list
            src.setInfo(visited); // set this node to black
            if(graph.getE(src.getKey()) == null) continue;
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

    private int Kosaraju(){
        //Pointers
        node_data node;

        //This stack will keep track of which node we will explore next.
        Stack<Integer> finish = new Stack<>();

        //This list will contain sets that represent the SCCs int this graph.
        List<Set<Integer>> SCC = new LinkedList<>();

        Collection<node_data> nodes = graph.getV();

        if(nodes != null && !nodes.isEmpty()){
            Iterator<node_data> iter = nodes.iterator();
            while(iter.hasNext()) {
                node_data next = iter.next();
                if(next.getInfo() != visited)
                {
                    next.setInfo(visited);
                    DFSFill(next, finish);
                }
            }

            setUnvisited();
            directed_weighted_graph reversed = reverseGraph(graph);
            while (!finish.isEmpty())
            {
                node = reversed.getNode(finish.pop());
                if(node.getInfo() != visited)
                {
                    Set<Integer> component = new HashSet<>();
                    component.add(node.getKey());
                    SCC.add(component);
                    DFSEmpty(node,component, reversed);
                }

            }
        }
        else return 1;

        return SCC.size();
    }

    private void DFSEmpty(node_data node, Set<Integer> comp, directed_weighted_graph graphT) {
        //Pointers
        edge_data currEdge;
        node_data next;
        Collection<edge_data> edges = graphT.getE(node.getKey());

        if(edges != null && !edges.isEmpty()){
            Iterator<edge_data> iter = edges.iterator();
            while(iter.hasNext()){
                next = graphT.getNode(iter.next().getDest());
                if(next.getInfo() != visited)
                {
                    next.setInfo(visited);
                    comp.add(next.getKey());
                    DFSEmpty(next, comp, graphT);
                }
            }
            return;
        }
    }

    private void DFSFill(node_data node, Stack<Integer> finishTime){
        //Pointers
        edge_data currEdge;
        node_data next;
        Collection<edge_data> edges = graph.getE(node.getKey());


        if(edges != null && !edges.isEmpty()) {
            Iterator<edge_data> iter = edges.iterator();
            while(iter.hasNext())
            {
                next = graph.getNode(iter.next().getDest());
                if(next.getInfo() != visited)
                {
                    next.setInfo(visited);
                    DFSFill(next, finishTime);
                }
            }
        }
        finishTime.push(node.getKey());
        return;
    }

    private directed_weighted_graph reverseGraph(directed_weighted_graph graph){
        //Pointers
        Collection<edge_data> edges;

        //We don't want to compromise the original graph so we will copy it and reverse its edges
        directed_weighted_graph reversed = this.copy();

        for(node_data node : graph.getV())
        {
            edges = graph.getE(node.getKey());
            if(edges != null && !edges.isEmpty()){
                for(edge_data edge : edges){
                    if(graph.getEdge(edge.getDest(),edge.getSrc()) == null) {
                        if (reversed.getEdge(edge.getSrc(), edge.getDest()).getInfo() != flipped) {
                            reversed.removeEdge(edge.getSrc(), edge.getDest());
                            reversed.connect(edge.getDest(), edge.getSrc(), edge.getWeight());
                            reversed.getEdge(edge.getDest(), edge.getSrc()).setInfo(flipped);
                        }
                    }
                }
            }
        }
        return reversed;
    }

    private void setEdgesInfo(directed_weighted_graph reversed) {
        String notFlipped = "not flipped";
        for(node_data node : reversed.getV())
        {
            Collection<edge_data> edges = reversed.getE(node.getKey());
            if(edges != null) {
                for (edge_data edge : edges)
                {
                    edge.setInfo(notFlipped);
                }
            }
        }
    }

    private void reverseEdge(edge_data edge, directed_weighted_graph reversedGraph) {
        if (edge == null) return;

        //Pointers
        int src = edge.getSrc();
        int dest = edge.getDest();
        double weight = edge.getWeight();

        //The flip
        reversedGraph.removeEdge(src, dest);
        reversedGraph.connect(dest, src, weight);

        //Setting the info
        reversedGraph.getEdge(dest,src).setInfo(flipped);
    }

    /**
     * Sets all tags of nodes to positive infinity and all infos to "white"
     */
    private void infWeights(){
        node_data node;
        Iterator<node_data> iter = graph.getV().iterator(); // run on the HashMap gr
        while (iter.hasNext()) {
            node = iter.next();
            node.setWeight(Double.POSITIVE_INFINITY);
            node.setInfo(unvisited);
        }
    }

    private void setUnvisited(){
        node_data node;
        Iterator<node_data> iter = graph.getV().iterator();
        while (iter.hasNext()) {
            node = iter.next();
            node.setInfo(unvisited);
        }
    }
}

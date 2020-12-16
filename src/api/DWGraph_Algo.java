package api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.annotations.JsonAdapter;
import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.*;
import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms {

    private directed_weighted_graph graph;
    private HashMap<node_data, node_data> parents = new HashMap<>();
    private String visited = "visited", unvisited = "unvisited", flipped = "flipped";

    /**
     * shallow copies specified graph g to this graph.
     *
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
     *
     * @return directed_weighted_graph - a deep copy of the underlying graph of this class
     */
    @Override
    public directed_weighted_graph copy() {
        if (graph == null) return null;

        //The graph we will be returning
        directed_weighted_graph copy = new DWGraph_DS();

        //If original graph is empty, a new empty graph will bu sufficient to return.
        if (graph.edgeSize() == 0 && graph.nodeSize() == 0) return copy;

        //We know those are not null because we passed the former if statement
        Collection<node_data> thisNodes = graph.getV();
        Collection<edge_data> currNeighbors;

        //run on all the nodes of this graph
        for (node_data currNode : thisNodes) {
            int currKey = currNode.getKey();

            //If copy does not contain a node with currKey
            if (copy.getNode(currKey) == null) {
                //copy the current node and add him to the copy graph
                node_data copiedNode = new NodeData(currNode);
                copy.addNode(copiedNode);
            }
            //run on all the edges going out from the current node if exist
            currNeighbors = graph.getE(currKey);
            if (currNeighbors != null) {

                for (edge_data currEdge : currNeighbors) {
                    int destKey = currEdge.getDest();
                    double currEdgeWeight = currEdge.getWeight();
                    if (copy.getEdge(currKey, destKey) == null) {
                        if (copy.getNode(destKey) == null) {
                            node_data origDest = graph.getNode(destKey);
                            node_data copiedDest = new NodeData(origDest);
                            copy.addNode(copiedDest);

                        }
                        copy.connect(currKey, destKey, currEdgeWeight);

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
     *
     * @return True iff  this graph is strongly connected, meaning, every node in this graph can be reached
     * from every other node in this graph.
     */
    @Override
    public boolean isConnected() {
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

        if (pathLength == Double.POSITIVE_INFINITY) pathLength = -1;

        return pathLength;

    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        double size = shortestPathDist(src, dest);
        if (size == -1) return null;
        node_data source = graph.getNode(src);
        node_data son = graph.getNode(dest);
        node_data dad;
        ArrayList<node_data> lst = new ArrayList<>(); // list represent the path
        lst.add(son);
        if (size == 0) return lst; // if the list is empty return
        for (node_data pointer : parents.values()) {
            dad = parents.get(son);
            lst.add(dad);
            if (dad == source) break;
            son = dad;
        }
        ArrayList<node_data> reversLst = new ArrayList<>();
        for (int i = 0; i < lst.size(); i++) {
            reversLst.add(lst.get(lst.size() - i - 1)); // revers the list
        }
        return reversLst;
    }

    @Override
    public boolean save(String file) {

        if (!file.endsWith(".json") && file.contains(".")) return false;
        else if (!file.endsWith(".json") && !file.contains("."))
            file = file + ".json";

        //We will write this JSON object to our file
        JSONObject thisGraph = new JSONObject();

        JSONArray nodes = new JSONArray();
        JSONArray edges = new JSONArray();
        JSONObject node;
        JSONObject edge;

        for (node_data currNode : graph.getV()) {
            node = new JSONObject();
            node.put("key", currNode.getKey());
            node.put("info", currNode.getInfo());
            node.put("weight", currNode.getWeight());
            node.put("tag", currNode.getTag());
            nodes.add(node);
            Collection<edge_data> myNeis = graph.getE(currNode.getKey());
            if (myNeis != null && !myNeis.isEmpty()) {
                for (edge_data currEdge : graph.getE(currNode.getKey())) {
                    edge = new JSONObject();
                    edge.put("src", currEdge.getSrc());
                    edge.put("dest", currEdge.getDest());
                    edge.put("weight", currEdge.getWeight());
                    edges.add(edge);
                }
            }
        }

        thisGraph.put("nodes", nodes);
        thisGraph.put("edges", edges);
        thisGraph.put("node size", graph.nodeSize());
        thisGraph.put("edge size", graph.edgeSize());


        try (FileWriter fw = new FileWriter(file)) {
            fw.write(thisGraph.toString());
            fw.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean load(String file) {

        JSONParser parser = new JSONParser();

        if (!file.endsWith(".json") && file.contains(".")) return false;
        else if (!file.endsWith(".json") && !file.contains("."))
            file = file + ".json";

        try {

            JSONObject myGraph = (JSONObject) parser.parse(new FileReader(file));

            JSONArray Jnodes = (JSONArray) myGraph.get("nodes");
            JSONArray Jedges = (JSONArray) myGraph.get("edges");

            node_data node;

            if (graph == null) graph = new DWGraph_DS();
            clearGraph();


            Iterator<JSONObject> nodeIterator = Jnodes.iterator();
            while (nodeIterator.hasNext()) {
                JSONObject jNode = nodeIterator.next();
                long key = (long) jNode.get("key");
                double weight = (double) jNode.get("weight");
                long tag = (long) jNode.get("tag");
                String info = (String) jNode.get("info");

                node = new NodeData((int) key, weight, info, (int) tag);
                graph.addNode(node);
            }

            Iterator<JSONObject> edgeIterator = Jedges.iterator();
            while (edgeIterator.hasNext()) {
                JSONObject jEdge = edgeIterator.next();
                long src = (long) jEdge.get("src");
                long dest = (long) jEdge.get("dest");
                double weight = (double) jEdge.get("weight");

                graph.connect((int) src, (int) dest, weight);
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //********* Private Methods *********//

    /**
     * This method is a java implementation of Dijkstra's algorithm to find shortest path between two vertices in
     * a graph.
     *
     * @param src
     */
    private void Dijkstra(node_data src) {

        //This queue will track which node we want to explore next
        PriorityQueue<node_data> pq = new PriorityQueue<>();

        //A nodes distance from itself is 0
        src.setWeight(0);

        // We mark which nodes we already visited and add them to the queue for further exploration
        pq.add(src);
        src.setInfo(visited);

        while (!pq.isEmpty()) {
            //We will explore this node next
            src = pq.poll();
            src.setInfo(visited);

            //If this node has no outgoing edges, there is nothing more to explore
            if (graph.getE(src.getKey()) == null) continue;

            //Exploring this node's outgoing edges
            for (edge_data pointer : graph.getE(src.getKey())) {
                node_data destNode = graph.getNode(pointer.getDest());

                //If the current path is shorter than the older one, replace nodes weight the correct one
                double pathWeight = src.getWeight() + pointer.getWeight();
                if (destNode.getWeight() > pathWeight) {
                    destNode.setWeight(pathWeight);

                    //Mark current src as dest's current 'cheapest' perant
                    parents.put(destNode, src);
                }

                if (destNode.getInfo().equals(unvisited)) {
                    pq.add(destNode);
                }
            }
        }
    }

    /**
     * This method is a java implementation of Kosarahu's algorithm to find number of SCC in a graph.
     * a SCC is a component in a graph in which from every vertex there is a path to every other vertex.
     * If the number of SCCs in the graph is 1, that means the graph itself is a SCC thus, th graph is connected.
     */
    private int Kosaraju() {

        //If the graph is empty or has only one node it is vacuously connected
        if (graph.nodeSize() <= 1) return 1;

        //This stack will keep track of which node finished exploring its children
        Stack<Integer> KStack = new Stack<>();

        //This stack will manage the way we explore our graph and will maintain a DFS approach
        Stack<Integer> DFSStack = new Stack<>();

        //This list will contain sets that represent the SCCs int this graph.
        Set<Set<Integer>> SCC = new HashSet<>();

        setUnvisited(graph);

        for (node_data node : graph.getV()) {
            if (node.getInfo() == unvisited) {
                node.setInfo(visited);
                DFSFill(KStack, DFSStack, node);
            }
        }

        directed_weighted_graph reversed = reverseGraph(graph);

        while (!KStack.isEmpty()) {
            Set<Integer> comp = DFSEmpty(DFSStack, SCC, reversed, KStack.pop());
            if (comp != null) SCC.add(comp);
        }
        return SCC.size();
    }

    /**
     * This method is an implementation of a DFS algorithm. We use it in order to empty the stack Kosaraju's algorithm is using.
     *
     * @param DFSStack - The stack that will help us maintain a DFS
     * @param SCC      - The set that will contain all of the nodes in the current SCC
     * @param graphT   - The reversed graph we will be working on
     */
    private Set<Integer> DFSEmpty(Stack<Integer> DFSStack, Set<Set<Integer>> SCC, directed_weighted_graph graphT, int currKey) {
        //Pointer
        node_data currNode = graphT.getNode(currKey);

        if (currNode.getInfo() == unvisited) {
            currNode.setInfo(visited);

            //We will be filling and returning this set
            Set<Integer> comp = new HashSet<>();
            comp.add(currNode.getKey());

            DFSStack.push(currNode.getKey());

            OUTER:
            while (!DFSStack.isEmpty()) {

                if (graphT.getE(DFSStack.peek()) == null) {
                    DFSStack.pop();
                    continue;
                }

                Collection<edge_data> edges = graphT.getE(DFSStack.peek());
                for (edge_data edge : edges) {
                    node_data currChild = graphT.getNode(edge.getDest());
                    if (currChild.getInfo() == unvisited) {
                        currChild.setInfo(visited);
                        DFSStack.push(currChild.getKey());
                        comp.add(currChild.getKey());
                        continue OUTER;
                    }
                }
                DFSStack.pop();
            }
            return comp;
        }
        return null;
    }

    /**
     * This method is an implementation of a DFS algorithm. We use it in order to fill the stack Kosaraju's algorithm is using.
     *
     * @param node
     * @param KStack
     * @param
     */
    private void DFSFill(Stack<Integer> KStack, Stack<Integer> DFSStack, node_data node) {

        DFSStack.push(node.getKey());

        OUTER:
        while (!DFSStack.isEmpty()) {
            if (graph.getE(DFSStack.peek()) == null) {
                KStack.push(DFSStack.pop());
                continue;
            }
            Collection<edge_data> edges = graph.getE(DFSStack.peek());

            for (edge_data edge : edges) {
                node_data currChild = graph.getNode(edge.getDest());
                if (currChild.getInfo() == unvisited) {
                    currChild.setInfo(visited);
                    DFSStack.push(currChild.getKey());
                    continue OUTER;
                }

            }
            KStack.push(DFSStack.pop());
        }
    }

    private directed_weighted_graph reverseGraph(directed_weighted_graph graph) {
        //We don't want to compromise the original graph so we will copy it and reverse its edges
        directed_weighted_graph reversed = new DWGraph_DS();

        for (node_data node : graph.getV()) {
            node.setInfo(unvisited);
            reversed.addNode(new NodeData(node));
        }
        for (node_data node : graph.getV()){
            Collection<edge_data> edges = graph.getE(node.getKey());
            if(edges != null) {
                for (edge_data edge : edges) {
                    reversed.connect(edge.getDest(), edge.getSrc(), edge.getWeight());
                }
            }
        }
        return reversed;
    }

    /**
     * Sets info of all edges in given graph to "not flipped". It is used to reverse a graph
     *
     * @param reversed
     */
    private void setEdgesInfo(directed_weighted_graph reversed) {
        String notFlipped = "not flipped";
        for (node_data node : reversed.getV()) {
            Collection<edge_data> edges = reversed.getE(node.getKey());
            if (edges != null) {
                for (edge_data edge : edges) {
                    edge.setInfo(notFlipped);
                }
            }
        }
    }

    /**
     * Sets all tags of nodes to positive infinity and all infos to "white"
     */
    private void infWeights() {
        node_data node;
        Iterator<node_data> iter = graph.getV().iterator(); // run on the HashMap gr
        while (iter.hasNext()) {
            node = iter.next();
            node.setWeight(Double.POSITIVE_INFINITY);
            node.setInfo(unvisited);
        }
    }

    /**
     * Sets the info of all nodes in caller graph to "unvisitd"
     */
    private void setUnvisited(directed_weighted_graph g) {
        node_data node;
        Iterator<node_data> iter = g.getV().iterator();
        while (iter.hasNext()) {
            node = iter.next();
            node.setInfo(unvisited);
        }
    }

    private void clearGraph() {
        if (graph == null) return;
        for (node_data currNode : graph.getV()) {
            graph.removeNode(currNode.getKey());
        }
    }

    //********* Private Methods *********//

    //********* Util Methods *********//

    public static directed_weighted_graph setGraphJSON(String json){

        Gson g = new Gson();
        directed_weighted_graph graph = (directed_weighted_graph) g.fromJson(json, DWGraph_Algo.class);
        return graph;

    }
}

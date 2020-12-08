package api;

import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms{

    private directed_weighted_graph graph;

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

    @Override
    public directed_weighted_graph copy() {
        return null;
    }

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
        return 0;
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

    //********* Private Methods *********//
}

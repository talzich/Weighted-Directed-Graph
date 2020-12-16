package gameClient;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;
import org.json.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Arena {

    public static final double EPS1 = 0.001, EPS2 = EPS1 * EPS1;

    private directed_weighted_graph graph;
    private List<CL_Agent> agents;
    private List<CL_Pokemon> pokemons;
    private List<String> info;

    private static Point3D MIN = new Point3D(0, 100, 0);
    private static Point3D MAX = new Point3D(0, 100, 0);

    public Arena() {
        info = new ArrayList<>();
    }



    // ********** Edge Methods ********* //

    /**
     * This method updates a single pokemon's edge from null to its correct current edge.
     * @param pokemon - The pokemon to update
     * @param g - The specific graph on which
     */
    public static void updateEdge(CL_Pokemon pokemon, directed_weighted_graph g) {

        Iterator<node_data> itr = g.getV().iterator();
        while (itr.hasNext()) {
            node_data v = itr.next();
            Iterator<edge_data> iter = g.getE(v.getKey()).iterator();
            while (iter.hasNext()) {
                edge_data e = iter.next();
                boolean f = isOnEdge(pokemon.getLocation(), e, pokemon.getType(), g);
                if (f) {
                    pokemon.set_edge(e);
                }
            }
        }
    }

    private static boolean isOnEdge(geo_location p, geo_location src, geo_location dest) {

        boolean ans = false;
        double dist = src.distance(dest);
        double d1 = src.distance(p) + p.distance(dest);
        if (dist > d1 - EPS2) {
            ans = true;
        }
        return ans;
    }

    private static boolean isOnEdge(geo_location p, int s, int d, directed_weighted_graph g) {
        geo_location src = g.getNode(s).getLocation();
        geo_location dest = g.getNode(d).getLocation();
        return isOnEdge(p, src, dest);
    }

    private static boolean isOnEdge(geo_location p, edge_data e, int type, directed_weighted_graph g) {
        int src = g.getNode(e.getSrc()).getKey();
        int dest = g.getNode(e.getDest()).getKey();
        if (type < 0 && dest > src) {
            return false;
        }
        if (type > 0 && src > dest) {
            return false;
        }
        return isOnEdge(p, src, dest, g);
    }

    private static Range2D GraphRange(directed_weighted_graph g) {
        Iterator<node_data> itr = g.getV().iterator();

        double x0 = 0, x1 = 0, y0 = 0, y1 = 0;
        boolean first = true;
        while (itr.hasNext()) {
            geo_location point = itr.next().getLocation();
            if (first) {

                x0 = point.x();
                x1 = x0;
                y0 = point.y();
                y1 = y0;
                first = false;

            } else {
                if (point.x() < x0) {
                    x0 = point.x();
                }
                if (point.x() > x1) {
                    x1 = point.x();
                }
                if (point.y() < y0) {
                    y0 = point.y();
                }
                if (point.y() > y1) {
                    y1 = point.y();
                }
            }
        }

        Range xr = new Range(x0, x1);
        Range yr = new Range(y0, y1);
        return new Range2D(xr, yr);
    }

    public static Range2Range w2f(directed_weighted_graph g, Range2D frame) {
        Range2D world = GraphRange(g);
        Range2Range ans = new Range2Range(world, frame);
        return ans;
    }

    // ********** Edge Methods ********* //


    // ********** Setters ********** //
    public void setPokemons(List<CL_Pokemon> pokemons) {
        this.pokemons = pokemons;
    }

    public void setAgents(List<CL_Agent> agents) {
        this.agents = agents;
    }

    public void setGraph(directed_weighted_graph g) {
        this.graph = g;
    }

    /**
     * This method gets a JSON type String that represents a graph and returns the appropriate graph.
     * @param jsonString
     * @return - directed_weighted_graph
     * @throws JSONException
     */
    public void setGraph(String jsonString) throws JSONException {

        JSONObject graphJSON = new JSONObject(jsonString);
        JSONArray Jedges = graphJSON.getJSONArray("Edges");
        JSONArray Jnodes = graphJSON.getJSONArray("Nodes");

        node_data node;

        graph = new DWGraph_DS();

        for (int i = 0; i < Jnodes.length(); i++) {
            JSONObject jNode = Jnodes.getJSONObject(i);
            int id = jNode.getInt("id");
            String  pos = jNode.getString("pos");
            node = new NodeData(id);
            geo_location point = new GeoLocation(pos);
            node.setLocation(point);
            graph.addNode(node);
        }

        for (int i = 0; i < Jedges.length(); i++) {
            JSONObject jEdge = Jedges.getJSONObject(i);
            int src = jEdge.getInt("src");
            int dest = jEdge.getInt("dest");
            double weight = jEdge.getDouble("w");
            graph.connect(src,dest,weight);
        }
    }

    /**
     * This method gets a JSON like String representing all the pokemons in the current level and
     * returns an ArrayList of the CL_Pokemon
     * @param pokemonJSON
     * @return
     */
    public void setPokemons(String pokemonJSON) {
        pokemons = new ArrayList<>();

        try {
            JSONObject origJson = new JSONObject(pokemonJSON);
            JSONArray pkmns = origJson.getJSONArray("Pokemons");
            for (int i = 0; i < pkmns.length(); i++) {
                JSONObject pkmnJson = pkmns.getJSONObject(i);
                JSONObject pk = pkmnJson.getJSONObject("Pokemon");
                int type = pk.getInt("type");
                double value = pk.getDouble("value");
                String pos = pk.getString("pos");
                CL_Pokemon pokemon = new CL_Pokemon(new Point3D(pos), type, value, 0, null);
                pokemons.add(pokemon);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method gets a String that looks like JSON, turns it into a real JSON and then
     * parsing it into a JAVA ArrayList of CL_Agent
     * @param agentString
     * @param graph
     * @return agents - an ArrayList of CL_Agent
     */
    public void setAgents(String agentString, directed_weighted_graph graph) {
        agents = new ArrayList<>();

        try {

            JSONObject agentJson = new JSONObject(agentString);
            JSONArray ags = agentJson.getJSONArray("Agents");

            for (int i = 0; i < ags.length(); i++)
            {
                CL_Agent agent = new CL_Agent(graph, 0);
                agent.update(ags.get(i).toString());
                agents.add(agent);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // ********** Setters ********** //



    // ********** Getters ********** //

    public List<CL_Agent> getAgents() {
        return agents;
    }

    public List<CL_Pokemon> getPokemons() {
        return pokemons;
    }

    public directed_weighted_graph getGraph(){return graph;}

    public List<String> getInfo() {
        return info;
    }

    // ********** Getters ********** //

}

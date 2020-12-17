package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a multi Agents Arena which move on a graph - grabs Pokemons and avoid the Zombies.
 * @author boaz.benmoshe
 *
 */
public class Arena {

	public static final double EPS =0.00001;

	private directed_weighted_graph graph;
	private List<CL_Agent> agents;
	private List<CL_Pokemon> pokemons;
	private List<String> info;

	private static Point3D MIN = new Point3D(0, 100,0);
	private static Point3D MAX = new Point3D(0, 100,0);

	/**
	 * A simple constructor
	 */
	public Arena() {
		info = new ArrayList<>();
	}


	// ********** Setters & Getters **********//

	public void setPokemons(List<CL_Pokemon> pokemons) {
		this.pokemons = pokemons;
	}

	public void setAgents(List<CL_Agent> agents) {
		this.agents = agents;
	}

	public void setGraph(directed_weighted_graph graph) {this.graph =graph;}

	public List<CL_Agent> getAgents() {return agents;}

	public List<CL_Pokemon> getPokemons() {return pokemons;}

	public directed_weighted_graph getGraph() {
		return graph;
	}

	public List<String> getInfo() {
		return info;
	}

	// ********** Setters & Getters **********//


	// ********** Beautified ********** //

	/**
	 * This method parses JSON-like Strings and returns the appropriate list of agents
	 * @param agentsJSON
	 * @param g
	 * @return
	 */
	public static List<CL_Agent> parseAgents(String agentsJSON, directed_weighted_graph g) {

		ArrayList<CL_Agent> agents = new ArrayList<>();

		try {
			JSONObject agentsJ = new JSONObject(agentsJSON);
			JSONArray agntArray = agentsJ.getJSONArray("Agents");

			for(int i=0; i < agntArray.length(); i++)
			{
				CL_Agent agent = new CL_Agent(g,0);
				agent.update(agntArray.get(i).toString());
				agents.add(agent);
			}
			//= getJSONArray("Agents");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return agents;
	}

	/**
	 * This method parses JSON-like Strings and returns the appropriate list of pokemons
	 * @param pokemonsJSON
	 * @return
	 */
	public static ArrayList<CL_Pokemon> parsePokemons(String pokemonsJSON) {

		ArrayList<CL_Pokemon> pokemons = new  ArrayList<>();

		try {

			JSONObject pkmnsJ = new JSONObject(pokemonsJSON);
			JSONArray pkmnArray = pkmnsJ.getJSONArray("Pokemons");

			for(int i=0; i < pkmnArray.length(); i++)
			{
				//Parsing
				JSONObject pokemonJ = pkmnArray.getJSONObject(i);
				JSONObject pkJ = pokemonJ.getJSONObject("Pokemon");

				//Getting the actual values
				int type = pkJ.getInt("type");
				double value = pkJ.getDouble("value");
				String pos = pkJ.getString("pos");

				//Setting the actual values to a new pokemon
				CL_Pokemon pokemon = new CL_Pokemon(new Point3D(pos), type, value, 0, null);
				pokemons.add(pokemon);
			}
		} catch (JSONException e) {e.printStackTrace();}

		return pokemons;
	}

	/**
	 * This methods updates the 'edge' field of a given pokemon to the actual edge said pokemon is on
	 * @param pokemon
	 * @param g
	 */
	public static void updateEdge(CL_Pokemon pokemon, directed_weighted_graph g) {

		for(node_data node : g.getV())
		{
			for(edge_data edge : g.getE(node.getKey()))
			{
				boolean found = isOnEdge(pokemon.getLocation(), edge, pokemon.getType(), g);
				if (found) pokemon.set_edge(edge);
			}
		}
	}

	/**
	 * The first in a set of three methods that help updateEdge determine on which edge a given pokemon is.
	 * This methods determines the direction of the edge this pokemon is on.
	 * @param pos
	 * @param edge
	 * @param type
	 * @param g
	 * @return
	 */
	private static boolean isOnEdge(geo_location pos, edge_data edge, int type, directed_weighted_graph g) {

		int src = g.getNode(edge.getSrc()).getKey();
		int dest = g.getNode(edge.getDest()).getKey();
		if(type<0 && dest>src) {return false;}
		if(type>0 && src>dest) {return false;}
		return isOnEdge(pos,src, dest, g);

	}

	/**
	 * The second in a set of three methods that help updateEdge determine on which edge a given pokemon is.
	 * This methods translates src and dest nodes to geoLocations.
	 * @param pos
	 * @param s
	 * @param d
	 * @param g
	 * @return
	 */
	private static boolean isOnEdge(geo_location pos, int s, int d, directed_weighted_graph g) {

		geo_location src = g.getNode(s).getLocation();
		geo_location dest = g.getNode(d).getLocation();
		return isOnEdge(pos,src,dest);

	}

	/**
	 * The third and final in a set of three methods that help updateEdge determine on which edge a given pokemon is.
	 * This methods determines whether the pokemon is actually on given edge by its position and the positions of its
	 * relative nodes
	 * @param pos
	 * @param src
	 * @param dest
	 * @return
	 */
	private static boolean isOnEdge(geo_location pos, geo_location src, geo_location dest ) {
		boolean ans = false;
		double dist = src.distance(dest);
		double d1 = src.distance(pos) + pos.distance(dest);
		if(dist>d1- EPS) {ans = true;}
		return ans;
	}

	// ********** Beautified ********** //

	private static Range2D GraphRange(directed_weighted_graph g) {
		Iterator<node_data> itr = g.getV().iterator();
		double x0=0,x1=0,y0=0,y1=0;
		boolean first = true;
		while(itr.hasNext()) {
			geo_location p = itr.next().getLocation();
			if(first) {
				x0=p.x(); x1=x0;
				y0=p.y(); y1=y0;
				first = false;
			}
			else {
				if(p.x()<x0) {x0=p.x();}
				if(p.x()>x1) {x1=p.x();}
				if(p.y()<y0) {y0=p.y();}
				if(p.y()>y1) {y1=p.y();}
			}
		}
		Range xr = new Range(x0,x1);
		Range yr = new Range(y0,y1);
		return new Range2D(xr,yr);
	}
	public static Range2Range w2f(directed_weighted_graph g, Range2D frame) {
		Range2D world = GraphRange(g);
		Range2Range ans = new Range2Range(world, frame);
		return ans;
	}



	// ********** Looks Unnecessary ********** //

	/*private void init( ) {
		MIN=null; MAX=null;
		double x0=0,x1=0,y0=0,y1=0;
		Iterator<node_data> iter = graph.getV().iterator();
		while(iter.hasNext()) {
			geo_location c = iter.next().getLocation();
			if(MIN==null) {x0 = c.x(); y0=c.y(); x1=x0;y1=y0;MIN = new Point3D(x0,y0);}
			if(c.x() < x0) {x0=c.x();}
			if(c.y() < y0) {y0=c.y();}
			if(c.x() > x1) {x1=c.x();}
			if(c.y() > y1) {y1=c.y();}
		}
		double dx = x1-x0, dy = y1-y0;
		MIN = new Point3D(x0-dx/10,y0-dy/10);
		MAX = new Point3D(x1+dx/10,y1+dy/10);

}*/

	/*public void setInfo(List<String> info) {
		this.info = info;
	}*/

}

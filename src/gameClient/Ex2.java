package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import gameClient.util.Point3D;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class Ex2 implements Runnable {

	//This list will update while the game is running to hold all the src nodes of edges pokemons are currently on
	private List<Integer> pkmnSrcs;

	/*
		This map holds the list of nodes which is the path agent has to take in order to get to the
		pokemon he is currently chasing
	 */
	private static HashMap<Agent, List<node_data>> pathMap;
	private static MyFrame window;
	private static Arena arena;
	private int level;
	private int id;

	public static void main(String[] a) {
		Thread client = new Thread(new Ex2());
		client.start();
	}

	//Figure out the resizeable window

	@Override
	public void run() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Please enter id: ");
		id = scanner.nextInt();

		System.out.println("Please enter level: ");
		level = scanner.nextInt();

		game_service game = Game_Server_Ex2.getServer(level);
		game.login(id);

		String graphJSON = game.getGraph();

		directed_weighted_graph graph = null;
		try {
			graph = graphFromJSON(graphJSON);
			init(game);
		} catch (JSONException e) {
			e.printStackTrace();
		}


		game.startGame();
		window.setTitle("Ex2 - Level: " + level);

		int ind = 0;
		long downTime = 100;

		while (game.isRunning()) {
			moveAgants(game, graph);
			try {
				if (ind % 1 == 0) {
					window.repaint();
				}
				Thread.sleep(downTime);
				ind++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String res = game.toString();
		System.out.println(game.timeToEnd());
		System.out.println(res);
		System.exit(0);
	}

	//Yet to beautify

	/**
	 * Moves each of the agents along the edge,
	 * in case the agent is on a node the next destination (next edge) is chosen (randomly).
	 *
	 * @param game
	 * @param g
	 * @param
	 */
	private static void moveAgants(game_service game, directed_weighted_graph g) {
		String movement = game.move();
		System.out.println(movement);
		List<Agent> agents = Arena.parseAgents(movement, g);
		arena.setAgents(agents);
		String pokeString = game.getPokemons();
		List<Pokemon> pokemons = Arena.parsePokemons(pokeString);
		arena.setPokemons(pokemons);
		for (int i = 0; i < agents.size(); i++) {
			Agent ag = agents.get(i);
			int id = ag.getID();
			int dest = ag.getNextNode();
			int currNode = ag.getCurrNode();
			double v = ag.getValue();
			if (dest == -1) {
				dest = nextNode(g, currNode, ag);
				game.chooseNextEdge(ag.getID(), dest);
				System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);
			}
		}
	}

	/**
	 * a very simple random walk implementation!
	 *
	 * @param graph
	 * @param curr
	 * @return
	 */
	private static int nextNode(directed_weighted_graph graph, int curr, Agent agent) {

		//For the first roung
		if(pathMap == null)
		{
			initPathMap();
		}

		int next = -1;


		//Check if agent is assigned with a pokemon
		List<node_data> currPath = pathMap.get(agent);

		//If an agent does not have a path, it 
		if(currPath != null){
			if (!currPath.isEmpty()){
				next = currPath.get(0).getKey();
				currPath.remove(0);
				return next;
			}
		}


		dw_graph_algorithms algo = new DWGraph_Algo();
		algo.init(graph);
		double shortestDist = Double.POSITIVE_INFINITY;
		Pokemon target = null;
		//This loop's purpose is to iterate through all the pokemons in the graph and choose the one we are closest to
		for (Pokemon pokemon : arena.getPokemons()){
			Arena.updateEdge(pokemon, graph);
			if(pokemon.isChased()) continue;
			int pokeSrc = pokemon.getEdge().getSrc();
			int pokeDest = pokemon.getEdge().getDest();
			List<node_data> potentialPath = algo.shortestPath(curr, pokeSrc);
			potentialPath.remove(0);

			double dist = potentialPath.size();
			if(dist == 0) return pokeDest;
			if(dist < shortestDist) {
				shortestDist = dist;
				pathMap.put(agent, potentialPath);
				next = potentialPath.get(0).getKey();
				target = pokemon;
			}
		}

		target.setChased();
		return next;
	}

	/**
	 * This method initializes the pathMap for each agent
	 */
	private static void initPathMap() {
		pathMap = new HashMap<>();
		for (Agent agent : arena.getAgents()){
			pathMap.put(agent, null);
		}

	}

	/**
	 * This method initializes everything that needs to be initialized before our game starts.
	 * @param game - the info provided from server regarding the current level
	 * @throws JSONException
	 */
	private void init(game_service game) throws JSONException {


		//The JSON-like Strings received from server
		String graphJSON = game.getGraph();
		String pkmnJSON = game.getPokemons();

		directed_weighted_graph graph = graphFromJSON(graphJSON);
		List<Pokemon> pokemons = pokemonFromJSON(pkmnJSON);

		arena = new Arena();
		arena.setGraph(graph);
		arena.setPokemons(pokemons);

		window = new MyFrame("Ex2");
		window.setSize(1000, 700);
		window.update(arena);
		window.setVisible(true);


		String gameInfo = game.toString();
		JSONObject gameJSON;
		try {
			gameJSON = new JSONObject(gameInfo);
			JSONObject serverJSON = gameJSON.getJSONObject("GameServer");

			int maxAgents = serverJSON.getInt("agents");

			for (int i = 0; i < pokemons.size(); i++) {
				Arena.updateEdge(pokemons.get(i), graph);
			}

			if (maxAgents < pokemons.size()) {

				for (int i = 0; i < maxAgents; i++) {
					edge_data edge = pokemons.get(i).getEdge();
					game.addAgent(edge.getSrc());
				}
			}
			else {

				int diff = maxAgents - pokemons.size();

				for (int i = 0; i < pokemons.size(); i++) {
					edge_data edge = pokemons.get(i).getEdge();
					game.addAgent(edge.getSrc());
				}

				for (int i = 0; i < diff; i++) {
					game.addAgent(i % (graph.nodeSize() - 1));
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private directed_weighted_graph graphFromJSON(String jsonString) throws JSONException {

		JSONObject graphJSON = new JSONObject(jsonString);
		JSONArray Jedges = graphJSON.getJSONArray("Edges");
		JSONArray Jnodes = graphJSON.getJSONArray("Nodes");

		node_data node;

		directed_weighted_graph graph = new DWGraph_DS();

		for (int i = 0; i < Jnodes.length(); i++) {
			JSONObject jNode = Jnodes.getJSONObject(i);
			int id = jNode.getInt("id");
			String pos = jNode.getString("pos");
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
			graph.connect(src, dest, weight);
		}
		return graph;
	}

	private List<Pokemon> pokemonFromJSON(String jsonString) throws JSONException{
		List<Pokemon> pokemons = new ArrayList<>();

		try {
			JSONObject origJson = new JSONObject(jsonString);
			JSONArray pkmns = origJson.getJSONArray("Pokemons");
			for (int i = 0; i < pkmns.length(); i++) {
				JSONObject pkmnJson = pkmns.getJSONObject(i);
				JSONObject pk = pkmnJson.getJSONObject("Pokemon");
				int type = pk.getInt("type");
				double value = pk.getDouble("value");
				String pos = pk.getString("pos");
				Pokemon pokemon = new Pokemon(new Point3D(pos), type, value,null);
				pokemons.add(pokemon);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return pokemons;
	}
}

package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import gameClient.util.Point3D;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class Ex2 implements Runnable {

	private static MyFrame window;
	private static Arena arena;
	private int level;
	private int id;

	public static void main(String[] a) {
		Thread client = new Thread(new Ex2());
		client.start();
	}

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
		String pkmnsJSON = game.getPokemons();

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

		System.out.println(res);
		System.exit(0);
	}


	/**
	 * Moves each of the agents along the edge,
	 * in case the agent is on a node the next destination (next edge) is chosen (randomly).
	 *
	 * @param game
	 * @param gg
	 * @param
	 */
	private static void moveAgants(game_service game, directed_weighted_graph gg) {
		String movement = game.move();
		List<CL_Agent> log = Arena.getAgents(movement, gg);
		arena.setAgents(log);
		//ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();
		String fs = game.getPokemons();
		List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
		arena.setPokemons(ffs);
		for (int i = 0; i < log.size(); i++) {
			CL_Agent ag = log.get(i);
			int id = ag.getID();
			int dest = ag.getNextNode();
			int src = ag.getSrcNode();
			double v = ag.getValue();
			if (dest == -1) {
				dest = nextNode(gg, src);
				game.chooseNextEdge(ag.getID(), dest);
				System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);
			}
		}
	}

	/**
	 * a very simple random walk implementation!
	 *
	 * @param g
	 * @param src
	 * @return
	 */
	private static int nextNode(directed_weighted_graph g, int src) {
		int ans = -1;
		Collection<edge_data> ee = g.getE(src);
		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int) (Math.random() * s);
		int i = 0;
		while (i < r) {
			itr.next();
			i++;
		}
		ans = itr.next().getDest();
		return ans;
	}

	private void init(game_service game) throws JSONException {

		String graphJSON = game.getGraph();
		String pkmnJSON = game.getPokemons();

		directed_weighted_graph graph = graphFromJSON(graphJSON);

		arena = new Arena();
		arena.setGraph(graph);
		arena.setPokemons(pokemonFromJSON(pkmnJSON));

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
			List<CL_Pokemon> pokemons = arena.getPokemons();

			for (int i = 0; i < pokemons.size(); i++) {
				Arena.updateEdge(pokemons.get(i), graph);
			}

			if (maxAgents < pokemons.size()) {

				for (int i = 0; i < maxAgents; i++) {
					edge_data edge = pokemons.get(i).get_edge();
					game.addAgent(edge.getSrc());
				}
			}
			else {

				int diff = maxAgents - pokemons.size();

				for (int i = 0; i < pokemons.size(); i++) {
					edge_data edge = pokemons.get(i).get_edge();
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

	private List<CL_Pokemon> pokemonFromJSON(String jsonString) throws JSONException{
		List<CL_Pokemon> pokemons = new ArrayList<>();

		try {
			JSONObject origJson = new JSONObject(jsonString);
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

		return pokemons;
	}
}

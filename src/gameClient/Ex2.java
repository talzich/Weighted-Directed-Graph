package gameClient;

import api.*;
import Server.Game_Server_Ex2;
import api.game_service;
import org.json.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Ex2 implements Runnable {

    Arena arena;
    MyFrame winodw;
    directed_weighted_graph graph;
    List<CL_Pokemon> pokemons;
    List<CL_Agent> agents = new ArrayList<>();
    game_service game;
    int level, id;

    private void login() {


        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter ID: ");
        id = scanner.nextInt();

        System.out.println("Please enter level [0-23]: ");
        level = scanner.nextInt();

    }

    /**
     * This method initializes the objects needed for placing and moving the agents in this level
     *
     * @param level
     * @throws JSONException
     */
    public void init(int level) throws JSONException {

        game = Game_Server_Ex2.getServer(level);

        arena = new Arena();

        arena.setGraph(game.getGraph());
        graph = arena.getGraph();

        arena.setPokemons(game.getPokemons());
        pokemons = arena.getPokemons();

        int maxAgents = 0;
        JSONObject gameInfo;
        try {
            gameInfo = new JSONObject(game.toString());
            JSONObject levelInfo = gameInfo.getJSONObject("GameServer");
            maxAgents = levelInfo.getInt("agents");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        updateEdges();
        placeAgents(maxAgents, game);

        arena.setAgents(game.getAgents(), graph);
        agents = arena.getAgents();

        winodw = new MyFrame("EX2");
        winodw.setSize(1000, 700);
        winodw.update(arena);

        game.startGame();
    }

    /**
     * This method updates the edge of every pokemon in this level
     */
    private void updateEdges() {
        for (CL_Pokemon pokemon : pokemons) {
            Arena.updateEdge(pokemon, graph);
        }
    }

    /**
     * This method does the initial placing of agents before the game starts
     *
     * @param maxAgents - number of agents allowed to play on this level
     * @param game      - the current level
     */
    private void placeAgents(int maxAgents, game_service game) {

        if (maxAgents < pokemons.size()) {

            for (int i = 0; i < maxAgents; i++) {

                edge_data edge = pokemons.get(i).get_edge();
                game.addAgent(edge.getSrc());
            }
        } else {

            int diff = maxAgents - pokemons.size();

            for (int i = 0; i < pokemons.size(); i++) {
                edge_data edge = pokemons.get(i).get_edge();
                game.addAgent(edge.getSrc());
            }

            for (int i = 0; i < diff; i++) {
                game.addAgent(i % (graph.nodeSize() - 1));
            }

        }
    }

    private void agentsPath() throws InterruptedException {

        AgentMovement agentMovement;

        for (CL_Agent agent : agents) {

            if (!agent.isMoving()) {

                agentMovement = new AgentMovement(agent);
                agentMovement.setGraph(graph);
                agentMovement.setPokemons(pokemons);
                Thread t = new Thread(agentMovement);
                t.start();
            }

        }
    }

    @Override
    public void run() {

        login();
        try {
            init(level);
            agentsPath();

            while (game.isRunning()) {
                Thread.sleep(1500);
                for (CL_Agent agent : agents) {

                    int pokeSrc = agent.getCurrPokemon().get_edge().getSrc();
                    int pokeDest = agent.getCurrPokemon().get_edge().getDest();
                    List<node_data> myPath = agent.getCurrPath();

                    if(agent.getSrcNode() == pokeSrc)
                        agent.setNextNode(pokeDest);

                    else
                    {
                        if(!myPath.isEmpty())
                        {
                            agent.setNextNode(myPath.get(0).getKey());
                            myPath.remove(0);
                        }

                        else
                        {
                            AgentMovement agentMovement = new AgentMovement(agent);
                            agentMovement.setGraph(graph);
                            agentMovement.setPokemons(pokemons);
                            Thread t = new Thread(agentMovement);
                            t.start();
                        }
                    }
                }
                System.out.println(game.move());
            }
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Thread client = new Thread(new Ex2());
        client.start();

    }

}

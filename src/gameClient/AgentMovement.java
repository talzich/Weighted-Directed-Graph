package gameClient;

import api.*;

import java.util.List;

public class AgentMovement implements Runnable{

    CL_Agent agent;
    List<CL_Pokemon> pokemons;
    List<node_data> shortestPath;
    CL_Pokemon nextPkmn;
    directed_weighted_graph graph;
    dw_graph_algorithms algo = new DWGraph_Algo();

    public AgentMovement(CL_Agent agent)
    {
        this.agent = agent;
    }

    public void setGraph(directed_weighted_graph graph) {
        this.graph = graph;
    }

    public void setPokemons(List<CL_Pokemon> pokemons){
        this.pokemons = pokemons;
    }

    public void setShortestPath(List<node_data> path){this.shortestPath = path;}

    // ********** Private Methods ********** //

    private void setNextPokemon(){

        double dist = Double.POSITIVE_INFINITY;
        double pathDist;

        for (CL_Pokemon pokemon : pokemons)
        {
            pathDist = algo.shortestPathDist(agent.getSrcNode(), pokemon.get_edge().getSrc());
            if (dist > pathDist && pathDist != -1) {
                dist = pathDist;
                nextPkmn = pokemon;
            }
        }

        if(nextPkmn != null){
            setShortestPath(algo.shortestPath(agent.getSrcNode(), nextPkmn.get_edge().getSrc()));
        }

        agent.setCurrPokemon(nextPkmn);

    }

    private void setAlgo(){
        algo.init(graph);
    }

    // ********** Private Methods ********** //



    @Override
    public void run() {
        setAlgo();
        setNextPokemon();
        agent.setCurrPath(shortestPath);
        agent.getCurrPath().remove(0);
    }
}

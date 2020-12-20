# Weighted Directed Graph

This repository documents the implementation of three java interfaces
 ("node_data", "DWGraph_DS", DWGraph_Algo) that together define the second assignment in the Object Oriented Programing 
 class at Ariel University.
 
## Overview 

 The assignment was defined by the three interfaces mentioned in the previous section
 and its goal was split into two parts.In the first part we had to build a weighted directed graph data structure, 
 as defined by the `node_data` and `DWGraph_DS` interfaces. 
 We had to build a class that would operate on a graph such as the one we
 built in the first part of the assignment, as defined by the `dw_graph_algoritmns`
 interface. 
 
 In the second part of the assignment we had to implement a 'Pokemon Game'. 
 Given a graph and Geo Locations of pokemons, we had to think of a way to get all the agents in the graph to chase pokemons and 
 catch them in the most efficient way we could find. 
 
 
 #### Our Build
 
 In order to build a graph data structure, we used two primary hashmaps, as well as a 
 secondary third one (all details can be viewed in javadoc format in the `DWGraph_DS` class).
 In order to define what a node in such a graph looks like we Implemented 
 the `node_data` interface.
 
 #### Dijkstra's Algorithm
 
 Part of this assignment was to implement Dijkstra's algorithm, as we did in Ex1. We used the traditional 
 implementation, using priority queue to monitor which part of the graph we should explore next. 
 The algorithm's way of exploring the graph can be best explained by the following animation. 
 
 ![Dijkstra](https://upload.wikimedia.org/wikipedia/commons/5/57/Dijkstra_Animation.gif)
 
 For the second part of the assignment we used, reformated and improved the efficiency of the provided classes - 
 'Arena', 'Pokemon', 'Agent' and 'Ex2'. 
 The purpose was to find the best way to explore a graph and catch all the pokemons on it.
 
 ## How to use
 
We provided a Ex2.jar file that can be run using double click like a .exe file or by using the Terminal\Command Line Prompt with the 
command `java -jar Ex2.jar <ID> <Level number>`. Both ways require the .jar file to be located in the same directory as the source of the graph that will
be provided to the game. 

Cath'em All!

  
    
 
 
 
 

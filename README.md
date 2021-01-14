# Weighted Directed Graph - Python

This repository documents the implementation of two python interfaces
 ("GraphInterface", "GraphAlgoInterface") that together define the fourth and last assignment in the Object Oriented Programing 
 class at Ariel University.
 
## Overview 

 The assignment was defined by the two interfaces mentioned in the previous section
 and its goal was split into three parts.In the first part we had to build a weighted directed graph data structure, 
 as defined by the 'GraphInterface' interface, build a class that would run all the algorithms from previous assignments as defined by the 'GraphAlgoInterface' interface. 
 The second part of the assignment was mainly focused on the matpoltlib module and using it to plot our graphs. 
 The third part of the assignment focused on comparing our implementation to the one in the previous assignment and the NetworkX module. 
 This comparison is availeable in the wiki of this repository.
 
 
 #### Our Build
 
 In order to build a graph data structure, we used dictionaries. 
 In order to define what a node and an edge in such a graph looks like we designed the 'Node' and 'Edge' classes.
 
 #### Dijkstra's Algorithm
 
 Part of this assignment was to implement Dijkstra's algorithm, as we did in Ex2. We used the traditional 
 implementation, using priority queue to monitor which part of the graph we should explore next. 
 The algorithm's way of exploring the graph can be best explained by the following animation. 
 
 ![Dijkstra](https://upload.wikimedia.org/wikipedia/commons/5/57/Dijkstra_Animation.gif)

 #### Kosaraju's Algorithm
We implemented Kosaraju's algorithm using DFS algorithms and a stack to keep track of our explored nodes.
We used the [following video] (https://www.youtube.com/watch?v=RpgcYiky7uw&ab_channel=TusharRoy-CodingMadeSimple) to help wrap our heads around the algorithm and design an implementation.



  
    
 
 
 
 

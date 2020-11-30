package api;

import java.util.HashMap;

public class NodeData implements node_data{

    private static int serial = 0;
    private int key = 0;
    private double weight = 0;
    private String info = "";
    private int tag = 0;
    private geo_location location;
    private HashMap<Integer, edge_data> edges;


    public NodeData() {
        this.key = serial++;
        edges = new HashMap<>();
    }

    public NodeData(int key) {
        this.key = key;
        edges = new HashMap<>();
    }

    public NodeData(NodeData copy) {
        this.key = copy.getKey();
        this.info = copy.getInfo();
        this.tag = copy.getTag();
        this.weight = copy.getWeight();
        this.edges = new HashMap<>();
    }

    public NodeData(int key, int tag, String info) {
        this.key = key;
        this.tag = tag;
        this.info = info;
        this.edges = new HashMap<>();
    }

    @Override
    public int getKey() {
        return this.key;
    }

    @Override
    public geo_location getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(geo_location p) {
        if(p == null) return;
        this.location = p;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(double w) {
        this.weight = w;
    }

    @Override
    public String getInfo() {
        return this.info;
    }

    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }
}
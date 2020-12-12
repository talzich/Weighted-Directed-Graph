package api;

import java.util.HashMap;

public class NodeData implements node_data, Comparable<node_data>{

    private static int serial = 0;
    private int key = 0;
    private double weight = 0;
    private String info = "";
    private int tag = 0;
    private geo_location location;


    public NodeData() {
        this.key = serial++;
    }

    public NodeData(int key) {
        this.key = key;
    }

    public NodeData(node_data other) {
        this.key = other.getKey();
        this.info = other.getInfo();
        this.tag = other.getTag();
        this.weight = other.getWeight();
    }

    public NodeData(int key, double weight, String info, int tag){
        this.key = key;
        this.weight = weight;
        this.info = info;
        this.tag = tag;
    }

    public NodeData(int key, int tag, String info) {
        this.key = key;
        this.tag = tag;
        this.info = info;
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

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final node_data other = (node_data) obj;


        return (this.key == other.getKey() && this.info.equals(getInfo()) && this.tag == other.getTag()
        && this.weight == other.getWeight());

    }

    @Override
    public int compareTo(node_data o) {
        if (o == null) return 1;

        if(this.getWeight() > o.getWeight()) return 1;
        else if (this.getWeight() == o.getWeight()) return 0;
        return -1;
    }

    @Override
    public String toString(){
        String s = ("Key: " + this.getKey() + " Weight: " + this.getWeight() + " Info: " + this.getInfo());
        return s;
    }
}
package api;

public class EdgeData implements edge_data {

    private int tag = 0;
    private double weight = 0;
    private String info = "";
    private node_data src, dest;

    /**
     * Simple constructor
     * @param src - the source node from which this edge pointing out
     * @param dest - the node this edge is pointing at
     * @param weight - the weight of this edge
     */
    public EdgeData(node_data src, node_data dest, double weight){
        this.src = src;
        this.dest =dest;
        this.weight = weight;
    }


    //*********** Setters & Getters ***********//
    @Override
    public int getSrc() {
        if (this.src == null) return -1;
        return this.src.getKey();
    }

    @Override
    public int getDest() {
        if(this.dest == null) return -1;
        return this.dest.getKey();
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public String getInfo() {
        return this.info;
    }

    @Override
    public void setInfo(String s) {
        if(s != null) this.info = s;
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }
    //*********** Setters & Getters ***********//
}

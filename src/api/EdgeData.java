package api;

public class EdgeData implements edge_data {

    private int tag = 0;
    private double weight = 0;
    private String info = " ";
    private int src, dest;

    /**
     * Simple constructor
     * @param src - the source node from which this edge pointing out
     * @param dest - the node this edge is pointing at
     * @param weight - the weight of this edge
     */
    public EdgeData(int src, int dest, double weight){
        this.src = src;
        this.dest =dest;
        this.weight = weight;
    }


    //*********** Setters & Getters ***********//
    @Override
    public int getSrc() {
        return this.src;
    }

    @Override
    public int getDest() {
        return this.dest;
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

    @Override
    public boolean equals(Object obj){

        //Simple check to see if we can cast
        if(obj == null) return false;
        if(obj.getClass() != this.getClass()) return false;

        final edge_data other = (edge_data) obj;

        return (this.weight == other.getWeight() && this.dest == other.getDest() && this.src == other.getSrc()
        && this.tag == other.getTag() && this.info.equals(other.getInfo()));



    }

    @Override
    public String toString(){
        String s = ("Weight: " + this.getWeight() + " Src: " + this.getSrc() + " Dest: " + this.getDest());
        return s;
    }
}

package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

public class Agent {

//		public static final double EPS = 0.0001;
//		private static int _count = 0;
//		private static int _seed = 3331;


    private int id;
    private geo_location position;
    private double speed;
    private edge_data currEdge;
    private node_data currNode;
    private directed_weighted_graph graph;
    private Pokemon currFruit;
    private long _sg_dt;

    private double value;

    /**
     * A simple constructor
     *
     * @param g
     * @param initNode
     */
    public Agent(directed_weighted_graph g, int initNode) {

        this.graph = g;
        this.currNode = graph.getNode(initNode);
        position = currNode.getLocation();
        id = -1;
        setSpeed(0);
        setValue(0);

    }

    /**
     * This method updates the id, speed, position, currNode, nextNode and value of this agent.
     * @param json - A JSON - like String
     */
    public void update(String json) {

        //A JSON object to hold the value of the info in param json
        JSONObject jObject;
        try {
            //Parsing
            jObject = new JSONObject(json);

            //Getting values
            JSONObject agent = jObject.getJSONObject("Agent");
            int id = agent.getInt("id");
            if (id == this.id || this.id == -1) {
                if (this.id == -1) {
                    this.id = id;
                }

                double speed = agent.getDouble("speed");
                String pos = agent.getString("pos");
                int src = agent.getInt("src");
                int dest = agent.getInt("dest");
                double value = agent.getDouble("value");

                //Setting the values
                Point3D point = new Point3D(pos);
                this.position = point;
                this.setCurrNode(src);
                this.setSpeed(speed);
                this.setNextNode(dest);
                this.setValue(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method produces a JSON like String to be used in other methods.
     * @return String
     */
    public String toJSON() {
        int nextKey = this.getNextNode();
        String json = "{\"Agent\":{"
                + "\"id\":" + this.id + ","
                + "\"value\":" + this.value + ","
                + "\"src\":" + this.currNode.getKey() + ","
                + "\"dest\":" + nextKey + ","
                + "\"speed\":" + this.getSpeed() + ","
                + "\"pos\":\"" + position.toString() + "\""
                + "}"
                + "}";
        return json;
    }

    public String toString() { return toJSON(); }

    /**
     * This methods stores the right value in this agent's currEdge field
     * @param dest
     * @return
     */
    public boolean setNextNode(int dest) {
        boolean status = false;
        int src = this.currNode.getKey();
        this.currEdge = graph.getEdge(src, dest);
        if (currEdge != null) {status = true;}
        else {currEdge = null;}
        return status;
    }

    /**
     * This method returns the destination of the edge this agent is currently on
     * @return
     */
    public int getNextNode() {
        int nextNode;
        if (this.currEdge == null) { nextNode = -1; }
        else { nextNode = this.currEdge.getDest(); }
        return nextNode;
    }


    // ********** Setters & Getters ********** //
    public int getSrcNode() {return this.currNode.getKey();}

    private void setValue(double value) {this.value = value;}

    public void setCurrNode(int src) {this.currNode = graph.getNode(src);}

    public boolean isMoving() {return this.currEdge != null;}

    public int getID() { return this.id; }

    public geo_location getLocation() { return position; }

    public double getValue() { return this.value; }

    public double getSpeed() { return this.speed; }

    public void setSpeed(double v) { this.speed = v; }

    public Pokemon getCurrFruit() { return currFruit; }

    public edge_data getCurrEdge() { return this.currEdge; }

    public void set_sg_dt(long _sg_dt) { this._sg_dt = _sg_dt; }
    // ********** Setters & Getters ********** //


    // ********** Not Sure ********** //

    public void set_SDT(long ddtt) {
        long ddt = ddtt;
        if (this.currEdge != null) {
            double w = getCurrEdge().getWeight();
            geo_location dest = graph.getNode(getCurrEdge().getDest()).getLocation();
            geo_location src = graph.getNode(getCurrEdge().getSrc()).getLocation();
            double de = src.distance(dest);
            double dist = position.distance(dest);
            if (this.getCurrFruit().get_edge() == this.getCurrEdge()) {
                dist = currFruit.getLocation().distance(this.position);
            }
            double norm = dist / de;
            double dt = w * norm / this.getSpeed();
            ddt = (long) (1000.0 * dt);
        }
        this.set_sg_dt(ddt);
    }

    public long get_sg_dt() {
        return _sg_dt;
    }

    // ********** Not Sure ********** //

}

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
    private CL_Pokemon currFruit;
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

    //@Override
    public int getSrcNode() {
        return this.currNode.getKey();
    }

    public String toJSON() {
        int d = this.getNextNode();
        String ans = "{\"Agent\":{"
                + "\"id\":" + this.id + ","
                + "\"value\":" + this.value + ","
                + "\"src\":" + this.currNode.getKey() + ","
                + "\"dest\":" + d + ","
                + "\"speed\":" + this.getSpeed() + ","
                + "\"pos\":\"" + position.toString() + "\""
                + "}"
                + "}";
        return ans;
    }

    private void setValue(double value) {
        this.value = value;
    }

    public boolean setNextNode(int dest) {
        boolean ans = false;
        int src = this.currNode.getKey();
        this.currEdge = graph.getEdge(src, dest);
        if (currEdge != null) {
            ans = true;
        } else {
            currEdge = null;
        }
        return ans;
    }

    public void setCurrNode(int src) {
        this.currNode = graph.getNode(src);
    }

    public boolean isMoving() {
        return this.currEdge != null;
    }

    public String toString() {
        return toJSON();
    }

    public String toString1() {
        String ans = "" + this.getID() + "," + position + ", " + isMoving() + "," + this.getValue();
        return ans;
    }

    public int getID() {
        // TODO Auto-generated method stub
        return this.id;
    }

    public geo_location getLocation() {
        // TODO Auto-generated method stub
        return position;
    }


    public double getValue() {
        // TODO Auto-generated method stub
        return this.value;
    }


    public int getNextNode() {
        int ans = -2;
        if (this.currEdge == null) {
            ans = -1;
        } else {
            ans = this.currEdge.getDest();
        }
        return ans;
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setSpeed(double v) {
        this.speed = v;
    }

    public CL_Pokemon getCurrFruit() {
        return currFruit;
    }

    public void setCurrFruit(CL_Pokemon curr_fruit) {
        this.currFruit = curr_fruit;
    }

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

    public edge_data getCurrEdge() {
        return this.currEdge;
    }

    public long get_sg_dt() {
        return _sg_dt;
    }

    public void set_sg_dt(long _sg_dt) {
        this._sg_dt = _sg_dt;
    }
}

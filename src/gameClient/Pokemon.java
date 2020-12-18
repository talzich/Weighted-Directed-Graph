package gameClient;
import api.edge_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

public class Pokemon {
	private edge_data edge;
	private double value;
	private int type;
	private Point3D pos;
	private double minDist;
	private int minRo;
	private boolean isChased;

	/**
	 * A simple constructor
	 * @param p
	 * @param t
	 * @param v
	 * @param e
	 */
	public Pokemon(Point3D p, int t, double v, edge_data e) {
		this.type = t;
		this.value = v;
		this.edge = e;
		this.pos = p;
		this.minDist = -1;
		this.minRo = -1;
	}


	/**
	 * A simple toString method
	 * @return
	 */
	public String toString() {return "Pokemon:{value="+ value +", type= "+ type +"}";}

	public boolean isChased(){
		return this.isChased;
	}


	// ********** Getters & Setters ********** //
	public edge_data getEdge() { return edge; }

	public void setEdge(edge_data edge) { this.edge = edge; }

	public Point3D getLocation() { return pos; }

	public int getType() { return type; }

	public void setChased(){
		this.isChased = true;
	}

	public void setUnchased(){
		this.isChased = false;
	}

	// ********** Getters & Setters ********** //


	// ********** Currently unused ********** //
	public static Pokemon init_from_json(String json) {
		Pokemon ans = null;
		try {
			JSONObject p = new JSONObject(json);
			int id = p.getInt("id");

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return ans;
	}

	public double getValue() { return value; }

	public double getMinDist() { return minDist; }

	public void setMinDist(double mid_dist) { this.minDist = mid_dist; }

	public int getMinRo() { return minRo; }

	public void setMinRo(int minRo) { this.minRo = minRo; }
	// ********** Currently unused ********** //
}

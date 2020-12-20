package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a very simple GUI class to present a
 * game on a graph.
 */

public class MyFrame extends JFrame {

	private int index;
	private Arena arena;
	private gameClient.util.Range2Range w2F;

	MyFrame(String a) {
		super(a);
	}

	public void update(Arena ar) {
		this.arena = ar;
		updateFrame();
	}

	private void updateFrame() {
		Range rx = new Range(20, this.getWidth() - 20);
		Range ry = new Range(this.getHeight() - 10, 150);
		Range2D frame = new Range2D(rx, ry);
		directed_weighted_graph g = arena.getGraph();
		w2F = Arena.w2f(g, frame);
	}

	public void paintComponents(Graphics g) {
		int w = this.getWidth();
		int h = this.getHeight();
		g.clearRect(0, 0, w, h);
		BufferedImage resizedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = resizedImage.createGraphics();
		//graphics2D.drawImage(background.getImage(), 0, 0, w, h, null);
		graphics2D.dispose();

		updateFrame();
		drawPokemons(g);
		drawGraph(g);
		drawAgants(g);
		drawInfo(g);

	}

	public void paint(Graphics g){

		int w = this.getWidth();
		int h = this.getHeight();

		Image buffer_image;
		Graphics buffer_graphics;
		// Create a new "canvas"
		buffer_image = createImage(w, h);
		buffer_graphics=buffer_image.getGraphics();
		paintComponents(buffer_graphics);
		// Draw on the new "canvas"
		// "Switch" the old "canvas" for the new one
		g.drawImage(buffer_image, 0, 0, this);

	}

	private void drawInfo(Graphics g) {
		List<String> str = arena.getInfo();
		String dt = "none";
		for (int i = 0; i < str.size(); i++) {
			g.drawString(str.get(i) + " dt: " + dt, 100, 60 + i * 20);
		}

	}

	private void drawGraph(Graphics g) {
		directed_weighted_graph gg = arena.getGraph();
		Iterator<node_data> iter = gg.getV().iterator();
		while (iter.hasNext()) {
			node_data n = iter.next();
			g.setColor(Color.blue);
			drawNode(n, 5, g);
			Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
			while (itr.hasNext()) {
				edge_data e = itr.next();
				g.setColor(Color.gray);
				drawEdge(e, g);
			}
		}
	}

	private void drawPokemons(Graphics g) {
		List<Pokemon> fs = arena.getPokemons();
		if (fs != null) {
			Iterator<Pokemon> itr = fs.iterator();

			while (itr.hasNext()) {

				Pokemon f = itr.next();
				Point3D c = f.getLocation();
				int r = 10;
				g.setColor(Color.green);
				if (f.getType() < 0) {
					g.setColor(Color.orange);
				}
				if (c != null) {

					geo_location fp = this.w2F.world2frame(c);
					g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
					//	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);

				}
			}
		}
	}

	private void drawAgants(Graphics g) {
		List<Agent> rs = arena.getAgents();
		//	Iterator<OOP_Point3D> itr = rs.iterator();
		g.setColor(Color.red);
		int i = 0;
		while (rs != null && i < rs.size()) {
			geo_location c = rs.get(i).getLocation();
			int r = 8;
			i++;
			if (c != null) {

				geo_location fp = this.w2F.world2frame(c);
				g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
			}
		}
	}

	private void drawNode(node_data n, int r, Graphics g) {
		geo_location pos = n.getLocation();
		geo_location fp = this.w2F.world2frame(pos);
		g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
		g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 4 * r);
	}

	private void drawEdge(edge_data e, Graphics g) {
		directed_weighted_graph gg = arena.getGraph();
		geo_location s = gg.getNode(e.getSrc()).getLocation();
		geo_location d = gg.getNode(e.getDest()).getLocation();
		geo_location s0 = this.w2F.world2frame(s);
		geo_location d0 = this.w2F.world2frame(d);
		g.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
		//	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
	}
}
package api;

import gameClient.util.Point3D;

public class GeoLocation implements geo_location{

    private double x, y, z;

    public GeoLocation(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public GeoLocation(String pos){
        String [] tokens = pos.split(",");
        this.x = Double.parseDouble(tokens[0]);
        this.y = Double.parseDouble(tokens[1]);
        this.z = Double.parseDouble(tokens[2]);

    }

    @Override
    public double x() {
        return this.x;
    }

    @Override
    public double y() {
        return this.y;
    }

    @Override
    public double z() {
        return this.z;
    }

    @Override
    public double distance(geo_location g) {
        Point3D p1 = new Point3D(this.x, this.y, this.z);
        Point3D p2 = new Point3D(g.x(), g.y(), g.z());
        return p1.distance(p2);
    }
}

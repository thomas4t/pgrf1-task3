package model;

import transforms.Mat4;
import transforms.Point3D;

public class Vertex {
    private Point3D position;
    private int color;
    public Vertex(double x, double y, double z){
        position = new Point3D(x,y,z);
        color = 0xffffff;
    }
    public Vertex(double x, double y, double z, int color){
        position = new Point3D(x,y,z);
        this.color = color;
    }
    public Vertex(Point3D p) {
        position = p;
        color = 0xffffff;
    }
    public Vertex(Point3D p, int color) {
        position = p;
        this.color = color;
    }

    public Point3D getPosition() {
        return position;
    }
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Returns new Vertex
     */
    public Vertex transformWith(Mat4 matrix) {
        return new Vertex(position.mul(matrix), color);
    }

    public boolean isNotInView() {
        return (-position.getW() >= position.getX() ||
                -position.getW() >= position.getY() ||
                 position.getX() >= position.getW() ||
                 position.getY() >= position.getW() ||
                 0 >= position.getZ() ||
                 position.getZ() >= position.getW()
        );
    }
}

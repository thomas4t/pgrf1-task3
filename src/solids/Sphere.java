package solids;

import model.Solid;
import model.Vertex;

public class Sphere extends Solid {
    //idk, probably wouldnt work
    public Sphere(float nOfVertices) {
        double size = .03;
        double inc = Math.PI * (3 - Math.sqrt(5));
        double off = 2 / nOfVertices;
        for (var k = 0; k < (nOfVertices); k++) {
            double y = k * off - 1 + (off / 2);
            double r = Math.sqrt(1 - y * y);
            double phi = k * inc;
            double xP = Math.cos(phi) * r * size;
            double yP = y * size;
            double zP = Math.sin(phi) * r * size;
            getVertices().add(new Vertex(xP, yP, zP));
        }
    }
}

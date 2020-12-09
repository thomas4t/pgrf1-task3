package solids;

import model.Solid;
import model.Vertex;

public class Tetrahedron extends Solid { //Čtyřstěn
    public Tetrahedron() {
        shouldBeTransformed = true;

        vertices.add(new Vertex(0,0,0, getRandomColor()));
        vertices.add(new Vertex(1,0,0, getRandomColor()));
        vertices.add(new Vertex(0,1,0, getRandomColor()));
        vertices.add(new Vertex(0,0,1, getRandomColor()));

        indices.add(0); indices.add(1); // edge 0-1
        indices.add(0); indices.add(2); // edge 0-2
        indices.add(0); indices.add(3); // edge 0-3

        indices.add(1); indices.add(2); // edge 1-2
        indices.add(1); indices.add(3); // edge 1-3

        indices.add(2); indices.add(3); // edge 2-3
    }


}

package solids;

import model.Solid;
import model.Vertex;

public class Block extends Solid {
    public Block() {
        shouldBeTransformed = true;

        vertices.add(new Vertex(0, 0, 0, getRandomColor()));
        vertices.add(new Vertex(1.5, 0, 0, getRandomColor()));
        vertices.add(new Vertex(0, 1, 0, getRandomColor()));
        vertices.add(new Vertex(1.5, 1, 0, getRandomColor()));
        vertices.add(new Vertex(0, 0, 1, getRandomColor()));
        vertices.add(new Vertex(1.5, 0, 1, getRandomColor()));
        vertices.add(new Vertex(0, 1, 1, getRandomColor()));
        vertices.add(new Vertex(1.5, 1, 1, getRandomColor()));

        indices.add(0); indices.add(1);
        indices.add(0); indices.add(2);
        indices.add(2); indices.add(3);
        indices.add(3); indices.add(1);
        indices.add(0); indices.add(4);
        indices.add(4); indices.add(5);
        indices.add(5); indices.add(7);
        indices.add(7); indices.add(6);
        indices.add(6); indices.add(4);
        indices.add(2); indices.add(6);
        indices.add(3); indices.add(7);
        indices.add(1); indices.add(5);


    }
}

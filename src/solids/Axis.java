package solids;

import model.Solid;
import model.Vertex;

public class Axis extends Solid {
    public Axis(){
        shouldBeTransformed = false;

        vertices.add(new Vertex(0, 0, 0, 0xffffff)); // vrchol na 0,0,0
        vertices.add(new Vertex(1, 0, 0, 0xff0000)); // 1 - k vrcholu (10,0,0) - cervena
        vertices.add(new Vertex(0, 1, 0, 0x00ff00)); // 2 - k vrcholu (0,10,0) - zelena
        vertices.add(new Vertex(0, 0, 1, 0x0000ff)); // 3 - k vrcholu (0,0,10) - modra

        indices.add(0); indices.add(1);
        indices.add(0); indices.add(2);
        indices.add(0); indices.add(3);
    }

}

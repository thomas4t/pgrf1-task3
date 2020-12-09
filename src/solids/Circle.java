package solids;

import model.Solid;
import model.Vertex;

public class Circle extends Solid {
    public Circle() {
        shouldBeTransformed = true;

        float a = 0;

        for (int i = 0; i < 36; i++) {
            a+=10;
            getVertices().add(new Vertex(0,
                    Math.cos(Math.toRadians(a)),
                    Math.sin(Math.toRadians(a)),
                    getRandomColor()
                    ));
        }

        for (int i = 0; i < 35; i++) {
            getIndices().add(i);
            getIndices().add(i+1);
        }
    }
}

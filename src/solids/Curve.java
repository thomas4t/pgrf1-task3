package solids;

import model.Solid;
import model.Vertex;
import transforms.Cubic;
import transforms.Mat4;
import transforms.Point3D;

public class Curve extends Solid {
    /**
     * Constructor for a basic curve based on Cubic(s)
     *
     * @param type           type of cubic to be used
     * @param amountOfPoints determines smoothness of lines
     */
    public Curve(CubicType type, int amountOfPoints) {
        shouldBeTransformed = true;
        Mat4 baseMatrix = new Mat4();
        switch (type) {
            case COONS -> baseMatrix = Cubic.COONS;
            case BEZIER -> baseMatrix = Cubic.BEZIER;
            case FERGUSON -> baseMatrix = Cubic.FERGUSON;
        }
        Cubic cubic = new Cubic(
                baseMatrix,
                new Point3D(0, 0, 0),
                new Point3D(1, 0, 0),
                new Point3D(0, 1, 0),
                new Point3D(0, 0, 1)
        );

        for (int i = 0; i <= amountOfPoints; i++) {
            getVertices().add(new Vertex(cubic.compute((double) i / amountOfPoints), getRandomColor()));
        }
        for (int i = 0; i < amountOfPoints; i++) {
            getIndices().add(i);
            getIndices().add(i + 1);
        }

    }
}

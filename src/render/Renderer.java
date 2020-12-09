package render;

import model.Scene;
import model.Solid;
import model.Vertex;
import rasterize.LineRasterizer;
import rasterize.Raster;
import transforms.*;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private final LineRasterizer lineRasterizer;
    private final Raster raster;
    private Mat4 model = new Mat4Identity();
    private Mat4 view = new Mat4Identity();
    private Mat4 projection = new Mat4Identity();

    public Renderer(Raster raster, LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
        this.raster = raster;
    }

    public void setModel(Mat4 model) {
        this.model = model;
    }

    public void setView(Mat4 view) {
        this.view = view;
    }

    public void setProjection(Mat4 projection) {
        this.projection = projection;
    }

    //Render vice objektu
    public void render(Scene scene) {
        for (Solid s : scene.getSolids()) {
            render(s);
        }
    }

    public void render(Solid solid) {
        //Modelovaci, pohledova a projekcni transformace
        Mat4 MVPMat;
        if (!solid.shouldBeTransformed()) {
            //Ignore model transformation
            MVPMat = new Mat4Identity().mul(view).mul(projection);
        } else {
            MVPMat = model.mul(view).mul(projection);
        }

        List<Vertex> transformedVertices = new ArrayList<>();
        for (Vertex v : solid.getVertices()) {
            //Transformujeme vsechny vrcholy spojenou matici
            transformedVertices.add(v.transformWith(MVPMat));
        }

        for (int i = 0; i < solid.getIndices().size(); i += 2) {
            int indexA = solid.getIndices().get(i);
            int indexB = solid.getIndices().get(i + 1);
            Vertex v1 = transformedVertices.get(indexA);
            Vertex v2 = transformedVertices.get(indexB);
            renderEdge(v1, v2);
        }
    }

    private void renderEdge(Vertex a, Vertex b) {
        // "Strict" Orezani
        if (a.isNotInView() || b.isNotInView()) {
            return;
        }

        //Dehomogenizace
        if (a.getPosition().dehomog().isEmpty() || b.getPosition().dehomog().isEmpty()) {
            return;
        }
        Vec3D va = a.getPosition().dehomog().get();
        Vec3D vb = b.getPosition().dehomog().get();

        //viewport transformace (do okna)
        int x1 = (int) ((va.getX() + 1) * (raster.getWidth() - 1) / 2);
        int x2 = (int) ((vb.getX() + 1) * (raster.getWidth() - 1) / 2);
        int y1 = (int) ((1 - va.getY()) * (raster.getHeight() - 1) / 2);
        int y2 = (int) ((1 - vb.getY()) * (raster.getHeight() - 1) / 2);

        //Vykresleni
        lineRasterizer.rasterize(x1, y1, x2, y2, b.getColor());
    }
}

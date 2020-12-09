package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Solid {
    protected boolean shouldBeTransformed;
    protected List<Vertex> vertices;
    protected List<Integer> indices;

    public Solid() {
        vertices = new ArrayList<>();
        indices = new ArrayList<>();
    }

    public boolean shouldBeTransformed() {
        return shouldBeTransformed;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public List<Integer> getIndices() {
        return indices;
    }

    protected int getRandomColor(){
        Random rnd = new Random();
        float r = rnd.nextFloat();
        float g = rnd.nextFloat();
        float b = rnd.nextFloat();
        return new Color(r, g, b).getRGB();
    }

    public void changeVertexColors(){
        for (Vertex v : vertices){
            v.setColor(getRandomColor());
        }
    }
}

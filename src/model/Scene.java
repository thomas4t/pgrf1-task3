package model;

import solids.Axis;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private List<Solid> solids;

    public Scene() {
        solids = new ArrayList<>();
    }

    public Scene(boolean withAxis) {
        solids = new ArrayList<>();
        if(withAxis){
            solids.add(new Axis());
        }
    }

    public Scene(List<Solid> solids) {
        this.solids = solids;
    }

    public List<Solid> getSolids() {
        return solids;
    }

    public void addSolid(Solid s) {
        solids.add(s);
    }

    public void reset(boolean withAxis) {
        solids.clear();
        if (withAxis) {
            addSolid(new Axis());
        }
    }

    /**
     * Changes colors of all objects
     * @param onlyTransformable determines whether colors of non-transformable objects should be included
     */
    public void changeColors(boolean onlyTransformable) {
        for (Solid s : solids) {
            if(onlyTransformable ){
                if (s.shouldBeTransformed) {
                    s.changeVertexColors();
                }
            }else{
                s.changeVertexColors();
            }
        }
    }


}

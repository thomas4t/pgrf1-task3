package rasterize;

import java.awt.*;

public class LineRasterizer {
    Raster raster;

    public LineRasterizer(Raster raster){
        this.raster = raster;
    }

    public void rasterize(int x1, int y1, int x2, int y2, Color color){
        int colorAsInt = color.getRGB();
        drawLine(x1,y1,x2,y2,colorAsInt);
    }

    public void rasterize(int x1, int y1, int x2, int y2, int color){
        drawLine(x1,y1,x2,y2, color);
    }

    private void drawLine(int x1, int y1, int x2, int y2, int color) {
        if ((x1 == x2) && (y1 == y2)) {
            raster.setPixel(x1, y1, color);

        } else {
            int dx = Math.abs(x2 - x1);
            int dy = Math.abs(y2 - y1);
            int delta = dx - dy;

            int shift_x, shift_y;

            if (x1 < x2) shift_x = 1;
            else shift_x = -1;
            if (y1 < y2) shift_y = 1;
            else shift_y = -1;

            while ((x1 != x2) || (y1 != y2)) {

                int p = 2 * delta;

                if (p > -dy) {
                    delta = delta - dy;
                    x1 = x1 + shift_x;
                }
                if (p < dx) {
                    delta = delta + dx;
                    y1 = y1 + shift_y;
                }
                raster.setPixel(x1, y1, color);
            }
        }
    }
}

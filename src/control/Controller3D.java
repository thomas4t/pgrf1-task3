package control;

import solids.*;
import model.Scene;
import rasterize.*;
import render.Renderer;
import transforms.*;
import view.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;


public class Controller3D implements Controller {

    private final Panel panel;
    private Renderer renderer;
    private Scene scene;
    private Mat4 model;
    private Mat4 projection;
    private JRadioButton projectionBtn;
    private JRadioButton animationBtn;
    private JRadioButton easterEggBtn;
    private Camera camera;
    private int w, h;
    private int currentX;
    private int currentY;
    private int previousX;
    private int previousY;
    private static Mat4 ORTHOGONAL_PROJECTION;
    private static Mat4 PERSPECTIVE_PROJECTION;
    private final static double STEP = 0.2;

    Timer animationTimer;
    Timer easterEggTimer;

    public Controller3D(Panel panel) {
        this.panel = panel;
        initObjects(panel.getRaster());
        initUI(panel);
        initListeners(panel);
        update();
    }

    public void initObjects(Raster raster) {
        w = raster.getWidth();
        h = raster.getHeight();

        PERSPECTIVE_PROJECTION = new Mat4PerspRH(Math.PI / 2, h / (double) w, 0.1, 10);
        ORTHOGONAL_PROJECTION = new Mat4OrthoRH(5, 5, 0.005, 100);

        renderer = new Renderer(raster, new LineRasterizer(raster));
        camera = new Camera()
                .withPosition(new Vec3D(-2, 0, 0))
                .withAzimuth(0)
                .withZenith(0);

        scene = new Scene(true);
        scene.addSolid(new Circle());

        model = new Mat4RotXYZ(Math.PI / 10, Math.PI / 5, Math.PI / 7);
        projection = PERSPECTIVE_PROJECTION;
    }

    public void initUI(Panel panel) {
        JLabel wsad = new JLabel("WSAD - Movement");
        wsad.setOpaque(true);
        wsad.setBackground(Color.ORANGE);
        JLabel mouseMvmt = new JLabel("Mouse btn 1/2 - Movement and rotation");
        mouseMvmt.setOpaque(true);
        mouseMvmt.setBackground(Color.ORANGE);
        JLabel zoom = new JLabel("MWHEEL - Zoom");
        zoom.setOpaque(true);
        zoom.setBackground(Color.ORANGE);
        JLabel tetra = new JLabel("1 - Cube");
        tetra.setOpaque(true);
        tetra.setBackground(Color.GREEN);
        JLabel circle = new JLabel("2 - Block");
        circle.setOpaque(true);
        circle.setBackground(Color.GREEN);

        JLabel coons = new JLabel("3 - COONS curve");
        coons.setOpaque(true);
        coons.setBackground(Color.CYAN);
        JLabel bezier = new JLabel("4 - BEZIER curve");
        bezier.setOpaque(true);
        bezier.setBackground(Color.CYAN);
        JLabel ferguson = new JLabel("5 - FERGUSON curve");
        ferguson.setOpaque(true);
        ferguson.setBackground(Color.CYAN);

        JLabel clear = new JLabel("C - Reset");
        clear.setOpaque(true);
        clear.setBackground(Color.RED);

        projectionBtn = new JRadioButton("Orthogonal projection");
        animationBtn = new JRadioButton("Animate movement");
        easterEggBtn = new JRadioButton("Easter egg");

        panel.add(wsad);
        panel.add(mouseMvmt);
        panel.add(zoom);
        panel.add(tetra);
        panel.add(circle);
        panel.add(coons);
        panel.add(bezier);
        panel.add(ferguson);

        panel.add(projectionBtn);
        panel.add(animationBtn);
        panel.add(easterEggBtn);

        panel.add(clear);
    }

    @Override
    public void initListeners(Panel panel) {
        // Button action listeners
        projectionBtn.addActionListener((ActionEvent e) -> {
            if (projectionBtn.isSelected()) {
                projection = ORTHOGONAL_PROJECTION;
            } else {
                projection = PERSPECTIVE_PROJECTION;
            }
            panel.grabFocus();
            update();
        });
        animationBtn.addActionListener((ActionEvent e) -> {
            if (animationBtn.isSelected()) {
                initAnimations();
            } else {
                animationTimer.cancel();
            }
            panel.grabFocus();
        });
        easterEggBtn.addActionListener((ActionEvent e) -> {
            if (easterEggBtn.isSelected()) {
                initEasterEgg();
            } else {
                easterEggTimer.cancel();
            }
            panel.grabFocus();
        });

        // Key events
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    //Camera movement
                    case KeyEvent.VK_W -> camera = camera.up(STEP);
                    case KeyEvent.VK_S -> camera = camera.down(STEP);
                    case KeyEvent.VK_A -> camera = camera.left(STEP);
                    case KeyEvent.VK_D -> camera = camera.right(STEP);
                    //Switch object to Cube
                    case KeyEvent.VK_1 -> {
                        scene.reset(true);
                        scene.addSolid(new Cube());
                    }
                    //Switch object to Block
                    case KeyEvent.VK_2 -> {
                        scene.reset(true);
                        scene.addSolid(new Block());
                    }
                    //Switch object to COONS Cubic
                    case KeyEvent.VK_3 -> {
                        scene.reset(true);
                        scene.addSolid(new Curve(CubicType.COONS, 10));
                    }
                    //Switch object to BEZIER Cubic
                    case KeyEvent.VK_4 -> {
                        scene.reset(true);
                        scene.addSolid(new Curve(CubicType.BEZIER, 10));
                    }
                    //Switch object to FERGUSON Cubic
                    case KeyEvent.VK_5 -> {
                        scene.reset(true);
                        scene.addSolid(new Curve(CubicType.FERGUSON, 10));
                    }
                    case KeyEvent.VK_C -> hardClear();
                }

                update();
            }
        });

        // Mouse events
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                currentX = e.getX();
                currentY = e.getY();
            }
        });
        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                previousX = currentX;
                previousY = currentY;

                currentX = e.getX();
                currentY = e.getY();

                double deltaX = currentX - previousX;
                double deltaY = currentY - previousY;

                //Moves the object(s) in the scene.
                if (SwingUtilities.isLeftMouseButton(e)) {
                    camera = camera.addAzimuth(Math.PI * deltaX / w);
                    camera = camera.addZenith(Math.PI * deltaY / w);
                }

                //Ensures the rotation of object(s) in the scene.
                if (SwingUtilities.isRightMouseButton(e)) {
                    model = model.mul(new Mat4RotXYZ(-deltaX * Math.PI / 120, deltaY * Math.PI / 120, 0));
                }

                update();
            }
        });
        panel.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                //Handler for zooming
                if (e.getWheelRotation() > 0) {
                    camera = camera.backward(STEP);
                } else {
                    camera = camera.forward(STEP);
                }
                update();
            }
        });

        //Resizing
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.resize();
                initObjects(panel.getRaster());
            }
        });
    }

    private void initAnimations() {
        animationTimer = new Timer();
        animationTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                double change = 0.01;
                model = model.mul(new Mat4RotXYZ(-change, change, 0));
                update();
            }
        }, 0, 10);
    }

    private void initEasterEgg() {
        easterEggTimer = new Timer();
        easterEggTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                scene.changeColors(true);
                update();
            }
        }, 0, 150);
    }

    private void update() {
        panel.clear();

        renderer.setModel(model);
        renderer.setProjection(projection);
        renderer.setView(camera.getViewMatrix());

        renderer.render(scene);

        panel.repaint();
    }

    private void hardClear() {
        initObjects(panel.getRaster());
        update();
    }

}

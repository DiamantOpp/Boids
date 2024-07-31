import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;

class Main extends JFrame {
    private JPanel panel;
    private Boid[] boids = new Boid[750];

    private int frame;
    private int fps_c;
    public int FPS;

    /* CUSTOMIZABLE */
    private final double TARGET_SPEED = 4.5;
    private final double RESOLVE = 0.1;
    private final double RANGE = 125;
    private final double SEPARATION = 0.5;
    private final double COHESION = 0.15; /* This value breaks everything if it goes to 0.2 or above, don't ask me why, I can't wrap my head around it either */
    private final double ALIGNMENT = 0.075;
    private final double SIZE = 15;
    private final boolean DEBUG = false;

    Main() {
        for (int i = 0; i < boids.length; i++)
            boids[i] = new Boid(SIZE);

        panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                // super.paint(g);
                Graphics2D g2D = (Graphics2D) g;
                g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
                g2D.setColor(java.awt.Color.BLACK);
                g2D.fillRect(0, 0, getWidth(), getHeight());
                int i = -1;
                for (Boid boid : boids) {
                    i++;
                    int nextX = boid.position.Xi() + boid.velocity.Xi();
                    int nextY = boid.position.Yi() + boid.velocity.Yi();
                    boid.draw(g2D, new int[] {nextX, nextY, (int) RANGE}, i == 0 && DEBUG);
                }
                g2D.dispose();
                g.dispose();
            }
        };

        add(panel);
        setUndecorated(true);
        pack();
        setSize(1920, 1080);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setTitle("Boids Simulation");
        setVisible(true);

        new Timer(1000/120, e -> {
            step();
            panel.repaint();
        }).start();

        new Timer(1000, e -> {
            FPS = fps_c;
            fps_c = 0;
            setTitle("Boids Simulation - " + String.valueOf(FPS) + " FPS");
        }).start();
    }

    private void step() {
        fps_c++;
        frame++;
        for (Boid boid : boids) {
            Vector target = boid.position.add(boid.velocity);
            boid.direction = Vector.lookAt(boid.position, target)-Math.PI/2;
            boid.position = target;
            if (boid.position.X() > getWidth()+boid.size) { boid.position = new Vector(-boid.size, boid.position.Y()); boid.recolor(frame); }
            if (boid.position.X() < -boid.size) { boid.position = new Vector(getWidth()+boid.size, boid.position.Y()); boid.recolor(frame); }
            if (boid.position.Y() > getHeight()+boid.size) { boid.position = new Vector(boid.position.X(), -boid.size); boid.recolor(frame); }
            if (boid.position.Y() < -boid.size) { boid.position = new Vector(boid.position.X(), getHeight()+boid.size); boid.recolor(frame); }
            boid.velocity = boid.velocity.add(boid.velocity.mul(TARGET_SPEED-boid.velocity.magnitude()).mul(RESOLVE));
            double sumX = 0, sumY = 0, sumVX = 0, sumVY = 0, count = 0;
            for (Boid other : boids) {
                double dist = Vector.dist(boid.position, other.position);
                if (dist > RANGE || boid == other || dist < 1)
                    continue;
                count++;
                sumX += other.position.X() - boid.position.X();
                sumY += other.position.Y() - boid.position.Y();
                sumVX += other.velocity.X() - boid.velocity.X();
                sumVY += other.velocity.Y() - boid.velocity.Y();
                double newX = -SEPARATION * ((other.position.X() - boid.position.X()) / dist);
                double newY = -SEPARATION * ((other.position.Y() - boid.position.Y()) / dist);
                boid.velocity = boid.velocity.add(new Vector(newX, newY));
            }
            if (count > 0) {
                if (Math.abs(sumX) > 0 && Math.abs(sumY) > 0) {
                    sumX /= count;
                    sumY /= count;
                    double newX = COHESION * sumX;
                    double newY = COHESION * sumY;
                    boid.velocity = boid.velocity.add(new Vector(newX, newY));
                }
                if (Math.abs(sumVX) > 0 && Math.abs(sumVY) > 0) {
                    sumVX /= count;
                    sumVY /= count;
                    double newX = ALIGNMENT * sumVX;
                    double newY = ALIGNMENT * sumVY;
                    boid.velocity = boid.velocity.add(new Vector(newX, newY));
                }
            }
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
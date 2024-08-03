import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.AlphaComposite;

public class Boid {
    public Color color = new Color(64, 224, 208);
    public Vector position = new Vector(Math.random() * 1920, Math.random() * 1080);
    public Vector velocity = new Vector();
    public double direction = Math.random()*Math.PI*2;
    public double size = 50;

    public Boid[] visibleBoids;

    Boid() {}

    Boid(double size) {
        this.size = size;
    }

    Boid(Color color) {
        this.color = color;
    }

    Boid(Color color, Vector position) {
        this.color = color;
        this.position = position;
    }

    Boid(Color color, Vector position, double size) {
        this.color = color;
        this.position = position;
        this.size = size;
    }

    public void recolor(int frame) { color = new Color(Color.HSBtoRGB(((float)frame/240) % 1, 1, 1)); }

    public void updateVisibleBoids(Boid[] boids) { visibleBoids = boids; }

    public void draw(Graphics2D g2D, int[] debug, boolean dodebug) {
        Vector p1 = position.move(0, size/2, direction); /* tip */
        Vector p2 = position.move(size/4, -size/2, direction); /* back-right 'fin' */
        Vector p3 = position.move(0, -size/3.5f, direction); /* back-center */
        Vector p4 = position.move(-size/4, -size/2, direction); /* back-left 'fin' */
        Polygon p = new Polygon();
        p.addPoint((int)p1.X(), (int)p1.Y());
        p.addPoint((int)p2.X(), (int)p2.Y());
        p.addPoint((int)p3.X(), (int)p3.Y());
        p.addPoint((int)p4.X(), (int)p4.Y());
        // g2D.setColor(Color.GREEN);
        // g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .05f));
        // if (dodebug)
        //     g2D.fillOval(position.Xi()-debug[2], position.Yi()-debug[2], debug[2]*2, debug[2]*2); /* range */
        g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
        g2D.setColor(Color.YELLOW);
        if (dodebug) {
            g2D.drawLine(position.Xi(), position.Yi(), debug[0], debug[1]); /* trajectory */
            if (visibleBoids != null && visibleBoids.length > 0)
            for (Boid boid : visibleBoids)
            g2D.drawLine(position.Xi(), position.Yi(), boid.position.Xi(), boid.position.Yi()); /* thoughts */
        }
        g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        g2D.setColor(dodebug? Color.RED : color);
        g2D.fillPolygon(p);
    }
}
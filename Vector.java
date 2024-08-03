public class Vector {
    private double X, Y;

    Vector() { X = 0; Y = 0; }
    Vector(double X, double Y) { this.X = X; this.Y = Y; }

    public double X() { return X; }
    public double Y() { return Y; }
    public float Xf() { return (float)X; }
    public float Yf() { return (float)Y; }
    public int Xi() { return (int)X; }
    public int Yi() { return (int)Y; }
    public String Xs() { return String.valueOf(X); }
    public String Ys() { return String.valueOf(Y); }

    public Vector add(Vector other) { return new Vector(X + other.X, Y + other.Y); }
    public Vector sub(Vector other) { return new Vector(X - other.X, Y - other.Y); }
    public Vector mul(double scalar) { return new Vector(X * scalar, Y * scalar); }
    public Vector div(double scalar) { return new Vector(X / scalar, Y / scalar); }

    public double magnitude() { return Math.sqrt(X*X + Y*Y); }
    public double dist(Vector other) { return sub(other).magnitude(); }
    public Vector unit() { return div(magnitude()); }

    public void moveTo(Vector target) { X = target.X(); Y = target.Y(); }
    public Vector move(double X, double Y, double direction) {
        double newX = X * Math.cos(direction) - Y * Math.sin(direction);
        double newY = X * Math.sin(direction) + Y * Math.cos(direction);
        return add(new Vector(newX, newY));
    }

    public static double dist(Vector a, Vector b) { return a.sub(b).magnitude(); }
    public static double lookAt(Vector a, Vector b) { return Math.atan2(b.Y() - a.Y(), b.X() - a.X()); }
}
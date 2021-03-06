package files.models.geometry;

import files.interfaces.Environment;
import files.models.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Triangle2D extends Shape implements Environment {
    private Point A, B, C;
    private Point[] points;
    private Line2D a, b, c;
    private Line2D[] lines;
    private boolean clockwise;

    Triangle2D(Point A, Point B, Point C) {
        this.A = A;
        this.B = B;
        this.C = C;
        points = new Point[]{A,B,C};
        a = new Line2D(B, C);
        b = new Line2D(C, A);
        c = new Line2D(A, B);
        lines = new Line2D[]{a,b,c};
        if (A.isLeftOfLine(a) && B.isLeftOfLine(b) && C.isLeftOfLine(c)) { // We use A, as we only need to use one of the 3 points
            clockwise = false;
        } else if (A.isRightOfLine(a) && B.isRightOfLine(b) && C.isRightOfLine(c)) {
            clockwise = true;
        } else {
            System.out.println("Warning: Triangle might be a line");
        }

    }

    public static void main(String[] args) {
        Triangle2D triangle = new Triangle2D(new Point(100, 100), new Point(50, 200), new Point(150, 200));
        System.out.println("clockwise: " + triangle.clockwise);
        Point obj = new Point(100, 150);
        System.out.println("obj colliding: " + triangle.isColliding(obj));

        Rectangle2D rectangle = new Rectangle2D(50,50,100,100, Color.BLACK);
        Point obj2 = new Point(75,75);
        rectangle.isColliding(obj2);
        System.out.println("obj2 colliding: "+rectangle.isColliding(obj2));
    }

    public boolean isColliding(Point point) {
        if (clockwise) {
            return (point.isRightOfLine(a) && point.isRightOfLine(b) && point.isRightOfLine(c))  || point.isOnLine(a) || point.isOnLine(b) || point.isOnLine(c);
        } else {
            return (point.isLeftOfLine(a) && point.isLeftOfLine(b) && point.isLeftOfLine(c)) || point.isOnLine(a) || point.isOnLine(b) || point.isOnLine(c);
        }
    }

    @Override
    public Line2D[] getLines() {
        return lines;
    }

    @Override
    public Point[] getPoints() {
        return points;
    }

    @Override
    public Point[] getGraphPoints() {
        return null;
    }

    @Override
    public void draw(GraphicsContext graphicsContext) {

    }

    @Override
    public void drawEdges(GraphicsContext graphicsContext) {

    }
}

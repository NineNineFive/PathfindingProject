package files.models.geometry;
import files.interfaces.Environment;
import files.models.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Rectangle2D extends Shape implements Environment {
    private Point A;
    private Point B;
    private Point C;
    private Point D;
    private Point[] points;
    private Point[] graphPoints;
    private Line2D AB, BD, CD, AC;
    private Line2D[] lines;
    private Vector position;
    private Vector size;
    private Triangle2D firstHalf;
    private Triangle2D secondHalf;
    public Color bgColor;
    public Color borderColor;
    private double borderSize;

    public Rectangle2D(double x, double y, double width, double height, Color bgColor){
        this(x, y, width, height, bgColor, Color.BLUE,2);
    }

    public Rectangle2D(double x, double y, double width, double height, Color bgColor, Color borderColor, double borderSize) {
        A = new Point(x,y);
        B = new Point(x+width,y);
        C = new Point(x,y+height);
        D = new Point(x+width,y+height);
        points = new Point[]{A,B,C,D};
        graphPoints = new Point[]{
            new Point(A.getX()-1,A.getY()-1),
            new Point(B.getX()+1,B.getY()-1),
            new Point(C.getX()-1,C.getY()+1),
            new Point(D.getX()+1,D.getY()+1)
        };
        AB = new Line2D(A,B);
        BD = new Line2D(B,D);
        CD = new Line2D(C,D);
        AC = new Line2D(A,C);
        lines = new Line2D[]{AB,BD,CD,AC};
        firstHalf = new Triangle2D(A,B,C);
        secondHalf = new Triangle2D(D,B,C);

        this.position = new Vector(x,y);
        this.size = new Vector(width, height);

        this.borderColor = borderColor;
        this.bgColor = bgColor;
        this.borderSize = borderSize;
    }

    public boolean isColliding(Point obj) {
        return firstHalf.isColliding(obj) || secondHalf.isColliding(obj);
    }

    @Override
    public void draw(GraphicsContext graphicsContext) {
        graphicsContext.setStroke(borderColor);

        if(bgColor!=null) {
            graphicsContext.setFill(bgColor);
        }

        graphicsContext.fillRect(position.getX(), position.getY(), size.getX(), size.getY());

        if (borderSize!=0){
            graphicsContext.setLineWidth(borderSize);
            graphicsContext.strokeRect(position.getX(),position.getY(),size.getX(),size.getY());
        }

    }

    @Override
    public void drawEdges(GraphicsContext graphicsContext) {

    }

    public Point getA() {
        return A;
    }

    public Point getB() {
        return B;
    }

    public Point getC() {
        return C;
    }

    public Point getD() {
        return D;
    }

    public Point[] getPoints() {
        return points;
    }

    public Line2D[] getLines() {
        return lines;
    }

    public Point[] getGraphPoints() {
        return graphPoints;
    }
}

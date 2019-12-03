package files.models.geometry;
import files.interfaces.Environment;
import files.models.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Rectangle2D extends Shape implements Environment {
    private Point A, B, C, D;
    private Line2D AB, BD, CD, AC;
    private Vector position;
    private Vector size;
    private Triangle2D firstHalf;
    private Triangle2D secondHalf;
    Color bgColor;
    Color borderColor;
    int borderSize;

    public Rectangle2D(double x, double y, double width, double height, Color bgColor){
        this(x, y, width, height, bgColor, null, 0);
    }

    public Rectangle2D(double x, double y, double width, double height, Color bgColor, Color borderColor, int borderSize) {
        A = new Point(x,y);
        B = new Point(x+width,y);
        C = new Point(x,y+height);
        D = new Point(x+width,y+height);
        AB = new Line2D(A,B);
        BD = new Line2D(B,D);
        CD = new Line2D(C,D);
        AC = new Line2D(A,C);
        firstHalf = new Triangle2D(A, B, C);
        secondHalf = new Triangle2D(D, B, C);

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
        if(bgColor!=null) {
            graphicsContext.setFill(bgColor);
        }

        graphicsContext.fillRect(position.getX(), position.getY(), size.getX(), size.getY());

        if (borderSize!=0){
            graphicsContext.setLineWidth(borderSize);
            graphicsContext.strokeRect(position.getX(),position.getY(),size.getX(),size.getY());
            graphicsContext.setStroke(borderColor);
        }
    }
}

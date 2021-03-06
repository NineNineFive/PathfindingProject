package files.interfaces;

import files.models.geometry.Point;
import javafx.scene.canvas.GraphicsContext;

public interface Environment {
    void draw(GraphicsContext graphicsContext);
    boolean isColliding(Point point);
}

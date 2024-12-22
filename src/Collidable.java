import javafx.geometry.Rectangle2D;

public interface Collidable {
    boolean checkCollision(Rectangle2D playerRect);
}
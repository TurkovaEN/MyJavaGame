import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;

public class Player {
    public double dx, dy; // speed
    public Rectangle2D rect; // coordinates width and height
    public boolean onGround; // check if sprite is on the ground
    public ImageView sprite; // sprite
    private double currentFrame;


    public Player(Image image) {
        sprite = new ImageView(image);
        rect = new Rectangle2D(245, 186, 110, 112); // rect (x,y, width, height)

        dx = dy = 0.1;
        currentFrame = 0;
        sprite.setViewport(new Rectangle2D(245, 186, 110, 112));
    }

    public void update(double time, MapCollider mapCollider) {
        rect = new Rectangle2D(rect.getMinX() + dx * time, rect.getMinY(), rect.getWidth(), rect.getHeight()); // x coordinate
        mapCollider.handleCollision(rect, new double[]{dx, dy}, new boolean[]{onGround}, 0); // Horizontal collision

        if (!onGround)
            dy += 0.0005 * time; // gravity acceleration during jump
        rect = new Rectangle2D(rect.getMinX(), rect.getMinY() + dy * time, rect.getWidth(), rect.getHeight()); // y coordinate
        onGround = false;
        mapCollider.handleCollision(rect, new double[]{dx, dy}, new boolean[]{onGround}, 1); // Vertical collision

        currentFrame += 0.005 * time;
        if (currentFrame > 7)
            currentFrame -= 7;

        if (dx > 0)
            sprite.setViewport(new Rectangle2D(160 * (int) currentFrame + 245, 185, 95, 118));
        if (dx < 0)
            sprite.setViewport(new Rectangle2D(160 * (int) currentFrame + 245 + 95, 185, -95, 118));

        sprite.setLayoutX(rect.getMinX());
        sprite.setLayoutY(rect.getMinY()); // set sprite position x, y

        dx = 0;
    }
}
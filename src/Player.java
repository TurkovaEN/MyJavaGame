import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;



public class Player {
    public double dx, dy; // скорость
    public Rectangle2D rect; // координаты, ширина и высота
    public boolean onGround; // проверка, находится ли спрайт на земле
    public ImageView sprite; // спрайт
    private double currentFrame;

    public Player(Image image) {
        sprite = new ImageView(image);
        rect = new Rectangle2D(245, 186, 110, 112); // Начальная позиция персонажа

        dx = 0; // Горизонтальная скорость
        dy = 0; // Вертикальная скорость
        currentFrame = 0;
        sprite.setViewport(new Rectangle2D(245, 186, 110, 112));
    }

    public void update(double time, MapCollider mapCollider) {
        double[] movement = {dx, dy}; // Сохраняем текущие скорости
        boolean[] onGroundFlag = {onGround};

        // Горизонтальное движение
        rect = new Rectangle2D(rect.getMinX() + movement[0] * time, rect.getMinY(), rect.getWidth(), rect.getHeight());
        rect = mapCollider.handleCollision(rect, movement, onGroundFlag, 0);

        // Вертикальное движение с учетом гравитации
        if (!onGround) {
            dy += 0.0015 * time; // Гравитация (ускорение вниз)
        }
        movement[1] = dy;

        rect = new Rectangle2D(rect.getMinX(), rect.getMinY() + movement[1] * time, rect.getWidth(), rect.getHeight());
        rect = mapCollider.handleCollision(rect, movement, onGroundFlag, 1);

        // Обновляем флаг onGround
        onGround = onGroundFlag[0];

        // Обновляем позицию спрайта
        sprite.setLayoutX(rect.getMinX ());
        sprite.setLayoutY(rect.getMinY());

        // Анимация и направление спрайта
        currentFrame += Math.abs(dx) * 0.02 * time; // Анимация только при движении
        if (currentFrame > 7) currentFrame -= 7;

        if (dx > 0) {
            sprite.setViewport(new Rectangle2D(160 * (int) currentFrame + 245, 185, 95, 118));
            sprite.setScaleX(1); // Движение вправо
        } else if (dx < 0) {
            sprite.setViewport(new Rectangle2D(160 * (int) currentFrame + 245, 185, 95, 118));
            sprite.setScaleX(-1); // Движение влево (отражение)
        }

        // Сброс dx после каждого кадра
        dx = 0;
    }
}
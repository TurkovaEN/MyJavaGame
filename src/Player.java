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
        rect = new Rectangle2D(245, 186, 110, 112); // rect (x,y, width, height)

        dx = dy = 0.1;
        currentFrame = 0;
        sprite.setViewport(new Rectangle2D(245, 186, 110, 112));
    }

    public void update(double time, MapCollider mapCollider) {
        // Обновляем горизонтальное движение
        rect = new Rectangle2D(rect.getMinX() + dx * time, rect.getMinY(), rect.getWidth(), rect.getHeight());

        // Обрабатываем столкновение с картой по горизонтали
        rect = mapCollider.handleCollision(rect, new double[]{dx, dy}, new boolean[]{onGround}, 0); // Горизонтальное столкновение

        // Обрабатываем гравитацию (если персонаж не на земле)
        if (!onGround) {
            dy += 0.0005 * time; // Ускорение гравитации
        }

        // Обновляем вертикальное движение
        rect = new Rectangle2D(rect.getMinX(), rect.getMinY() + dy * time, rect.getWidth(), rect.getHeight());

        // Обрабатываем столкновение с картой по вертикали
        onGround = false; // Пока не обнаружим столкновение, на земле не стоим
        rect = mapCollider.handleCollision(rect, new double[]{dx, dy}, new boolean[]{onGround}, 1); // Вертикальное столкновение

        // Если персонаж на земле, сбрасываем вертикальную скорость
        if (onGround) {
            dy = 0; // Останавливаем вертикальное движение
        }

        // Обновление спрайта
        currentFrame += 0.005 * time;
        if (currentFrame > 7) currentFrame -= 7;

        // Обновление спрайта по направлению
        if (dx > 0)
            sprite.setViewport(new Rectangle2D(160 * (int) currentFrame + 245, 185, 95, 118));
        if (dx < 0)
            sprite.setViewport(new Rectangle2D(160 * (int) currentFrame + 245 + 95, 185, -95, 118));

        // Обновляем позицию спрайта
        sprite.setLayoutX(rect.getMinX());
        sprite.setLayoutY(rect.getMinY());

        // Останавливаем горизонтальное движение (пока клавиша не отпущена)
        dx = 0;
    }
}
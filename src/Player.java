import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class Player {
    public double dx, dy; // Скорости по осям
    public Rectangle2D rect; // Координаты, ширина и высотаЭ
    public boolean onGround; // Проверка, находится ли спрайт на земле
    public ImageView sprite; // Спрайт игрока
    private double currentFrame; // Текущий кадр анимации

    // Конструктор, инициализирующий спрайт и начальные параметры
    public Player(Image image) {
        sprite = new ImageView(image);
        rect = new Rectangle2D(100, 5, 110, 112); // Начальная позиция персонажа
        dx = 0; // Горизонтальная скорость
        dy = 0; // Вертикальная скорость
        currentFrame = 0; // Начальный кадр анимации
        sprite.setViewport(new Rectangle2D(245, 186, 110, 112)); // Установка области отображения спрайта
    }

    // Метод обновления состояния игрока
    public void update(double time, MapCollider mapCollider) {
        double[] movement = {dx, dy}; // Сохраняем текущие скорости
        boolean[] onGroundFlag = {onGround}; // Флаг, указывающий на то, находится ли игрок на земле

        // Горизонтальное движение
        rect = new Rectangle2D(rect.getMinX() + movement[0] * time, rect.getMinY(), rect.getWidth(), rect.getHeight());
        rect = mapCollider.handleCollision(rect, movement, onGroundFlag, 0); // Проверка столкновений по X

        // Вертикальное движение с учетом гравитации
        if (!onGround) {
            dy += 0.0015 * time; // Применение гравитации
        }
        movement[1] = dy; // Обновление вертикальной скорости

        rect = new Rectangle2D(rect.getMinX(), rect.getMinY() + movement[1] * time, rect.getWidth(), rect.getHeight());
        rect = mapCollider.handleCollision(rect, movement, onGroundFlag, 1); // Проверка столкновений по Y

        // Обновляем флаг onGround
        onGround = onGroundFlag[0];

        // Обновляем позицию спрайта
        sprite.setLayoutX(rect.getMinX());
        sprite.setLayoutY(rect.getMinY());

        // Анимация и направление спрайта
        currentFrame += Math.abs(dx) * 0.02 * time; // Анимация только при движении
        if (currentFrame > 7) currentFrame -= 7; // Цикл анимации

        // Установка области отображения спрайта в зависимости от направления
        if (dx > 0) {
            sprite.setViewport(new Rectangle2D(160 * (int) currentFrame + 245, 185, 95, 118)); // Движение вправо
            sprite.setScaleX(1);
        } else if (dx < 0) {
            sprite.setViewport(new Rectangle2D(160 * (int) currentFrame + 245, 185, 95, 118)); // Движение влево
            sprite.setScaleX(-1); // Отражение спрайта
        }

        // Сброс dx после каждого кадра
        dx = 0;
    }
}
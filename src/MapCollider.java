import javafx.geometry.Rectangle2D;

public class MapCollider {

    // Метод проверки столкновений со стенами
    public Rectangle2D handleCollision(Rectangle2D rect, double[] dxdy, boolean[] onGround, int dir) {
        boolean collided = false;

        // Проверяем все клетки карты, с которыми может столкнуться персонаж
        for (int i = (int) rect.getMinY() / Map.TILE_SIZE; i < (rect.getMaxY() / Map.TILE_SIZE); i++) {
            for (int j = (int) rect.getMinX() / Map.TILE_SIZE; j < (rect.getMaxX() / Map.TILE_SIZE); j++) {
                if (i >= 0 && i < Map.H && j >= 0 && j < Map.W && Map.tileMap[i].charAt(j) == '#') {
                    collided = true; // Обнаружено столкновение

                    if (dir == 0) {  // Горизонтальное столкновение
                        // Столкновение с правой стеной
                        if (dxdy[0] > 0) {
                            rect = new Rectangle2D(j * Map.TILE_SIZE - rect.getWidth(), rect.getMinY(), rect.getWidth(), rect.getHeight());
                        }
                        // Столкновение с левой стеной
                        if (dxdy[0] < 0) {
                            rect = new Rectangle2D(j * Map.TILE_SIZE + Map.TILE_SIZE, rect.getMinY(), rect.getWidth(), rect.getHeight());
                        }
                        dxdy[0] = 0; // Обнуляем горизонтальную скорость
                    } else {  // Вертикальное столкновение
                        // Столкновение с верхней стеной
                        if (dxdy[1] < 0) {
                            rect = new Rectangle2D(rect.getMinX(), i * Map.TILE_SIZE + Map.TILE_SIZE, rect.getWidth(), rect.getHeight());
                            dxdy[1] = 0; // Обнуляем вертикальную скорость
                        }
                        // Столкновение с нижней стеной
                        if (dxdy[1] > 0) {
                            rect = new Rectangle2D(rect.getMinX(), i * Map.TILE_SIZE - rect.getHeight(), rect.getWidth(), rect.getHeight());
                            dxdy[1] = 0; // Обнуляем вертикальную скорость
                            onGround[0] = true; // Персонаж на земле
                        }
                    }
                    break;  // Прерываем цикл, так как мы уже нашли столкновение
                }
            }
        }
        return rect; // Возвращаем обновленный rect с учетом столкновения
    }
}
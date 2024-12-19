import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class PlayerController {
    // Метод обработки ввода с клавиатуры
    public void handleInput(Player player, KeyEvent event) {
        // Движение влево
        if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
            player.dx = -0.9; // Установка скорости влево
        }

        // Движение вправо
        if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
            player.dx = 0.9; // Установка скорости вправо
        }

        // Прыжок
        if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W || event.getCode() == KeyCode.SPACE) {
            if (player.onGround) {
                player.dy = -0.85; // Установка вертикальной скорости для прыжка
                player.onGround = false; // Сбрасываем флаг "на земле"
            }
        }
    }
}
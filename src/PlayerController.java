import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class PlayerController {
    public void handleInput(Player player, KeyEvent event) {
        if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
            player.dx = -0.99; // Движение влево
        }

        if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
            player.dx = 0.99; // Движение вправо
        }

        if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W || event.getCode() == KeyCode.SPACE) {
            if (player.onGround) {
                player.dy = -0.45; // Высота прыжка
                player.onGround = false;
            }
        }
    }
}
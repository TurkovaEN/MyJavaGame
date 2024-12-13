import javafx.scene.input.KeyCode;

public class PlayerController {
    public void handleInput(Player player) {
        if (javafx.scene.input.InputEvent.getKeyCode() == KeyCode.LEFT || javafx.scene.input.InputEvent.getKeyCode() == KeyCode.A) {
            player.dx = -0.1;
        }

        if (javafx.scene.input.InputEvent.getKeyCode() == KeyCode.RIGHT || javafx.scene.input.InputEvent.getKeyCode() == KeyCode.D) {
            player.dx = 0.1;
        }

        if (javafx.scene.input.InputEvent.getKeyCode() == KeyCode.UP || javafx.scene.input.InputEvent.getKeyCode() == KeyCode.W || javafx.scene.input.InputEvent.getKeyCode() == KeyCode.SPACE) {
            if (player.onGround) {
                player.dy = -0.45; // jump height
                player.onGround = false;
            }
        }
    }
}
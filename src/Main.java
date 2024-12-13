import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 1570, 730);

        // Load textures
        Image playerImage = new Image("sprite_character.png");
        Image backgroundImage = new Image("background.jpg");
        ImageView background = new ImageView(backgroundImage);
        root.getChildren().add(background);

        Map gameMap = new Map("stone.jpg");
        Player player = new Player(playerImage);
        MapCollider mapCollider = new MapCollider();
        PlayerController playerController = new PlayerController();
        KeyDoorInteraction keyDoor = new KeyDoorInteraction("key.png", "door_close.png", "door_open.png", "ofont.ru_Arial Cyr.ttf");

        root.getChildren().add(player.sprite);
        root.getChildren().add(keyDoor.keySprite);
        root.getChildren().add(keyDoor.doorSprite);

        long lastNanoTime = System.nanoTime();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double time = (now - lastNanoTime) / 1_000_000.0;
                lastNanoTime = now;

                playerController.handleInput(player);
                player.update(time, mapCollider);

                // Check for key collision
                if (keyDoor.checkKeyCollision(player.rect)) {
                    keyDoor.handleKeyCollision();
                }
                // Check for door collision and transition to next level
                if (keyDoor.checkDoorCollision(player.rect) && keyDoor.isDoorOpen()) {
                    keyDoor.showWinMessage(root);
                    stop(); // Stop the game after winning
                }

                gameMap.draw(root.getGraphicsContext2D());
            }
        };
        timer.start();

        primaryStage.setTitle("MyGame");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
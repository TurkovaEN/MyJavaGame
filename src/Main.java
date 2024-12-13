import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.geometry.Rectangle2D;

public class Main extends Application {
    private long lastNanoTime; // Объявляем переменную как поле класса

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(1570, 730);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Pane root = new Pane(canvas);
        Scene scene = new Scene(root);

        // Загрузка текстур
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

        long NanoTime = System.nanoTime(); // Инициализация переменной

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double time = (now - lastNanoTime) / 1_000_000.0;
                lastNanoTime = now;

                //playerController.handleInput(player);
                player.update(time, mapCollider);

                // Проверка на столкновение с ключом
                if (keyDoor.checkKeyCollision(new Rectangle2D(player.rect.getMinX(), player.rect.getMinY(), player.rect.getWidth(), player.rect.getHeight()))) {
                    keyDoor.handleKeyCollision();
                }
                // Проверка на столкновение с открытой дверью и переход на следующий уровень
                if (keyDoor.checkDoorCollision(new Rectangle2D(player.rect.getMinX(), player.rect.getMinY(), player.rect.getWidth(), player.rect.getHeight())) && keyDoor.isDoorOpen()) {
                    keyDoor.showWinMessage(root);
                    stop(); // Остановить игру после победы
                }

                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Очистка канваса
                gameMap.draw(gc); // Рисуем карту
                // Рисуем другие элементы, если необходимо
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
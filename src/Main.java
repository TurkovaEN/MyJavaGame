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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Main extends Application {
    private long lastNanoTime;

    @Override

    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(Map.W * Map.TILE_SIZE, Map.H * Map.TILE_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Pane root = new Pane();
        Scene scene = new Scene(root);

        // Загрузка фона и добавление на экран
        Image backgroundImage = new Image(getClass().getResourceAsStream("/background.jpg"));
        ImageView background = new ImageView(backgroundImage);
        root.getChildren().add(background);
        background.setOpacity(0.5);

        // Инициализация объектов игры
        Map gameMap = new Map(getClass().getResourceAsStream("/stone.jpg"));
        Player player = new Player(new Image(getClass().getResourceAsStream("/sprite_character.png")));
        MapCollider mapCollider = new MapCollider();
        PlayerController playerController = new PlayerController(); // Создаем экземпляр PlayerController

        // Добавление канваса для отрисовки карты
        root.getChildren().add(canvas);
        root.getChildren().add(player.sprite);

        // Анимационный таймер
        lastNanoTime = System.nanoTime();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double time = (now - lastNanoTime) / 1_000_000.0;
                lastNanoTime = now;

                // Обновление состояния игры
                player.update(time, mapCollider);

                // Очистка и отрисовка
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                gameMap.draw(gc);
            }
        };
        timer.start();

        // Обработка событий клавиатуры
        scene.setOnKeyPressed(event -> playerController.handleInput(player, event));
        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A || event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
                player.dx = 0; // Сброс горизонтальной скорости при отпускании клавиши
            }
        }
        );

        primaryStage.setTitle("MyGame");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
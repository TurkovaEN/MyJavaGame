import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Main extends Application {
    private long lastNanoTime; // Время последнего обновления

    @Override
    public void start(Stage primaryStage) {
        // Создаем канвас, используя размеры карты
        Canvas canvas = new Canvas(Map.W * Map.TILE_SIZE, Map.H * Map.TILE_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Pane root = new Pane();

        // Создаем сцену с размерами, равными размерам карты
        Scene scene = new Scene(root, Map.W * Map.TILE_SIZE, Map.H * Map.TILE_SIZE);

        // Загрузка фона и добавление на экран
        Image backgroundImage = new Image(getClass().getResourceAsStream("/background.jpg"));
        ImageView background = new ImageView(backgroundImage);
        background.setFitWidth(scene.getWidth());
        background.setFitHeight(scene.getHeight());
        background.setPreserveRatio(false);
        root.getChildren().add(background);

        // Инициализация объектов игры
        Map gameMap = new Map(getClass().getResourceAsStream("/stone.jpg"));
        Player player = new Player(new Image(getClass().getResourceAsStream("/sprite_character.png")));
        MapCollider mapCollider = new MapCollider();
        PlayerController playerController = new PlayerController();
        KeyDoorInteraction keyDoorInteraction = new KeyDoorInteraction(
                getClass().getResourceAsStream("/key.png"),
                getClass().getResourceAsStream("/door_close.jpg"),
                getClass().getResourceAsStream("/door_open.jpg"),
                "arialmt.ttf"
        );
        keyDoorInteraction.draw(root); // Отображаем ключ и дверь

        // Добавление канваса для отрисовки карты и спрайта игрока
        root.getChildren().add(canvas);
        root.getChildren().add(player.sprite);

        // Анимационный таймер для обновления состояния игры
        lastNanoTime = System.nanoTime();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double time = (now - lastNanoTime) / 1_000_000.0; // Вычисляем время с последнего обновления
                lastNanoTime = now;

                // Обновление состояния игрока
                player.update(time, mapCollider);

                // Проверка столкновения с ключом
                if (keyDoorInteraction.checkKeyCollision(player.rect)) {
                    keyDoorInteraction.handleKeyCollision();
                }

                // Проверка столкновения с дверью
                if (keyDoorInteraction.checkDoorCollision(player.rect) && keyDoorInteraction.isDoorOpen()) {
                    keyDoorInteraction.showWinMessage(root, primaryStage);
                }

                // Очистка и отрисовка
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                gameMap.draw(gc);
            }
        };
        timer.start();

        // Обработка событий клавиатуры
        scene.setOnKeyPressed(event -> playerController.handleInput(player, event));
        scene.setOnKeyReleased(event -> {
            // Сброс горизонтальной скорости при отпускании клавиши
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A || event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
                player.dx = 0;
            }
        });

        primaryStage.setTitle("MyGame");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Запуск приложения
    }
}
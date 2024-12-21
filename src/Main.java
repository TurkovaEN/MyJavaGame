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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

public class Main extends Application {
    private long lastNanoTime;

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

        // Устанавливаем фон, чтобы он заполнил весь экран
        background.setFitWidth(scene.getWidth()); // Устанавливаем ширину фона, равную ширине сцены
        background.setFitHeight(scene.getHeight()); // Устанавливаем высоту фона, равную высоте сцены
        background.setPreserveRatio(false); // Отключаем сохранение пропорций, чтобы фон растягивался по всему экрану
        root.getChildren().add(background);

        // Инициализация объектов игры
        Map gameMap = new Map(getClass().getResourceAsStream("/stone.jpg"));
        Player player = new Player(new Image(getClass().getResourceAsStream("/sprite_character.png")));
        MapCollider mapCollider = new MapCollider();
        PlayerController playerController = new PlayerController();

        // Использование ArrayList для хранения объектов KeyDoorInteraction
        ArrayList<KeyDoorInteraction> interactions = new ArrayList<>();
        try {

            // Добавление объектов производного класса
            interactions.add(new SpecialKeyDoorInteraction(
                    getClass().getResourceAsStream("/key2.jpg"),
                    getClass().getResourceAsStream("/door_close2.jpg"),
                    getClass().getResourceAsStream("/door_open2.jpg"),
                    "arialmt.ttf",
                    "This is a special key!"
            ));
            // Добавление объектов базового класса
            interactions.add(new KeyDoorInteraction(
                    getClass().getResourceAsStream("/key.png"),
                    getClass().getResourceAsStream("/door_close.jpg"),
                    getClass().getResourceAsStream("/door_open.jpg"),
                    "arialmt.ttf"
            ));


            // Использование нового метода
            for (KeyDoorInteraction interaction : interactions) {
                if (interaction instanceof SpecialKeyDoorInteraction) {
                    ((SpecialKeyDoorInteraction) interaction).useSpecialKey();
                }
            }

            // Сортировка по координате X ключа
            interactions.sort(Comparator.comparingDouble(k -> k.keySprite.getLayoutX()));

            for (KeyDoorInteraction interaction : interactions) {
                interaction.draw(root);
            }
        } catch (Exception e) {
            System.err.println("Error initializing KeyDoorInteraction: " + e.getMessage());
            return;
        }

        for (KeyDoorInteraction interaction : interactions) {
            interaction.draw(root);
        }

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

                // Подсчет столкновений
                int collisionCount = mapCollider.countCollisions(player.rect);
                System.out.println("Number of collisions: " + collisionCount);

                // Поиск открытой двери
                Optional<KeyDoorInteraction> openDoor = interactions.stream()
                        .filter(KeyDoorInteraction::isDoorOpen)
                        .findFirst();


                if (openDoor.isPresent()) {
                    System.out.println("door is opened");
                }


                for (KeyDoorInteraction interaction : interactions) {
                    if (interaction.checkKeyCollision(player.rect)) {
                        interaction.handleKeyCollision();
                    }
                    if (interaction.checkDoorCollision(player.rect) && interaction.isDoorOpen()) {
                        interaction.showWinMessage(root, primaryStage);
                        // Остановить таймер, чтобы игра не продолжалась
                        this.stop();
                        break; // Выход из цикла, если дверь открыта
                    }
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
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A || event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
                player.dx = 0; // Сброс горизонтальной скорости при отпускании клавиши
            }
        });

        primaryStage.setTitle("MyGame");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
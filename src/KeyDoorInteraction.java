import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.layout.Pane;
import javafx.geometry.Rectangle2D;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.InputStream;

public class KeyDoorInteraction extends GameObject implements Comparable<KeyDoorInteraction>, Collidable {
    protected String keyName; // Имя ключа
    public static int score = 0; // Статическое поле для очков
    public boolean hasKey; // Флаг, указывающий, есть ли у игрока ключ
    public ImageView keySprite; // Спрайт ключа
    public ImageView doorSprite; // Спрайт двери
    private Image keyTexture; // Текстура ключа
    private Image doorClosedTexture; // Текстура закрытой двери
    private Image doorOpenedTexture; // Текстура открытой двери
    private Text keyText; // Текст для отображения состояния ключа

    // Конструктор, загружающий изображения ключа и двери
    public KeyDoorInteraction(InputStream keyPath, InputStream doorClosedPath, InputStream doorOpenedPath, String fontPath) {
        hasKey = false; // Изначально у игрока нет ключа
        try {
            keyTexture = loadImage(keyPath); // Загрузка текстуры ключа
            doorClosedTexture = loadImage(doorClosedPath); // Загрузка текстуры закрытой двери
            doorOpenedTexture = loadImage(doorOpenedPath); // Загрузка текстуры открытой двери
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
            throw new RuntimeException("Failed to initialize KeyDoorInteraction", e);
        }
        // Инициализация изображений для ключа и двери
        keySprite = new ImageView(keyTexture);
        doorSprite = new ImageView(doorClosedTexture);

        // Инициализация текста для отображения состояния ключа
        keyText = new Text();
        keyText.setFont(new Font(30));
        keyText.setFill(javafx.scene.paint.Color.WHITE);
        updateKeyText(); // Обновление текста состояния ключа

        // Начальные позиции для ключа и двери
        keySprite.setLayoutX(1420);
        keySprite.setLayoutY(160);
        doorSprite.setLayoutX(1280);
        doorSprite.setLayoutY(590);
    }

    // Метод для загрузки изображения с обработкой исключений
    private Image loadImage(InputStream path) throws Exception {
        if (path == null) {
            throw new Exception("Image path is null");
        }
        return new Image(path);
    }

    // Метод для обновления текста состояния ключа
    public void updateKeyText() {
        String keyStatus = this.hasKey ? "yes" : "no"; // Определяем текст в зависимости от наличия ключа
        this.keyText.setText("Key: " + keyStatus); // Установка текста
    }

    // Метод для добавления ключа и двери на панель
    public void draw(Pane pane) {
        // Удаляем старые спрайты, если они уже добавлены
        if (pane.getChildren().contains(keySprite)) {
            pane.getChildren().remove(keySprite);
        }
        if (pane.getChildren().contains(doorSprite)) {
            pane.getChildren().remove(doorSprite);
        }
        if (pane.getChildren().contains(keyText)) {
            pane.getChildren().remove(keyText);
        }
        // Устанавливаем позиции для новых спрайтов
        keySprite.setLayoutX(1420);
        keySprite.setLayoutY(160);
        doorSprite.setLayoutX(1280);
        doorSprite.setLayoutY(590);
        // Добавляем новые спрайты на панель
        pane.getChildren().addAll(keySprite, doorSprite, keyText); // Добавление спрайтов и текста на панель
    }

    // Метод для проверки столкновения с ключом
    public boolean checkKeyCollision(Rectangle2D playerRect) {
        Rectangle2D keyBounds = new Rectangle2D(
                keySprite.getLayoutX(),
                keySprite.getLayoutY(),
                keySprite.getImage().getWidth(),
                keySprite.getImage().getHeight()
        );
        return keyBounds.intersects(playerRect); // Проверка пересечения прямоугольников
    }

    // Метод для обработки столкновения с ключом
    public void handleKeyCollision() {
        hasKey = true; // Установка флага наличия ключа
        updateKeyText(); // Обновление текста состояния ключа
        keySprite.setLayoutX(-100); // Убираем ключ с экрана
        doorSprite.setImage(doorOpenedTexture); // Меняем изображение двери на открытую

        KeyDoorInteraction.addScore(10); // Добавляем очки
        System.out.println("Current score: " + KeyDoorInteraction.score);
    }

    // Перегруженный метод для обработки столкновения с ключом с дополнительной информацией
    public void handleKeyCollision(String additionalInfo) {
        hasKey = true; // Установка флага наличия ключа
        updateKeyText(); // Обновление текста состояния ключа
        keySprite.setLayoutX(-100); // Убираем ключ с экрана
        doorSprite.setImage(doorOpenedTexture); // Меняем изображение двери на открытую
        System.out.println(additionalInfo); // Вывод дополнительной информации
    }

    // Метод для проверки столкновения с дверью
    public boolean checkDoorCollision(Rectangle2D playerRect) {
        Rectangle2D doorBounds = new Rectangle2D(
                doorSprite.getLayoutX(),
                doorSprite.getLayoutY(),
                doorSprite.getImage().getWidth(),
                doorSprite.getImage().getHeight()
        );
        return doorBounds.intersects(playerRect); // Проверка пересечения прямоугольников
    }

    // Метод для проверки, открыта ли дверь
    public boolean isDoorOpen() {
        return hasKey; // Возвращаем флаг наличия ключа
    }

    // Метод для отображения сообщения о победе
    public void showWinMessage(Pane pane, Stage stage) {
        Text winText = new Text("You completed the level! Continuing the game is in development..."); // Сообщение о победе
        winText.setFont(new Font(32));
        winText.setFill(javafx.scene.paint.Color.GREEN);
        winText.setLayoutX((pane.getWidth() - winText.getLayoutBounds().getWidth()) / 2); // Центрируем текст по ширине
        winText.setLayoutY((pane.getHeight() - winText.getLayoutBounds().getHeight()) / 2); // Центрируем текст по высоте
        pane.getChildren().add(winText); // Добавляем текст на панель

        // Пауза на 3 секунды перед удалением сообщения
        new Thread(() -> {
            try {
                Thread.sleep(3000); // Задержка
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Используем Platform.runLater для удаления сообщения из основного потока
            Platform.runLater(() -> {
                pane.getChildren().remove(winText); // Удаляем сообщение о победе
                stage.close(); // Закрываем окно
            });
        }).start();
    }

    @Override
    public int compareTo(KeyDoorInteraction other) {
        // Сравниваем по координате X (или любому другому критерию)
        return Double.compare(this.keySprite.getLayoutX(), other.keySprite.getLayoutX());
    }

    @Override
    public boolean checkCollision(Rectangle2D playerRect) {
        return checkKeyCollision(playerRect) || checkDoorCollision(playerRect);
    }

    @Override
    public String toString() {
        return "KeyDoorInteraction: hasKey=" + hasKey;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        KeyDoorInteraction cloned = (KeyDoorInteraction) super.clone();
        cloned.keySprite = new ImageView(this.keySprite.getImage());
        cloned.doorSprite = new ImageView(this.doorSprite.getImage());
        return cloned;
    }

    public static void addScore(int points) {
        score += points; // Увеличивает счетчик очков
    }
}